package com.keplerworks.f1tipper.service

import com.keplerworks.f1tipper.dto.DriverDTO
import com.keplerworks.f1tipper.exception.DriverNotFoundException
import com.keplerworks.f1tipper.model.Driver
import com.keplerworks.f1tipper.repository.DriverRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DriverService @Autowired constructor(private val driverRepo: DriverRepository) {

    fun getAllDrivers(): List<Driver> {
        return driverRepo.findAll()
    }


    fun getDriverById(id: Long): Driver {
        return driverRepo.findDriverById(id)
                .orElseThrow {
                    DriverNotFoundException("Driver was not found using id: $id")
                }
    }

    fun getDriver(id: Long): Driver {
        return driverRepo.findDriverById(id).orElse(Driver())
    }
}