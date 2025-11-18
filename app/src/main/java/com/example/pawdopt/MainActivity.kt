package com.example.pawdopt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pawdopt.data.local.UserPreferencesDataStore
import com.example.pawdopt.navigation.BottomBar
import com.example.pawdopt.navigation.BottomNavItem
import com.example.pawdopt.navigation.Routes
import com.example.pawdopt.ui.screens.AddPetScreen
import com.example.pawdopt.ui.screens.HomeScreen
import com.example.pawdopt.ui.screens.LoginScreen
import com.example.pawdopt.ui.screens.MyRequestsScreen
import com.example.pawdopt.ui.screens.PetDetailScreen
import com.example.pawdopt.ui.screens.ProfileScreen
import com.example.pawdopt.ui.screens.RegisterScreen
import com.example.pawdopt.ui.theme.PawdoptTheme
import com.example.pawdopt.viewmodel.AddPetViewModel
import com.example.pawdopt.viewmodel.AdoptionViewModel
import com.example.pawdopt.viewmodel.PetViewModel
import com.example.pawdopt.viewmodel.UserViewModel
import com.example.pawdopt.viewmodel.UserViewModelFactory







class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            val prefs = UserPreferencesDataStore(this)

            val userViewModel: UserViewModel = viewModel(
                factory = UserViewModelFactory(prefs)
            )

            val petViewModel = PetViewModel()
            val adoptionViewModel = AdoptionViewModel()

            PawdoptTheme {
                App(navController, userViewModel, petViewModel, adoptionViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    navController: NavHostController,
    userViewModel: UserViewModel,
    petViewModel: PetViewModel,
    adoptionViewModel: AdoptionViewModel
) {
    val bottomItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.MyRequests,
        BottomNavItem.Profile
    )

    val bottomBarRoutes = listOf(Routes.HOME, Routes.MY_REQUESTS, Routes.PROFILE)

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            if (currentRoute in bottomBarRoutes) {
                BottomBar(navController = navController, items = bottomItems)
            }
        },
        topBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            when (navBackStackEntry?.destination?.route) {
                Routes.HOME -> TopAppBar(title = { Text("Mascotas disponibles") })
                Routes.MY_REQUESTS -> TopAppBar(title = { Text("Mis solicitudes") })
                Routes.PROFILE -> TopAppBar(title = { Text("Mi perfil") })
                Routes.ADD_PET -> TopAppBar(
                    title = { Text("Agregar Mascota") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                HomeScreen(navController = navController, viewModel = petViewModel)
            }

            composable(
                route = Routes.PET_DETAIL,
                arguments = listOf(navArgument("petId") { type = NavType.LongType })
            ) { backStackEntry ->

                val petId = backStackEntry.arguments?.getLong("petId") ?: -1L

                PetDetailScreen(
                    navController = navController,
                    petId = petId, 
                    petViewModel = petViewModel,
                    userViewModel = userViewModel,
                    adoptionViewModel = adoptionViewModel
                )
            }

            composable(Routes.MY_REQUESTS) {
                MyRequestsScreen(
                    navController = navController,
                    adoptionViewModel = adoptionViewModel,
                    userViewModel = userViewModel
                )
            }

            composable(Routes.PROFILE) {
                ProfileScreen(navController = navController, userViewModel = userViewModel)
            }

            composable(Routes.LOGIN) {
                LoginScreen(navController = navController, userViewModel = userViewModel)
            }

            composable(Routes.REGISTER) {
                RegisterScreen(navController = navController, userViewModel = userViewModel)
            }

            composable(Routes.ADD_PET) {
                val addPetViewModel: AddPetViewModel = viewModel()

                AddPetScreen(
                    navController = navController,
                    viewModel = addPetViewModel,
                    petViewModel = petViewModel,
                    userViewModel = userViewModel
                )
            }
        }
    }
}