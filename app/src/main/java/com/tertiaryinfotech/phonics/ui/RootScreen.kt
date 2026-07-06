package com.tertiaryinfotech.phonics.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.tertiaryinfotech.phonics.data.PracticeKind
import com.tertiaryinfotech.phonics.speech.Speaker

/** Root bottom-tab navigation: Learn · Practice · Rules · Feedback · About — mirrors the iOS MainTabView. */
@Composable
fun RootScreen(speaker: Speaker) {
    var tab by remember { mutableIntStateOf(0) }
    var learnDestination by remember { mutableStateOf<LearnDestination>(LearnDestination.Home) }
    var practiceDestination by remember { mutableStateOf<PracticeDestination>(PracticeDestination.Home) }
    var rulesDestination by remember { mutableStateOf<RulesDestination>(RulesDestination.Home) }

    // Hardware/gesture back pops to the tab's home screen.
    BackHandler(enabled = tab == 0 && learnDestination != LearnDestination.Home) {
        learnDestination = when (learnDestination) {
            is LearnDestination.Detail ->
                LearnDestination.Category((learnDestination as LearnDestination.Detail).phoneme.category)
            else -> LearnDestination.Home
        }
    }
    BackHandler(enabled = tab == 1 && practiceDestination != PracticeDestination.Home) {
        practiceDestination = PracticeDestination.Home
    }
    BackHandler(enabled = tab == 2 && rulesDestination != RulesDestination.Home) {
        rulesDestination = RulesDestination.Home
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf(
                    Triple("Learn", Icons.AutoMirrored.Filled.MenuBook, 0),
                    Triple("Practice", Icons.Filled.Mic, 1),
                    Triple("Rules", Icons.Filled.WarningAmber, 2),
                    Triple("Feedback", Icons.AutoMirrored.Filled.Chat, 3),
                    Triple("About", Icons.Filled.Info, 4),
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
                0 -> when (val d = learnDestination) {
                    LearnDestination.Home -> LearnHomeScreen(speaker) { learnDestination = it }
                    is LearnDestination.Category -> PhonemeListScreen(
                        d.category, speaker,
                        onOpen = { learnDestination = LearnDestination.Detail(it) },
                        onBack = { learnDestination = LearnDestination.Home })
                    is LearnDestination.Detail -> PhonemeDetailScreen(
                        d.phoneme, speaker,
                        onBack = { learnDestination = LearnDestination.Category(d.phoneme.category) })
                    LearnDestination.Settings -> SettingsScreen(
                        speaker, onBack = { learnDestination = LearnDestination.Home })
                }
                1 -> when (val d = practiceDestination) {
                    PracticeDestination.Home -> PracticeHomeScreen(speaker) { practiceDestination = it }
                    PracticeDestination.Emotion -> EmotionPracticeScreen(
                        speaker, onBack = { practiceDestination = PracticeDestination.Home })
                    is PracticeDestination.Kind ->
                        if (d.kind == PracticeKind.MINIMAL_PAIRS) MinimalPairsScreen(
                            speaker, onBack = { practiceDestination = PracticeDestination.Home })
                        else PracticeLinesScreen(
                            d.kind, speaker, onBack = { practiceDestination = PracticeDestination.Home })
                }
                2 -> when (val d = rulesDestination) {
                    RulesDestination.Home -> RulesHomeScreen(speaker) {
                        rulesDestination = RulesDestination.Detail(it)
                    }
                    is RulesDestination.Detail -> RuleDetailScreen(
                        d.rule, speaker, onBack = { rulesDestination = RulesDestination.Home })
                }
                3 -> FeedbackScreen()
                else -> AboutScreen()
            }
        }
    }
}
