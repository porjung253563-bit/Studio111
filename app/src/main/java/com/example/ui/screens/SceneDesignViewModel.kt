package com.example.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.SceneEntity
import com.example.data.SceneRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class SceneDesignViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SceneRepository
    private var currentSceneId: Int? = null

    init {
        val dao = AppDatabase.getDatabase(application).sceneDao()
        repository = SceneRepository(dao)
    }

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _location = MutableStateFlow("")
    val location = _location.asStateFlow()

    private val _timeOfDay = MutableStateFlow("")
    val timeOfDay = _timeOfDay.asStateFlow()
    
    private val _atmosphere = MutableStateFlow("")
    val atmosphere = _atmosphere.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating = _isGenerating.asStateFlow()

    private val _generatedImageUrl = MutableStateFlow<String?>(null)
    val generatedImageUrl = _generatedImageUrl.asStateFlow()

    fun updateField(field: String, value: String) {
        when (field) {
            "name" -> _name.value = value
            "location" -> _location.value = value
            "timeOfDay" -> _timeOfDay.value = value
            "atmosphere" -> _atmosphere.value = value
        }
    }

    fun loadScene(id: Int) {
        viewModelScope.launch {
            val scene = repository.getSceneById(id)
            scene?.let {
                currentSceneId = it.id
                _name.value = it.name
                _location.value = it.location
                _timeOfDay.value = it.timeOfDay
                _atmosphere.value = it.atmosphere
                _generatedImageUrl.value = it.imageUrl
            }
        }
    }

    fun generateScene() {
        viewModelScope.launch {
            _isGenerating.value = true
            _generatedImageUrl.value = null
            
            // Mocking API call to image generation service
            delay(2500)
            
            val seed = if (_name.value.isNotBlank()) _name.value.replace(" ", "") else Random.nextInt().toString()
            _generatedImageUrl.value = "https://picsum.photos/seed/$seed/600/400"
            
            saveCurrentScene()
            _isGenerating.value = false
        }
    }

    fun saveCurrentScene() {
        viewModelScope.launch {
            if (_name.value.isBlank()) return@launch
            val newScene = SceneEntity(
                id = currentSceneId ?: 0,
                name = _name.value,
                location = _location.value,
                timeOfDay = _timeOfDay.value,
                atmosphere = _atmosphere.value,
                imageUrl = _generatedImageUrl.value
            )
            val savedId = repository.insert(newScene)
            currentSceneId = savedId.toInt()
        }
    }
}
