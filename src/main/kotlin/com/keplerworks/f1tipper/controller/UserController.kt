package com.keplerworks.f1tipper.controller

import com.keplerworks.f1tipper.dto.UserDTO
import com.keplerworks.f1tipper.service.FormulaUserService
import com.keplerworks.f1tipper.service.NotificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/user")
class UserController @Autowired constructor(private val service: FormulaUserService, private val notificationService: NotificationService) {
    /*@GetMapping("")
    fun getUser(@RequestBody userDTO: UserDTO): UserDTO {
        return service.getUser(userDTO.username).toUserDTO()
    }*/

    @GetMapping("")
    fun getSelf(request: HttpServletRequest): UserDTO {
        return service.getUser(request.userPrincipal.name).toUserDTO()
    }

    /*@PostMapping("")
    fun saveUser(@RequestBody userDTO: UserDTO): UserDTO {
        return service.saveUser(userDTO.toNewFormulaUser()).toUserDTO()
    }*/
}