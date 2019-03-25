package heven.holt.lib_common.http


class ApiException(code: Int, message: String) : Exception(message, Throwable(code.toString()))