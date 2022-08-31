package di

class RepositoryModule(
    dataModule: DataModule,
) {
    private val configTimeMapperFactory: ConfigTimeMapperFactory = ConfigTimeMapperFactory()
    val configRepositoryFactory: ConfigRepositoryFactory = ConfigRepositoryFactory(
        standardInputDataSource = dataModule.standardInputDataSourceFactory.create(),
        configTimeMapper = configTimeMapperFactory.create(),
    )
}
