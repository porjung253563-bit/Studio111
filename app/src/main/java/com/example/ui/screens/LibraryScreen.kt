package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Landscape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.CharacterEntity
import com.example.data.SceneEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LibraryScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToProject: (Int) -> Unit,
    onNavigateToCharacterAI: (Int) -> Unit,
    onNavigateToSceneGen: (Int) -> Unit,
    viewModel: LibraryViewModel = viewModel()
) {
    val projects by viewModel.projects.collectAsState()
    val characters by viewModel.characters.collectAsState()
    val scenes by viewModel.scenes.collectAsState()
    
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Movies", "Characters", "Scenes")

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Library",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title, fontWeight = FontWeight.Bold) }
                        )
                    }
                }
            }
        },
        bottomBar = {
            BentoBottomNav(currentRoute = "library", onNavigate = { route -> 
                if (route == "home") onNavigateToHome() 
            })
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    if (projects.isEmpty()) {
                        item { EmptyStateMessage("No movies in your library.") }
                    } else {
                        items(projects, key = { "proj_${it.id}" }) { project ->
                            ProjectCard(
                                project = project,
                                onClick = { onNavigateToProject(project.id) },
                                onDelete = { viewModel.deleteProject(project.id) }
                            )
                        }
                    }
                }
                1 -> {
                    if (characters.isEmpty()) {
                        item { EmptyStateMessage("No characters in your library.") }
                    } else {
                        items(characters, key = { "char_${it.id}" }) { character ->
                            CharacterLibraryCard(
                                character = character,
                                onClick = { onNavigateToCharacterAI(character.id) },
                                onDelete = { viewModel.deleteCharacter(character.id) }
                            )
                        }
                    }
                }
                2 -> {
                    if (scenes.isEmpty()) {
                        item { EmptyStateMessage("No scenes in your library.") }
                    } else {
                        items(scenes, key = { "scene_${it.id}" }) { scene ->
                            SceneLibraryCard(
                                scene = scene,
                                onClick = { onNavigateToSceneGen(scene.id) },
                                onDelete = { viewModel.deleteScene(scene.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStateMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(message, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun CharacterLibraryCard(character: CharacterEntity, onClick: () -> Unit, onDelete: () -> Unit) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Face, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = character.name.ifBlank { "Unnamed Character" },
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = character.role.ifBlank { "Edited ${dateFormat.format(Date(character.timestamp))}" },
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Outlined.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun SceneLibraryCard(scene: SceneEntity, onClick: () -> Unit, onDelete: () -> Unit) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.tertiaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Landscape, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = scene.name.ifBlank { "Unnamed Scene" },
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = scene.location.ifBlank { "Edited ${dateFormat.format(Date(scene.timestamp))}" },
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Outlined.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
        }
    }
}
