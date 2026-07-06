package com.tertiaryinfotech.phonics.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallSplit
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Exposure
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RMobiledata
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.graphics.vector.ImageVector

/** The nine phonics groups of the Learn tab — mirrors the iOS PhonicsCategory. */
enum class PhonicsCategory(val title: String, val blurb: String) {
    CONSONANT("Consonants", "Single consonant letter sounds."),
    SHORT_VOWEL("Short Vowels", "The five short vowel sounds."),
    LONG_VOWEL("Long Vowels", "Vowels that 'say their name', often via silent-e."),
    DIGRAPH("Digraphs", "Two letters making one new sound."),
    BLEND("Blends", "Two consonants blended, each still heard."),
    VOWEL_TEAM("Vowel Teams", "Two vowels working together for one sound."),
    DIPHTHONG("Diphthongs", "Gliding vowel sounds that shift mid-sound."),
    R_CONTROLLED("R-Controlled", "Vowels reshaped by a following 'r'."),
    SOFT_SOUND("Soft Sounds", "When c and g soften before e, i, or y.");

    val icon: ImageVector
        get() = when (this) {
            CONSONANT -> Icons.Filled.TextFields
            SHORT_VOWEL -> Icons.Filled.Title
            LONG_VOWEL -> Icons.Filled.FontDownload
            DIGRAPH -> Icons.AutoMirrored.Filled.MenuBook
            BLEND -> Icons.Filled.Link
            VOWEL_TEAM -> Icons.Filled.Group
            DIPHTHONG -> Icons.Filled.GraphicEq
            R_CONTROLLED -> Icons.Filled.RMobiledata
            SOFT_SOUND -> Icons.Filled.Air
        }
}

/** A grapheme→phoneme unit the learner can tap to hear. */
data class Phoneme(
    val grapheme: String,          // e.g. "sh", "igh", "c"
    val ipa: String,               // e.g. "/ʃ/"
    val soundDescription: String,  // plain-language cue
    val exampleWords: List<String>,
    val category: PhonicsCategory,
) {
    /** The bare IPA symbols (no slashes / arrows) — e.g. "/eɪ/" → "eɪ". */
    val soundIPA: String
        get() = Regex("/[^/]+/").find(ipa)?.value?.replace("/", "")
            ?: ipa.replace("/", "").trim()

    /** Stable key for the "now playing the sound" highlight (distinct from word playback). */
    val soundKey: String get() = "sound-$category-$grapheme-$ipa"

    /**
     * A respelling that the speech engine pronounces as this *isolated phoneme*.
     * TTS engines don't reliably honour raw IPA, so we feed a plain-text cue keyed on the
     * phoneme's IPA (e.g. /iː/ → "ee", /tʃ/ → "chuh"). Falls back to the grapheme.
     */
    val soundSpelling: String get() = respelling[soundIPA] ?: grapheme

    companion object {
        /** IPA → engine-friendly respelling. Multiple graphemes share an IPA (ai/ay/a_e → /eɪ/). */
        val respelling: Map<String, String> = mapOf(
            // Consonants — stops/affricates take a light schwa; continuants are stretched.
            "b" to "buh", "d" to "duh", "ɡ" to "guh", "g" to "guh", "k" to "kuh",
            "p" to "puh", "t" to "tuh",
            "dʒ" to "juh", "tʃ" to "chuh",
            "f" to "fuh", "v" to "vuh", "h" to "huh", "w" to "wuh", "j" to "yuh",
            "r" to "ruh", "l" to "luh",
            "s" to "sss", "z" to "zzz", "m" to "mmm", "n" to "nnn", "ʃ" to "shh",
            "ŋ" to "ung", "θ" to "thh", "ð" to "thuh",
            // Short vowels (clipped — inherently approximate in isolation).
            "æ" to "aah", "ɛ" to "eh", "ɪ" to "ih", "ɒ" to "aw", "ʌ" to "uh",
            // Long vowels & vowel teams.
            "eɪ" to "ai", "iː" to "ee", "aɪ" to "eye", "oʊ" to "oh", "juː" to "you", "uː" to "oo",
            // Diphthongs.
            "ɔɪ" to "oy", "aʊ" to "ow", "ɔː" to "awe",
            // R-controlled (real-word cues read most accurately).
            "ɑːr" to "are", "ɔːr" to "ore", "ɜːr" to "er",
            // Consonant blends — said as the cluster onset.
            "bl" to "bluh", "kl" to "cluh", "fl" to "fluh", "ɡr" to "gruh", "tr" to "truh",
            "st" to "stuh", "sp" to "spuh", "sn" to "snuh", "str" to "struh",
        )
    }
}

/** An irregular-spelling / exception rule, with worked examples the learner can hear. */
data class ExceptionRule(
    val title: String,
    val summary: String,
    val explanation: String,
    val examples: List<RuleExample>,
    val icon: ImageVector,
)

data class RuleExample(
    val word: String,
    val note: String,       // why it's irregular / how it's said
)

