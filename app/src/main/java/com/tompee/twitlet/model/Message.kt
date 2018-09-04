package com.tompee.twitlet.model

import java.util.*

data class Message(val postId: String,
                   val message: String,
                   val image: String,
                   val time: Date)