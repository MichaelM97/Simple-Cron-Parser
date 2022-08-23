package di

import ConfigRepository
import LoadConfigUseCase

class LoadConfigUseCaseFactory internal constructor(
    private val configRepository: ConfigRepository,
) : Factory<LoadConfigUseCase> {
    override fun create(): LoadConfigUseCase {
        return LoadConfigUseCase(
            configRepository = configRepository,
        )
    }
}
