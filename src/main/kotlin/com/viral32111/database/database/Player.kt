package com.viral32111.database.database

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Player(
	@SerialName( "uuid" ) val uuid: String,
	@SerialName( "username" ) val username: String
)
