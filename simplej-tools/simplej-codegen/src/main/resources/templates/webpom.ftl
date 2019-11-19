<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>${groupId}</groupId>
    <artifactId>${artifactId}</artifactId>
    <version>${version}</version>
    <name>${artifactId}</name>
    <packaging>jar</packaging>
    <#--<packaging>war</packaging>-->
    <description>${description}</description>

    <parent>
        <groupId>${parentGroupId}</groupId>
        <artifactId>${parentArtifactId}</artifactId>
        <version>${parentVersion}</version>
    </parent>

    <repositories>
        <repository>
            <id>mavenCentral</id>
            <name>public maven Central Repo</name>
            <url>https://repo.maven.apache.org/maven2</url>
        </repository>
    </repositories>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>com.haven.simplej</groupId>
            <artifactId>simplej-web</artifactId>
        </dependency>
        <#if useRpcFlag == 1>
            <dependency>
                <groupId>com.haven.simplej</groupId>
                <artifactId>rpc-server</artifactId>
            </dependency>
            <dependency>
                <groupId>com.haven.simplej</groupId>
                <artifactId>rpc-client</artifactId>
            </dependency>
            <dependency>
                <groupId>${groupId}</groupId>
                <artifactId>${serviceName}-api</artifactId>
            </dependency>
        </#if>

        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>jstl</artifactId>
        </dependency>
        <#if useRpcFlag == 0>
        <dependency>
            <groupId>${groupId}</groupId>
            <artifactId>${serviceName}</artifactId>
        </dependency>
        </#if>
    </dependencies>

    <build>
        <defaultGoal>clean compiler</defaultGoal>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.7.0</version>
                <configuration>
                    <encoding>UTF-8</encoding>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <fork>true</fork>
                    <!-- spring-boot:run 中文乱码解决 -->
                    <jvmArguments>-Dfile.encoding=UTF-8</jvmArguments>
                </configuration>
            </plugin>

            <#--<plugin>-->
                <#--<groupId>org.apache.maven.plugins</groupId>-->
                <#--<artifactId>maven-war-plugin</artifactId>-->
                <#--<configuration>-->
                    <#--<webResources>-->
                        <#--<resource>-->
                            <#--<directory>src/main/webapp</directory>-->
                        <#--</resource>-->
                    <#--</webResources>-->
                <#--</configuration>-->
            <#--</plugin>-->
        </plugins>
    </build>

</project>