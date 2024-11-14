package com.example.finalproject

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.MutableState
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

import com.example.finalproject.data.*
import com.example.finalproject.ui.components.*
import com.example.finalproject.ui.screens.*

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

    val profiles = remember { mutableStateOf<List<UserProfile>>(emptyList()) }

    PetAppContent(
        navController = navController,
        profiles = profiles
    )
}

@Composable
fun PetAppContent(
    navController: NavHostController,
    profiles: MutableState<List<UserProfile>>
) {
    var isLoggedIn by remember { mutableStateOf(false) }
    var incorrectLogin by remember { mutableStateOf(false) }
    var currentUserProfile by remember { mutableStateOf<UserProfile?>(null) }



    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = PetScreens.valueOf(
        backStackEntry?.destination?.route ?: PetScreens.Home.name
    )

    Scaffold(
        topBar = {
            PetAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PetScreens.Login.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = PetScreens.Login.name) {
                LoginScreen(
                    login = { username, password ->
                        currentUserProfile = profiles.value.find {
                            it.username == username && it.password == password
                        }
                        if(currentUserProfile != null) {
                            isLoggedIn = true
                            incorrectLogin = false
                            navController.navigate(PetScreens.Home.name)
                        } else {
                            incorrectLogin = true
                        }

                    },
                    register = { navController.navigate(PetScreens.Register.name) },
                    incorrectLogin = incorrectLogin
                )
            }
            composable(route = PetScreens.Home.name) {
                HomeScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    onNavigate = { route -> navController.navigate(route) },
                    profiles = profiles.value.filter { it.username != currentUserProfile?.username }
                )
            }
            composable(route = PetScreens.Pet.name) {
                PetScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    pets = currentUserProfile?.pets ?: emptyList(),
                    onNavigateToNewPet = {navController.navigate(PetScreens.NewPet.name)}
                )
            }
            composable(route = PetScreens.Profile.name) {
                ProfileScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    userProfile = currentUserProfile ?: UserProfile(
                        username="",
                        password="",
                        name="",
                        bio="",
                        pets = emptyList()
                    ),
                    logout = { isLoggedIn = false; navController.navigate("Login") }
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
            composable(route = PetScreens.Register.name) {
                RegisterScreen(
                    register = { username, password, name, bio ->
                        registerNewUser(context = navController.context, username, password, name, bio)
                        navController.popBackStack()
                    }
                )
            }
            composable(route = PetScreens.NewPet.name) {
                NewPetScreen (
                    addPet = { newPet ->
                        currentUserProfile?.pets = currentUserProfile?.pets.orEmpty() + newPet
                    },
                    onNavigateBack = {navController.popBackStack()}
                )
            }
        }
    }
}

//Creating new user and sending to the database
fun registerNewUser(context: Context, username: String, password: String, name: String, bio: String) {
    val values = ContentValues().apply {
        put("username", username)
        put("password", password)
        put("name", name)
        put("bio", bio)
    }

    try {
        val uri = context.contentResolver.insert(PetAppContentProvider.CONTENT_URI, values)

        if (uri != null) {
            Toast.makeText(context, "Profile created", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to create profile", Toast.LENGTH_SHORT).show()
        }
    } catch (e: SQLException) {
        Toast.makeText(context, "Username already exists.", Toast.LENGTH_LONG).show()
    }
}

//Ensuring usernames are unique
fun isUsernameTaken(context: Context, username: String): Boolean {
    val uri = Uri.parse("content://com.example.finalproject/users")
    val projection = arrayOf("username")
    val selection = "username = ?"
    val selectionArgs = arrayOf(username)

    val cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
    val isTaken = (cursor?.count ?: 0) > 0
    cursor?.close()
    return isTaken
}