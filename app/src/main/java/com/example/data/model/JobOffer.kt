package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class ApplicationStatus(
    val label: String,
    val emoji: String,
    val containerColorHex: Long,
    val contentColorHex: Long
) {
    NOT_APPLIED("À postuler", "📜", 0xFFF3EBE1, 0xFF5B1D2E),
    PREPARED("Candidature prête", "📝", 0xFFEBF3FA, 0xFF1D4ED8),
    SENT("Candidature envoyée", "✉️", 0xFFE2EFE0, 0xFF15803D),
    INTERVIEW("Entretien prévu", "🗓️", 0xFFFEF3C7, 0xFFB45309),
    ACCEPTED("Offre acceptée", "🎉", 0xFFDCFCE7, 0xFF166534),
    DECLINED("Non retenu(e)", "💙", 0xFFFEE2E2, 0xFFB91C1C)
}

data class JobOffer(
    val id: String,
    val title: String,
    val institution: String,
    val location: String,
    val distanceKm: Int,
    val contractType: String, // CDI, CDD, Stage, etc.
    val profile: String,
    val missions: String,
    val source: String, // Le Forem, Culture.be, Site officiel, etc.
    val postedDate: String,
    val postedTimestamp: Long,
    val sector: String, // Administration, Production, Secrétariat, Coordination, Logistique
    val isSaved: Boolean = false,
    val applicationStatus: ApplicationStatus = ApplicationStatus.NOT_APPLIED,
    val notes: String = ""
)
