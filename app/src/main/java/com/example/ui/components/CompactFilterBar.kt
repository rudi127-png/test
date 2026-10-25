package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun CompactFilterBar(
    filterState: SearchFilterState,
    onOpenFilterDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF3EBE1), RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE5D5C5), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Edit button
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(TerracottaRed)
                .clickable { onOpenFilterDialog() }
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .testTag("filter_tune_button"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "Modifier filtres",
                tint = Color.White,
                modifier = Modifier.padding(1.dp)
            )
            Text(
                text = "Filtres",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Badge 1: Rayon
        FilterBadgeChip(
            text = "[📍 Rayon : ${filterState.rayonKm}km Charleroi]",
            onClick = onOpenFilterDialog
        )

        // Badge 2: Contrat
        FilterBadgeChip(
            text = "[💼 Contrat : ${filterState.contractFilter}]",
            onClick = onOpenFilterDialog
        )

        // Badge 3: Publication
        FilterBadgeChip(
            text = "[🗓️ Publication : ${filterState.publicationFilter}]",
            onClick = onOpenFilterDialog
        )

        // Badge 4: Expérience
        FilterBadgeChip(
            text = "[🎓 Expérience : ${filterState.experienceFilter}]",
            onClick = onOpenFilterDialog
        )
    }
}

@Composable
private fun FilterBadgeChip(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = WineBurgundy,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFD3C2B3), RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}
