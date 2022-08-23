package di

import ParseInputArgsUseCase

class ParseInputArgsUseCaseFactory internal constructor() : Factory<ParseInputArgsUseCase> {
    override fun create(): ParseInputArgsUseCase {
        return ParseInputArgsUseCase()
    }
}
