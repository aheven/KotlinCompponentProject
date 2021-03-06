package heven.holt.lib_common.http

import com.blankj.utilcode.util.LogUtils
import heven.holt.lib_common.BuildConfig
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Buffer
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

object HttpClientManager {
    private const val DEFAULT_TIMEOUT = 20L // 请求超时时间
    val mOkHttpClient: OkHttpClient

    init {
        //手动创建一个OkHttpClient并设置超时时间
        val httpClientBuilder = OkHttpClient.Builder()
        mOkHttpClient = httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request()
                val newBuilder = request.url().newBuilder()


                val headers = request.headers("urlType")

//                    val requestPramsMap = ApiPublicParams.getRequestPubMap()
//                    for (entry in requestPramsMap.entries) {
//                        newBuilder.addQueryParameter(entry.key, entry.value)
//                    }

                val requestBuilder = request.newBuilder()
                //添加头部
                requestBuilder.addHeader("User-Agent", NetWorkImpl.getUserAgent())
//                    val apiToken = ApiPublicParams.getApiToken()
                var newRequest: Request
//                    if (apiToken == null || OKHttpHelper.filterAuthToken(request.url())) {
                newRequest = requestBuilder.url(newBuilder.build()).build()
//                    } else {
//                        newRequest = requestBuilder.addHeader("Authorization", apiToken).url(newBuilder.build()).build()
//                    }
                if (headers != null && !headers.isEmpty()) {
                    //根据head获取baseUrl
                    requestBuilder.removeHeader("urlType")
                    val urlName = headers[0]
                    var baseUrl: HttpUrl? = null
                    if ("requestCode" == urlName) {
                        //可通过标记添加多个类型的baseUrl
                        baseUrl = HttpUrl.parse("")
                    }
                    if (baseUrl != null) {
                        val httpUrl = newRequest.url().newBuilder()
                            .scheme(baseUrl.scheme())
                            .host(baseUrl.host())
                            .port(baseUrl.port())
                            .build()
                        newRequest = requestBuilder.url(httpUrl).build()
                    }
                }

                if (BuildConfig.DEBUG) {
                    val buffer = Buffer()
                    newRequest.body()?.writeTo(buffer)

                    val result = String(buffer.clone().readByteArray())
                    LogUtils.i("HttpClientManager=(${request.url()}) ($result)")
                }
                val time = System.currentTimeMillis()
                val response = chain.proceed(newRequest)
                if (BuildConfig.DEBUG) {
                    val charset = response.body()?.contentType()?.charset()
                    val source = response.body()?.source()
                    source?.request(Long.MAX_VALUE)
//                    LogUtils.i(
//                        "response=请求时长=${System.currentTimeMillis() - time}=(${request.url()}) (${source?.buffer()?.clone()?.readString(
//                            charset
//                                ?: Charset.forName("utf-8")
//                        )})"
//                    )
                    LogUtils.json("response=请求时长=${System.currentTimeMillis() - time}=(${request.url()})",source?.buffer()?.clone()?.readString(
                        charset
                            ?: Charset.forName("utf-8")
                    ))
                }
                response
            }.build()
    }
}