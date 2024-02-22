package com.xplo.data.core

sealed class Resource<T>(val data: T?, val callInfo: CallInfo?) {
    class Success<T>(data: T, callInfo: CallInfo?) : Resource<T>(data, callInfo)
    class Failure<T>(callInfo: CallInfo?) : Resource<T>(null, callInfo)
}