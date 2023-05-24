package com.viral32111.database.config

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MongoDB(
	@Required @SerialName( "server" ) val server: Server = Server(),
	@Required @SerialName( "database" ) val database: Database = Database()
)
