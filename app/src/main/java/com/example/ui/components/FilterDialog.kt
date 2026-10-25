package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TerracottaRed
import com.example.ui.theme.WineBurgundy
import com.example.ui.viewmodel.SearchFilterState

@Composable
fun FilterDialog(
    filterState: SearchFilterState,
    onDismiss: () -> Unit,
    onApplyFilters: (rayonKm: Int, contract: String, publication: String, experience: String, keyword: String) -> Unit
) {
    var tempRayon by remember { mutableStateOf(filterState.rayonKm) }
    var tempContract by remember { mutableStateOf(filterState.contractFilter) }
    var tempPublication by remember { mutableStateOf(filterState.publicationFilter) }
    var tempExperience by remember { mutableStateOf(filterState.experienceFilter) }
    var tempKeyword by remember { mutableStateOf(filterState.keyword) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "🎨 Personnaliser la recherche pour Aline",
                    style = MaterialTheme.typography.titleMedium,
                    color = WineBurgundy
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Keyword Search
                Column {
                    Text("🔍 Mots-clés / Poste / Mot-clé", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = tempKeyword,
                        onValueChange = { tempKeyword = it },
                        placeholder = { Text("ex: Secrétariat, Production, FWB...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("filter_keyword_input"),
                        singleLine = true
                    )
                }

                // Rayon autour de Charleroi
                Column {
                    Text("📍 Rayon autour de Charleroi (km)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(10, 25, 50, 75, 100).forEach { km ->
                            val selected = tempRayon == km
                            FilterChip(
                                selected = selected,
                                onClick = { tempRayon = km },
                                label = { Text("${km}km", fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = TerracottaRed,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Type de contrat
                Column {
                    Text("💼 Type de contrat", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Tous", "CDD / CDI", "CDI", "CDD", "Stage").forEach { c ->
                            val selected = tempContract == c
                            FilterChip(
                                selected = selected,
                                onClick = { tempContract = c },
                                label = { Text(c, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = TerracottaRed,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Date de publication
                Column {
                    Text("🗓️ Date de publication", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Tous", "Aujourd'hui", "7 jours").forEach { p ->
                            val selected = tempPublication == p
                            FilterChip(
                                selected = selected,
                                onClick = { tempPublication = p },
                                label = { Text(p, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = TerracottaRed,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Expérience
                Column {
                    Text("🎓 Niveau d'expérience", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Tous", "Débutant", "Confirmé").forEach { e ->
                            val selected = tempExperience == e
                            FilterChip(
                                selected = selected,
                                onClick = { tempExperience = e },
                                label = { Text(e, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = TerracottaRed,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onApplyFilters(tempRayon, tempContract, tempPublication, tempExperience, tempKeyword)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = TerracottaRed),
                modifier = Modifier.testTag("apply_filters_button")
            ) {
                Text("Appliquer les filtres")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}
