package com.keplerworks.f1tipper.model.entity

import com.keplerworks.f1tipper.type.RapidSessionType
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id

@Entity
class RapidSession(
    @Id
    val id: Long,
    @Enumerated(EnumType.STRING)
    val type: RapidSessionType,
    val raceId: Long
)