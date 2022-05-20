package com.keplerworks.f1tipper.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.keplerworks.f1tipper.type.BetSubjectType
import javax.persistence.*

@Entity
class BetSubject (
    val type: BetSubjectType,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    val name: String = "",
    @JsonIgnore
    val alias: String = "",
    val imgUrl: String? = "",
    val flag: String = "",
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(
        name = "bet_subjects_races",
        joinColumns = [JoinColumn(name = "bet_subject_id")],
        inverseJoinColumns = [JoinColumn(name = "race_id")]
    )
    @JsonIgnore
    val races: Set<Race> = setOf()
)