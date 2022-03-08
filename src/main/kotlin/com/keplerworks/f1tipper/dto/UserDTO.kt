package com.keplerworks.f1tipper.dto

import com.keplerworks.f1tipper.model.FormulaRole
import com.keplerworks.f1tipper.model.FormulaUser
import com.keplerworks.f1tipper.model.League

data class UserDTO(
    val username: String = "",
    //val password: String = "",
    val leagues: Set<League>
)
{
    /*fun toNewFormulaUser() = FormulaUser(
        username = username,
        password = password,
        roles = setOf(FormulaRole(name = "FORMULA_USER"))
    )*/
}
