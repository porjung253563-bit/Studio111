package com.example.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LibraryViewModel(application: Application) : AndroidViewModel(application) {
    private val movieRepository: MovieRepository
    private val characterRepository: CharacterRepository
    private val sceneRepository: SceneRepository

    init {
        val db = AppDatabase.getDatabase(application)
        movieRepository = MovieRepository(db.movieProjectDao())
        characterRepository = CharacterRepository(db.characterDao())
        sceneRepository = SceneRepository(db.sceneDao())
    }

    val projects: StateFlow<List<MovieProject>> = movieRepository.allProjects.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val characters: StateFlow<List<CharacterEntity>> = characterRepository.allCharacters.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val scenes: StateFlow<List<SceneEntity>> = sceneRepository.allScenes.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun deleteProject(id: Int) = viewModelScope.launch { movieRepository.deleteById(id) }
    
    fun deleteCharacter(id: Int) = viewModelScope.launch { characterRepository.deleteById(id) }

    fun deleteScene(id: Int) = viewModelScope.launch { sceneRepository.deleteById(id) }
}
