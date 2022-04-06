package com.keplerworks.f1tipper.model

import com.keplerworks.f1tipper.dto.UserDTO
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import javax.persistence.*

@Entity
class FormulaUser(
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    val id: Long = 0,
    val username: String,
    var password: String,
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    val roles: Set<FormulaRole>,
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_leagues",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "league_id")]
    )
    val leagues: MutableSet<League> = mutableSetOf()

)
{
    /*fun toUserDTO(): UserDTO {
        return UserDTO(username, leagues)
    }*/

    fun toUser(): User {
        val authorities = mutableSetOf<SimpleGrantedAuthority>()
        roles.forEach{
            authorities.add(SimpleGrantedAuthority(it.name))
        }
        return User(username, password, authorities)
    }

}