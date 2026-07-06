package com.tertiaryinfotech.phonics.data

/**
 * All static learning content. Curriculum follows standard synthetic-phonics
 * structure (see en.wikipedia.org/wiki/Phonics): phonemes grouped by type,
 * exception/irregular rules, and graded practice material. Mirrors the iOS app 1:1.
 */
object ContentLibrary {

    // region Phonemes

    val consonants = listOf(
        Phoneme("b", "/b/", "Lips together, then a voiced pop.", listOf("bat", "rabbit", "tub"), PhonicsCategory.CONSONANT),
        Phoneme("k", "/k/", "Back of the tongue lifts, short hard click.", listOf("cat", "kite", "duck"), PhonicsCategory.CONSONANT),
        Phoneme("d", "/d/", "Tongue taps behind the teeth, voiced.", listOf("dog", "ladder", "bed"), PhonicsCategory.CONSONANT),
        Phoneme("f", "/f/", "Top teeth on bottom lip, blow air.", listOf("fish", "coffee", "leaf"), PhonicsCategory.CONSONANT),
        Phoneme("g", "/ɡ/", "Hard throat sound, voiced.", listOf("goat", "wagon", "bag"), PhonicsCategory.CONSONANT),
        Phoneme("h", "/h/", "A soft breath of air.", listOf("hat", "ahead", "hop"), PhonicsCategory.CONSONANT),
        Phoneme("j", "/dʒ/", "Tongue to roof, voiced jump.", listOf("jam", "jet", "jump"), PhonicsCategory.CONSONANT),
        Phoneme("l", "/l/", "Tongue tip up, sides open, voiced.", listOf("leg", "yellow", "ball"), PhonicsCategory.CONSONANT),
        Phoneme("m", "/m/", "Lips closed, hum through the nose.", listOf("map", "hammer", "drum"), PhonicsCategory.CONSONANT),
        Phoneme("n", "/n/", "Tongue up, hum through the nose.", listOf("net", "dinner", "sun"), PhonicsCategory.CONSONANT),
        Phoneme("p", "/p/", "Lips together, then a quiet pop.", listOf("pig", "apple", "cup"), PhonicsCategory.CONSONANT),
        Phoneme("r", "/r/", "Tongue curls back, voiced growl.", listOf("red", "carrot", "car"), PhonicsCategory.CONSONANT),
        Phoneme("s", "/s/", "Tongue near teeth, hiss like a snake.", listOf("sun", "messy", "bus"), PhonicsCategory.CONSONANT),
        Phoneme("t", "/t/", "Tongue taps behind teeth, crisp.", listOf("top", "butter", "hat"), PhonicsCategory.CONSONANT),
        Phoneme("v", "/v/", "Teeth on lip like /f/, but voiced.", listOf("van", "river", "five"), PhonicsCategory.CONSONANT),
        Phoneme("w", "/w/", "Round the lips, then glide.", listOf("win", "away", "we"), PhonicsCategory.CONSONANT),
        Phoneme("y", "/j/", "Tongue high, glides into the vowel.", listOf("yes", "yellow", "yarn"), PhonicsCategory.CONSONANT),
        Phoneme("z", "/z/", "Like /s/ but with voice buzzing.", listOf("zip", "buzzer", "zoo"), PhonicsCategory.CONSONANT),
    )

    val shortVowels = listOf(
        Phoneme("a", "/æ/", "Open mouth — 'a' as in apple.", listOf("apple", "cat", "hat"), PhonicsCategory.SHORT_VOWEL),
        Phoneme("e", "/ɛ/", "Relaxed — 'e' as in egg.", listOf("egg", "bed", "pen"), PhonicsCategory.SHORT_VOWEL),
        Phoneme("i", "/ɪ/", "Short and crisp — 'i' as in igloo.", listOf("igloo", "sit", "pin"), PhonicsCategory.SHORT_VOWEL),
        Phoneme("o", "/ɒ/", "Round mouth — 'o' as in octopus.", listOf("octopus", "dog", "pot"), PhonicsCategory.SHORT_VOWEL),
        Phoneme("u", "/ʌ/", "Relaxed grunt — 'u' as in umbrella.", listOf("umbrella", "cup", "bus"), PhonicsCategory.SHORT_VOWEL),
    )

