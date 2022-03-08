package com.keplerworks.f1tipper.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class League(
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    val id: Long = 0,
    val name: String,
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_leagues",
        joinColumns = [JoinColumn(name = "league_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    @JsonIgnore
    val users: MutableSet<FormulaUser> = mutableSetOf()
)
