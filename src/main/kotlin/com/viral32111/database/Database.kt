package com.viral32111.database

import net.fabricmc.api.DedicatedServerModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress( "UNUSED" )
class Database: DedicatedServerModInitializer {
	companion object {
		val LOGGER: Logger = LoggerFactory.getLogger( "database" )
	}

	override fun onInitializeServer() {
		LOGGER.info( "Database initialised." )
	}
}
