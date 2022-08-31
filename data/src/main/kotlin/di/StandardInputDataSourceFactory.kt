package di

import StandardInputDataSource
import StandardInputDataSourceImpl

class StandardInputDataSourceFactory internal constructor() : Factory<StandardInputDataSource> {
    override fun create(): StandardInputDataSource {
        return StandardInputDataSourceImpl()
    }
}
