import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    kotlin("jvm") version "1.3.61"
    kotlin("kapt") version "1.3.61"
    `maven-publish`
}

group = "com.simgle"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    maven("http://maven.aliyun.com/nexus/content/groups/public/")
    maven("http://maven.aliyun.com/nexus/content/repositories/jcenter")
    mavenLocal()
}

dependencies {
    compileOnly("org.springframework.boot:spring-boot-starter:2.2.2.RELEASE")
    compileOnly("com.simgle:simgle-core:+")
    implementation("org.springframework:spring-orm:5.2.2.RELEASE")
    implementation("org.hibernate:hibernate-core:5.4.1.Final")
    implementation("com.zaxxer:HikariCP:3.4.1")
    implementation("mysql:mysql-connector-java:8.0.15")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.2.2.RELEASE") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    kapt("org.springframework.boot:spring-boot-configuration-processor:2.2.2.RELEASE")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.create("sourceJar", Jar::class.java) {
    this.archiveClassifier.set("sources")
    this.from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(tasks["sourceJar"]) {
                this.classifier = "sources"
            }
        }
    }
    repositories {

    }
}
