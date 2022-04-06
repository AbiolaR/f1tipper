package com.keplerworks.f1tipper.dto

import com.keplerworks.f1tipper.model.League

data class UserDTO(
    val selectedLeague: League,
    val leagues: Set<League>,
    val loginData: LoginDataDTO,
    val selectedLanguage: String = ""
)