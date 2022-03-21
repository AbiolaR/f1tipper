package com.keplerworks.f1tipper.model

import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
class Position (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    val betSubjectId: Long = 0,
    @Type(type = "org.hibernate.type.NumericBooleanType")
    val fastestLap: Boolean = false,
    val position: Int = 0,
    val betItemId: Long? = null,
    @ManyToOne
    val result: Result? = null
) {
    companion object {
        fun Int.toPositionGroup(): List<Int> {
            return listOf(this - 1, this, this + 1)
        }
    }
}