package di

import BuildConfigUseCase
import ConfigRepository

class LoadConfigUseCaseFactory internal constructor(
    private val configRepository: ConfigRepository,
) : Factory<BuildConfigUseCase> {
    override fun create(): BuildConfigUseCase {
        return BuildConfigUseCase(
            configRepository = configRepository,
        )
    }
}
