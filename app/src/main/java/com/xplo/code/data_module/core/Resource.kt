package com.xplo.code.data_module.core

sealed class Resource<T>(val data: T?, val callInfo: com.xplo.code.data_module.core.CallInfo?) {
    class Success<T>(data: T, callInfo: com.xplo.code.data_module.core.CallInfo?) : com.xplo.code.data_module.core.Resource<T>(data, callInfo)
    class Failure<T>(callInfo: com.xplo.code.data_module.core.CallInfo?) : com.xplo.code.data_module.core.Resource<T>(null, callInfo)
}