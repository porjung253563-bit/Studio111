package com.example.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNavigateToCreate = { navController.navigate("create") },
                onNavigateToProject = { id -> navController.navigate("project/$id") },
                onNavigateToCharacterAI = { navController.navigate("character_design") },
                onNavigateToSceneGen = { navController.navigate("scene_design") },
                onNavigateToLibrary = { navController.navigate("library") { popUpTo("home") { inclusive = false } } }
            )
        }
        composable("library") {
            LibraryScreen(
                onNavigateToHome = { navController.navigate("home") { popUpTo("home") { inclusive = true } } },
                onNavigateToProject = { id -> navController.navigate("project/$id") },
                onNavigateToCharacterAI = { id -> navController.navigate("character_design?characterId=$id") },
                onNavigateToSceneGen = { id -> navController.navigate("scene_design?sceneId=$id") }
            )
        }
        composable(
            route = "character_design?characterId={characterId}",
            arguments = listOf(navArgument("characterId") { type = NavType.IntType; defaultValue = -1 })
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getInt("characterId") ?: -1
            CharacterDesignScreen(
                characterId = if (characterId != -1) characterId else null,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "scene_design?sceneId={sceneId}",
            arguments = listOf(navArgument("sceneId") { type = NavType.IntType; defaultValue = -1 })
        ) { backStackEntry ->
            val sceneId = backStackEntry.arguments?.getInt("sceneId") ?: -1
            SceneDesignScreen(
                sceneId = if (sceneId != -1) sceneId else null,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("create") {
            CreateScreen(
                onNavigateBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }
        composable(
            route = "project/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.IntType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getInt("projectId") ?: return@composable
            ProjectDetailScreen(
                projectId = projectId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
