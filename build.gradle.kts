plugins {
	id( "fabric-loom" )
	kotlin( "jvm" ) version( System.getProperty( "kotlin_version" ) )
	kotlin( "plugin.serialization" ) version( System.getProperty( "kotlin_version" ) )
}

base {
	archivesName.set( project.extra[ "archives_base_name" ] as String )
}
version = project.extra[ "mod_version" ] as String
group = project.extra[ "maven_group" ] as String

repositories {
	maven {
		url = uri( "https://maven.pkg.github.com/viral32111/events" )
		credentials {
			username = project.findProperty( "ghpkg.user" ) as String? ?: System.getenv( "GHPKG_USER" )
			password = project.findProperty( "ghpkg.token" ) as String? ?: System.getenv( "GHPKG_TOKEN" )
		}
	}
}

dependencies {

	// Minecraft
	minecraft( "com.mojang", "minecraft", project.extra[ "minecraft_version" ] as String )

	// Minecraft source mappings - https://github.com/FabricMC/yarn
	mappings( "net.fabricmc", "yarn", project.extra[ "yarn_mappings" ] as String, null, "v2" )

	// Fabric Loader - https://github.com/FabricMC/fabric-loader
	modImplementation( "net.fabricmc", "fabric-loader", project.extra[ "loader_version" ] as String )

	// Fabric API - https://github.com/FabricMC/fabric
	modImplementation( "net.fabricmc.fabric-api", "fabric-api", project.extra[ "fabric_version" ] as String )

	// Kotlin support for Fabric - https://github.com/FabricMC/fabric-language-kotlin
	modImplementation( "net.fabricmc", "fabric-language-kotlin", project.extra[ "fabric_language_kotlin_version" ] as String )

	// Kotlin JSON serialization
	implementation( "org.jetbrains.kotlinx", "kotlinx-serialization-json", project.extra[ "kotlinx_serialization_json_version" ] as String )

	// My callbacks - https://github.com/viral32111/events
	modImplementation( "com.viral32111", "events", project.extra[ "events_version" ] as String )

	// MongoDB Java Sync Driver - https://www.mongodb.com/docs/drivers/java/sync/current/
	implementation( "org.mongodb:mongodb-driver-core:4.9.1" )
	implementation( "org.mongodb:mongodb-driver-sync:4.9.1" )
	implementation( "org.mongodb:bson:4.9.1" )
	include( "org.mongodb:mongodb-driver-core:4.9.1" ) // Bundle dependency into JAR
	include( "org.mongodb:mongodb-driver-sync:4.9.1" ) // Bundle dependency into JAR
	include( "org.mongodb:bson:4.9.1" ) // Bundle dependency into JAR

	//implementation( "ch.qos.logback:logback-classic:1.4.7" )
	//include( "ch.qos.logback:logback-classic:1.4.7" )

	// MongoDB Reactive Streams Driver - https://www.mongodb.com/docs/drivers/reactive-streams/
	//implementation( "org.mongodb:mongodb-driver-reactivestreams:4.9.0" )

	//implementation( "io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.2" )
	//implementation( "org.jetbrains.kotlin:kotlin-reflect:1.8.21" )
	//implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.1" )

	// KMongo (Reactive Streams) - https://litote.org/kmongo/
	//implementation( "org.litote.kmongo:kmongo-async:4.9.0" )

	// Spring Data MongoDB - https://www.mongodb.com/developer/languages/kotlin/spring-boot3-kotlin-mongodb/
	/*implementation( "org.springframework.boot:spring-boot-starter-data-mongodb:3.1.0" )
	implementation( "org.springframework.boot:spring-boot-starter-data-mongodb-reactive:3.1.0" )
	implementation( "io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.2" )
	implementation( "org.jetbrains.kotlin:kotlin-reflect:1.8.21" )
	implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.1" )*/

}

tasks {
	val javaVersion = JavaVersion.toVersion( ( project.extra[ "java_version" ] as String ).toInt() )

	withType<JavaCompile> {
		options.encoding = "UTF-8"
		sourceCompatibility = javaVersion.toString()
		targetCompatibility = javaVersion.toString()
		options.release.set( javaVersion.toString().toInt() )
	}

	withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
		kotlinOptions {
			jvmTarget = javaVersion.toString()
		}
	}

	jar {
		from( "LICENSE.txt" ) {
			rename { "${ it }_${ base.archivesName.get() }.txt" }
		}
	}

	processResources {

		// Metadata
		filesMatching( "fabric.mod.json" ) {
			expand( mutableMapOf(
				"java" to project.extra[ "java_version" ] as String,
				"minecraft" to project.extra[ "minecraft_version" ] as String,
				"version" to project.extra[ "mod_version" ] as String,
				"fabricloader" to project.extra[ "loader_version" ] as String,
				"fabric_api" to project.extra[ "fabric_version" ] as String,
				"fabric_language_kotlin" to project.extra[ "fabric_language_kotlin_version" ] as String
			) )
		}

		// Mixins
		filesMatching( "*.mixins.json" ) {
			expand( mutableMapOf(
				"java" to project.extra[ "java_version" ] as String
			) )
		}

	}

	java {
		toolchain {
			languageVersion.set( JavaLanguageVersion.of( javaVersion.toString() ) )
		}

		sourceCompatibility = javaVersion
		targetCompatibility = javaVersion

		withSourcesJar()
	}
}
