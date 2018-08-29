# simple-server-skeleton
This is a skeleton web server based on Jetty. Includes: Jetty + Spring + Hibernate + H2. Build tool: Gradle.

This project is easy way to create REST server. Based on https://github.com/angryziber/simple-java. The main difference is that you can easily create standalone executable jar and run it from command line.

## It includes:
- Gradle;
- Jetty;
- Spring 3;
- H2 + Hibernate 3 + Liquibase;
- JUnit + Mockito;

## Packaging
```
$ gradlew shadowJar
```

## Launching
```
$ java -jar path_to_yout_jar.jar
```
