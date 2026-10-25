package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.JobOffer
import com.example.ui.components.CompactFilterBar
import com.example.ui.components.RefreshNoticeBanner
import com.example.ui.components.JobCard
import com.example.ui.theme.TerracottaRed
import com.example.ui.theme.WineBurgundy
import com.example.ui.viewmodel.JobViewModel

@Composable
fun HomeScreen(
    viewModel: JobViewModel,
    onOpenFilterDialog: () -> Unit,
    onSelectJobDetail: (JobOffer) -> Unit,
    onOpenAiHelp: (JobOffer) -> Unit
) {
    val filterState by viewModel.filterState.collectAsState()
    val filteredJobs by viewModel.filteredJobs.collectAsState()
    val isRefreshedNoticeVisible by viewModel.isRefreshedNoticeVisible.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Hero Header Banner
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_hero_culture_1792769024369),
                            contentDescription = "Bannière Culture Charleroi",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    androidx.compose.ui.graphics.Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color(0xAA4A1525))
                                    )
                                )
                        )
                        Text(
                            text = "Only4Aline 🎭",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = Color.White,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Espace Emploi Administratif • Arts & Culture",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = WineBurgundy
                        )
                        Text(
                            text = "Offres de gestion, secrétariat, production et logistique culturelle (Charleroi & 50km).",
                            fontSize = 13.sp,
                            color = Color(0xFF52443D)
                        )
                    }
                }
            }
        }

        // 24h Auto-Refresh Banner Notice if triggered
        if (isRefreshedNoticeVisible) {
            item {
                RefreshNoticeBanner(
                    onDismiss = { viewModel.dismissRefreshNotice() }
                )
            }
        }

        // Compact Filter Bar (MANDATORY REQUIREMENT)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Filtres actifs :",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = WineBurgundy
                )
                CompactFilterBar(
                    filterState = filterState,
                    onOpenFilterDialog = onOpenFilterDialog
                )
            }
        }

        // Search Bar & Manual Refresh Button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = filterState.keyword,
                    onValueChange = { viewModel.updateFilters(keyword = it) },
                    placeholder = { Text("Rechercher un poste ou lieu...", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = WineBurgundy) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("home_search_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                IconButton(
                    onClick = { viewModel.refreshSearchManual() },
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF3EBE1))
                        .testTag("manual_refresh_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Réactualiser les offres",
                        tint = TerracottaRed
                    )
                }
            }
        }

        // Results Header Count
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎭 ${filteredJobs.size} offre(s) disponible(s)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = WineBurgundy,
                    fontFamily = FontFamily.Serif
                )

                Text(
                    text = "Zone : ${filterState.rayonKm} km",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TerracottaRed
                )
            }
        }

        // Job Offers List
        if (filteredJobs.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
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
                        Text("🎨", fontSize = 40.sp)
                        Text(
                            text = "Aucune offre ne correspond à ces critères.",
                            fontWeight = FontWeight.Bold,
                            color = WineBurgundy
                        )
                        Text(
                            text = "Essaie d'élargir le rayon ou de modifier les filtres de contrat.",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.updateFilters(rayonKm = 50, contractFilter = "Tous", keyword = "") },
                            colors = ButtonDefaults.buttonColors(containerColor = TerracottaRed)
                        ) {
                            Text("Réinitialiser les filtres")
                        }
                    }
                }
            }
        } else {
            items(filteredJobs, key = { it.id }) { job ->
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
