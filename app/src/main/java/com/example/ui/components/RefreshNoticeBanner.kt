package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TerracottaRed
import com.example.ui.theme.WineBurgundy

@Composable
fun RefreshNoticeBanner(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFFEF3C7), RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(12.dp))
            .padding(12.dp)
            .testTag("24h_refresh_notice_banner"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Réactualisé",
                tint = Color(0xFFB45309)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "✨ Recherche réactualisée (24h)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF92400E)
                )
                Text(
                    text = "La dernière recherche datant de plus de 24h, les offres ont été mises à jour. Seules tes offres sauvegardées en favoris ont été conservées.",
                    fontSize = 12.sp,
                    color = Color(0xFF78350F)
                )
            }
        }

        IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("dismiss_refresh_banner_button")
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Fermer notification",
                tint = Color(0xFF92400E)
            )
        }
    }
}
