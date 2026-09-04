package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SceneDao {
    @Query("SELECT * FROM scenes ORDER BY timestamp DESC")
    fun getAllScenes(): Flow<List<SceneEntity>>

    @Query("SELECT * FROM scenes WHERE id = :id")
    suspend fun getSceneById(id: Int): SceneEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScene(scene: SceneEntity): Long

    @Query("DELETE FROM scenes WHERE id = :id")
    suspend fun deleteSceneById(id: Int)
}
