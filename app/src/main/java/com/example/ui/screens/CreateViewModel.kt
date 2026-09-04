package com.example.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.AIGenerator
import com.example.data.AppDatabase
import com.example.data.MovieProject
import com.example.data.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MovieRepository

    init {
        val dao = AppDatabase.getDatabase(application).movieProjectDao()
        repository = MovieRepository(dao)
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _generatedProjectId = MutableStateFlow<Int?>(null)
    val generatedProjectId: StateFlow<Int?> = _generatedProjectId.asStateFlow()

    fun generateMovie(prompt: String) {
        if (prompt.isBlank()) return
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            val result = AIGenerator.generateMovieScript(prompt)
            if (result != null) {
                val project = MovieProject(
                    prompt = prompt,
                    title = result.title,
                    story = result.story,
                    characters = result.characters,
                    scenes = result.scenes,
                    script = result.script
                )
                repository.insert(project)
                // Assuming it's the latest, wait, room insert doesn't return ID directly unless we change DAO.
                // For simplicity, we just navigate back to Home.
                _generatedProjectId.value = -1 // signal success
            } else {
                _error.value = "Failed to generate movie script. Check your API key and connection."
            }
            
            _isLoading.value = false
        }
    }
    
    fun resetState() {
        _generatedProjectId.value = null
        _error.value = null
    }
}
