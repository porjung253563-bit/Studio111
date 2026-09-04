package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scenes")
data class SceneEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val location: String,
    val timeOfDay: String,
    val atmosphere: String,
    val imageUrl: String?,
    val timestamp: Long = System.currentTimeMillis()
)
