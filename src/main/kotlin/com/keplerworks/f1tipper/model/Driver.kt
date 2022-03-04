package com.keplerworks.f1tipper.model

import com.keplerworks.f1tipper.dto.DriverDTO
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Driver(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    val id: Long = 0,
    private val firstName: String = "",
    private val lastName: String = "",
    private val imgUrl: String = "",
)
{
    fun toDriverDTO() = DriverDTO(
        id,
        "$firstName $lastName".ifBlank { "" },
        imgUrl
    )
}
