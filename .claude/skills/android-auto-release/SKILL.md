---
name: android-auto-release
description: Set up CI/CD that auto-builds, signs, and uploads a native Android app (AAB) to the Google Play Console on every push to main (GitHub Actions on ubuntu). The build is signed with your release keystore (from GitHub secrets) and uploaded to a chosen track (internal / closed / production) via a Play Developer API service account, with release notes pulled from distribution/whatsnew (or CHANGELOG.md). Handles auto-incrementing versionCode, the "first upload must be manual" rule, secrets, and the Play API gotchas. Use when a user wants automated Play Store releases / "auto submit to Play on push" / a release pipeline for an Android Gradle app.
license: MIT
metadata:
  version: "1.0.0"
---

# Android Auto-Release — GitHub Actions → Google Play

Build, sign, and upload an Android **AAB** to Google Play automatically on every push to
`main`. Runs on `ubuntu-latest`. Uploads via the **Play Developer API** using a **service
account**, so no manual console clicks per update.

> **First release is manual.** The Play Developer API can only *update* an app that already
> has at least one AAB on a track. Do the **first** submission with the `android-app-submission`
> skill (console UI), then this pipeline handles every update after.

## What you need (one-time setup)

### 1. A Play service account with Play Console access
1. Google Cloud Console → the project linked to Play → **IAM & Admin → Service Accounts →
   Create**. No roles needed in GCP. Create a **JSON key** and download it.
2. Play Console → **Setup → API access** → link the project if needed → grant the service
   account **app access** with at least *Release to testing tracks* / *Release apps to
   production* (or "Release manager"). Wait a few minutes for propagation.

### 2. GitHub repository secrets  (Settings → Secrets and variables → Actions)
| Secret | What |
| --- | --- |
| `PLAY_SERVICE_ACCOUNT_JSON` | the full contents of the service-account JSON key |
| `KEYSTORE_BASE64` | `base64 -i keystore/<app>-release.jks` (one line) |
| `KEYSTORE_PASSWORD` | keystore store password |
| `KEY_ALIAS` | key alias |
| `KEY_PASSWORD` | key password (== store password for PKCS12) |
| `MAPS_API_KEY` | (if used) Google Maps Android key |
| `LTA_DATAMALL_ACCOUNT_KEY` | (if used) any app data key surfaced via BuildConfig |

> NEVER commit the keystore or JSON. They live only in GitHub secrets. Keep an offline backup
> of the keystore — losing it means you can never update the app again.

## Make the Gradle build CI-friendly

- **versionCode must be unique and monotonic.** Let CI override it from the run number so you
  never hand-bump it. In `app/build.gradle.kts`:
  ```kotlin
  val ciVersionCode = (project.findProperty("versionCodeOverride") as String?)?.toIntOrNull()
  defaultConfig {
      versionCode = ciVersionCode ?: 2          // local default
      versionName = "1.1"
  }
  ```
- **Signing already reads `local.properties`** (per the submission skill). The workflow writes
  a `local.properties` from secrets so the existing `RELEASE_*` wiring just works.

## The workflow  `.github/workflows/android-release.yml`

