import java.util.Properties

val keystoreProperties =
    Properties().apply {
        val propertiesFile = rootProject.file("keystore.properties")
        if (propertiesFile.exists()) {
            propertiesFile.inputStream().use(::load)
        }
    }

val releaseSigningRequested =
    gradle.startParameter.taskNames.any { taskName ->
        taskName.contains("release", ignoreCase = true)
    }

fun Any.callNoArg(name: String): Any =
    javaClass.methods
        .first { it.name == name && it.parameterCount == 0 }
        .invoke(this)

fun Any.callOneArg(
    name: String,
    value: Any?,
) {
    javaClass.methods
        .first { it.name == name && it.parameterCount == 1 }
        .invoke(this, value)
}

@Suppress("UNCHECKED_CAST")
fun Any.namedContainer(name: String): NamedDomainObjectContainer<Any> = callNoArg(name) as NamedDomainObjectContainer<Any>

fun Properties.requiredString(name: String): String =
    requireNotNull(getProperty(name)) {
        "keystore.properties is missing required '$name'"
    }.trim()

fun Properties.requiredSigningValue(name: String): String {
    val value = requiredString(name)
    val normalized = value.lowercase()
    require(value.isNotBlank() && normalized !in setOf("change-me", "changeme", "todo", "password")) {
        "keystore.properties contains an unsafe placeholder for '$name'"
    }
    return value
}

fun Properties.validSigningConfig(): Boolean =
    runCatching {
        val storeFilePath = requiredSigningValue("storeFile")
        requiredSigningValue("storePassword")
        requiredSigningValue("keyAlias")
        requiredSigningValue("keyPassword")
        val storeFile = rootProject.file(storeFilePath)
        require(storeFile.isFile) {
            "keystore.properties points to a missing keystore file: ${storeFile.path}"
        }
    }.isSuccess

fun Properties.requireValidSigningConfig() {
    val storeFilePath = requiredSigningValue("storeFile")
    requiredSigningValue("storePassword")
    requiredSigningValue("keyAlias")
    requiredSigningValue("keyPassword")
    val storeFile = rootProject.file(storeFilePath)
    require(storeFile.isFile) {
        "keystore.properties points to a missing keystore file: ${storeFile.path}"
    }
}

val androidExtension = extensions.getByName("android")
val signingConfigs = androidExtension.namedContainer("getSigningConfigs")
val releaseSigningConfig = signingConfigs.findByName("release") ?: signingConfigs.create("release")

if (keystoreProperties.isNotEmpty() && (keystoreProperties.validSigningConfig() || releaseSigningRequested)) {
    keystoreProperties.requireValidSigningConfig()

    releaseSigningConfig.callOneArg("setStoreFile", rootProject.file(keystoreProperties.requiredSigningValue("storeFile")))
    releaseSigningConfig.callOneArg("setStorePassword", keystoreProperties.requiredSigningValue("storePassword"))
    releaseSigningConfig.callOneArg("setKeyAlias", keystoreProperties.requiredSigningValue("keyAlias"))
    releaseSigningConfig.callOneArg("setKeyPassword", keystoreProperties.requiredSigningValue("keyPassword"))

    val releaseBuildType = androidExtension.namedContainer("getBuildTypes").getByName("release")
    releaseBuildType.callOneArg("setSigningConfig", releaseSigningConfig)
}
