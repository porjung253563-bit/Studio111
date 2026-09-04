package com.example.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.MovieProject
import com.example.data.MovieRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MovieRepository

    init {
        val dao = AppDatabase.getDatabase(application).movieProjectDao()
        repository = MovieRepository(dao)
    }

    val projects: StateFlow<List<MovieProject>> = repository.allProjects
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteProject(id: Int) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }
}
