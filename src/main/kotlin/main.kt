import di.MainComponent
import models.Config
import models.InputArgs

/**
 * Entry point for SimpleCronParser.
 *
 * @param args the arguments passed when running the script.
 */
fun main(args: Array<String>) {
    // Build the dependency graph
    val mainComponent = MainComponent()
    // Process the input args
    processInput(args, mainComponent)
}

private fun processInput(args: Array<String>, mainComponent: MainComponent) {
    val parseInputArgsUseCase = mainComponent.domainModule.parseInputArgsUseCaseFactory.create()
    parseInputArgsUseCase(args)
        .onSuccess { loadConfig(it, mainComponent) }
        .onFailure { println(it.message) }
}

private fun loadConfig(inputArgs: InputArgs, mainComponent: MainComponent) {
    val loadConfigUseCase = mainComponent.domainModule.loadConfigUseCaseFactory.create()
    loadConfigUseCase(inputArgs.configFilePath)
        .onSuccess { printOutput(it, inputArgs, mainComponent) }
        .onFailure { println(it.message) }
}

private fun printOutput(config: Config, inputArgs: InputArgs, mainComponent: MainComponent) {
    val buildConfigOutputUseCase = mainComponent.domainModule.buildConfigOutputUseCaseFactory.create()
    buildConfigOutputUseCase(config, inputArgs.currentTime)
        .forEach { println(it) }
}
