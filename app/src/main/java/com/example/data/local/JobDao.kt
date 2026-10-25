package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {
    @Query("SELECT * FROM saved_jobs ORDER BY savedTimestamp DESC")
    fun getAllSavedJobs(): Flow<List<SavedJobEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedJob(job: SavedJobEntity)

    @Query("DELETE FROM saved_jobs WHERE id = :id")
    suspend fun deleteSavedJobById(id: String)

    @Query("UPDATE saved_jobs SET applicationStatusName = :statusName, notes = :notes WHERE id = :id")
    suspend fun updateJobStatusAndNotes(id: String, statusName: String, notes: String)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_jobs WHERE id = :id)")
    suspend fun isJobSaved(id: String): Boolean
}
