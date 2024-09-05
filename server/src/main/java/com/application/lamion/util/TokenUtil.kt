package com.application.lamion.util

import com.application.lamion.model.User
import com.application.lamion.service.UserService
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TokenUtil @Autowired constructor(private val service: UserService) {
    fun create(user: User): String {
        return JWT.create()
            .withIssuer("auth0")
            .withClaim(User::class.java.simpleName, user.email)
            .sign(algorithm)
    }

    fun verify(token: String): User? {
        try {
            val verifier = JWT.require(algorithm)
                .withIssuer("auth0")
                .build() //Reusable verifier instance
            val jwt = verifier.verify(token)
            val emailClaim = jwt.getClaim(User::class.java.simpleName)

            val email = emailClaim.`as`(String::class.java)
            return service.findByEmail(email)
        } catch (e: JWTDecodeException) {
            return null
        }
    }

    private val algorithm: Algorithm
        get() = Algorithm.HMAC256("secret")
}
