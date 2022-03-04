package com.keplerworks.f1tipper.repository

import com.keplerworks.f1tipper.model.FormulaUser
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface FormulaUserRepository : JpaRepository<FormulaUser, Long>{
    fun findByUsername(username: String): Optional<FormulaUser>
}