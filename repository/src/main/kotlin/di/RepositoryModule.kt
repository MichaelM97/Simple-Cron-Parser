package di

class RepositoryModule(
    dataModule: DataModule,
) {
    private val configTimeMapperFactory: ConfigTimeMapperFactory = ConfigTimeMapperFactory()
    val configRepositoryFactory: ConfigRepositoryFactory = ConfigRepositoryFactory(
        fileDataSource = dataModule.fileDataSourceFactory.create(),
        configTimeMapper = configTimeMapperFactory.create(),
    )
}
