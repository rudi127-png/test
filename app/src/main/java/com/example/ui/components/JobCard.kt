package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ApplicationStatus
import com.example.data.model.JobOffer
import com.example.ui.theme.TerracottaRed
import com.example.ui.theme.WineBurgundy

@Composable
fun JobCard(
    job: JobOffer,
    onToggleBookmark: () -> Unit,
    onClickDetail: () -> Unit,
    onOpenAiHelp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClickDetail() }
            .testTag("job_card_${job.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Row: Title & Institution + Favorite Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "🎭 ${job.title}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = WineBurgundy,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "— ${job.institution}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TerracottaRed
                    )
                }

                // Heart / Bookmark Toggle
                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier.testTag("bookmark_button_${job.id}")
                ) {
                    Icon(
                        imageVector = if (job.isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (job.isSaved) "Retirer des favoris" else "Mettre en favoris",
                        tint = if (job.isSaved) TerracottaRed else Color.Gray
                    )
                }
            }

            // Application Status Badge if active
            if (job.applicationStatus != ApplicationStatus.NOT_APPLIED) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(job.applicationStatus.containerColorHex),
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${job.applicationStatus.emoji} Statut : ${job.applicationStatus.label}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(job.applicationStatus.contentColorHex)
                        )
                    }
                }
            }

            // Bullet 1: Lieu & Distance | Contrat
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFAF5EF), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "• Lieu & Distance : ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = WineBurgundy
                )
                Text(
                    text = "${job.location} (${job.distanceKm} km de Charleroi) | ",
                    fontSize = 13.sp,
                    color = Color.Black
                )
                Text(
                    text = "Contrat : ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = WineBurgundy
                )
                Text(
                    text = job.contractType,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TerracottaRed
                )
            }

            // Bullet 2: Profil
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFFDF9), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFF0E4D8), RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Row {
                    Text(
                        text = "• Profil : ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = WineBurgundy
                    )
                    Text(
                        text = job.profile,
                        fontSize = 13.sp,
                        color = Color(0xFF2C2523)
                    )
                }
            }

            // Bullet 3: Missions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFFDF9), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFF0E4D8), RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Row {
                    Text(
                        text = "• Missions : ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = WineBurgundy
                    )
                    Text(
                        text = job.missions,
                        fontSize = 13.sp,
                        color = Color(0xFF2C2523)
                    )
                }
            }

            // Bullet 4: Source official
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "• Source : ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = WineBurgundy
                    )
                    Text(
                        text = job.source,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF3F644A)
                    )
                }

                Text(
                    text = job.postedDate,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            // Footer Actions Row: Details & AI Help
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onOpenAiHelp,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("ai_help_button_${job.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Aide IA Aline",
                        tint = TerracottaRed,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text("Aide IA Aline", color = TerracottaRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = onClickDetail,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("job_detail_button_${job.id}")
                ) {
                    Text("Détails & Suivi", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
