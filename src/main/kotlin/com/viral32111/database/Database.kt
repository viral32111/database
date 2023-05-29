package com.viral32111.database

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.viral32111.database.config.Config
import com.viral32111.database.database.Player
import com.viral32111.events.callback.server.PlayerJoinCallback
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.ActionResult
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.reactivestreams.findOne
import org.litote.kmongo.reactivestreams.getCollection
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.StandardOpenOption
import kotlin.io.path.createDirectory
import kotlin.io.path.notExists
import kotlin.io.path.readText
import kotlin.io.path.writeText

@Suppress( "UNUSED" )
class Database: DedicatedServerModInitializer {
	companion object {
		val LOGGER: Logger = LoggerFactory.getLogger( "database" )

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

		val connectionString = ConnectionString( "mongodb://${ databaseUser }:${ databasePassword }@${ serverAddress }:${ serverPort }/${ databaseName }?directConnection=true&tls=false" )
		val clientSettings = MongoClientSettings.builder()
			.applyConnectionString( connectionString )
			.retryWrites( true )
			.retryReads( true )
			.build()

		val client = KMongo.createClient( clientSettings )
		val database = client.getDatabase( databaseName )
		val collection = database.getCollection<Player>()

		runBlocking {
			collection.insertOne( Player( "1-2-3-4", "hi" ) )
			collection.findOne( Player::username eq "hi" )
		}
	}

}
