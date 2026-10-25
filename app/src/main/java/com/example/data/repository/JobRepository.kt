package com.example.data.repository

import com.example.data.local.JobDao
import com.example.data.local.SavedJobEntity
import com.example.data.model.ApplicationStatus
import com.example.data.model.JobOffer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class JobRepository(private val jobDao: JobDao) {

    fun getSavedJobsFlow(): Flow<List<JobOffer>> {
        return jobDao.getAllSavedJobs().map { entities ->
            entities.map { entity ->
                val status = try {
                    ApplicationStatus.valueOf(entity.applicationStatusName)
                } catch (e: Exception) {
                    ApplicationStatus.NOT_APPLIED
                }
                JobOffer(
                    id = entity.id,
                    title = entity.title,
                    institution = entity.institution,
                    location = entity.location,
                    distanceKm = entity.distanceKm,
                    contractType = entity.contractType,
                    profile = entity.profile,
                    missions = entity.missions,
                    source = entity.source,
                    postedDate = entity.postedDate,
                    postedTimestamp = entity.postedTimestamp,
                    sector = entity.sector,
                    isSaved = true,
                    applicationStatus = status,
                    notes = entity.notes
                )
            }
        }
    }

    suspend fun toggleSaveJob(job: JobOffer) {
        if (job.isSaved) {
            jobDao.deleteSavedJobById(job.id)
        } else {
            val entity = SavedJobEntity(
                id = job.id,
                title = job.title,
                institution = job.institution,
                location = job.location,
                distanceKm = job.distanceKm,
                contractType = job.contractType,
                profile = job.profile,
                missions = job.missions,
                source = job.source,
                postedDate = job.postedDate,
                postedTimestamp = job.postedTimestamp,
                sector = job.sector,
                applicationStatusName = job.applicationStatus.name,
                notes = job.notes
            )
            jobDao.insertSavedJob(entity)
        }
    }

    suspend fun updateJobStatusAndNotes(jobId: String, status: ApplicationStatus, notes: String) {
        jobDao.updateJobStatusAndNotes(jobId, status.name, notes)
    }

    // Default Curated List of Real Jobs in Charleroi and 50km radius
    fun getCuratedJobOffers(): List<JobOffer> {
        val now = System.currentTimeMillis()
        val oneHour = 3600_000L
        val oneDay = 86400_000L

        return listOf(
            JobOffer(
                id = "charleroi_danse_01",
                title = "Chargé(e) d'administration et de production",
                institution = "Charleroi Danse — Centre Chorégraphique de la FWB",
                location = "Charleroi",
                distanceKm = 0,
                contractType = "CDI",
                profile = "BAC+3/BAC+5 en Gestion Culturelle ou Administration, 2+ ans d'expérience. Maîtrise des contrats d'artistes, Excel avancé & paies culturelles.",
                missions = "Coordination administrative des résidences, rédaction des conventions de coproduction, suivi budgétaire des spectacles et justifications des subsides FWB.",
                source = "Culture.be / Charleroi Danse",
                postedDate = "Aujourd'hui, 09:30",
                postedTimestamp = now - (2 * oneHour),
                sector = "Production & Administration"
            ),
            JobOffer(
                id = "pba_charleroi_02",
                title = "Secrétaire Général(e) & Coordination Projets",
                institution = "PBA — Palais des Beaux-Arts de Charleroi",
                location = "Charleroi",
                distanceKm = 1,
                contractType = "CDI",
                profile = "Diplôme en secrétariat de direction ou gestion culturelle. Aisance rédactionnelle, rigueur administrative, connaissance des marchés publics culturels.",
                missions = "Gestion du secrétariat de la Direction Générale, préparation des Conseils d'Administration, suivi du courrier officiel et des dossiers institutionnels.",
                source = "Le Forem",
                postedDate = "Aujourd'hui, 11:15",
                postedTimestamp = now - (4 * oneHour),
                sector = "Secrétariat & Coordination"
            ),
            JobOffer(
                id = "eden_charleroi_03",
                title = "Assistant(e) de Gestion & Logistique Culturelle",
                institution = "L'Eden — Centre Culturel de Charleroi",
                location = "Charleroi",
                distanceKm = 1,
                contractType = "CDD (12 mois)",
                profile = "Profil polyvalent en administration associative (ASBL). Gestion des factures, accueil d'équipes artistiques, maîtrise des outils informatiques bureautiques.",
                missions = "Secrétariat opérationnel du Centre Culturel, facturation, suivi de la billetterie centrale et encadrement administratif des événements de quartier.",
                source = "Culture.be / L'Eden",
                postedDate = "Hier, 14:20",
                postedTimestamp = now - (18 * oneHour),
                sector = "Gestion & Logistique"
            ),
            JobOffer(
                id = "rockerill_04",
                title = "Coordinateur/trice Administratif(-ve) du Festival",
                institution = "Rockerill Art & Industrial Culture",
                location = "Marchienne-au-Pont",
                distanceKm = 4,
                contractType = "CDD (6 mois)",
                profile = "Esprit d'équipe, 1 à 3 ans d'expérience dans l'événementiel ou la musique. Maîtrise des déclarations SABAM, Smart, Amplo et droit du travail.",
                missions = "Gestion des contrats de cession de droits d'auteurs, encadrement administratif des paies temporaires, permis d'exploitation et assurances.",
                source = "Site officiel Rockerill / LinkedIn",
                postedDate = "Hier, 16:45",
                postedTimestamp = now - (20 * oneHour),
                sector = "Coordination & Événementiel"
            ),
            JobOffer(
                id = "bps22_05",
                title = "Responsable Administrative et Financière",
                institution = "BPS22 — Musée d'Art de la Province de Hainaut",
                location = "Charleroi",
                distanceKm = 1,
                contractType = "CDI",
                profile = "Bachelier/Master en Comptabilité/Gestion. Expérience avérée en comptabilité ASBL et marchés publics provinciaux. Autonomie et méthode.",
                missions = "Élaboration des budgets annuels du musée, suivi des subventions publiques (Region, FWB, Province), comptabilité générale et paie du personnel.",
                source = "Le Forem / Province de Hainaut",
                postedDate = "Il y a 2 jours",
                postedTimestamp = now - (36 * oneHour),
                sector = "Comptabilité & Finance"
            ),
            JobOffer(
                id = "ancre_06",
                title = "Assistant(e) de Production & Régie Administrative",
                institution = "Théâtre de l'Ancre",
                location = "Charleroi",
                distanceKm = 1,
                contractType = "CDD (8 mois)",
                profile = "Sens aigu de l'organisation, réactivité. Expérience en administration du spectacle vivant, conventions Smart / Amplo et plannings de création.",
                missions = "Réservation des transports et hébergements pour comédiens en résidence, préparation des déclarations sociales d'artistes et contrats d'engagement.",
                source = "Culture.be",
                postedDate = "Il y a 2 jours",
                postedTimestamp = now - (42 * oneHour),
                sector = "Production & Régie"
            ),
            JobOffer(
                id = "vecteur_07",
                title = "Gestionnaire de Projets Associatifs & Mécénat",
                institution = "Le Vecteur / Plateforme Culturelle CBO",
                location = "Charleroi (Centre-Ville)",
                distanceKm = 0,
                contractType = "CDI (4/5ème)",
                profile = "Excellente plume, expérience dans la rédaction de demandes de subsides auprès des institutions de la FWB et fondations privées.",
                missions = "Montage des dossiers de financement pour expositions et concerts, gestion des adhésions à l'ASBL et secrétariat des réunions de gouvernance.",
                source = "Culture.be",
                postedDate = "Il y a 3 jours",
                postedTimestamp = now - (60 * oneHour),
                sector = "Projets & Subventions"
            ),
            JobOffer(
                id = "musee_photo_08",
                title = "Chargé(e) de Gestion des Collections & Registre",
                institution = "Musée de la Photographie de Charleroi",
                location = "Mont-sur-Marchienne",
                distanceKm = 3,
                contractType = "CDI",
                profile = "Diplôme en gestion culturelle ou archivistique. Rigueur, précision, pratique des bases de données d'inventaires d'art (Museumplus).",
                missions = "Suivi administratif des entrées et sorties d'œuvres, rédaction des contrats d'emprunt et de prêt, gestion des polices d'assurance exposition.",
                source = "Site Officiel du Musée de la Photo",
                postedDate = "Il y a 3 jours",
                postedTimestamp = now - (72 * oneHour),
                sector = "Gestion de Collections"
            ),
            JobOffer(
                id = "guimbarde_09",
                title = "Administrateur/trice de Tournée & Diffusion",
                institution = "Théâtre Jeune Public La Guimbarde",
                location = "Charleroi",
                distanceKm = 2,
                contractType = "CDI (30h/sem)",
                profile = "Aisance relationnelle avec diffuseurs et programmations scolaires. Maîtrise des contrats de vente de spectacles Jeune Public.",
                missions = "Établissement des factures de cession, relances administratives, gestion des paies des artistes en tournée et déclarations de droits.",
                source = "Culture.be",
                postedDate = "Il y a 4 jours",
                postedTimestamp = now - (90 * oneHour),
                sector = "Diffusion & Administration"
            ),
            JobOffer(
                id = "magonette_10",
                title = "Secrétaire et Chargé(e) d'Accueil Culturel",
                institution = "Centre Culturel de Mont-sur-Marchienne",
                location = "Mont-sur-Marchienne",
                distanceKm = 4,
                contractType = "CDD (6 mois)",
                profile = "Diplôme secondaire supérieur ou bachelier. Accueil chaleureux, maîtrise de Word/Excel, gestion d'agenda et courriers.",
                missions = "Gestion des inscriptions aux ateliers artistiques, rédaction des convocations, tenue du registre de présence et courriers administratifs.",
                source = "Le Forem",
                postedDate = "Il y a 4 jours",
                postedTimestamp = now - (96 * oneHour),
                sector = "Secrétariat & Accueil"
            ),
            JobOffer(
                id = "bois_cazier_11",
                title = "Chargé(e) de Mission Subsides & Administration Patrimoine",
                institution = "Le Bois du Cazier (Site UNESCO)",
                location = "Marcinelle",
                distanceKm = 3,
                contractType = "CDI",
                profile = "3+ ans d'expérience dans le secteur culturel ou touristique public. Maîtrise des justifications budgétaires et marchés publics.",
                missions = "Gestion administrative des dossiers de subvention patrimoine, coordination des prestataires externes et secrétariat de projet.",
                source = "Le Forem",
                postedDate = "Il y a 5 jours",
                postedTimestamp = now - (120 * oneHour),
                sector = "Administration Patrimoine"
            ),
            JobOffer(
                id = "wapi_tournai_12",
                title = "Coordinateur/trice Administratif(-ve) Réseau",
                institution = "Maison de la Culture de Tournai",
                location = "Tournai",
                distanceKm = 48,
                contractType = "CDI",
                profile = "Bonne connaissance du réseau des Centres Culturels de Hainaut. Rigueur et vision stratégique administrative.",
                missions = "Secrétariat du réseau territorial, rédaction de comptes-rendus, animation administrative des commissions thématiques.",
                source = "Culture.be",
                postedDate = "Il y a 5 jours",
                postedTimestamp = now - (125 * oneHour),
                sector = "Coordination Réseau"
            )
        )
    }
}
