package com.ymo.data.model.error

class Error(val code: Int = 0, val description: String) {
    constructor(exception: Exception) : this(
        code = DEFAULT_ERROR, description = exception.message
            ?: ""
    )
}

const val NO_INTERNET_CONNECTION = -1
const val NETWORK_ERROR = -2
const val DEFAULT_ERROR = -3
const val PASS_WORD_ERROR = -101
const val USER_NAME_ERROR = -102
const val CHECK_YOUR_FIELDS = -103
const val SEARCH_ERROR = -104
const val BAD_REQUEST_ERROR = 400
const val UNAUTHORIZED_USER_ERROR = 401
const val INTERNAL_SERVER_ERROR = 500
const val CONNECTING_SERVER_ERROR = 422
