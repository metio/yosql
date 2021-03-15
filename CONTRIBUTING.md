# Contributor Guide

Thanks a lot for contributing to `YoSQL`!

## Local Setup

In order to build `YoSQL`, you will need Java 15+ and Maven 3.6.3+. Make sure that both are installed on your system and available on your `PATH`.

```shell
# check java
$ java -version
openjdk version "15.0.2" 2021-01-19
OpenJDK Runtime Environment (build 15.0.2+7-27)
OpenJDK 64-Bit Server VM (build 15.0.2+7-27, mixed mode, sharing)

# check javac
$ javac -version
javac 15.0.2

# check maven version
$ mvn --version
Apache Maven 3.6.3 (cecedd343002696d0abb50b32b541b8a6ba2883f)
Maven home: /usr/share/maven
Java version: 15.0.2, vendor: Oracle Corporation, runtime: /usr/java/openjdk-15
Default locale: en, platform encoding: UTF-8
OS name: "linux", version: "5.10.15-200.fc33.x86_64", arch: "amd64", family: "unix"
```

## Building the project

In order to build the project use the typical Maven goals, e.g.:

```shell
# build all modules and run all tests
$ mvn verify
```

**Note**: Each pull request will be build by GitHub and call `mvn verify`. In case you want to reproduce errors locally, run that first and fix any errors that show up.

## Creating forks or feature branches

In case you want to contribute code, either fork the repository if you are not yet an active contributor or create a git branch for every feature or bugfix you are working on. You can open up a pull request as early as you like, we recommend doing it right after creating your fork and adding a "WIP" prefix to your pull request. You can directly ping contributors with specific questions about existing code and how to implement your idea in the best way possible.

Since `YoSQL` will not have a plugin system by design, we are trying to make contributor changes to the project as easy as possible. In case you encounter any errors, e.g. while building the project, don't hesitate to open a ticket in order to get that fixed first.
