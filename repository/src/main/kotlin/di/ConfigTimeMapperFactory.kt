package di

import mapper.ConfigTimeMapper

internal class ConfigTimeMapperFactory : Factory<ConfigTimeMapper> {
    override fun create(): ConfigTimeMapper {
        return ConfigTimeMapper()
    }
}
