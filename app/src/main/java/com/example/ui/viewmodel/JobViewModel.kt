package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiAssistantManager
import com.example.data.local.AppDatabase
import com.example.data.model.ApplicationStatus
import com.example.data.model.JobOffer
import com.example.data.repository.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SearchFilterState(
    val rayonKm: Int = 50,
    val contractFilter: String = "CDD / CDI",
    val publicationFilter: String = "Tous",
    val experienceFilter: String = "Tous",
    val keyword: String = ""
)

class JobViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = JobRepository(db.jobDao())

    // Search Filter State
    private val _filterState = MutableStateFlow(SearchFilterState())
    val filterState: StateFlow<SearchFilterState> = _filterState

    // 24h Refresh State Management
    private val _lastSearchTimestamp = MutableStateFlow(System.currentTimeMillis())
    val lastSearchTimestamp: StateFlow<Long> = _lastSearchTimestamp

    private val _isRefreshedNoticeVisible = MutableStateFlow(false)
    val isRefreshedNoticeVisible: StateFlow<Boolean> = _isRefreshedNoticeVisible

    // Base Curated Job List
    private val _baseCuratedJobs = MutableStateFlow(repository.getCuratedJobOffers())

    // Combined Flow: Merges Room DB Saved States with Search Filters
    val filteredJobs: StateFlow<List<JobOffer>> = combine(
        _baseCuratedJobs,
        repository.getSavedJobsFlow(),
        _filterState
    ) { curated, savedList, filters ->
        val savedMap = savedList.associateBy { it.id }

        // Merge saved state into curated jobs
        val mergedJobs = curated.map { job ->
            val savedEntity = savedMap[job.id]
            if (savedEntity != null) {
                job.copy(
                    isSaved = true,
                    applicationStatus = savedEntity.applicationStatus,
                    notes = savedEntity.notes
                )
            } else {
                job.copy(isSaved = false, applicationStatus = ApplicationStatus.NOT_APPLIED, notes = "")
            }
        }

        // Apply filters
        mergedJobs.filter { job ->
            val matchesRayon = job.distanceKm <= filters.rayonKm
            val matchesContract = when (filters.contractFilter) {
                "Tous" -> true
                "CDD / CDI" -> job.contractType.contains("CDI") || job.contractType.contains("CDD")
                "CDI" -> job.contractType.contains("CDI")
                "CDD" -> job.contractType.contains("CDD")
                "Stage" -> job.contractType.contains("Stage", ignoreCase = true)
                else -> true
            }
            val matchesPublication = when (filters.publicationFilter) {
                "Tous" -> true
                "Aujourd'hui", "24h" -> (System.currentTimeMillis() - job.postedTimestamp) <= 86400_000L
                "7 jours" -> (System.currentTimeMillis() - job.postedTimestamp) <= (7 * 86400_000L)
                else -> true
            }
            val matchesKeyword = if (filters.keyword.isBlank()) true else {
                val kw = filters.keyword.lowercase()
                job.title.lowercase().contains(kw) ||
                        job.institution.lowercase().contains(kw) ||
                        job.missions.lowercase().contains(kw) ||
                        job.sector.lowercase().contains(kw) ||
                        job.location.lowercase().contains(kw)
            }

            matchesRayon && matchesContract && matchesPublication && matchesKeyword
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Saved Jobs Flow for Favorites Tab
    val savedJobs: StateFlow<List<JobOffer>> = repository.getSavedJobsFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // AI Dialog State
    private val _aiLoading = MutableStateFlow(false)
    val aiLoading: StateFlow<Boolean> = _aiLoading

    private val _aiDialogTitle = MutableStateFlow("")
    val aiDialogTitle: StateFlow<String> = _aiDialogTitle

    private val _aiDialogContent = MutableStateFlow<String?>(null)
    val aiDialogContent: StateFlow<String?> = _aiDialogContent

    // AI Chat History
    private val _chatMessages = MutableStateFlow(
        listOf(
            "Bonjour Aline ! 🎭 Je suis ton assistant dédié pour la recherche d'emploi culturel à Charleroi et aux alentours. Comment puis-je t'aider aujourd'hui ?" to false
        )
    )
    val chatMessages: StateFlow<List<Pair<String, Boolean>>> = _chatMessages

    init {
        check24HourRefreshRequirement()
    }

    private fun check24HourRefreshRequirement() {
        val now = System.currentTimeMillis()
        val twentyFourHoursMs = 24 * 60 * 60 * 1000L
        if (now - _lastSearchTimestamp.value > twentyFourHoursMs) {
            _isRefreshedNoticeVisible.value = true
            _lastSearchTimestamp.value = now
        }
    }

    fun dismissRefreshNotice() {
        _isRefreshedNoticeVisible.value = false
    }

    fun refreshSearchManual() {
        viewModelScope.launch {
            _lastSearchTimestamp.value = System.currentTimeMillis()
            _isRefreshedNoticeVisible.value = true
            _baseCuratedJobs.value = repository.getCuratedJobOffers()
        }
    }

    fun updateFilters(
        rayonKm: Int = _filterState.value.rayonKm,
        contractFilter: String = _filterState.value.contractFilter,
        publicationFilter: String = _filterState.value.publicationFilter,
        experienceFilter: String = _filterState.value.experienceFilter,
        keyword: String = _filterState.value.keyword
    ) {
        _filterState.value = SearchFilterState(
            rayonKm = rayonKm,
            contractFilter = contractFilter,
            publicationFilter = publicationFilter,
            experienceFilter = experienceFilter,
            keyword = keyword
        )
    }

    fun toggleBookmark(job: JobOffer) {
        viewModelScope.launch {
            repository.toggleSaveJob(job)
        }
    }

    fun updateJobStatus(jobId: String, status: ApplicationStatus, notes: String) {
        viewModelScope.launch {
            repository.updateJobStatusAndNotes(jobId, status, notes)
        }
    }

    // AI Features for Specific Job
    fun generateCoverLetter(job: JobOffer) {
        viewModelScope.launch {
            _aiLoading.value = true
            _aiDialogTitle.value = "📜 Lettre de motivation — ${job.institution}"
            _aiDialogContent.value = null
            val letter = GeminiAssistantManager.generateCoverLetter(job)
            _aiDialogContent.value = letter
            _aiLoading.value = false
        }
    }

    fun generateJobSummary(job: JobOffer) {
        viewModelScope.launch {
            _aiLoading.value = true
            _aiDialogTitle.value = "✨ Résumé & Atouts Aline — ${job.institution}"
            _aiDialogContent.value = null
            val summary = GeminiAssistantManager.generateJobSummary(job)
            _aiDialogContent.value = summary
            _aiLoading.value = false
        }
    }

    fun generateInterviewPrep(job: JobOffer) {
        viewModelScope.launch {
            _aiLoading.value = true
            _aiDialogTitle.value = "🗓️ Préparation Entretien — ${job.institution}"
            _aiDialogContent.value = null
            val prep = GeminiAssistantManager.generateInterviewPrep(job)
            _aiDialogContent.value = prep
            _aiLoading.value = false
        }
    }

    fun dismissAiDialog() {
        _aiDialogContent.value = null
    }

    // AI Chat
    fun sendChatMessage(userText: String) {
        if (userText.isBlank()) return
        val currentList = _chatMessages.value.toMutableList()
        currentList.add(userText to true)
        _chatMessages.value = currentList

        viewModelScope.launch {
            _aiLoading.value = true
            val aiResponse = GeminiAssistantManager.sendChatQuestion(userText)
            val updatedList = _chatMessages.value.toMutableList()
            updatedList.add(aiResponse to false)
            _chatMessages.value = updatedList
            _aiLoading.value = false
        }
    }
}
