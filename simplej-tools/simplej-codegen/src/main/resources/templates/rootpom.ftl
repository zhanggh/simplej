<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>${groupId}</groupId>
    <artifactId>${parentArtifactId}</artifactId>
    <version>${version}</version>
    <name>${projectName}</name>
    <packaging>pom</packaging>
    <description>${description}</description>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.0.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
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
        <dozer.version>5.5.1</dozer.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

    </properties>

    <modules>
    <#if projectType != "simpleApp">
        <module>${webName}</module>
    </#if>
        <module>${serviceName}</module>
        <#if useRpcFlag == 1>
        <module>${serviceName}-api</module>
        </#if>
    </modules>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.haven.simplej</groupId>
                <artifactId>simplej-base</artifactId>
                <version>1.4</version>
            </dependency>
            <dependency>
                <groupId>com.haven.simplej</groupId>
                <artifactId>simplej-web</artifactId>
                <version>1.4</version>
            </dependency>
            <dependency>
                <groupId>com.haven.simplej</groupId>
                <artifactId>simplej-db</artifactId>
                <version>1.4</version>
            </dependency>
            <dependency>
                <groupId>com.haven.simplej</groupId>
                <artifactId>rpc-server</artifactId>
                <version>1.4</version>
            </dependency>
            <dependency>
                <groupId>com.haven.simplej</groupId>
                <artifactId>rpc-client</artifactId>
                <version>1.4</version>
            </dependency>
            <!-- optional for BeanMapper -->
            <dependency>
                <groupId>net.sf.dozer</groupId>
                <artifactId>dozer</artifactId>
                <version>@{dozer.version}</version>
            </dependency>
        <#if projectType != "simpleApp">
            <dependency>
                <groupId>${groupId}</groupId>
                <artifactId>${webName}</artifactId>
                <version>@{project.version}</version>
            </dependency>
        </#if>
            <dependency>
                <groupId>${groupId}</groupId>
                <artifactId>${serviceName}</artifactId>
                <version>@{project.version}</version>
            </dependency>
            <#if useRpcFlag == 1>
                <dependency>
                    <groupId>${groupId}</groupId>
                    <artifactId>${serviceName}-api</artifactId>
                    <version>@{project.version}</version>
                </dependency>
            </#if>
        </dependencies>
    </dependencyManagement>
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