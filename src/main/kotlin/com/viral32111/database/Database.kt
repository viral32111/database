package com.viral32111.database

import com.mongodb.*
import com.mongodb.client.MongoClients
import com.mongodb.connection.ClusterConnectionMode
import com.viral32111.database.config.Config
import com.viral32111.events.callback.server.PlayerJoinCallback
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.ActionResult
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LoggerContext
import java.net.ConnectException
import java.nio.file.StandardOpenOption
import java.util.concurrent.TimeUnit
import kotlin.io.path.createDirectory
import kotlin.io.path.notExists
import kotlin.io.path.readText
import kotlin.io.path.writeText

@Suppress( "UNUSED" )
class Database: DedicatedServerModInitializer {
	companion object {
		val LOGGER: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger( "com.viral32111.database" )

		@OptIn( ExperimentalSerializationApi::class )
		val JSON = Json {
			prettyPrint = true
			prettyPrintIndent = "\t"
			ignoreUnknownKeys = true
		}

		const val CONFIGURATION_DIRECTORY_NAME = "viral32111"
		const val CONFIGURATION_FILE_NAME = "database.json"
	}

	override fun onInitializeServer() {
		LOGGER.info( "Database initialised." )

		val serverConfigurationDirectory = FabricLoader.getInstance().configDir
		val myConfigurationDirectory = serverConfigurationDirectory.resolve( CONFIGURATION_DIRECTORY_NAME )
		val myConfigurationFile = myConfigurationDirectory.resolve( CONFIGURATION_FILE_NAME )

		if ( myConfigurationDirectory.notExists() ) {
			myConfigurationDirectory.createDirectory()
			LOGGER.info( "Created configuration directory '${ myConfigurationDirectory }'." )
		}

		if ( myConfigurationFile.notExists() ) {
			val jsonConfig = JSON.encodeToString( Config() )

			myConfigurationFile.writeText( jsonConfig, options = arrayOf(
				StandardOpenOption.CREATE_NEW,
				StandardOpenOption.WRITE
			) )

			LOGGER.info( "Created configuration file '${ myConfigurationFile }'." )
		}

		val jsonConfig = myConfigurationFile.readText()
		val config = JSON.decodeFromString<Config>( jsonConfig )

		LOGGER.info( "Server: ${ config.mongoDB.server.address }:${ config.mongoDB.server.port }, Database: ${ config.mongoDB.database.name } (${ config.mongoDB.database.user }, ${ config.mongoDB.database.password })" )

		// Disable MongoDB Driver logging
		java.util.logging.Logger.getLogger( java.util.logging.Logger.GLOBAL_LOGGER_NAME ).level = java.util.logging.Level.OFF
		java.util.logging.Logger.getLogger( "org.mongodb.driver" ).level = java.util.logging.Level.OFF
		java.util.logging.Logger.getLogger( "com.mongodb" ).level = java.util.logging.Level.OFF
		org.slf4j.LoggerFactory.getLogger( "org.mongodb.driver" ).atLevel( org.slf4j.event.Level.ERROR )
		org.slf4j.LoggerFactory.getLogger( "com.mongodb" ).atLevel( org.slf4j.event.Level.ERROR )
		//( org.slf4j.LoggerFactory.getILoggerFactory() as ch.qos.logback.classic.LoggerContext ).getLogger( "org.mongodb.driver" ).level = ch.qos.logback.classic.Level.OFF
		//( org.slf4j.LoggerFactory.getILoggerFactory() as ch.qos.logback.classic.LoggerContext ).getLogger( "com.mongodb" ).level = ch.qos.logback.classic.Level.OFF
		//(  as LoggerContext ).getLogger( "org.mongodb.driver" ).level = Level.OFF
		//( LogManager.getLogger( "org.mongodb.driver" ) as  ).level = Level.OFF
		//( org.slf4j.LoggerFactory.getILoggerFactory() as LoggerContext ).getLogger( "com.mongodb" ).level = Level.OFF
		//( LoggerFactory.getILoggerFactory() as LoggerContext ).getLogger( "org.mongodb.driver" ).level = Level.OFF
		//( LoggerFactory.getILoggerFactory() as LoggerContext ).getLogger( "com.mongodb" ).level = Level.OFF
		//LogManager.getLogger( "org.mongodb.driver" ).level = Level.OFF

		tryMongoDB( config )

		PlayerJoinCallback.EVENT.register { connection, player ->
			LOGGER.info( "${ player.displayName } (${ connection.address }) connected" )
			return@register ActionResult.PASS
		}
	}

	private fun tryMongoDB( config: Config ) {
		val serverAddress = config.mongoDB.server.address
		val serverPort = config.mongoDB.server.port
		val databaseName = config.mongoDB.database.name
		val databaseUser = config.mongoDB.database.user
		val databasePassword = config.mongoDB.database.password

		try {
			val client = MongoClients.create( MongoClientSettings.builder()
				.applicationName( "Minecraft" )
				.credential( MongoCredential.createCredential( databaseUser, databaseName, databasePassword.toCharArray() ) )
				.applyToClusterSettings { builder ->
					builder.mode( ClusterConnectionMode.SINGLE ) // Direct connection
					builder.serverSelectionTimeout( 1, TimeUnit.SECONDS )
					builder.hosts( listOf( ServerAddress( serverAddress, serverPort ) )
				) }
				.applyToSocketSettings { builder ->
					builder.connectTimeout( 3, TimeUnit.SECONDS )
					builder.readTimeout( 3, TimeUnit.SECONDS )
				}
				.applyToSslSettings { builder ->
					builder.enabled( false ) // Disable TLS
				}
				.retryWrites( false )
				.retryReads( false )
				.build() )

			val database = client.getDatabase( databaseName )
			val collection = database.getCollection( "Players" )

			collection.find().forEach { document ->
				LOGGER.info( document.toJson() )
			}
		} catch ( exception: MongoSocketOpenException ) {
			LOGGER.error( "Failed to connect to the MongoDB server! (${ exception.message })" )
		} catch ( exception: ConnectException ) {
			LOGGER.error( "Failed to connect to the MongoDB server! (${ exception.message })" )
		} catch ( exception: MongoTimeoutException ) {
			LOGGER.error( "Timed out while connecting to the MongoDB server! (${ exception.message })" )
		} catch ( exception: MongoSocketException ) {
			LOGGER.error( "Socket exception while connecting to the MongoDB server! (${ exception.message }" )
		} catch ( exception: Exception ) {
			LOGGER.error( "Exception while connecting to the MongoDB server! (${ exception.message }" )
		} catch ( exception: RuntimeException ) {
			LOGGER.error( "Runtime exception while connecting to the MongoDB server! (${ exception.message }" )
		}

	}

}