```yaml
name: Android Release to Play

on:
  push:
    branches: [ main ]
    paths-ignore: [ '**.md', 'docs/**', '.claude/**' ]
  workflow_dispatch:
    inputs:
      track:
        description: 'Play track'
        default: 'internal'
        type: choice
        options: [ internal, alpha, beta, production ]

concurrency:
  group: android-release
  cancel-in-progress: false

jobs:
  release:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - uses: actions/setup-java@v4
        with: { distribution: temurin, java-version: '17' }

      - name: Decode keystore
        run: |
          mkdir -p keystore
          echo "${{ secrets.KEYSTORE_BASE64 }}" | base64 -d > keystore/release.jks

      - name: Write local.properties
        run: |
          {
            echo "sdk.dir=$ANDROID_SDK_ROOT"
            echo "MAPS_API_KEY=${{ secrets.MAPS_API_KEY }}"
            echo "LTA_DATAMALL_ACCOUNT_KEY=${{ secrets.LTA_DATAMALL_ACCOUNT_KEY }}"
            echo "RELEASE_STORE_FILE=keystore/release.jks"
            echo "RELEASE_STORE_PASSWORD=${{ secrets.KEYSTORE_PASSWORD }}"
            echo "RELEASE_KEY_ALIAS=${{ secrets.KEY_ALIAS }}"
            echo "RELEASE_KEY_PASSWORD=${{ secrets.KEY_PASSWORD }}"
          } > local.properties

      - name: Release notes (from CHANGELOG → whatsnew)
        run: |
          mkdir -p distribution/whatsnew
          # take the bullet lines of the top CHANGELOG entry, cap at 500 chars (Play limit)
          awk '/^## \[/{n++} n==1 && /^- /{print}' CHANGELOG.md | head -c 480 \
            > distribution/whatsnew/whatsnew-en-US || true
          [ -s distribution/whatsnew/whatsnew-en-US ] || echo "Bug fixes and improvements." \
            > distribution/whatsnew/whatsnew-en-US

      - name: Build signed AAB
        run: ./gradlew :app:bundleRelease -PversionCodeOverride=$(( ${{ github.run_number }} + 100 ))

      - name: Upload to Google Play
        uses: r0adkll/upload-google-play@v1
        with:
          serviceAccountJsonPlainText: ${{ secrets.PLAY_SERVICE_ACCOUNT_JSON }}
          packageName: <APPLICATION_ID>        # e.g. com.alfredang.sgevcharging
          releaseFiles: app/build/outputs/bundle/release/*.aab
          track: ${{ github.event.inputs.track || 'internal' }}
          status: completed
          whatsNewDirectory: distribution/whatsnew
```

Notes:
- `+ 100` offsets the run number so CI versionCodes stay above any you uploaded manually
  (set the offset above your last manual versionCode).
- **Track:** default `internal` rolls out fastest. Use `alpha` for your **closed** track
  (Play's API name for the default closed track is `alpha`). `production` triggers full review.
- `status: completed` rolls out immediately to the track's testers; use `draft` if you want to
  finish/submit manually in the console.
- The action **fails loudly** if the versionCode already exists or the service account lacks
  permission — read its log; those are the two most common failures.

## Deploy

```bash
# add the override hook to app/build.gradle.kts (see above), then:
git add app/build.gradle.kts .github/workflows/android-release.yml distribution/whatsnew/
git commit -m "ci: auto-release to Google Play on push to main"
git push origin main
gh workflow enable "Android Release to Play" 2>/dev/null || true
gh secret set PLAY_SERVICE_ACCOUNT_JSON < service-account.json
gh secret set KEYSTORE_BASE64 < <(base64 -i keystore/<app>-release.jks)
gh secret set KEYSTORE_PASSWORD --body '<pw>'
gh secret set KEY_ALIAS --body '<alias>'
gh secret set KEY_PASSWORD --body '<pw>'
# plus MAPS_API_KEY / LTA_DATAMALL_ACCOUNT_KEY if used
gh workflow run "Android Release to Play"   # test once via dispatch
gh run watch
```

## Gotchas

- **"APK/AAB not allowed: version code already used"** → bump the offset or you re-ran a build;
  versionCode must strictly increase.
- **"The caller does not have permission"** → service account not granted app access in Play
  Console, or you're targeting an app with **no first manual release**.
- **"Package not found"** → wrong `packageName`, or first release never done in the console.
- **Closed track name** in the API is `alpha`; `beta` is the open/closed beta; custom tracks
  use their track id. `internal` is the Internal testing track (fast, up to 100 testers).
- Keep `paths-ignore` so doc-only commits don't burn a versionCode.
- This pairs with **`android-app-submission`** (first manual release + policy/store setup) and
  **`android-feedback-about`** (in-app About/Feedback UI). Maintain `CHANGELOG.md` so the
  whatsnew notes stay meaningful.
```
