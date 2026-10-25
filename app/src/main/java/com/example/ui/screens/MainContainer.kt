package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.data.model.JobOffer
import com.example.ui.components.AiLetterDialog
import com.example.ui.components.FilterDialog
import com.example.ui.components.JobDetailDialog
import com.example.ui.theme.TerracottaRed
import com.example.ui.theme.WineBurgundy
import com.example.ui.viewmodel.JobViewModel

@Composable
fun MainContainer(
    viewModel: JobViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedJobDetail by remember { mutableStateOf<JobOffer?>(null) }

    val filterState by viewModel.filterState.collectAsState()
    val aiLoading by viewModel.aiLoading.collectAsState()
    val aiDialogTitle by viewModel.aiDialogTitle.collectAsState()
    val aiDialogContent by viewModel.aiDialogContent.collectAsState()

    // Handle Back Button gracefully
    BackHandler(enabled = selectedTab != 0 || selectedJobDetail != null || showFilterDialog) {
        if (selectedJobDetail != null) {
            selectedJobDetail = null
        } else if (showFilterDialog) {
            showFilterDialog = false
        } else if (selectedTab != 0) {
            selectedTab = 0
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFFFFDF9),
                contentColor = WineBurgundy
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.TheaterComedy, contentDescription = "Offres") },
                    label = { Text("Offres (50km)", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TerracottaRed,
                        selectedTextColor = TerracottaRed,
                        indicatorColor = Color(0xFFFDE8E3)
                    ),
                    modifier = Modifier.testTag("nav_tab_offers")
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoris") },
                    label = { Text("Favoris & Suivi", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TerracottaRed,
                        selectedTextColor = TerracottaRed,
                        indicatorColor = Color(0xFFFDE8E3)
                    ),
                    modifier = Modifier.testTag("nav_tab_favorites")
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "Assistant IA") },
                    label = { Text("Assistant Aline", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TerracottaRed,
                        selectedTextColor = TerracottaRed,
                        indicatorColor = Color(0xFFFDE8E3)
                    ),
                    modifier = Modifier.testTag("nav_tab_ai_assistant")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> HomeScreen(
                    viewModel = viewModel,
                    onOpenFilterDialog = { showFilterDialog = true },
                    onSelectJobDetail = { selectedJobDetail = it },
                    onOpenAiHelp = { job ->
                        selectedJobDetail = job
                        viewModel.generateJobSummary(job)
                    }
                )
                1 -> FavoritesScreen(
                    viewModel = viewModel,
                    onSelectJobDetail = { selectedJobDetail = it },
                    onOpenAiHelp = { job ->
                        selectedJobDetail = job
                        viewModel.generateJobSummary(job)
                    }
                )
                2 -> AiAssistantScreen(
                    viewModel = viewModel
                )
            }
        }

        // Filter Dialog
        if (showFilterDialog) {
            FilterDialog(
                filterState = filterState,
                onDismiss = { showFilterDialog = false },
                onApplyFilters = { rayon, contract, pub, exp, kw ->
                    viewModel.updateFilters(
                        rayonKm = rayon,
                        contractFilter = contract,
                        publicationFilter = pub,
                        experienceFilter = exp,
                        keyword = kw
                    )
                }
            )
        }

        // Job Detail Dialog
        selectedJobDetail?.let { job ->
            JobDetailDialog(
                job = job,
                onDismiss = { selectedJobDetail = null },
                onToggleBookmark = { viewModel.toggleBookmark(job) },
                onUpdateStatusAndNotes = { status, notes ->
                    viewModel.updateJobStatus(job.id, status, notes)
                },
                onGenerateLetter = { viewModel.generateCoverLetter(job) },
                onGenerateSummary = { viewModel.generateJobSummary(job) },
                onGenerateInterviewPrep = { viewModel.generateInterviewPrep(job) }
            )
        }

        // AI Text Dialog (Cover Letter, Summary, Interview Prep)
        if (aiLoading || aiDialogContent != null) {
            AiLetterDialog(
                title = aiDialogTitle,
                content = aiDialogContent,
                isLoading = aiLoading,
                onDismiss = { viewModel.dismissAiDialog() }
            )
        }
    }
}
