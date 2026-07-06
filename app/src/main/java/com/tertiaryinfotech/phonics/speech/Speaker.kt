package com.tertiaryinfotech.phonics.speech

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tertiaryinfotech.phonics.data.Emotion
import com.tertiaryinfotech.phonics.data.Phoneme
import java.util.Locale

/** The accent the speech engine should use. */
enum class Accent(val label: String, val flag: String, val subtitle: String, val locale: Locale) {
    BRITISH("British", "🇬🇧", "Received Pronunciation (en-GB)", Locale.UK),
    AMERICAN("American", "🇺🇸", "General American (en-US)", Locale.US);

    companion object {
        fun from(raw: String?): Accent = if (raw == AMERICAN.name) AMERICAN else BRITISH
    }
}

/** The voice gender the speech engine should prefer when a matching voice exists. */
enum class VoiceGender(val label: String) {
    FEMALE("Female"),
    MALE("Male");

    companion object {
        fun from(raw: String?): VoiceGender = if (raw == MALE.name) MALE else FEMALE
    }
}

/**
 * Accent-aware, emotion-aware voice-over — mirrors the iOS SpeechManager.
 *
 * Words play real human recordings bundled from Wiktionary / Wikimedia Commons
 * (assets/word_audio, preferring the chosen accent, falling back uk↔us then au),
 * with the on-device TextToSpeech engine as fallback. Sentences and isolated
 * phoneme cues are synthesized with genuine en-GB / en-US voices, fully offline.
 */
class Speaker(context: Context) {

    private val appContext = context.applicationContext
    private val prefs = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
    private val mainHandler = Handler(Looper.getMainLooper())

    var accent: Accent by mutableStateOf(Accent.from(prefs.getString("accent", null)))
        private set
    var gender: VoiceGender by mutableStateOf(VoiceGender.from(prefs.getString("voiceGender", null)))
        private set

    /** Sentence speaking rate on the Android TTS scale (1.0 = normal). Persisted. */
    var speechRate: Float by mutableStateOf(prefs.getFloat("speechRate", 1.0f))
        private set

    /** Key of the utterance currently playing (for per-row highlight in lists). */
    var nowPlaying: String? by mutableStateOf(null)
        private set
    var isSpeaking: Boolean by mutableStateOf(false)
        private set

    private var ready by mutableStateOf(false)
    private var mediaPlayer: MediaPlayer? = null

    /** Names of the bundled word recordings, e.g. "rain_uk.m4a". */
    private val wordClips: Set<String> =
        runCatching { appContext.assets.list("word_audio")?.toSet() }.getOrNull().orEmpty()

    private val tts: TextToSpeech = TextToSpeech(appContext) { status ->
        if (status == TextToSpeech.SUCCESS) ready = true
    }

