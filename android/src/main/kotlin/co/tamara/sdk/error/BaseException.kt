package co.tamara.sdk.error

open class BaseException(var errorCode: Int, message: String?) : Throwable(message) {

    companion object{
        fun newInstance(code: Int, message: String?): BaseException{
            return when(code){
                400 -> BadRequestException(message)
                401 -> NotAuthorizedException(message)
                404 -> NotFoundException(message)
                else -> UnknownException(message)
            }
        }
    }
}