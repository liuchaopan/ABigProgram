package com.pan.abigprogram.http.service

data class ServiceManager(val userService: UserService,
                          val loginService: LoginService)
