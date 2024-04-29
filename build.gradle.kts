plugins {
    `java-library`
    `maven-publish`
    id("xyz.jpenilla.run-paper") version "2.2.4" // Adds runServer and runMojangMappedServer tasks for testing
}

group = "net.zyuiop"
version = "1.0.0-SNAPSHOT"

java {
    // Configure the java toolchain. This allows gradle to auto-provision JDK 21 on systems that only have JDK 11 installed for example.
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks.jar {
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree) // OR .map { zipTree(it) }
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


repositories {
    mavenLocal()
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.papermc.io/repository/maven-public//")
}

dependencies {
    compileOnly("net.zyuiop:bukkitbridge:1.0.0-SNAPSHOT")
    compileOnly("net.zyuiop:bridgetools:1.0.0-SNAPSHOT")
    compileOnly("net.zyuiop:uhcrun:1.0.0-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    implementation("net.zyuiop:BridgeConnector:1.0.0-SNAPSHOT")
    implementation("com.electronwill.night-config:toml:3.6.0")
    compileOnly("net.zyuiop:CrossPermissionsAPI:1.0.0-SNAPSHOT")
}

tasks {
    compileJava {
        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release = 21
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
}

