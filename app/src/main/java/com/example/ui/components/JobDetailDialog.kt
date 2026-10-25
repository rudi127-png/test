package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.TerracottaRed
import com.example.ui.theme.WineBurgundy

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JobDetailDialog(
    job: JobOffer,
    onDismiss: () -> Unit,
    onToggleBookmark: () -> Unit,
    onUpdateStatusAndNotes: (status: ApplicationStatus, notes: String) -> Unit,
    onGenerateLetter: () -> Unit,
    onGenerateSummary: () -> Unit,
    onGenerateInterviewPrep: () -> Unit
) {
    var currentStatus by remember { mutableStateOf(job.applicationStatus) }
    var personalNotes by remember { mutableStateOf(job.notes) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "🎭 ${job.title}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = WineBurgundy
                    )
                    Text(
                        text = "— ${job.institution}",
                        fontSize = 14.sp,
                        color = TerracottaRed,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Fermer")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Info Box
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFAF5EF), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("• Lieu & Distance : ${job.location} (${job.distanceKm} km de Charleroi)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text("• Contrat : ${job.contractType}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TerracottaRed)
                    Text("• Domaine : ${job.sector}", fontSize = 13.sp)
                    Text("• Source Officielle : ${job.source}", fontSize = 13.sp, color = Color(0xFF3F644A), fontWeight = FontWeight.Bold)
                }

                // Profile
                Column {
                    Text("• Profil recherché :", fontWeight = FontWeight.Bold, color = WineBurgundy, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(job.profile, fontSize = 13.sp, color = Color(0xFF2C2523))
                }

                // Missions
                Column {
                    Text("• Missions synthétiques :", fontWeight = FontWeight.Bold, color = WineBurgundy, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(job.missions, fontSize = 13.sp, color = Color(0xFF2C2523))
                }

                // AI Tools Section for Aline
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFDE8E3), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFF8C4B8), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = TerracottaRed)
                        Text(
                            text = " Mon Assistant IA Culture (Aline)",
                            fontWeight = FontWeight.Bold,
                            color = TerracottaRed,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onGenerateLetter,
                        colors = ButtonDefaults.buttonColors(containerColor = TerracottaRed),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dialog_generate_letter_button")
                    ) {
                        Text("📜 Générer une lettre de motivation")
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Button(
                            onClick = onGenerateSummary,
                            colors = ButtonDefaults.buttonColors(containerColor = WineBurgundy),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("✨ Résumé & Atouts", fontSize = 12.sp)
                        }

                        Button(
                            onClick = onGenerateInterviewPrep,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F644A)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("🗓️ Préparer entretien", fontSize = 12.sp)
                        }
                    }
                }

                // Application Status Tracker
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFFDF9), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFE8DCD0), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text("📝 Mon suivi de candidature", fontWeight = FontWeight.Bold, color = WineBurgundy, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ApplicationStatus.values().forEach { status ->
                            val isSelected = currentStatus == status
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) Color(status.containerColorHex) else Color(0xFFF5EFE6),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, Color(status.contentColorHex)) else null,
                                modifier = Modifier.clickable {
                                    currentStatus = status
                                    onUpdateStatusAndNotes(status, personalNotes)
                                }
                            ) {
                                Text(
                                    text = "${status.emoji} ${status.label}",
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color(status.contentColorHex) else Color.DarkGray,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Notes personnelles d'Aline :", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = personalNotes,
                        onValueChange = {
                            personalNotes = it
                            onUpdateStatusAndNotes(currentStatus, it)
                        },
                        placeholder = { Text("ex: CV envoyé le 24/10, relance prévue mardi...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .testTag("dialog_personal_notes_input"),
                        maxLines = 3
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onUpdateStatusAndNotes(currentStatus, personalNotes)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = TerracottaRed)
            ) {
                Text("Enregistrer")
            }
        }
    )
}
