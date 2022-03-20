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
            val groups = listOf(
                listOf(1,2,3),
                listOf(4,5,6),
                listOf(7,8,9),
                listOf(10,11,12),
                listOf(13,14,15),
                listOf(16,17,18),
                listOf(19,20)
            )

            groups.forEach { group ->
                if (group.contains(this)) {
                    return group
                }
            }

            throw Exception("Invalid position number") // TODO custom exception
        }
    }
}