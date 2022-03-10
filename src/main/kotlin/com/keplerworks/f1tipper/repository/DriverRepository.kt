package com.keplerworks.f1tipper.repository

import com.keplerworks.f1tipper.model.Driver
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface DriverRepository : JpaRepository<Driver, Long> {
    fun findDriverById(id: Long): Optional<Driver>

    fun findDriverByFirstNameAndLastName(firstName: String, lastName: String): Optional<Driver>
}