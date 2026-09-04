package com.example.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.CharacterEntity
import com.example.data.CharacterRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class CharacterDesignViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CharacterRepository
    private var currentCharacterId: Int? = null

    init {
        val dao = AppDatabase.getDatabase(application).characterDao()
        repository = CharacterRepository(dao)
    }

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _role = MutableStateFlow("")
    val role = _role.asStateFlow()

    private val _appearance = MutableStateFlow("")
    val appearance = _appearance.asStateFlow()
    
    private val _personality = MutableStateFlow("")
    val personality = _personality.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating = _isGenerating.asStateFlow()

    private val _generatedImageUrl = MutableStateFlow<String?>(null)
    val generatedImageUrl = _generatedImageUrl.asStateFlow()

    fun updateField(field: String, value: String) {
        when (field) {
            "name" -> _name.value = value
            "role" -> _role.value = value
            "appearance" -> _appearance.value = value
            "personality" -> _personality.value = value
        }
    }

    fun loadCharacter(id: Int) {
        viewModelScope.launch {
            val char = repository.getCharacterById(id)
            char?.let {
                currentCharacterId = it.id
                _name.value = it.name
                _role.value = it.role
                _appearance.value = it.appearance
                _personality.value = it.personality
                _generatedImageUrl.value = it.imageUrl
            }
        }
    }

    fun generateProfile() {
        viewModelScope.launch {
            _isGenerating.value = true
            _generatedImageUrl.value = null
            
            // Mocking API call to image generation service
            delay(2500)
            
            val seed = if (_name.value.isNotBlank()) _name.value else Random.nextInt().toString()
            _generatedImageUrl.value = "https://api.dicebear.com/9.x/bottts/png?seed=$seed&size=400"
            
            saveCurrentCharacter()
            _isGenerating.value = false
        }
    }

    fun saveCurrentCharacter() {
        viewModelScope.launch {
            if (_name.value.isBlank()) return@launch
            val newChar = CharacterEntity(
                id = currentCharacterId ?: 0,
                name = _name.value,
                role = _role.value,
                appearance = _appearance.value,
                personality = _personality.value,
                imageUrl = _generatedImageUrl.value
            )
            val savedId = repository.insert(newChar)
            currentCharacterId = savedId.toInt()
        }
    }
}
