package com.phinion.gcepluselearning.models

data class User(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val school: String = "",
    val selectedModule: String = "",
)
