import java.util.Properties

val keystoreProperties =
    Properties().apply {
        val propertiesFile = rootProject.file("keystore.properties")
        if (propertiesFile.exists()) {
            propertiesFile.inputStream().use(::load)
        }
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
    }

val androidExtension = extensions.getByName("android")
val signingConfigs = androidExtension.namedContainer("getSigningConfigs")
val releaseSigningConfig = signingConfigs.findByName("release") ?: signingConfigs.create("release")

if (keystoreProperties.isNotEmpty()) {
    releaseSigningConfig.callOneArg("setStoreFile", rootProject.file(keystoreProperties.requiredString("storeFile")))
    releaseSigningConfig.callOneArg("setStorePassword", keystoreProperties.requiredString("storePassword"))
    releaseSigningConfig.callOneArg("setKeyAlias", keystoreProperties.requiredString("keyAlias"))
    releaseSigningConfig.callOneArg("setKeyPassword", keystoreProperties.requiredString("keyPassword"))

    val releaseBuildType = androidExtension.namedContainer("getBuildTypes").getByName("release")
    releaseBuildType.callOneArg("setSigningConfig", releaseSigningConfig)
}
