plugins {
    id("java")
}

group = "edu.sharif"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.neo4j.driver:neo4j-java-driver:4.4.2")
    implementation("com.opencsv:opencsv:5.5.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}