    val longVowels = listOf(
        Phoneme("a_e", "/eɪ/", "Magic-e makes 'a' say its name.", listOf("cake", "name", "gate"), PhonicsCategory.LONG_VOWEL),
        Phoneme("e_e", "/iː/", "Long e — 'ee' sound.", listOf("these", "theme", "Pete"), PhonicsCategory.LONG_VOWEL),
        Phoneme("i_e", "/aɪ/", "Magic-e makes 'i' say its name.", listOf("bike", "time", "kite"), PhonicsCategory.LONG_VOWEL),
        Phoneme("o_e", "/oʊ/", "Magic-e makes 'o' say its name.", listOf("home", "rose", "note"), PhonicsCategory.LONG_VOWEL),
        Phoneme("u_e", "/juː/", "Magic-e makes 'u' say its name.", listOf("cube", "mute", "tube"), PhonicsCategory.LONG_VOWEL),
    )

    val digraphs = listOf(
        Phoneme("sh", "/ʃ/", "Quiet 'shhh' — finger to lips.", listOf("ship", "fish", "shop"), PhonicsCategory.DIGRAPH),
        Phoneme("ch", "/tʃ/", "Like a sneezing train — 'ch-ch'.", listOf("chip", "lunch", "chair"), PhonicsCategory.DIGRAPH),
        Phoneme("th", "/θ/", "Tongue between teeth, unvoiced.", listOf("thin", "bath", "thumb"), PhonicsCategory.DIGRAPH),
        Phoneme("th", "/ð/", "Tongue between teeth, voiced.", listOf("this", "mother", "them"), PhonicsCategory.DIGRAPH),
        Phoneme("wh", "/w/", "Breathy 'w' — as in question words.", listOf("when", "wheel", "whale"), PhonicsCategory.DIGRAPH),
        Phoneme("ph", "/f/", "'ph' makes the /f/ sound.", listOf("phone", "graph", "dolphin"), PhonicsCategory.DIGRAPH),
        Phoneme("ck", "/k/", "Hard /k/ at the end of short words.", listOf("duck", "sock", "kick"), PhonicsCategory.DIGRAPH),
        Phoneme("ng", "/ŋ/", "Hum through the nose, back of tongue up.", listOf("ring", "song", "long"), PhonicsCategory.DIGRAPH),
    )

    val blends = listOf(
        Phoneme("bl", "/bl/", "Blend /b/ and /l/ smoothly.", listOf("blue", "black", "blink"), PhonicsCategory.BLEND),
        Phoneme("cl", "/kl/", "Blend /k/ and /l/.", listOf("clap", "clock", "cloud"), PhonicsCategory.BLEND),
        Phoneme("fl", "/fl/", "Blend /f/ and /l/.", listOf("flag", "flip", "fly"), PhonicsCategory.BLEND),
        Phoneme("gr", "/ɡr/", "Blend /g/ and /r/.", listOf("green", "grab", "grow"), PhonicsCategory.BLEND),
        Phoneme("tr", "/tr/", "Blend /t/ and /r/.", listOf("tree", "train", "trip"), PhonicsCategory.BLEND),
        Phoneme("st", "/st/", "Blend /s/ and /t/.", listOf("stop", "star", "best"), PhonicsCategory.BLEND),
        Phoneme("sp", "/sp/", "Blend /s/ and /p/.", listOf("spin", "spot", "wasp"), PhonicsCategory.BLEND),
        Phoneme("sn", "/sn/", "Blend /s/ and /n/.", listOf("snap", "snow", "snail"), PhonicsCategory.BLEND),
        Phoneme("str", "/str/", "Three-sound blend /s/+/t/+/r/.", listOf("street", "strong", "string"), PhonicsCategory.BLEND),
    )

