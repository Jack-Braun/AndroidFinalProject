package com.example.finalproject

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.net.Uri
import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.google.android.gms.maps.model.LatLng

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
        title = {Text(currentScreen.name)},
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

    PetAppContent(
        navController = navController,
    )
}

@Composable
fun PetAppContent(
    navController: NavHostController,
) {
    var isLoggedIn by remember { mutableStateOf(false) }
    var currentUserProfile by remember { mutableStateOf<UserProfile?>(null) }
    var pets by remember { mutableStateOf<List<Pet>>(emptyList()) }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = PetScreens.valueOf(
        backStackEntry?.destination?.route ?: PetScreens.Home.name
    )

    val events = fetchEvents(LocalContext.current)

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
                val context = LocalContext.current
                LoginScreen(
                    login = { username, password ->
                        val userCursor = context.contentResolver.query(
                            Uri.parse("content://com.example.finalproject/users"),
                            arrayOf("username", "password", "name", "bio"),
                            "username = ? AND password = ?",
                            arrayOf(username, password),
                            null
                        )

                        if (userCursor?.moveToFirst() == true) {
                            currentUserProfile = UserProfile(
                                username = userCursor.getString(userCursor.getColumnIndexOrThrow("username")),
                                password = userCursor.getString(userCursor.getColumnIndexOrThrow("password")),
                                name = userCursor.getString(userCursor.getColumnIndexOrThrow("name"))
                                    ?: "Not Set",
                                bio = userCursor.getString(userCursor.getColumnIndexOrThrow("bio"))
                                    ?: "Not Set",
                                pets = getUserPets(context, username)
                            )
                            pets = currentUserProfile?.pets ?: emptyList()
                            isLoggedIn = true
                            userCursor.close()
                            navController.navigate(PetScreens.Home.name)
                        } else {
                            userCursor?.close()
                        }
                    },
                    register = { navController.navigate(PetScreens.Register.name) },
                )
            }
            composable(route = PetScreens.Home.name) {
                val context = LocalContext.current
                val profiles = fetchProfiles(context)
                HomeScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    onNavigate = { route -> navController.navigate(route) },
                    profiles = profiles.filter { it.username != currentUserProfile?.username },
                    newEvent = { navController.navigate(PetScreens.NewEvent.name) },
                    events = events,
                    currentUserProfile = currentUserProfile,
                    addUserToEventAction = { eventId, username ->
                        addUserToEvent(context, eventId, username)
                    }
                )
            }
            composable(route = PetScreens.Pet.name) {
                PetScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    pets = pets,
                    onNavigateToNewPet = { navController.navigate(PetScreens.NewPet.name) }
                )
            }
            composable(route = PetScreens.Profile.name) {
                ProfileScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    //has a default UserProfile in case something goes wrong
                    userProfile = currentUserProfile ?: UserProfile(
                        username = "",
                        password = "",
                        name = "",
                        bio = "",
                        pets = emptyList()
                    ),
                    logout = { isLoggedIn = false; navController.navigate("Login") },
                    onEditProfile = { navController.navigate(PetScreens.EditProfile.name) }
                )
            }
            composable(route = PetScreens.Map.name) {
                MapScreen()
            }
            composable(route = PetScreens.Health.name) {
                HealthScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
            composable(route = PetScreens.Register.name) {
                RegisterScreen(
                    register = { username, password, name, bio ->
                        registerNewUser(
                            context = navController.context,
                            username,
                            password,
                            name,
                            bio
                        )
                        navController.popBackStack()
                    }
                )
            }
            composable(route = PetScreens.NewPet.name) {
                NewPetScreen(
                    addPet = { newPet, context -> addPet(newPet, context) },
                    onNavigateBack = { navController.popBackStack() },
                    currentUserProfile = currentUserProfile,
                    //This function is needed or the PetScreen will not show the current pet list after adding a new pet
                    updatePetList = { newPet ->
                        pets = pets + newPet
                    }
                )
            }
            composable(route = PetScreens.EditProfile.name) {
                currentUserProfile?.let { profile ->
                    EditProfileScreen(
                        userProfile = profile,
                        updateUserProfile = { name, bio, username ->
                            updateUserProfileInDB(
                                context = navController.context,
                                username,
                                name,
                                bio
                            )
                            currentUserProfile = currentUserProfile?.copy(name = name, bio = bio)
                        },
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
            composable(route = PetScreens.NewEvent.name) {
                EventScreen(
                    onNavigateBack = { navController.popBackStack() },
                    addEvent = { event -> addEventToDatabase(navController.context, event) },
                    fetchParks = {
                        try {
                            RetrofitInstance.api.getParks().results
                        } catch (e: Exception) {
                            //returns empty list if error
                            emptyList()
                        }
                    }

                )
            }
        }
    }
}

fun addUserToEvent(context: Context, eventId: Int, username: String) {
    val uri = Uri.parse("content://com.example.finalproject/events/")
    val selection = "id = ?"
    val selectionArgs = arrayOf(eventId.toString())

    val cursor = context.contentResolver.query(
        uri,
        arrayOf("id", "name", "address", "date", "attendees"),
        selection,
        selectionArgs,
        null
    )

    cursor?.use {
        if (it.moveToFirst()) {
            val attendeesString = it.getString(it.getColumnIndexOrThrow("attendees"))
            val attendees = attendeesString?.split(",")?.toMutableList() ?: mutableListOf()

            if (!attendees.contains(username)) {
                attendees.add(username)

                val updatedAttendees = attendees.joinToString(",")

                val values = ContentValues().apply {
                    put("attendees", updatedAttendees)
                }
                context.contentResolver.update(
                    uri,
                    values,
                    selection,
                    selectionArgs
                )
            }
        }
    }
}

fun fetchEvents(context: Context): List<Event> {
    val events = mutableListOf<Event>()
    val uri = Uri.parse("content://com.example.finalproject/events")
    val cursor = context.contentResolver.query(uri, null, null, null, null)

    cursor?.use {
        while (it.moveToNext()) {
            val id = it.getInt(it.getColumnIndexOrThrow("id"))
            val name = it.getString(it.getColumnIndexOrThrow("name"))
            val address = it.getString(it.getColumnIndexOrThrow("address"))
            val date = it.getString(it.getColumnIndexOrThrow("date"))
            val attendees = it.getString(it.getColumnIndexOrThrow("attendees"))
                ?.split(",")?.filter { attendee -> attendee.isNotBlank() } ?: emptyList()
            events.add(Event(id, name, address, date, attendees.toMutableList()))
        }
    }
    return events
}

fun updateUserProfileInDB(context: Context, username: String, name: String, bio: String) {
    val values = ContentValues().apply {
        put("name", name)
        put("bio", bio)
    }
    context.contentResolver.update(
        Uri.parse("content://com.example.finalproject/users"),
        values,
        "username = ?",
        arrayOf(username)
    )
}

fun getUserPets(context: Context, username: String): List<Pet> {
    val pets = mutableListOf<Pet>()
    val petsCursor = context.contentResolver.query(
        PetAppContentProvider.PET_CONTENT_URI,
        arrayOf("id", "name", "age", "animal", "colour", "breed"),
        "owner = ?",
        arrayOf(username),
        null
    )

    petsCursor?.use {
        while (it.moveToNext()) {
            val pet = Pet(
                id = it.getInt(it.getColumnIndexOrThrow("id")),
                name = it.getString(it.getColumnIndexOrThrow("name")),
                age = it.getInt(it.getColumnIndexOrThrow("age")),
                animal = it.getString(it.getColumnIndexOrThrow("animal")),
                colour = it.getString(it.getColumnIndexOrThrow("colour")),
                breed = it.getString(it.getColumnIndexOrThrow("breed")),
                owner = username
            )
            pets.add(pet)
        }
    }
    return pets
}

fun addEventToDatabase(context: Context, event: Event) {
    val contentValues = ContentValues().apply {
        put("name", event.name)
        put("address", event.address)
        put("date", event.date)
        put("attendees", event.attendees.joinToString(","))
    }

    context.contentResolver.insert(
        Uri.parse("content://com.example.finalproject/events"),
        contentValues
    )
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
        val uri = context.contentResolver.insert(Uri.parse("content://com.example.finalproject/users"), values)

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

fun fetchProfiles(context: Context): List<UserProfile> {
    val uri = Uri.parse("content://com.example.finalproject/users")
    val projection = arrayOf("username", "name", "bio")
    val profiles = mutableListOf<UserProfile>()

    val cursor = context.contentResolver.query(uri, projection, null, null, null)
    cursor?.use {
        while (it.moveToNext()) {
            val username = it.getString(it.getColumnIndexOrThrow("username"))
            val name = it.getString(it.getColumnIndexOrThrow("name")) ?: "Not Set"
            val bio = it.getString(it.getColumnIndexOrThrow("bio")) ?: "Not Set"

            profiles.add(UserProfile(username = username, name = name, bio = bio, password = "", pets = emptyList()))
            //password is left blank here because it is not needed to be shown
        }
    }
    return profiles
}