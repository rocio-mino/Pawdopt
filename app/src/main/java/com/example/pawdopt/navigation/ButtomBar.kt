package com.example.pawdopt.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person


sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(Routes.HOME, "Inicio", Icons.Default.Home)
    object MyRequests : BottomNavItem(Routes.MY_REQUESTS, "Solicitudes", Icons.Default.List)
    object Profile : BottomNavItem(Routes.PROFILE, "Perfil", Icons.Default.Person)
}

@Composable
fun BottomBar(navController: NavHostController, items: List<BottomNavItem>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    // Siempre navega, incluso si la ruta ya está activa.
                    navController.navigate(item.route) {
                        // Limpia las pantallas anteriores del stack
                        popUpTo(navController.graph.startDestinationRoute ?: Routes.HOME) {
                            inclusive = false
                        }
                        // Evita duplicar pantallas
                        launchSingleTop = true
                        restoreState = false
                    }
                }
            )
        }
    }
}