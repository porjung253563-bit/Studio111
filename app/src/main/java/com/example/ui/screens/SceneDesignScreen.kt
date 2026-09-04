package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SceneDesignScreen(
    sceneId: Int?,
    onNavigateBack: () -> Unit,
    viewModel: SceneDesignViewModel = viewModel()
) {
    val name by viewModel.name.collectAsState()
    val location by viewModel.location.collectAsState()
    val timeOfDay by viewModel.timeOfDay.collectAsState()
    val atmosphere by viewModel.atmosphere.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val generatedImageUrl by viewModel.generatedImageUrl.collectAsState()

    val scrollState = rememberScrollState()

    LaunchedEffect(sceneId) {
        if (sceneId != null) {
            viewModel.loadScene(sceneId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scene Gen", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { 
                        viewModel.saveCurrentScene()
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
                text = "Scene Settings",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            OutlinedTextField(
                value = name,
                onValueChange = { viewModel.updateField("name", it) },
                label = { Text("Scene Name") },
                placeholder = { Text("E.g., The Final Showdown") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = location,
                onValueChange = { viewModel.updateField("location", it) },
                label = { Text("Location") },
                placeholder = { Text("E.g., Abandoned Warehouse, Cyberpunk City") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                shape = RoundedCornerShape(12.dp)
            )
            
            OutlinedTextField(
                value = timeOfDay,
                onValueChange = { viewModel.updateField("timeOfDay", it) },
                label = { Text("Time of Day") },
                placeholder = { Text("E.g., Sunset, Midnight, High Noon") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = atmosphere,
                onValueChange = { viewModel.updateField("atmosphere", it) },
                label = { Text("Atmosphere / Lighting") },
                placeholder = { Text("E.g., Gloomy, Neon-lit, Peaceful, Foggy") },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                maxLines = 3,
                shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = { viewModel.generateScene() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !isGenerating && name.isNotBlank() && location.isNotBlank()
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Rendering Scene...")
                } else {
                    Icon(Icons.Filled.AutoAwesome, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generate Scene Visual")
                }
            }

            if (generatedImageUrl != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Generated Scene",
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
                            contentDescription = "Scene Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
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
                            if (location.isNotBlank()) {
                                Text(
                                    text = "$location • $timeOfDay",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
