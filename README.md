![Lucent](https://github.com/project-transparent/lucent/blob/main/logo.png)

**Lucent** is a framework for creating simplistic AST-transforming annotation processors.

## Overview

In the world of Javac AST hacking, there tends to be a large amount of boilerplate exclusive to creating the processors.<br>
Processors are also just not meant to perform such a thing, and as such, aren't decided to do so cleanly.

Lucent aims to fix that by offering well-documented, easy-to-use classes that offer tons of potential while being easy to maintain and create. It's also only *8 KB!*

### Requirements
Lucent is designed to use JDK 8 but supports later versions, such versions may be unstable with this project at the moment.

## Primary Classes

### Processor

The [`LucentProcessor`](https://github.com/project-transparent/lucent/blob/main/src/main/java/org/transparent/lucent/processor/LucentProcessor.java) is a clean wrapper for the base `AbstractProcessor` class from JSR-269.

It:
- Automatically initializes the classes required to use the Javac Tree API.
- Handles filtering of element kinds.
- Allows you to use annotation classes instead of their names if wanted.
- Creates a new instance of [`LucentTranslator`](https://github.com/project-transparent/lucent/blob/main/src/main/java/org/transparent/lucent/transform/LucentTranslator.java) for transforming.
- Allows you to hook into the different parts of a `LucentProcessor#process(RoundEnvironment)` call.

And it's all perfectly extendable by the developer.

### Translator

The `LucentTranslator` is yet another wrapper, but this time for `TreeTranslator` from the Javac Tree API. It isn't as special as the aforementioned processor, but it is just as easy to setup and use.

### Validator

The [`LucentValidator`](https://github.com/project-transparent/lucent/blob/main/src/main/java/org/transparent/lucent/transform/LucentValidator.java) is a special class that is instantiated by a translator and facilitates the filtering of different AST tree nodes. This creates a level of separation between validation and transformation in the event that an annotation must use the same translator but with slightly different rules. Validators are optional but highly recommended.

A new validator can be created by extending [`LucentBaseValidator`](https://github.com/project-transparent/lucent/blob/main/src/main/java/org/transparent/lucent/transform/LucentBaseValidator.java), which, by default, returns true for all methods. Configure to your project's needs.

## Installation (Gradle - Local)

1. Clone the repo (https://github.com/project-transparent/lucent).
2. Run `gradlew publishToMavenLocal` in the root directory of the repo.
3. Add `mavenLocal()` to your repositories.
4. Add `implementation 'org.transparent:lucent:<version>'` to your dependencies.
