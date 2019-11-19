<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>${groupId}</groupId>
    <artifactId>${artifactId}</artifactId>
    <version>${version}</version>
    <name>${artifactId}</name>
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
            <artifactId>simplej-base</artifactId>
        </dependency>

        <#if useRpcFlag == 1>
        <dependency>
            <groupId>${groupId}</groupId>
            <artifactId>${artifactId}-api</artifactId>
        </dependency>
        <dependency>
            <groupId>com.haven.simplej</groupId>
            <artifactId>rpc-server</artifactId>
        </dependency>
        <dependency>
            <groupId>com.haven.simplej</groupId>
            <artifactId>rpc-client</artifactId>
        </dependency>
        <dependency>
            <groupId>net.sf.dozer</groupId>
            <artifactId>dozer</artifactId>
        </dependency>
        </#if>
        <#if useDbFlag == 1>
        <dependency>
            <groupId>com.haven.simplej</groupId>
            <artifactId>simplej-db</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
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
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>build-helper-maven-plugin</artifactId>
                <version>1.10</version>
                <executions>
                    <execution>
                        <id>add-source</id>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>add-source</goal>
                        </goals>
                        <configuration>
                            <sources>
                                <!--其他源码目录-->
                                <source>@{basedir}/src/output/java</source>
                                <source>@{basedir}/src/output/resources</source>
                            </sources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</project>