package com.tompee.twitlet.model

data class User(var email: String = "",
                var isAuthenticated: Boolean = false,
                var nickname: String = "",
                var imageUrl: String = "")