package com.keplerworks.f1tipper.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class Position (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    val driverId: Long = 0,
    val position: Int = 0,
    val betId: Long? = null,
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