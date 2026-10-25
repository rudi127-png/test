package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_jobs")
data class SavedJobEntity(
    @PrimaryKey val id: String,
    val title: String,
    val institution: String,
    val location: String,
    val distanceKm: Int,
    val contractType: String,
    val profile: String,
    val missions: String,
    val source: String,
    val postedDate: String,
    val postedTimestamp: Long,
    val sector: String,
    val savedTimestamp: Long = System.currentTimeMillis(),
    val applicationStatusName: String = "NOT_APPLIED",
    val notes: String = ""
)