    val vowelTeams = listOf(
        Phoneme("ai", "/eɪ/", "Two vowels, one long-a sound.", listOf("rain", "train", "paint"), PhonicsCategory.VOWEL_TEAM),
        Phoneme("ay", "/eɪ/", "Long-a, usually at word ends.", listOf("play", "day", "stay"), PhonicsCategory.VOWEL_TEAM),
        Phoneme("ee", "/iː/", "Long-e — 'see' sound.", listOf("see", "tree", "green"), PhonicsCategory.VOWEL_TEAM),
        Phoneme("ea", "/iː/", "Often long-e (but see exceptions!).", listOf("eat", "team", "leaf"), PhonicsCategory.VOWEL_TEAM),
        Phoneme("oa", "/oʊ/", "Long-o — 'boat' sound.", listOf("boat", "road", "coat"), PhonicsCategory.VOWEL_TEAM),
        Phoneme("ie", "/aɪ/", "Long-i — 'pie' sound.", listOf("pie", "tie", "lie"), PhonicsCategory.VOWEL_TEAM),
        Phoneme("igh", "/aɪ/", "Three letters, one long-i sound.", listOf("light", "night", "high"), PhonicsCategory.VOWEL_TEAM),
    )

    val diphthongs = listOf(
        Phoneme("oi", "/ɔɪ/", "Glide from 'o' to 'ee'.", listOf("coin", "oil", "boil"), PhonicsCategory.DIPHTHONG),
        Phoneme("oy", "/ɔɪ/", "Same as 'oi', usually at word ends.", listOf("boy", "toy", "joy"), PhonicsCategory.DIPHTHONG),
        Phoneme("ou", "/aʊ/", "Glide 'ah' to 'oo' — 'ouch!'.", listOf("out", "house", "cloud"), PhonicsCategory.DIPHTHONG),
        Phoneme("ow", "/aʊ/", "Same as 'ou' — 'cow' sound.", listOf("cow", "now", "down"), PhonicsCategory.DIPHTHONG),
        Phoneme("au", "/ɔː/", "Open 'aw' sound.", listOf("sauce", "haul", "August"), PhonicsCategory.DIPHTHONG),
        Phoneme("aw", "/ɔː/", "Same 'aw' sound, often at word ends.", listOf("saw", "paw", "draw"), PhonicsCategory.DIPHTHONG),
    )

    val rControlled = listOf(
        Phoneme("ar", "/ɑːr/", "'ar' as in car — the bossy r.", listOf("car", "star", "farm"), PhonicsCategory.R_CONTROLLED),
        Phoneme("or", "/ɔːr/", "'or' as in fork.", listOf("fork", "born", "storm"), PhonicsCategory.R_CONTROLLED),
        Phoneme("er", "/ɜːr/", "'er' as in her — schwa-r.", listOf("her", "fern", "term"), PhonicsCategory.R_CONTROLLED),
        Phoneme("ir", "/ɜːr/", "Same 'er' sound, spelt 'ir'.", listOf("bird", "girl", "shirt"), PhonicsCategory.R_CONTROLLED),
        Phoneme("ur", "/ɜːr/", "Same 'er' sound, spelt 'ur'.", listOf("turn", "hurt", "burn"), PhonicsCategory.R_CONTROLLED),
    )

    val softSounds = listOf(
        Phoneme("c → /s/", "/s/", "Soft c before e, i, or y.", listOf("city", "cent", "ice"), PhonicsCategory.SOFT_SOUND),
        Phoneme("g → /dʒ/", "/dʒ/", "Soft g before e, i, or y.", listOf("giant", "gem", "cage"), PhonicsCategory.SOFT_SOUND),
    )

    val phonemes: List<Phoneme> = consonants + shortVowels + longVowels +
        digraphs + blends + vowelTeams + diphthongs + rControlled + softSounds

    fun phonemes(category: PhonicsCategory): List<Phoneme> =
        phonemes.filter { it.category == category }

    // endregion

    // region Exception / irregular rules

