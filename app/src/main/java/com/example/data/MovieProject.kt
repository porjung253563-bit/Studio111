package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_projects")
data class MovieProject(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val prompt: String,
    val title: String,
    val story: String,
    val characters: String,
    val scenes: String,
    val script: String,
    val timestamp: Long = System.currentTimeMillis()
)
