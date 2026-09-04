package com.example.api

import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class MovieScriptResult(
    val title: String,
    val story: String,
    val characters: String,
    val scenes: String,
    val script: String
)

object AIGenerator {
    suspend fun generateMovieScript(prompt: String): MovieScriptResult? = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext null
        }

        val systemInstructionText = """
            You are a professional movie maker AI.
            Based on the user's prompt, generate a complete short movie structure.
            Return ONLY a valid JSON object with the following string fields:
            - title (The title of the movie)
            - story (A short summary of the plot)
            - characters (A list of characters and their descriptions)
            - scenes (A breakdown of the scenes)
            - script (The actual dialogue and action lines)
            Do not include Markdown formatting like ```json.
        """.trimIndent()

        val request = GenerateContentRequest(
            contents = listOf(
                Content(parts = listOf(Part(text = prompt)))
            ),
            generationConfig = GenerationConfig(
                responseMimeType = "application/json",
                temperature = 0.7f
            ),
            systemInstruction = Content(
                parts = listOf(Part(text = systemInstructionText))
            )
        )

        try {
            val response = RetrofitClient.service.generateContent(apiKey, request)
            val jsonResponse = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: return@withContext null
            
            // Parse JSON response manually or using Moshi
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val adapter = moshi.adapter(Map::class.java)
            val result = adapter.fromJson(jsonResponse) as? Map<*, *> ?: return@withContext null

            MovieScriptResult(
                title = result["title"] as? String ?: "Untitled",
                story = result["story"] as? String ?: "No story provided.",
                characters = result["characters"] as? String ?: "No characters provided.",
                scenes = result["scenes"] as? String ?: "No scenes provided.",
                script = result["script"] as? String ?: "No script provided."
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
