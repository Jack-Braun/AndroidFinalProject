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
    Login(R.string.login_title),
    NewPet(R.string.new_pet_title),
    EditProfile(R.string.edit_profile_screen),
    EditPet(R.string.edit_pet_screen),
    NewEvent(R.string.new_event_title),
    OtherProfiles(R.string.other_profiles_title)
}


//A list of the screens for looping purposes
val screens = listOf(
    PetScreens.Profile,
    PetScreens.OtherProfiles,
    PetScreens.Pet,
    PetScreens.Map
)