package com.example.data

import kotlinx.coroutines.flow.Flow

class SceneRepository(private val dao: SceneDao) {
    val allScenes: Flow<List<SceneEntity>> = dao.getAllScenes()

    suspend fun getSceneById(id: Int): SceneEntity? = dao.getSceneById(id)

    suspend fun insert(scene: SceneEntity): Long = dao.insertScene(scene)

    suspend fun deleteById(id: Int) = dao.deleteSceneById(id)
}
