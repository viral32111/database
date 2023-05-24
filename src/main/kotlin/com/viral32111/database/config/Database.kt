package com.viral32111.database.config

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Database(
	@Required @SerialName( "name" ) val name: String = "Minecraft",
	@Required @SerialName( "user" ) val user: String = "JohnDoe",
	@Required @SerialName( "password" ) val password: String = "P4ssw0rd!"
) {
	init {
		require( name.isNotBlank() ) { "MongoDB database name cannot be empty or blank." }
		require( user.isNotBlank() ) { "MongoDB database user cannot be empty or blank." }
		require( password.isNotBlank() ) { "MongoDB database password cannot be empty or blank." }
	}
}
