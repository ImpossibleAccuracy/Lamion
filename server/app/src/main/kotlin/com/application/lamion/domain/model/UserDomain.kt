package com.application.lamion.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "user", schema = "public")
class UserDomain(
    id: Int? = null,

    @Column(name = "email")
    var email: String,

    @Column(name = "password")
    var passwordHash: String,
) : BaseModel<Int>(id), UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> =
        setOf()

    override fun getPassword(): String =
        passwordHash

    override fun getUsername(): String =
        email

    override fun isAccountNonExpired(): Boolean =
        true

    override fun isAccountNonLocked(): Boolean =
        true

    override fun isCredentialsNonExpired(): Boolean =
        true

    override fun isEnabled(): Boolean =
        true
}
