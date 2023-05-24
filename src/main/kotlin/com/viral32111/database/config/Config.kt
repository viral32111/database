package com.viral32111.database.config

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Config(
	@Required @SerialName( "mongodb" ) val mongoDB: MongoDB = MongoDB()
)
