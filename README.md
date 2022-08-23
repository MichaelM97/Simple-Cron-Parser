# SimpleCronParser

A simple cron parser that takes a config file as input, and a simulated current time, and outputs when each job will be
performed next.

An example config file can be seen
at [.src/test/kotlin/resources/test_config.txt](https://github.com/MichaelM97/SimpleCronParser/blob/main/src/test/resources/test_config.txt)

## Running the script

Prerequisite - ensure that you have Java installed & available to run from the command line.

1. Either compile the project per the instructions below or download the `jar`
   from [Releases](https://github.com/MichaelM97/SimpleCronParser/releases)
2. Open the command line at the same directory as the `jar` and run the following command

```bash
java -jar SimpleCronParser-1.0.jar <config file path> <current time>
```

## Compile

Run the `jar` Gradle task, this will create a compiled `jar` in the `.build/libs` folder.

## Module structure

* Project root (`src`) - Contains the entry point (`main.kt`), the main DI component & any integration tests
* `:core` - Classes that are common across all modules (e.g. models)
* `data` - Data sources, i.e. logic for loading system files
* `repository` - Repositories responsible for interacting with data sources & mapping data
* `domain` - UseCases responsible for interacting with repositories/performing domain-specific logic
