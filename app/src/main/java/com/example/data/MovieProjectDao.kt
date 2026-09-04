package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieProjectDao {
    @Query("SELECT * FROM movie_projects ORDER BY timestamp DESC")
    fun getAllProjects(): Flow<List<MovieProject>>

    @Query("SELECT * FROM movie_projects WHERE id = :id")
    suspend fun getProjectById(id: Int): MovieProject?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: MovieProject)

    @Query("DELETE FROM movie_projects WHERE id = :id")
    suspend fun deleteProjectById(id: Int)
}
