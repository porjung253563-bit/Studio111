package com.example.data

import kotlinx.coroutines.flow.Flow

class CharacterRepository(private val dao: CharacterDao) {
    val allCharacters: Flow<List<CharacterEntity>> = dao.getAllCharacters()

    suspend fun getCharacterById(id: Int): CharacterEntity? = dao.getCharacterById(id)

    suspend fun insert(character: CharacterEntity): Long = dao.insertCharacter(character)

    suspend fun deleteById(id: Int) = dao.deleteCharacterById(id)
}
