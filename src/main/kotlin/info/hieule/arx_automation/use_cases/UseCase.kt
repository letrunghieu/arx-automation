package info.hieule.arx_automation.use_cases

interface UseCase<TRequest, TResponse> {
    fun execute(request: TRequest): TResponse
}