enum class PracticeKind(val title: String, val blurb: String) {
    MINIMAL_PAIRS(
        "Minimal Pairs",
        "Two words that differ by a single sound. Hear the contrast, then say each."),
    TONGUE_TWISTERS(
        "Tongue Twisters",
        "Repeat tricky sound clusters to build fluency and control."),
    SHORT_MESSAGES(
        "Short Messages",
        "Bite-size everyday phrases — perfect for a quick daily session."),
    SENTENCES(
        "Sentence Drills",
        "Full sentences to practise rhythm, linking, and stress.");

    val icon: ImageVector
        get() = when (this) {
            MINIMAL_PAIRS -> Icons.Filled.SwapHoriz
            TONGUE_TWISTERS -> Icons.Filled.RecordVoiceOver
            SHORT_MESSAGES -> Icons.AutoMirrored.Filled.Chat
            SENTENCES -> Icons.AutoMirrored.Filled.FormatAlignLeft
        }
}

/** A minimal pair: two words differing by one phoneme. */
data class MinimalPair(
    val first: String,
    val second: String,
    val contrast: String,   // e.g. "short i vs long e"
)

/** A single practice line the learner reads aloud and can hear. */
data class PracticeLine(
    val text: String,
    val focus: String,      // what sound/skill it targets
)

/**
 * An intonation/emotion preset that reshapes how a sentence is spoken.
 * Maps onto Android TTS setSpeechRate / setPitch / volume — and also reshapes the
 * sentence's punctuation, because the synthesizer's prosody (rising questions, excited
 * bursts, trailing sadness) responds far more strongly to "!", "…" and "?" than to
 * pitch alone. Rate is on the Android scale (1.0 = normal), converted from the iOS presets.
 */
data class Emotion(
    val name: String,
    val icon: ImageVector,
    val cue: String,             // coaching tip on how the voice should move
    val rate: Float,             // Android TTS scale, 1.0 = normal
    val pitch: Float,            // Android TTS setPitch, 1.0 = normal
    val volume: Float,           // 0.0...1.0
    val preDelayMs: Long,
    val punctuation: String,     // re-applied to the sentence to drive prosody
) {
    /** Strip any trailing sentence punctuation from `base`, then apply this emotion's. */
    fun render(base: String): String {
        val stripped = base.trim().replace(Regex("[.!?…]+$"), "")
        return stripped + punctuation
    }

    companion object {
        val all: List<Emotion> = listOf(
            Emotion("Neutral", Icons.Filled.RemoveCircleOutline,
                "Flat, even tone. A steady baseline to compare the others against.",
                rate = 0.96f, pitch = 1.00f, volume = 1.0f, preDelayMs = 0, punctuation = "."),
            Emotion("Happy", Icons.Filled.Mood,
                "Bright and lifted — pitch rises and the pace quickens.",
                rate = 1.15f, pitch = 1.35f, volume = 1.0f, preDelayMs = 0, punctuation = "!"),
            Emotion("Excited", Icons.Filled.AutoAwesome,
                "Fast and high-energy, with big pitch jumps and a burst at the end.",
                rate = 1.33f, pitch = 1.55f, volume = 1.0f, preDelayMs = 0, punctuation = "!!"),
            Emotion("Sad", Icons.Filled.SentimentDissatisfied,
                "Slow and low. Pitch sags and the energy trails away.",
                rate = 0.69f, pitch = 0.70f, volume = 0.9f, preDelayMs = 150, punctuation = "…"),
            Emotion("Angry", Icons.Filled.LocalFireDepartment,
                "Hard, low and forceful — clipped and punchy.",
                rate = 1.08f, pitch = 0.76f, volume = 1.0f, preDelayMs = 0, punctuation = "!"),
            Emotion("Question", Icons.AutoMirrored.Filled.HelpOutline,
                "Rising intonation — the pitch climbs on the final words.",
                rate = 0.94f, pitch = 1.22f, volume = 1.0f, preDelayMs = 0, punctuation = "?"),
            Emotion("Calm", Icons.Filled.Spa,
                "Gentle and unhurried. Soft volume, mid-low steady pitch.",
                rate = 0.75f, pitch = 0.90f, volume = 0.8f, preDelayMs = 100, punctuation = "."),
            Emotion("Surprised", Icons.Filled.PriorityHigh,
                "Sudden high pitch on the key word, then settles.",
                rate = 1.04f, pitch = 1.60f, volume = 1.0f, preDelayMs = 0, punctuation = "?!"),
        )
    }
}

/** Icons used by the Rules tab (kept here so ContentLibrary stays icon-import-free). */
object RuleIcons {
    val silent = Icons.AutoMirrored.Filled.VolumeOff
    val ough = Icons.Filled.Quiz
    val softCG = Icons.AutoMirrored.Filled.CallSplit
    val iBeforeE = Icons.Filled.Spellcheck
    val sightWords = Icons.Filled.Visibility
    val schwa = Icons.AutoMirrored.Filled.ShowChart
    val doubling = Icons.Filled.Exposure
}
