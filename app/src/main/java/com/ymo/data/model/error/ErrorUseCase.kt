package com.ymo.data.model.error

import retrofit2.HttpException

interface ErrorUseCase {
    fun getError(errorCode: Int): Error
    fun getHttpError(httpException: HttpException):Error
}