    val exceptionRules = listOf(
        ExceptionRule(
            "Silent Letters",
            "Letters that are written but not pronounced.",
            "Many English words carry letters that have gone silent over centuries. The spelling preserves a word's history even though the sound has dropped. Learn the common silent-letter patterns rather than sounding every letter out.",
            listOf(
                RuleExample("knee", "Silent k before n — say 'nee'."),
                RuleExample("write", "Silent w before r — say 'rite'."),
                RuleExample("lamb", "Silent b after m — say 'lam'."),
                RuleExample("calf", "Silent l — say 'caf'."),
                RuleExample("listen", "Silent t — say 'lissen'."),
                RuleExample("hour", "Silent h — say 'our'."),
            ), RuleIcons.silent),

        ExceptionRule(
            "The Many Sounds of 'ough'",
            "One spelling, many pronunciations.",
            "'ough' is famous for being read several different ways. There is no single rule — these are best memorised as whole words.",
            listOf(
                RuleExample("though", "Sounds like 'oh'."),
                RuleExample("through", "Sounds like 'oo'."),
                RuleExample("rough", "Sounds like 'uff'."),
                RuleExample("cough", "Sounds like 'off'."),
                RuleExample("bought", "Sounds like 'awt'."),
                RuleExample("plough", "Sounds like 'ow'."),
            ), RuleIcons.ough),

        ExceptionRule(
            "Soft C and Soft G",
            "c and g soften before e, i, or y.",
            "Before the vowels e, i, or y, the letter c usually says /s/ and g usually says /j/. Before a, o, u (or a consonant) they keep their hard sounds /k/ and /g/. A handful of words break even this rule.",
            listOf(
                RuleExample("city", "Soft c → /s/ before i."),
                RuleExample("cat", "Hard c → /k/ before a."),
                RuleExample("giant", "Soft g → /j/ before i."),
                RuleExample("got", "Hard g → /g/ before o."),
                RuleExample("girl", "Exception — hard g before i."),
                RuleExample("get", "Exception — hard g before e."),
            ), RuleIcons.softCG),

        ExceptionRule(
            "'i' before 'e', except after 'c'",
            "A spelling guide with notable exceptions.",
            "The classic rhyme: 'i before e, except after c, or when sounding like \"ay\" as in neighbour and weigh.' It helps often — but English keeps a list of stubborn exceptions.",
            listOf(
                RuleExample("believe", "i before e — follows the rule."),
                RuleExample("receive", "e before i after c — follows the rule."),
                RuleExample("weigh", "'ay' sound — e before i."),
                RuleExample("weird", "Exception — e before i, no c."),
                RuleExample("science", "Exception — i before e after c."),
                RuleExample("their", "Exception — common sight word."),
            ), RuleIcons.iBeforeE),

        ExceptionRule(
            "Tricky Sight Words",
            "High-frequency words that defy phonics.",
            "Some of the most common words in English cannot be sounded out reliably. Because we read them so often, it is most efficient to recognise them instantly by sight.",
            listOf(
                RuleExample("the", "Schwa vowel — 'thuh'."),
                RuleExample("said", "Reads as 'sed', not 'say-id'."),
                RuleExample("was", "Reads as 'wuz'."),
                RuleExample("one", "Reads as 'wun'."),
                RuleExample("two", "Silent w — 'too'."),
                RuleExample("of", "f reads as /v/ — 'ov'."),
            ), RuleIcons.sightWords),

        ExceptionRule(
            "The Schwa — the lazy vowel",
            "Unstressed vowels relax to 'uh'.",
            "The schwa /ə/ is the most common vowel sound in English. In unstressed syllables, almost any vowel can collapse into a quick, neutral 'uh'. It is why spelling and sound drift apart in longer words.",
            listOf(
                RuleExample("banana", "The a's reduce to 'buh-NA-nuh'."),
                RuleExample("pencil", "The i reduces — 'pen-suhl'."),
                RuleExample("about", "The first a is a schwa — 'uh-bout'."),
                RuleExample("problem", "The e reduces — 'prob-luhm'."),
                RuleExample("sofa", "The final a is a schwa — 'so-fuh'."),
            ), RuleIcons.schwa),

        ExceptionRule(
            "Doubling & Drop-the-e",
            "How endings change a word's spelling.",
            "When adding endings like -ing or -ed: double the final consonant after a short stressed vowel (run → running); and drop a silent e before a vowel ending (make → making). These keep the vowel sound consistent.",
            listOf(
                RuleExample("running", "Double n — keeps the short u."),
                RuleExample("hoping", "Drop e — keeps the long o."),
                RuleExample("hopping", "Double p — keeps the short o."),
                RuleExample("making", "Drop e before -ing."),
                RuleExample("stopped", "Double p before -ed."),
            ), RuleIcons.doubling),
    )

