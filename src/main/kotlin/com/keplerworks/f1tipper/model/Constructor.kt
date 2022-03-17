package com.keplerworks.f1tipper.model

import javax.persistence.*

@Entity
class Constructor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    val id: Long = 0
    private val name: String = ""
    private val imgUrl: String = ""
}