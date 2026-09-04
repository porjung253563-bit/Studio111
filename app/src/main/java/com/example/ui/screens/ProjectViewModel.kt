package com.example.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.MovieProject
import com.example.data.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProjectViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MovieRepository

    init {
        val dao = AppDatabase.getDatabase(application).movieProjectDao()
        repository = MovieRepository(dao)
    }

    private val _project = MutableStateFlow<MovieProject?>(null)
    val project: StateFlow<MovieProject?> = _project.asStateFlow()

    fun loadProject(id: Int) {
        viewModelScope.launch {
            _project.value = repository.getProjectById(id)
        }
    }
}
