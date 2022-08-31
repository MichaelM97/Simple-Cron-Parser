package di

import ConfigRepository

class DomainModule(
    configRepository: ConfigRepository,
) {
    val parseInputArgsUseCaseFactory: ParseInputArgsUseCaseFactory = ParseInputArgsUseCaseFactory()
    val loadConfigUseCaseFactory: LoadConfigUseCaseFactory = LoadConfigUseCaseFactory(
        configRepository = configRepository,
    )
    val buildConfigOutputUseCaseFactory: BuildConfigOutputUseCaseFactory = BuildConfigOutputUseCaseFactory()
}
