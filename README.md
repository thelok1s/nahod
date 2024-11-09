# [EN] NAHOD File System Analyzer

<img alt="appicon.png" height="256" src="src/main/resources/assets/appicon.png" width="256"/>

[Перейти к русской версии](#ru-файловый-анализатор-менеджер-_наход_)

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

### Creating a Fat (Shadow) JAR

This project also supports creating a fat (shadow) JAR using the shadowJar task. To generate a fat JAR file, run:

`./gradlew shadowJar`

This will bundle the application and its dependencies into a single JAR file located in the build/libs directory. 

### Creating an Installer

jpackage plugin could be used to create an installer for different platforms.
_Current jpackage configuration was not used on production, so it might need some adjustments. Check build.gradle for further information._
To create an installer, run:

`./gradlew jpackage`

# [RU] Файловый анализатор (менеджер) _"НАХОД"_

<img alt="appicon.png" height="256" src="src/main/resources/assets/appicon.png" width="256"/>

**НАХОД** это анализатор файловой системы, построеный с помощью JavaFx, созданный для визуального обозревания и анализа файлов и папок. Вдохновленный проприетарным Finder с macOS, НАХОД предлагает открытый код и приятный глазу думерский UI для просмотра папок и файлов на устройстве. Приложение использует библиотеку [AppleFX](https://github.com/HanSolo/AppleFX), чтобы улучшить внешний вид на не маках (кринж).

## Особенности

- **Анализ папок и файлов:** НАХОД позволяет пользователям анализировать папки и файлы, отображая важную информацию, такую как имя, размер, дата создания и прочие метаданные

- **Древовидная навигация:** Исследуйте папки и файлы в древовидной структуре, аналогичной традиционным файловым менеджерам, с раскрываемыми папками и иерархической организацией

- **Метаданные файлов:** Просматривайте подробные метаданные для каждого файла, включая размер, дату создания, дату изменения и другие

- **Скрытие/показ скрытых файлов:** Возможность переключить видимость скрытых файлов 

- **Кроссплатформенность:** НАХОД работает на всех платформах с Java, включая Windows, macOS и Linux

## Скриншоты

![screenshot.png](assets/screenshot.png)

## Требования

- **Java 11 или выше**: Приложение создано с использованием библиотеки JavaFX, которая требует как минимум Java 11.

## Установка

Перейдите на [Релизы](https://github.com/thelok1s/nahod/releases)

## Сборка из исходного кода

Этот проект использует Gradle для сборки и упаковки приложения. Он использует несколько плагинов для управления зависимостями, создания JAR с модульным подходом и упаковки приложения в установщик для разных платформ.

### Требования

- JDK 21
- Gradle 8.5 или новее

### Сборка

Для сборки приложения используйте следующую команду Gradle:

`./gradlew clean build`

### Это выполнит:
- Компиляцию исходного кода
- Создание JAR файла
- Упаковку приложения с необходимыми зависимостями

Запуск приложения

Для запуска приложения используйте:

`./gradlew run`

Эта команда запустит главный класс, определенный в конфигурации плагина приложения (io.lok1s.nahod.MainLauncher).

### Создание модульного JAR

Проект использует jlink и плагин [badass-jlink](https://github.com/beryx/badass-jlink-plugin) для создания модульного JAR. Чтобы собрать JAR файл, выполните:

`./gradlew jlink`

### Создание Fat (Shadow) JAR

Этот проект также поддерживает создание Fat (Shadow) JAR с помощью shadowJar. Чтобы создать Fat JAR файл, выполните:

`./gradlew shadowJar`

Это объединит приложение и его зависимости в один JAR файл, расположенный в директории build/libs.

### Создание установщика

Плагин jpackage может быть использован для создания установщика для разных платформ.
_jpackage не использовался для релизной версии, так что возможно потребуется донастрйока конфигурации. Проверьте build.gradle для дополнительной информации._
Для создания установщика, выполните:

`./gradlew jpackage`

