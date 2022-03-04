package com.keplerworks.f1tipper.controller

import com.keplerworks.f1tipper.dto.DriverDTO
import com.keplerworks.f1tipper.toDriverDTO
import com.keplerworks.f1tipper.model.Driver
import com.keplerworks.f1tipper.service.DriverService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/driver")
class DriverController @Autowired constructor(val service: DriverService) {
    @GetMapping("all")
    fun getAllDrivers(): List<DriverDTO> {
        return service.getAllDrivers().toDriverDTO()
    }

    @GetMapping("id/{id}")
    fun getDriverById(@PathVariable id: Long): DriverDTO {
        return service.getDriverById(id).toDriverDTO()
    }

    /*@GetMapping("")
    fun driverOverview(@Value ("forward:/static/f1tipper-frontend/index.html") html: Resource): Resource {
        return html;//"forward:/f1tipper-frontend/index.html"
    }*/


}