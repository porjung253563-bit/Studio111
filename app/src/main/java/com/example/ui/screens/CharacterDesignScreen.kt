package com.example.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDesignScreen(
    characterId: Int?,
    onNavigateBack: () -> Unit,
    viewModel: CharacterDesignViewModel = viewModel()
) {
    val name by viewModel.name.collectAsState()
    val role by viewModel.role.collectAsState()
    val appearance by viewModel.appearance.collectAsState()
    val personality by viewModel.personality.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val generatedImageUrl by viewModel.generatedImageUrl.collectAsState()

    val scrollState = rememberScrollState()
    
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var isTtsReady by remember { mutableStateOf(false) }

    DisposableEffect(context) {
        val textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isTtsReady = true
            }
        }
        tts = textToSpeech
        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    LaunchedEffect(characterId) {
        if (characterId != null) {
            viewModel.loadCharacter(characterId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Character AI", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { 
                        viewModel.saveCurrentCharacter()
                        onNavigateBack()
                    }) {
                        Text("Save", fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Define Traits",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            OutlinedTextField(
                value = name,
                onValueChange = { viewModel.updateField("name", it) },
                label = { Text("Character Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = role,
                onValueChange = { viewModel.updateField("role", it) },
                label = { Text("Role (e.g. Protagonist, Villain)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = appearance,
                onValueChange = { viewModel.updateField("appearance", it) },
                label = { Text("Appearance Details") },
                placeholder = { Text("E.g., Tall, cyberpunk attire, neon glowing eyes...") },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                maxLines = 3,
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = personality,
                onValueChange = { viewModel.updateField("personality", it) },
                label = { Text("Personality & Backstory") },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                maxLines = 3,
                shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = { viewModel.generateProfile() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !isGenerating && name.isNotBlank() && appearance.isNotBlank()
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generating Avatar...")
                } else {
                    Icon(Icons.Filled.AutoAwesome, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generate Visual Profile")
                }
            }

            if (generatedImageUrl != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Generated Profile",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                Card(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column {
                        AsyncImage(
                            model = generatedImageUrl,
                            contentDescription = "Character Avatar",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                                .background(MaterialTheme.colorScheme.tertiaryContainer),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = name,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            if (role.isNotBlank()) {
                                Text(
                                    text = role,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            
                            if (isTtsReady) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        val intro = "Hello, my name is ${if (name.isNotBlank()) name else "a mysterious person"}. "
                                        val roleText = if (role.isNotBlank()) "I am the $role. " else ""
                                        val desc = if (personality.isNotBlank()) personality else ""
                                        val textToSpeak = intro + roleText + desc
                                        tts?.language = Locale.US
                                        tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                ) {
                                    Icon(Icons.Filled.VolumeUp, contentDescription = "Voice Preview")
                                    Spacer(Modifier.width(8.dp))
                                    Text("Preview Voice")
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
