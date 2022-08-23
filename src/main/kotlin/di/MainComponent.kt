package di

class MainComponent {
    private val dataModule: DataModule = DataModule()
    private val repositoryModule: RepositoryModule = RepositoryModule(
        dataModule = dataModule,
    )
    val domainModule: DomainModule = DomainModule(
        configRepository = repositoryModule.configRepositoryFactory.create(),
    )
}
