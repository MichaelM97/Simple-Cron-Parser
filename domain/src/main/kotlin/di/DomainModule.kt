package di

import ConfigRepository

/**
 * TODO
 */
class DomainModule(
    configRepository: ConfigRepository,
) {
    val parseInputArgsUseCaseFactory: ParseInputArgsUseCaseFactory = ParseInputArgsUseCaseFactory()
    val loadConfigUseCaseFactory: LoadConfigUseCaseFactory = LoadConfigUseCaseFactory(
        configRepository = configRepository,
    )
    val buildConfigOutputUseCaseFactory: BuildConfigOutputUseCaseFactory = BuildConfigOutputUseCaseFactory()
}
