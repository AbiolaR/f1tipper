package com.keplerworks.f1tipper.service

import com.keplerworks.f1tipper.model.FormulaUser
import com.keplerworks.f1tipper.repository.FormulaUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class FormulaUserService (private val userRepo: FormulaUserRepository,
                          private val mailService: EmailService) : UserDetailsService {
    private val passwordEncoder = BCryptPasswordEncoder()

    fun getUser(username: String): FormulaUser {
        return userRepo.findByUsername(username).orElse(null)
    }

    fun saveUser(user: FormulaUser): FormulaUser {
        user.password = passwordEncoder.encode(user.password)
        return userRepo.save(user)
    }

    fun changeUserPassword(username: String, password: String) {
        getUser(username)
        mailService.sendChangePasswordMail(username, passwordEncoder.encode(password))
    }

    override fun loadUserByUsername(username: String) = getUser(username).toUser()
}