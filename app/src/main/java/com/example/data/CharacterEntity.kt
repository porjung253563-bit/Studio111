package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val role: String,
    val appearance: String,
    val personality: String,
    val imageUrl: String?,
    val timestamp: Long = System.currentTimeMillis()
)
