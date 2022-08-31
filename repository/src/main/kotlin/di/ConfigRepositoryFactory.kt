package di

import ConfigRepository
import ConfigRepositoryImpl
import StandardInputDataSource
import mapper.ConfigTimeMapper

class ConfigRepositoryFactory internal constructor(
    private val standardInputDataSource: StandardInputDataSource,
    private val configTimeMapper: ConfigTimeMapper,
) : Factory<ConfigRepository> {
    override fun create(): ConfigRepository {
        return ConfigRepositoryImpl(
            standardInputDataSource = standardInputDataSource,
            configTimeMapper = configTimeMapper,
        )
    }
}
