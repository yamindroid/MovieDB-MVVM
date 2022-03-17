package com.ymo.data.model.error.mapper

import retrofit2.HttpException

interface ErrorMapperHelper {
    fun getErrorString(errorId: Int): String
    val errorsMap: Map<Int, String>
    fun getHttpExceptionError(httpException: HttpException):String
}
