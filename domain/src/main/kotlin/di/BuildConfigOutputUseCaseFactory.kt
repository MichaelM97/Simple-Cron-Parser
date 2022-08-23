package di

import BuildConfigOutputUseCase

class BuildConfigOutputUseCaseFactory internal constructor() : Factory<BuildConfigOutputUseCase> {
    override fun create(): BuildConfigOutputUseCase {
        return BuildConfigOutputUseCase()
    }
}
