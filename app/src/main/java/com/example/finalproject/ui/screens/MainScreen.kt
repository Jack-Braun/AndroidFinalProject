package com.example.finalproject.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.finalproject.data.UserProfile
import com.example.finalproject.ui.components.PetScreens

//The bar shown at the top of all screens
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetAppBar(
    currentScreen: PetScreens,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {Text(stringResource(currentScreen.title))},
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if(canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}

@Composable
fun PetApp(
    navController: NavHostController = rememberNavController()
) {

    val testProfile = UserProfile(
        username = "Steve",
        password = "password",
        name = "Steve Smith",
        bio = "I am a test profile"
    )

    var isLoggedIn by remember { mutableStateOf(false) }

    if (isLoggedIn) {
        PetAppContent(
            navController = navController,
            userProfile = testProfile,
            logout = { isLoggedIn = false }
        )
    } else {
        LoginScreen(
            login = { isLoggedIn = true },
            userProfile = testProfile
        )
    }
}

@Composable
fun PetAppContent(
    navController: NavHostController,
    userProfile: UserProfile,
    logout: () -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = PetScreens.valueOf(
        backStackEntry?.destination?.route ?: PetScreens.Home.name
    )
    Scaffold(
        topBar = {
            PetAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {navController.navigateUp()}
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PetScreens.Home.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            composable(route = PetScreens.Home.name) {
                HomeScreen(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                    onNavigate = {route -> navController.navigate(route)}
                )
            }
            composable(route = PetScreens.Pet.name) {
                PetScreen(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                )
            }
            composable(route = PetScreens.Profile.name) {
                ProfileScreen(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                    userProfile = userProfile,
                    logout = logout
                )
            }
            composable(route = PetScreens.Map.name) {
                MapScreen(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                )
            }
            composable(route = PetScreens.Health.name) {
                HealthScreen(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                )
            }
        }
    }
}
