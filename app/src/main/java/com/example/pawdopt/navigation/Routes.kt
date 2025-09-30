package com.example.pawdopt.navigation

object Routes {
    const val HOME = "home"
    const val PET_DETAIL = "pet_detail/{petId}"
    fun petDetailRoute(petId: Int) = "pet_detail/$petId"

    const val ADD_PET = "add_pet"
    const val ADOPTION_REQUEST = "adoption_request/{petId}"
    fun adoptionRequestRoute(petId: Int) = "adoption_request/$petId"

    const val MY_REQUESTS = "my_requests"
    const val PROFILE = "profile"

    const val LOGIN = "login"
    const val REGISTER = "register"
}
