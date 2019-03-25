package heven.holt.lib_common.model.vo

import heven.holt.lib_common.utils.ObjectUtils

class HttpRequest private constructor() {

    val header: HttpHeader = HttpHeader()
    val body: MutableMap<String, Any> = mutableMapOf()


    companion object {
        fun obtainHttpRequest(vararg filed: Map.Entry<String, Any?>): HttpRequest {
            val httpRequest = HttpRequest()
            val body = httpRequest.body
            filed.forEach {
                if (it.value != null)
                    body[it.key] = it.value!!
            }
            return httpRequest
        }

        fun obtainHttpRequest(any: Any): HttpRequest {
            val httpRequest = HttpRequest()
            val body = httpRequest.body
            any::class.java.declaredFields.forEach {
                val value = ObjectUtils.getValueByFieldName(it.name, any)
                if (value != null && "null" != value && "" != value)
                    body[it.name] = value
            }
            return httpRequest
        }
    }
}