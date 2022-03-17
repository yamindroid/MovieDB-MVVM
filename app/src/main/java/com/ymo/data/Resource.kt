package com.ymo.data

data class Resource<out T>(val status: Status, val data: T?, val errorMessage: String?) {

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(errorMessage: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, errorMessage)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

    }

}