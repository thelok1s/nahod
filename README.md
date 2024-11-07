# NAHOD File System Analyzer

<img alt="appicon.png" height="256" src="src/main/resources/assets/appicon.png" width="256"/>

**NAHOD** is a file system analyzer built with JavaFX, designed to visually explore and analyze folders and files. Inspired by proprietary macOS Finder, NAHOD offers open-source codebase and a clean, doomer-core UI for viewing files and directories. The application leverages the [AppleFX](https://github.com/HanSolo/AppleFX) library to enhance look and feel on non-mac (cringe) platforms.

## Features

- **Directory and File Analysis:** NAHOD enables users to analyze specified directories and files, displaying information such as names, sizes, creation dates, and file metadata

- **Tree View Navigation:** Explore directories and files in a tree-like structure, similar to traditional file explorers, with expandable folders and hierarchical organization

- **File Metadata:** View comprehensive metadata for each file, including size, creation date, modification date, and more

- **Hidden Files Toggle:** Easily toggle the visibility of hidden files to suit your needs

- **Cross-Platform Compatibility:** NAHOD works seamlessly across different platforms including Windows, macOS, and Linux, offering a consistent and smooth user experience

## Screenshots

![screenshot.png](assets/screenshot.png)

## Prerequisites

- **Java 11 or higher**: The application is built with JavaFX, which requires at least Java 11.

## Installation

See [Releases page](https://github.com/thelok1s/nahod/releases)

## Building from Source

This project uses Gradle to build and package the application. It utilizes several plugins to manage dependencies, create a custom JAR with a modular approach, and package the application as an installer for different platforms.

### Requirements

- JDK 21 
- Gradle 8.5 or newer

### Build

To build the application, use the following Gradle command:

`./gradlew clean build`

### This will:
- Compile the source code
- Generate a JAR file
- Package the application with necessary dependencies

Running the Application

To run the application, use:

`./gradlew run`

This command will run the main class defined in the application plugin configuration (io.lok1s.nahod.MainLauncher).

### Creating a Modular JAR

This project uses the jlink and [badass-jlink](https://github.com/beryx/badass-jlink-plugin) plugin to create modular JAR. To generate a JAR file, run:

`./gradlew jlink`

### Creating a Shadow (Fat) JAR

This project also supports creating a fat (shadow) JAR using the shadowJar task. To generate a fat JAR file, run:

`./gradlew shadowJar`

This will bundle the application and its dependencies into a single JAR file located in the build/libs directory. 

### Creating an Installer

jpackage plugin could be used to create an installer for different platforms.
_Current jpackage configuration was not used on production, so it might need some adjustments. Check build.gradle for further information._
To create an installer, run:

`./gradlew jpackage`

