package com.keplerworks.f1tipper.service

import com.keplerworks.f1tipper.model.FormulaUser
import com.keplerworks.f1tipper.repository.FormulaUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class FormulaUserService (@Autowired private val userRepo: FormulaUserRepository) : UserDetailsService {
    private val passwordEncoder = BCryptPasswordEncoder()

    fun getUser(username: String): FormulaUser {
        return userRepo.findByUsername(username).orElse(null)
    }

    fun saveUser(user: FormulaUser): FormulaUser {
        user.password = passwordEncoder.encode(user.password)
        return userRepo.save(user)
    }

    override fun loadUserByUsername(username: String) = getUser(username).toUser()
}

/*@Bean
@Override
public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
}*/