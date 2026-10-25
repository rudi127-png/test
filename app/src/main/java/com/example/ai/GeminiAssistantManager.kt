package com.example.ai

import android.util.Log
import com.example.data.model.JobOffer
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

object GeminiAssistantManager {
    private const val TAG = "GeminiAssistantManager"

    private val generativeModel by lazy {
        try {
            Firebase.ai.generativeModel("gemini-3.8-flash")
        } catch (e: Exception) {
            Log.w(TAG, "Firebase.ai lazy model init deferred or fallback active: ${e.message}")
            null
        }
    }

    suspend fun generateCoverLetter(job: JobOffer): String {
        val prompt = """
            Tu es un expert RH et conseiller en recrutement spécialisé dans le secteur culturel, artistique et du spectacle vivant en Belgique (Wallonie / Bruxelles / FWB).
            Rédige une ébauche de lettre de motivation personnalisée et bien structurée pour Aline, candidate à un poste administratif.
            
            DONNÉES DU POSTE :
            • Intitulé : ${job.title}
            • Structure / Institution : ${job.institution}
            • Lieu : ${job.location} (Distance : ${job.distanceKm} km de Charleroi)
            • Type de contrat : ${job.contractType}
            • Profil recherché : ${job.profile}
            • Missions : ${job.missions}
            
            CONSIGNES POUR ALINE :
            - Adopte un ton professionnel, passionné par la culture et le spectacle vivant, et très méthodique.
            - Mets en avant ses compétences clés en administration culturelle (gestion des contrats d'artistes, subventions FWB / Région / Province, secrétariat, rigueur budgétaire, Smart/Amplo, logistique).
            - Explique son enthousiasme à rejoindre ${job.institution} dans la région de Charleroi / Hainaut.
            - Structure la lettre avec Objet, Formule d'appel, Accroche, Corps de texte (Adéquation Profil/Missions), et Formule de politesse chaleureuse.
        """.trimIndent()

        return executeAiOrFallback(
            prompt = prompt,
            fallbackGenerator = { getFallbackCoverLetter(job) }
        )
    }

    suspend fun generateJobSummary(job: JobOffer): String {
        val prompt = """
            Analyse cette offre d'emploi administratif culturel pour Aline :
            Poste : ${job.title}
            Institution : ${job.institution} (${job.location}, ${job.distanceKm}km de Charleroi)
            Missions : ${job.missions}
            Profil : ${job.profile}

            Formate une réponse courte et motivante en 3 points clés pour Aline :
            1. 🎭 Pourquoi cette structure (${job.institution}) est intéressante.
            2. 📜 Les atouts administratifs à valoriser.
            3. 📍 Côté pratique & proximité avec Charleroi.
        """.trimIndent()

        return executeAiOrFallback(
            prompt = prompt,
            fallbackGenerator = { getFallbackJobSummary(job) }
        )
    }

    suspend fun generateInterviewPrep(job: JobOffer): String {
        val prompt = """
            Aline prépare un entretien d'embauche pour le poste de "${job.title}" au sein de "${job.institution}".
            Donne-lui 4 conseils pratiques et 3 questions probables du jury pour réussir cet entretien dans le milieu culturel carolo.
        """.trimIndent()

        return executeAiOrFallback(
            prompt = prompt,
            fallbackGenerator = { getFallbackInterviewPrep(job) }
        )
    }

    suspend fun sendChatQuestion(question: String): String {
        val prompt = """
            Tu es l'Assistant IA dédié d'Aline pour sa recherche d'emploi administratif dans le secteur des arts, de la culture et du spectacle vivant (région de Charleroi & 50km).
            Réponds de façon chaleureuse, enthousiaste, bienveillante et précise avec des emojis culturels (🎨, 🎭, 📜, 🎵).
            
            Question d'Aline : "$question"
        """.trimIndent()

        return executeAiOrFallback(
            prompt = prompt,
            fallbackGenerator = { getFallbackChatAnswer(question) }
        )
    }

    private suspend fun executeAiOrFallback(
        prompt: String,
        fallbackGenerator: () -> String
    ): String {
        return try {
            val model = generativeModel
            if (model != null) {
                withTimeout(45_000L) {
                    val response = model.generateContent(prompt)
                    val text = response.text
                    if (!text.isNullOrBlank()) {
                        text
                    } else {
                        fallbackGenerator()
                    }
                }
            } else {
                fallbackGenerator()
            }
        } catch (e: TimeoutCancellationException) {
            Log.w(TAG, "Ai generation timed out, using fallback")
            fallbackGenerator()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Ai model call failed: ${e.message}, using fallback")
            fallbackGenerator()
        }
    }

