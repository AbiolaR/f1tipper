package com.keplerworks.f1tipper.controller

import com.keplerworks.f1tipper.service.FormulaUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController (private val userService: FormulaUserService) {

    @PostMapping("/password/new")
    fun changePassword(@RequestParam username: String, @RequestParam password: String): Boolean {
        userService.changeUserPassword(username, password)
        return true
    }
}