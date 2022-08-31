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
    // Parse the input args
    val inputArgs = parseInputArgs(args, mainComponent) ?: return
    // Load the config
    val config = loadConfig(mainComponent) ?: return
    // Build the output & print
    buildAndPrintOutput(config, inputArgs, mainComponent)
}

private fun parseInputArgs(args: Array<String>, mainComponent: MainComponent): InputArgs? {
    val parseInputArgsUseCase = mainComponent.domainModule.parseInputArgsUseCaseFactory.create()
    parseInputArgsUseCase(args)
        .onSuccess { return it }
        .onFailure { println(it.message) }
    return null
}

private fun loadConfig(mainComponent: MainComponent): Config? {
    val loadConfigUseCase = mainComponent.domainModule.loadConfigUseCaseFactory.create()
    loadConfigUseCase()
        .onSuccess { return it }
        .onFailure { println(it.message) }
    return null
}

private fun buildAndPrintOutput(config: Config, inputArgs: InputArgs, mainComponent: MainComponent) {
    val buildConfigOutputUseCase = mainComponent.domainModule.buildConfigOutputUseCaseFactory.create()
    buildConfigOutputUseCase(config, inputArgs.currentTime)
        .forEach { println(it) }
}