    // High quality offline fallback generators tailored for Aline
    private fun getFallbackCoverLetter(job: JobOffer): String {
        return """
Objet : Candidature au poste de ${job.title} — ${job.institution}

Madame, Monsieur,

C’est avec un enthousiasme sincère et une vive motivation que je vous adresse ma candidature pour le poste de ${job.title} au sein de ${job.institution}. Attentive à la vitalité culturelle et artistique de notre région, je suis particulièrement admirative du travail accompli par votre structure dans le paysage des arts et du spectacle vivant.

Forte d'un parcours axé sur la gestion administrative, le secrétariat et la coordination de projets, je maîtrise l'ensemble des compétences indispensables à la conduite de cette fonction :
• Suivi administratif et financier : gestion des factures, pièces justificatives pour les dossiers de subventions (FWB, Région Wallonne, Province) et rigueur budgétaire.
• Logistique culturelle : élaboration des contrats d'artistes et d'engagement (Smart, Amplo), organisation des résidences et plannings d'accueil.
• Secrétariat et coordination : rédaction de correspondances officielles, procès-verbaux et lien avec les partenaires institutionnels.

Votre offre pour ${job.institution} (${job.location}) correspond parfaitement à ma recherche de proximité (zone Charleroi) et à mon souhait de mettre ma rigueur administrative au service de la création artistique.

Polyvalente, réactive et dotée d'un excellent sens relationnel, je serais ravie de vous rencontrer lors d'un entretien pour vous exposer plus de vive voix ma motivation.

Je vous prie d'agréer, Madame, Monsieur, l'expression de mes salutations distinguées et artistiques.

Aline
        """.trimIndent()
    }

    private fun getFallbackJobSummary(job: JobOffer): String {
        return """
✨ **Synthèse personnalisée pour Aline**

🎭 **1. Pourquoi ce poste ?**
L'institution **${job.institution}** à ${job.location} est un acteur majeur du secteur culturel. Le rôle de *${job.title}* offre un juste équilibre entre secrétariat de projet et gestion opérationnelle.

📜 **2. Tes atouts à valoriser :**
• Ta maîtrise des procédures administratives et budgétaires.
• Ta capacité d'adaptation aux spécificités des statuts d'artistes et subventions culturelles.
• Ton autonomie et ta rigueur dans la tenue des dossiers.

📍 **3. Proximité & Confort :**
Situé à **${job.location}** (à seulement **${job.distanceKm} km** de Charleroi), ce poste s'inscrit parfaitement dans ton périmètre de recherche avec un contrat **${job.contractType}**.
        """.trimIndent()
    }

    private fun getFallbackInterviewPrep(job: JobOffer): String {
        return """
🗓️ **Préparation à l'entretien pour ${job.institution}**

💡 **4 Conseils Pratiques :**
1. **Connaître la programmation :** Consulte les dernières saisons et actualités de ${job.institution}.
2. **Clarifier la gestion des subsides :** Revois le fonctionnement des arrêtés de subvention FWB et le décret des Centres Culturels / Arts de la Scène.
3. **Illustrer avec du concret :** Prépare deux exemples de dossiers complexes que tu as gérés (ex : contrat de cession de droits ou urgence logistique).
4. **Montrer son ancrage local :** Exprime ton attachement au développement culturel dans le Bassin de Charleroi.

❓ **3 Questions probables du jury :**
• *"Comment gérez-vous les priorités entre le secrétariat quotidien et l'urgence des résidences d'artistes ?"*
• *"Quelle est votre expérience avec les outils informatiques de gestion et les contrats type Smart/Amplo ?"*
• *"Pourquoi souhaitez-vous rejoindre spécifiquement ${job.institution} ?"*
        """.trimIndent()
    }

    private fun getFallbackChatAnswer(question: String): String {
        val qLower = question.lowercase()
        return when {
            qLower.contains("lettre") || qLower.contains("motivation") -> {
                "🎨 **Conseil Lettre de Motivation** : Dans le secteur culturel carolo, mets toujours en avant 3 piliers : ta rigueur administrative (Excel, facturation), ta connaissance des aides/subsides (FWB, Wallonie, Province), et ta sensibilité aux contraintes des artistes sur le terrain."
            }
            qLower.contains("contrat") || qLower.contains("cdd") || qLower.contains("cdi") -> {
                "📜 **Conseil Contrats Culturels** : Dans la FWB, les contrats CDD et CDI en ASBL culturelle respectent les Commissions Paritaires CP 329.02 (Secteur socio-culturel) ou CP 304 (Spectacle vivant). N'hésite pas à demander la CP lors de ton entretien !"
            }
            qLower.contains("charleroi") || qLower.contains("forem") || qLower.contains("distance") -> {
                "📍 **Ancrage Charleroi** : La scène culturelle carolo est en plein essor ! De Charleroi Danse au Palais des Beaux-Arts, en passant par L'Eden, Le Vecteur et le BPS22, toutes ces institutions recherchent des profils administratifs rigoureux et réactifs."
            }
            else -> {
                "🎭 **Assistant Only4Aline** : Je suis à ton service, Aline ! Tu peux me demander de rédiger une lettre de motivation, d'analyser une offre, de préparer un entretien, ou de te donner des conseils sur le marché de l'emploi administratif culturel autour de Charleroi."
            }
        }
    }
}
