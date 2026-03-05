package com.example.watchmemory.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.watchmemory.data.ShowRepository
import com.example.watchmemory.ui.screen.AboutScreen
import com.example.watchmemory.ui.screen.EditScreen
import com.example.watchmemory.ui.screen.HomeScreen
import com.example.watchmemory.ui.theme.LocalBrutalColors
import com.example.watchmemory.viewmodel.EditViewModelFactory
import com.example.watchmemory.viewmodel.HomeViewModelFactory

object Routes {
    const val HOME = "home"
    const val EDIT = "edit/{id}"
    const val ABOUT = "about"
    fun editRoute(id: Long = -1L): String = "edit/$id"
}

private enum class NavTab(val route: String, val icon: ImageVector, val label: String) {
    HOME(Routes.HOME, Icons.Default.Home, "FEED"),
    ADD("add_action", Icons.Default.Add, "ADD"),
    ABOUT(Routes.ABOUT, Icons.Outlined.Info, "ABOUT")
}

@Composable
fun WatchMemoryNavGraph(
    navController: NavHostController,
    repository: ShowRepository,
    userPreferences: com.example.watchmemory.data.UserPreferences
) {
    val dur = 300
    val brutal = LocalBrutalColors.current

    Scaffold(
        bottomBar = { BottomBar(navController) },
        containerColor = brutal.surface
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding),
            enterTransition = {
                fadeIn(tween(dur)) + slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left, tween(dur)
                )
            },
            exitTransition = {
                fadeOut(tween(dur)) + slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left, tween(dur)
                )
            },
            popEnterTransition = {
                fadeIn(tween(dur)) + slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right, tween(dur)
                )
            },
            popExitTransition = {
                fadeOut(tween(dur)) + slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right, tween(dur)
                )
            }
        ) {
            composable(Routes.HOME) {
                val vm = viewModel<com.example.watchmemory.viewmodel.HomeViewModel>(
                    factory = HomeViewModelFactory(repository, userPreferences)
                )
                HomeScreen(
                    viewModel = vm,
                    onEditShow = { navController.navigate(Routes.editRoute(it)) },
                    onAddShow = {
                        navController.navigate(Routes.editRoute()) {
                            popUpTo(Routes.HOME) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onProfileClick = { navController.navigate(Routes.ABOUT) }
                )
            }

            composable(
                route = Routes.EDIT,
                arguments = listOf(navArgument("id") {
                    type = NavType.LongType
                    defaultValue = -1L
                })
            ) {
                val vm = viewModel<com.example.watchmemory.viewmodel.EditViewModel>(
                    factory = EditViewModelFactory(repository)
                )
                EditScreen(
                    viewModel = vm,
                    onNavigateBack = {
                        navController.popBackStack(Routes.HOME, inclusive = false)
                    }
                )
            }

            composable(Routes.ABOUT) {
                val vm = viewModel<com.example.watchmemory.viewmodel.ProfileViewModel>(
                    factory = com.example.watchmemory.viewmodel.ProfileViewModelFactory(userPreferences)
                )
                AboutScreen(viewModel = vm)
            }
        }
    }
}

@Composable
private fun BottomBar(navController: NavHostController) {
    val entry by navController.currentBackStackEntryAsState()
    val currentRoute = entry?.destination?.route
    val brutal = LocalBrutalColors.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brutal.surface)
            .border(width = 4.dp, color = brutal.border)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavTab.entries.forEach { tab ->
                val selected = when (tab) {
                    NavTab.HOME -> currentRoute == Routes.HOME
                    NavTab.ADD -> currentRoute?.startsWith("edit") == true
                    NavTab.ABOUT -> currentRoute == Routes.ABOUT
                }

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            when (tab) {
                                NavTab.ADD -> navController.navigate(Routes.editRoute()) {
                                    popUpTo(Routes.HOME) { inclusive = false }
                                    launchSingleTop = true
                                }
                                NavTab.HOME -> navController.navigate(Routes.HOME) {
                                    popUpTo(Routes.HOME) { inclusive = true }
                                    launchSingleTop = true
                                }
                                NavTab.ABOUT -> navController.navigate(Routes.ABOUT) {
                                    popUpTo(Routes.HOME) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        }
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(if (selected) 48.dp else 40.dp)
                            .then(
                                if (selected) Modifier
                                    .background(brutal.accent, RoundedCornerShape(10.dp))
                                    .border(2.dp, brutal.border, RoundedCornerShape(10.dp))
                                else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            tab.icon,
                            contentDescription = tab.label,
                            modifier = Modifier.size(24.dp),
                            tint = brutal.border
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        tab.label.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (selected) FontWeight.Black else FontWeight.ExtraBold,
                        color = if (selected) brutal.shadow else brutal.border,
                        letterSpacing = 2.sp
                    )
                }
            }
        }
    }
}
