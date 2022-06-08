package com.keplerworks.f1tipper.model.entity

import com.keplerworks.f1tipper.type.RapidF1LiveSessionType
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id

@Entity
class RapidF1LiveSession(
    @Id
    val id: Long,
    @Enumerated(EnumType.STRING)
    val type: RapidF1LiveSessionType,
    val raceId: Long
)