    // endregion

    // region Practice content

    val minimalPairs = listOf(
        MinimalPair("ship", "sheep", "short i /ɪ/ vs long e /iː/"),
        MinimalPair("bit", "beat", "short i /ɪ/ vs long e /iː/"),
        MinimalPair("pen", "pan", "short e /ɛ/ vs short a /æ/"),
        MinimalPair("cot", "caught", "short o /ɒ/ vs 'aw' /ɔː/"),
        MinimalPair("thin", "this", "unvoiced /θ/ vs voiced /ð/"),
        MinimalPair("vine", "fine", "voiced /v/ vs unvoiced /f/"),
        MinimalPair("rice", "rise", "unvoiced /s/ vs voiced /z/"),
        MinimalPair("cap", "cab", "unvoiced /p/ vs voiced /b/"),
        MinimalPair("wet", "vet", "/w/ vs /v/"),
        MinimalPair("light", "right", "/l/ vs /r/"),
    )

    val tongueTwisters = listOf(
        PracticeLine("She sells sea shells by the sea shore.", "/ʃ/ and /s/ contrast"),
        PracticeLine("Red lorry, yellow lorry, red lorry, yellow lorry.", "/l/ and /r/ blends"),
        PracticeLine("The thirty-three thieves thought they thrilled the throne.", "/θ/ digraph"),
        PracticeLine("Peter Piper picked a peck of pickled peppers.", "/p/ plosive"),
        PracticeLine("Six slippery snails slid slowly seaward.", "/s/ blends"),
        PracticeLine("Fresh fried fish, fish fresh fried.", "/f/ and /r/ blends"),
    )

    val shortMessages = listOf(
        PracticeLine("Good morning! How are you today?", "greeting intonation"),
        PracticeLine("Thank you so much for your help.", "gratitude, soft tone"),
        PracticeLine("Can you please say that again?", "polite request, rising end"),
        PracticeLine("I'll be there in five minutes.", "contractions and linking"),
        PracticeLine("That sounds like a great idea.", "long vowel teams"),
        PracticeLine("Let's catch up later this week.", "blends and short vowels"),
        PracticeLine("Have a wonderful weekend!", "warm closing tone"),
        PracticeLine("No worries, take your time.", "reassuring, calm pace"),
    )

    val sentenceDrills = listOf(
        PracticeLine("The quick brown fox jumps over the lazy dog.", "all-letter warm-up"),
        PracticeLine("Sarah parked her car in the dark farmyard.", "r-controlled 'ar'"),
        PracticeLine("Bright lights at night give me a fright.", "'igh' long-i team"),
        PracticeLine("The boy enjoyed the noisy toy.", "/ɔɪ/ diphthong"),
        PracticeLine("Please leave the green peas on the plate.", "long-e vowel teams"),
        PracticeLine("Three thin thieves thanked the theatre.", "/θ/ in connected speech"),
    )

    fun lines(kind: PracticeKind): List<PracticeLine> = when (kind) {
        PracticeKind.TONGUE_TWISTERS -> tongueTwisters
        PracticeKind.SHORT_MESSAGES -> shortMessages
        PracticeKind.SENTENCES -> sentenceDrills
        PracticeKind.MINIMAL_PAIRS -> emptyList()   // handled via minimalPairs
    }

    // endregion

    /** Emotion / intonation scripts. */
    val emotionScripts = listOf(
        "We won the game!",
        "I can't believe it's already over.",
        "Are you coming to the party?",
        "Everything is going to be okay.",
        "Please don't touch that.",
        "That was the best day of my life.",
        "I really need to talk to you.",
        "Look at how big it has grown.",
    )
}
