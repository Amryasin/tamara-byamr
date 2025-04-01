package co.tamara.sdk.error

class BadRequestException(message: String?) :
    BaseException(400, message)