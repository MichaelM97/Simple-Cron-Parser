package di

import ConfigRepository
import ConfigRepositoryImpl
import FileDataSource
import mapper.ConfigTimeMapper

class ConfigRepositoryFactory internal constructor(
    private val fileDataSource: FileDataSource,
    private val configTimeMapper: ConfigTimeMapper,
) : Factory<ConfigRepository> {
    override fun create(): ConfigRepository {
        return ConfigRepositoryImpl(
            fileDataSource = fileDataSource,
            configTimeMapper = configTimeMapper,
        )
    }
}
