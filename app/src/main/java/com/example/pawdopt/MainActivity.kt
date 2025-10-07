package com.example.pawdopt

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
import com.example.pawdopt.viewmodel.AddPetViewModel
import com.example.pawdopt.viewmodel.AdoptionViewModel
import com.example.pawdopt.viewmodel.PetViewModel
import com.example.pawdopt.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application)
    )
    val petViewModel: PetViewModel = viewModel()
    val adoptionViewModel: AdoptionViewModel = viewModel()

    val bottomItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.MyRequests,
        BottomNavItem.Profile
    )

    // Lista de rutas donde queremos mostrar la BottomBar
    val bottomBarRoutes = listOf(Routes.HOME, Routes.MY_REQUESTS, Routes.PROFILE)

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            if (currentRoute in bottomBarRoutes) {
                BottomBar(navController = navController, items = bottomItems)
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
                arguments = listOf(navArgument("petId") { type = NavType.IntType })
            ) { backStackEntry ->
                val petId = backStackEntry.arguments?.getInt("petId") ?: -1
                PetDetailScreen(
                    navController = navController,
                    petId = petId,
                    petViewModel = petViewModel,
                    userViewModel = userViewModel,
                    adoptionViewModel = adoptionViewModel
                )
            }

            composable(Routes.MY_REQUESTS) {
                MyRequestsScreen(navController = navController, adoptionViewModel = adoptionViewModel, userViewModel = userViewModel)
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
                val currentUserId = userViewModel.state.value.currentUser?.id ?: 0
                AddPetScreen(navController = navController, viewModel = addPetViewModel, petViewModel = petViewModel, currentUserId = currentUserId)
            }
        }
    }
}




