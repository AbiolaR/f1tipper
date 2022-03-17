package com.keplerworks.f1tipper.model

import com.keplerworks.f1tipper.type.BetSubjectType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class BetSubject (
    val type: BetSubjectType,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    val name: String = "",
    val imgUrl: String? = ""
)