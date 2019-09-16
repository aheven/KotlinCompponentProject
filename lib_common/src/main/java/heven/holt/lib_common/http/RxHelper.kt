package heven.holt.lib_common.http

import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import heven.holt.lib_common.model.vo.BaseModel
import heven.holt.lib_common.model.vo.SingleBaseModel
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object RxHelper {


    private val gson = Gson()

    fun <T> handleResult(): ObservableTransformer<BaseModel<T>, T> =
        ObservableTransformer { upstream ->
            val flatMap = upstream.flatMap<T> { result ->
                if (result.isSuccess) {
                    if (result.data == null) {
                        Observable.error(ApiResultDataNullException())
                    } else {
                        LogUtils.i(gson.toJson(result))
                        createData(result)
                    }
                } else {
                    Observable.error(ApiException(result.code, result.msg))
                }
            }
            flatMap.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

    private fun <T> createData(data: BaseModel<T>): ObservableSource<out T>? {
        return Observable.create { e ->
            try {
                data.data?.let { e.onNext(it) }
                e.onComplete()
            } catch (ex: Exception) {
                e.onError(ex)
            }
        }
    }

    private fun <T> createSingleData(data: T): ObservableSource<out T>? {
        return Observable.create { e ->
            try {
                e.onNext(data)
                e.onComplete()
            } catch (ex: Exception) {
                e.onError(ex)
            }
        }
    }

    fun <T> handleSingleResult(): ObservableTransformer<T, T> = ObservableTransformer { upstream ->
        val flatMap = upstream.flatMap<T> { result ->
            result as SingleBaseModel
            if (result.isSuccess) {
//                YLogUtil.logI(gson.toJson(result))
                createSingleData(result)
            } else {
                Observable.error<T>(ApiException(result.code, result.msg))
            }
        }
        flatMap.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}