# SimpleCronParser

A simple cron parser that takes a config file as input and a simulated current time, and outputs when each job will be
run next.

An example config file can be seen
at [example_config.txt](https://github.com/MichaelM97/Simple-Cron-Parser/blob/main/example_config.txt)

## Running the script

Prerequisite - ensure that you have Java installed & available to run from the command line.

1. Either compile the project per the instructions below or download the latest `jar`
   from [Releases](https://github.com/MichaelM97/Simple-Cron-Parser/releases)
2. Open the command line at the same directory as the `jar` and run the following command

```bash
cat <config file path> | java -jar SimpleCronParser-1.1.jar <current time>
```

## Compile

Run the `jar` Gradle task, this will create a compiled `jar` in the `.build/libs` folder.

## Module structure

* Project root (`src`) - Contains the entry point (`main.kt`), the main DI component & end-to-end tests
* `domain` - UseCases responsible for interacting with repositories/performing domain-specific logic
* `repository` - Repositories responsible for interacting with data sources & mapping data
* `data` - Data sources, e.g. logic for interacting with stdin
* `:core` - Classes that are common across all modules (e.g. models)
