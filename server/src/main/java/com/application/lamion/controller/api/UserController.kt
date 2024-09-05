package com.application.lamion.controller.api

import com.application.lamion.exception.BadRequest
import com.application.lamion.exception.Unauthorized
import com.application.lamion.model.User
import com.application.lamion.model.responses.TokenResponse
import com.application.lamion.service.UserService
import com.application.lamion.util.Password
import com.application.lamion.util.TokenUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/api")
class UserController @Autowired constructor(private val userService: UserService, private val tokenUtil: TokenUtil) {
    @GetMapping("/user")
    fun profile(@RequestHeader("Authorization") token: String?): ResponseEntity<*> {
        if (token == null) {
            return ResponseEntity.status(401).body(Unauthorized())
        }

        val user = tokenUtil.verify(token)
        return ResponseEntity.ok(user)
    }

    @PostMapping("/login")
    fun login(@RequestBody form: LoginForm): ResponseEntity<*> {
        if (form.isEmpty) {
            return ResponseEntity.badRequest().body(
                BadRequest()
            )
        }

        val user = userService.findByEmail(form.email)

        if (user != null && Password.verify(form.password!!, user.password)) {
            val token = tokenUtil.create(user)
            return ResponseEntity.ok(TokenResponse(user, token))
        } else {
            return ResponseEntity.badRequest().body(
                BadRequest(USER_NOT_FOUND)
            )
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody data: LoginForm): ResponseEntity<*> {
        if (userService.existsByEmail(data.email)) {
            return ResponseEntity.badRequest().body(
                BadRequest(USER_ALREADY_EXIST)
            )
        }

        val user = User(
            email = data.email!!,
            password = Password.hash(data.password!!)
        )
        userService.create(user)

        val token = tokenUtil.create(user)
        return ResponseEntity.ok(TokenResponse(user, token))
    }

    class LoginForm {
        var email: String? = null
        var password: String? = null

        val isEmpty: Boolean
            get() = ((email == null || email!!.isBlank()) || (password == null || password!!.isBlank()))
    }

    companion object {
        private const val USER_NOT_FOUND = "User with such credentials does not exist."
        private const val USER_ALREADY_EXIST = "User with such credentials already exist."
    }
}
