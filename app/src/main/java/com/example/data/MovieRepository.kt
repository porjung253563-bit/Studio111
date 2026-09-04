package com.example.data

import kotlinx.coroutines.flow.Flow

class MovieRepository(private val dao: MovieProjectDao) {
    val allProjects: Flow<List<MovieProject>> = dao.getAllProjects()

    suspend fun getProjectById(id: Int): MovieProject? = dao.getProjectById(id)

    suspend fun insert(project: MovieProject) = dao.insertProject(project)

    suspend fun deleteById(id: Int) = dao.deleteProjectById(id)
}
