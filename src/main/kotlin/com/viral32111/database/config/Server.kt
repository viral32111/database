package com.viral32111.database.config

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Server(
	@Required @SerialName( "address" ) val address: String = "127.0.0.1",
	@Required @SerialName( "port" ) val port: Int = 27017
) {
	init {
		require( address.isNotBlank() ) { "MongoDB server IP address cannot be empty or blank." }
		require( port in 1 .. 65535 ) { "MongoDB server port number must be between 1 and 65535." }
	}
}
