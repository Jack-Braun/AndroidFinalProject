package com.example.finalproject.ui.components

import androidx.annotation.StringRes
import com.example.finalproject.R

//The various screens in the app
enum class PetScreens(@StringRes val title: Int) {
    Home(R.string.home_title),
    Profile(R.string.profile_title),
    Pet(R.string.pet_title),
    Health(R.string.health_title),
    Map(R.string.map_title),
    Register(R.string.register_title),
    Login(R.string.login_title)
}

//A list of the screens for looping purposes
val screens = listOf(
    PetScreens.Home,
    PetScreens.Profile,
    PetScreens.Pet,
    PetScreens.Health,
    PetScreens.Map
)