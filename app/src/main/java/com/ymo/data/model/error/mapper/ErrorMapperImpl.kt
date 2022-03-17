package com.ymo.data.model.error.mapper

import android.content.Context
import com.ymo.R
import com.ymo.data.model.error.*
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class ErrorMapperImpl @Inject constructor(@ApplicationContext val context: Context) :
    ErrorMapperHelper {

    override fun getErrorString(errorId: Int): String {
        return context.getString(errorId)
    }

    override val errorsMap: Map<Int, String>
        get() = mapOf(
            Pair(NO_INTERNET_CONNECTION, getErrorString(R.string.no_internet)),
            Pair(NETWORK_ERROR, getErrorString(R.string.network_error)),
            Pair(PASS_WORD_ERROR, getErrorString(R.string.invalid_password)),
            Pair(USER_NAME_ERROR, getErrorString(R.string.invalid_username)),
            Pair(CHECK_YOUR_FIELDS, getErrorString(R.string.invalid_username_and_password)),
            Pair(SEARCH_ERROR, getErrorString(R.string.search_error)),
            Pair(BAD_REQUEST_ERROR, getErrorString(R.string.bad_request)),
            Pair(UNAUTHORIZED_USER_ERROR, getErrorString(R.string.unauthorized_user)),
            Pair(INTERNAL_SERVER_ERROR, getErrorString(R.string.internal_server_error)),
            Pair(CONNECTING_SERVER_ERROR, getErrorString(R.string.connecting_server_error))
        ).withDefault { getErrorString(R.string.network_error) }

    override fun getHttpExceptionError(httpException: HttpException): String {
        var errorMessage = ""
        try {
            val jsonObject = httpException.response()?.errorBody()?.string()
            errorMessage = JSONObject(jsonObject).getString("message")
        } catch (e: HttpException) {
            errorsMap.getValue(e.code())
        }
        return errorMessage
    }

}
