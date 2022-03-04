package com.keplerworks.f1tipper.repository

import com.keplerworks.f1tipper.model.FormulaRole
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface FormulaRoleRepository : JpaRepository<FormulaRole, Long> {
    fun findByName(name: String): Optional<FormulaRole>
}