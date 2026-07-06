---
name: android-feedback-about
description: Add a Material 3 / Jetpack Compose bottom navigation with Feedback and About tabs to a native Android app (matching the Tertiary Infotech house style). The Feedback tab has a Title field, a Message field, and a "Send via WhatsApp" button that opens wa.me/6588666375 with the composed text. The About tab shows the app name + description, a Developer card ("Tertiary Infotech Academy Pte. Ltd." + tertiaryinfotech.com link), an optional Data-source card with a link, and a Version row (BuildConfig). Use when asked to add About/Feedback tabs, a bottom nav, a WhatsApp feedback form, or an in-app source attribution/About screen.
license: MIT
metadata:
  version: "1.0.0"
---

# Android Feedback + About tabs (Compose, Material 3)

Add a **bottom navigation** with **Feedback** and **About** tabs to an existing Compose app,
in the Tertiary Infotech house style. Drop the content tab(s) you already have alongside them.

- **Feedback tab:** `Title` + `Message` text fields and a **Send via WhatsApp** button that
  opens `https://wa.me/6588666375?text=<urlencoded title + message>`.
- **About tab:** an app card (name + about text), a **Developer** card
  ("Tertiary Infotech Academy Pte. Ltd." + `tertiaryinfotech.com` link), an optional
  **Data source** card (link to the official data source — required if surfacing government
  data; see `android-app-submission` §8), and a **Version** row from `BuildConfig`.

## Wiring

1. `app/build.gradle.kts` → `buildFeatures { buildConfig = true }` and a real
   `versionName`/`versionCode` (the About screen reads `BuildConfig.VERSION_NAME` /
   `VERSION_CODE`).
2. Host the tabs in a root `Scaffold(bottomBar = NavigationBar { … })` and swap content by
   selected index. Apply the scaffold's **bottom** inset to the content so your existing
   screen doesn't hide behind the bar.
3. Open links/WhatsApp/mailto with `LocalUriHandler.current.openUri(...)` — no extra
   permissions, no Intent plumbing. WhatsApp opens via the `wa.me` https link.

## Reference implementation (`ui/RootScreen.kt`)

```kotlin
private const val DEVELOPER_URL = "https://www.tertiaryinfotech.com"
private const val FEEDBACK_WHATSAPP = "6588666375"        // +65 8866 6375
// If the app shows government data, also link the official source on the About screen:
// private const val DATA_SOURCE_URL = "https://datamall.lta.gov.sg/content/datamall/en.html"

@Composable
fun RootScreen(/* pass your existing screen's params */) {
    var tab by remember { mutableIntStateOf(0) }
    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf(
                    Triple("Map", Icons.Filled.Map, 0),               // your existing tab
                    Triple("Feedback", Icons.AutoMirrored.Filled.Chat, 1),
                    Triple("About", Icons.Filled.Info, 2),
                ).forEach { (label, icon, i) ->
                    NavigationBarItem(
                        selected = tab == i,
                        onClick = { tab = i },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                    )
                }
            }
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(bottom = padding.calculateBottomPadding())) {
            when (tab) {
                0 -> /* YourExistingScreen(...) */ Unit
                1 -> FeedbackScreen()
                else -> AboutScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeedbackScreen() {
    val uri = LocalUriHandler.current
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    Scaffold(topBar = { TopAppBar(title = { Text("Feedback") }) }) { p ->
        Column(
            Modifier.fillMaxSize().padding(p).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("We'd love your feedback", fontWeight = FontWeight.Bold, fontSize = 22.sp)
            OutlinedTextField(title, { title = it }, label = { Text("Title") },
                singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(message, { message = it }, label = { Text("Message") },
                minLines = 4, modifier = Modifier.fillMaxWidth())
            Button(
                onClick = {
                    val body = buildString {
                        if (title.isNotBlank()) append("*").append(title.trim()).append("*\n")
                        append(message.trim())
                    }
                    val text = java.net.URLEncoder.encode(body, "UTF-8")
                    uri.openUri("https://wa.me/$FEEDBACK_WHATSAPP?text=$text")
                },
                enabled = title.isNotBlank() || message.isNotBlank(),
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                Spacer(Modifier.width(8.dp)); Text("Send via WhatsApp")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutScreen() {
    val uri = LocalUriHandler.current
    Scaffold(topBar = { TopAppBar(title = { Text("About") }) }) { p ->
        Column(
            Modifier.fillMaxSize().padding(p).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Card {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("<App name>", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("<One-paragraph description of what the app does.>",
                        color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                }
            }
            Text("DEVELOPER", fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Card {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Tertiary Infotech Academy Pte. Ltd.", fontWeight = FontWeight.SemiBold)
                    Row(Modifier.fillMaxWidth().clickable { uri.openUri(DEVELOPER_URL) },
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Language, null, Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("tertiaryinfotech.com", textDecoration = TextDecoration.Underline)
                    }
                }
            }
            // Optional Data-source card here (required for government data) — same Row+link pattern.
            Card {
                Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("Version", fontWeight = FontWeight.Medium)
                    Spacer(Modifier.weight(1f))
                    Text("${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
```

## Imports that catch people out

- `androidx.compose.material.icons.automirrored.filled.Send` / `.Chat` (the plain
  `Icons.Filled.Send`/`Chat` are deprecated → build warnings).
- `androidx.compose.ui.platform.LocalUriHandler`,
  `androidx.compose.ui.text.style.TextDecoration`, `androidx.compose.foundation.clickable`,
  `androidx.compose.foundation.verticalScroll`, `rememberScrollState`.
- `material-icons-extended` dependency for `Language`, `Map`, `Info`, `Chat`.

## Conventions

- WhatsApp number is **6588666375** (Singapore, country code included, no `+`/spaces).
  The `wa.me` link works whether or not WhatsApp is installed (falls back to web).
- Keep the brand green app-bar/nav accent consistent with the rest of the app.
- The About **Data source** card is mandatory when the app shows government/official data
  (LTA DataMall, data.gov.sg, OneMap, etc.) — it doubles as the Play "Misleading Claims"
  source-link fix. See the `android-app-submission` skill.
- Verify with `./gradlew :app:assembleDebug` before shipping.
```