    init {
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                mainHandler.post { isSpeaking = true }
            }
            override fun onDone(utteranceId: String?) {
                mainHandler.post { isSpeaking = false; nowPlaying = null }
            }
            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                mainHandler.post { isSpeaking = false; nowPlaying = null }
            }
        })
    }

    fun chooseAccent(a: Accent) {
        accent = a
        prefs.edit().putString("accent", a.name).apply()
    }

    fun chooseGender(g: VoiceGender) {
        gender = g
        prefs.edit().putString("voiceGender", g.name).apply()
    }

    fun chooseSpeechRate(rate: Float) {
        speechRate = rate
        prefs.edit().putFloat("speechRate", rate).apply()
    }

    /** Speak `text` in the chosen accent, optionally shaped by an emotion preset. */
    fun speak(text: String, emotion: Emotion? = null, rate: Float? = null) {
        val clean = text.trim()
        if (clean.isEmpty() || !ready) return
        stopPlayback()

        // Emotions reshape the sentence's punctuation so the synthesizer applies real
        // prosody (rising questions, excited bursts, trailing sadness).
        val spoken = emotion?.render(clean) ?: clean
        applyVoice()
        tts.setSpeechRate(emotion?.rate ?: rate ?: speechRate)
        tts.setPitch(emotion?.pitch ?: 1.0f)

        val params = Bundle().apply {
            putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, emotion?.volume ?: 1.0f)
        }
        nowPlaying = clean
        if (emotion != null && emotion.preDelayMs > 0) {
            tts.playSilentUtterance(emotion.preDelayMs, TextToSpeech.QUEUE_FLUSH, "pre-delay")
            tts.speak(spoken, TextToSpeech.QUEUE_ADD, params, "line")
        } else {
            tts.speak(spoken, TextToSpeech.QUEUE_FLUSH, params, "line")
        }
    }

    /**
     * Demonstrate a phoneme's *isolated sound* via TTS, fed a hidden respelling cue the
     * engine pronounces correctly (e.g. /iː/ → "ee") — not the example word, and not raw
     * IPA (which on-device voices don't reliably honour).
     */
    fun speakPhoneme(phoneme: Phoneme) {
        if (!ready) return
        stopPlayback()
        applyVoice()
        tts.setSpeechRate(0.79f)          // slower — an isolated sound, not a word
        tts.setPitch(1.0f)
        nowPlaying = phoneme.soundKey
        tts.speak(phoneme.soundSpelling, TextToSpeech.QUEUE_FLUSH, null, "sound")
    }

    /**
     * Play a real human recording of `word` (bundled from Wiktionary / Wikimedia Commons),
     * preferring the chosen accent and falling back to the other accent, then to TTS.
     * `key` drives the "now playing" highlight (defaults to the word itself).
     */
    fun speakWord(word: String, key: String? = null) {
        stopPlayback()
        val base = word.lowercase().trim()
        val order = if (accent == Accent.BRITISH) listOf("uk", "us", "au") else listOf("us", "uk", "au")
        val clip = order.map { "${base}_$it.m4a" }.firstOrNull { it in wordClips }

        if (clip != null) {
            runCatching {
                val player = MediaPlayer()
                appContext.assets.openFd("word_audio/$clip").use { fd ->
                    player.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
                }
                player.setOnCompletionListener {
                    mainHandler.post { isSpeaking = false; nowPlaying = null }
                    it.release()
                    if (mediaPlayer === it) mediaPlayer = null
                }
                player.prepare()
                mediaPlayer = player
                nowPlaying = key ?: word
                isSpeaking = true
                player.start()
            }.onSuccess { return }
            // Clip failed to play → fall through to TTS.
        }
        if (!ready) return
        applyVoice()
        tts.setSpeechRate(0.96f)
        tts.setPitch(1.0f)
        nowPlaying = key ?: word
        tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, "word")
    }

    fun stop() {
        stopPlayback()
        nowPlaying = null
        isSpeaking = false
    }

    fun shutdown() {
        stopPlayback()
        tts.shutdown()
    }

    private fun stopPlayback() {
        if (ready) tts.stop()
        mediaPlayer?.run {
            runCatching { stop() }
            release()
        }
        mediaPlayer = null
    }

    /**
     * Pin the language to the accent's region so the OS supplies the correct accent
     * (never another region's default), then prefer an installed voice matching the
     * user's gender preference, highest quality first — mirrors the iOS voice picker.
     */
    private fun applyVoice() {
        tts.language = accent.locale
        val tag = accent.locale.toLanguageTag()      // "en-GB" / "en-US"
        val regionVoices = runCatching {
            tts.voices?.filter { v ->
                v.locale.toLanguageTag() == tag && !v.isNetworkConnectionRequired
            }
        }.getOrNull().orEmpty()
        if (regionVoices.isEmpty()) return

        val wanted = if (gender == VoiceGender.MALE) listOf("male", "#male") else listOf("female", "#female")
        val other = if (gender == VoiceGender.MALE) listOf("female", "#female") else listOf("male", "#male")
        val gendered = regionVoices.filter { v ->
            wanted.any { it in v.name.lowercase() }
        }.ifEmpty {
            // Engines that don't label gender: exclude the opposite label if present.
            regionVoices.filterNot { v -> other.any { it in v.name.lowercase() } }
        }
        val pool = gendered.ifEmpty { regionVoices }
        pool.maxByOrNull(Voice::getQuality)?.let { tts.voice = it }
    }
}
