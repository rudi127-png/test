package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ApplicationStatus
import com.example.data.model.JobOffer
import com.example.ui.components.JobCard
import com.example.ui.theme.TerracottaRed
import com.example.ui.theme.WineBurgundy
import com.example.ui.viewmodel.JobViewModel

@Composable
fun FavoritesScreen(
    viewModel: JobViewModel,
    onSelectJobDetail: (JobOffer) -> Unit,
    onOpenAiHelp: (JobOffer) -> Unit
) {
    val savedJobs by viewModel.savedJobs.collectAsState()
    var statusFilter by remember { mutableStateOf("Toutes") }

    val filteredSaved = when (statusFilter) {
        "À postuler" -> savedJobs.filter { it.applicationStatus == ApplicationStatus.NOT_APPLIED || it.applicationStatus == ApplicationStatus.PREPARED }
        "Envoyées" -> savedJobs.filter { it.applicationStatus == ApplicationStatus.SENT }
        "Entretiens" -> savedJobs.filter { it.applicationStatus == ApplicationStatus.INTERVIEW }
        else -> savedJobs
    }

    val totalSent = savedJobs.count { it.applicationStatus == ApplicationStatus.SENT }
    val totalInterviews = savedJobs.count { it.applicationStatus == ApplicationStatus.INTERVIEW }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "💙 Mes Favoris & Candidatures d'Aline",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = WineBurgundy
            )
            Text(
                text = "Tes offres sauvegardées restent conservées lors des réactualisations de 24h.",
                fontSize = 13.sp,
                color = Color(0xFF52443D)
            )
        }

        // Pipeline Stats Dashboard Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Stat 1
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${savedJobs.size}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = WineBurgundy)
                        Text("Favoris", fontSize = 12.sp, color = Color.Gray)
                    }

                    Box(
                        modifier = Modifier
                            .height(30.dp)
                            .padding(horizontal = 1.dp)
                            .background(Color.LightGray)
                    )

                    // Stat 2
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$totalSent ✉️", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
                        Text("Envoyées", fontSize = 12.sp, color = Color.Gray)
                    }

                    Box(
                        modifier = Modifier
                            .height(30.dp)
                            .padding(horizontal = 1.dp)
                            .background(Color.LightGray)
                    )

                    // Stat 3
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$totalInterviews 🗓️", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                        Text("Entretiens", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }

        // Status Filter Chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("Toutes", "À postuler", "Envoyées", "Entretiens").forEach { label ->
                    val selected = statusFilter == label
                    FilterChip(
                        selected = selected,
                        onClick = { statusFilter = label },
                        label = { Text(label, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TerracottaRed,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // List or Empty state
        if (filteredSaved.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("📜", fontSize = 44.sp)
                        Text(
                            text = if (savedJobs.isEmpty()) "Aucune offre sauvegardée pour le moment." else "Aucune candidature dans ce statut.",
                            fontWeight = FontWeight.Bold,
                            color = WineBurgundy
                        )
                        Text(
                            text = "Clique sur le cœur 🤍 d'une offre pour la sauvegarder dans tes favoris.",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        } else {
            items(filteredSaved, key = { it.id }) { job ->
                JobCard(
                    job = job,
                    onToggleBookmark = { viewModel.toggleBookmark(job) },
                    onClickDetail = { onSelectJobDetail(job) },
                    onOpenAiHelp = { onOpenAiHelp(job) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
