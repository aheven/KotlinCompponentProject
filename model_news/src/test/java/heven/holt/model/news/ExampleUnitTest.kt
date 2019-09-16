package heven.holt.model.news

import heven.holt.lib_common.http.ApiResultDataNullException
import heven.holt.model.news.mvp.model.vo.RoomQuickVo
import heven.holt.model.news.mvp.model.vo.RoomTitleVo
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    /**
     * ====================================RxJava2 创建操作符 start=========================================
     */

    @Test
    fun rxJava1() {
        Observable.just(
            1,
            2,
            RoomQuickVo("1001", null, null, null, null, null, null, null, null, null),
            5,
            4.0,
            "A"
        )
            .subscribe {
                print(it)
            }
    }

    @Test
    fun rxJava2() {
        //fromJust 底层调用的就是fromArray
        val array = arrayOf(1, 2, 3, 4, "A", 4.0)
        Observable.fromArray(array)
            .subscribe {
                print(it)
            }
    }

    @Test
    fun rxJava3() {
        //repeat操作符，创建一个重复发射指定数据或数据序列的Observable
        Observable.fromArray(1, 2, 3, 4, "A", 4.0)
            .repeat(2)
            .subscribe {
                print(it)
            }
    }

    @Test
    fun rxJava4() {
        //例子为1s后重复发射数据
        Observable.fromArray(1, 2, 3, 4, "A", 4.0)
            .repeatWhen { Observable.timer(1, TimeUnit.SECONDS) }
            .subscribe {
                print(it)
            }
    }

    @Test
    fun rxJava5() {
        //最基本的订阅创建
        Observable.create<Any> {
            it.onNext(1)
            it.onNext(2)
            it.onNext("A")
            it.onNext(3.0)
            it.onComplete()
        }.subscribe {
            print(it)
        }
    }

    @Test
    fun rxJava6() {
        //defer,只有当订阅者订阅才创建Observable
        Observable.defer {
            Observable.just(1, 2, 3, 4, 5)
        }.subscribe { print(it) }
    }

    @Test
    fun rxJava7() {
        //range,创建一个发射指定范围的整数序列
        Observable.range(1, 10)
            .subscribe { print(it) }
    }

    @Test
    fun rxJava8() {
        //interval,创建一个发射按指定时间间隔的整数序列
        Observable.interval(1, TimeUnit.SECONDS)
            .subscribe { print(it) }
    }

    @Test
    fun rxJava9() {
        //timer,创建一个在给定的延时之后发射单个数据
        Observable.timer(1, TimeUnit.SECONDS)
            .subscribe { print(it) }
    }

    @Test
    fun rxJava10() {
        //empty,创建一个什么都不做，直接通知完成（OnComplete）的Observable
        Observable.empty<Any>()
            .subscribe(object : Observer<Any> {
                override fun onComplete() {
                    print("this function will call.")
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Any) {
                }

                override fun onError(e: Throwable) {
                }
            })
    }

    @Test
    fun rxJava11() {
        //error,创建一个什么都不做，直接通知完成（onError）的Observable
        Observable.error<String>(ApiResultDataNullException())
            .subscribe(object : Observer<String> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: String) {
                }

                override fun onError(e: Throwable) {
                    print("this function will call.")
                }
            })
    }

    @Test
    fun rxJava12() {
        //never,创建一个入不发射任何数据的Observable
        Observable.never<String>()
            .subscribe(object : Observer<String> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                    print("only this function will call.")
                }

                override fun onNext(t: String) {
                }

                override fun onError(e: Throwable) {
                }
            })
    }

    /**
     * ====================================RxJava2 创建操作符 end=========================================
     */

    /**
     * ====================================RxJava2 变换操作符 start=========================================
     */
    @Test
    fun rxJava13() {
        //buffer,缓存，定期从Observable手机数据到一个集合，然后把这些数据打包发射，而不是一次发射一个
        Observable.just("one", "two", "three", "four", "five")
            .buffer(3, 1)
            .subscribe { print(it) }
    }

    @Test
    fun rxJava14() {
        //map,对序列的每一项都应用一个函数来变换Observable发射的数据类型
        Observable.just("one", "two", "three", "four", "five")
            .map { RoomQuickVo(it) }
            .subscribe { print(it) }
    }

    @Test
    fun rxJava15() {
        //map,对序列的每一项都应用一个函数来变换Observable发射的数据类型
        Observable.just("one", "two", "three", "four", "five")
            .flatMap { Observable.just(RoomTitleVo(it)) }
            .subscribe { print(it) }
    }
    /**
     * ====================================RxJava2 变换操作符 end=========================================
     */
}