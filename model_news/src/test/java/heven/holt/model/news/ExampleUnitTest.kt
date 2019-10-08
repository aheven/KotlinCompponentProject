package heven.holt.model.news

import heven.holt.lib_common.http.ApiResultDataNullException
import heven.holt.model.news.mvp.model.vo.RoomQuickVo
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
        //flatMap,将Observable发射的数据集合变换为Observables集合，然后将这些Observable发射的数据平坦化的放进一个单独的Observable
        //注意，相对于concatMap来说，flatMap发射出来的数据是无序的
        Observable.fromArray(1, 2, 3, 4, 5)
            .flatMap {
                var delay = 0
                if (it == 3) {
                    delay = 500
                }
                Observable.just(it * 10).delay(delay.toLong(), TimeUnit.SECONDS)
            }
            .subscribe { println(it) }
    }

    @Test
    fun rxJava16() {
        //concatMap,将Observable发射的数据集合变换为Observables集合，然后将这些Observable发射的数据平坦化的放进一个单独的Observable
        //注意，相对于flatMap来说，concatMap发射出来的数据是有序的
        Observable.fromArray(1, 2, 3, 4, 5)
            .concatMap {
                var delay = 0
                if (it == 3) {
                    delay = 500
                }
                Observable.just(it * 10).delay(delay.toLong(), TimeUnit.SECONDS)
            }
            .subscribe { println(it) }
    }

    @Test
    fun rxJava17() {
        //flatMapIterable,功能与flatMap类似，但是FlatMapIterable是把每一个元素转换成Iterable，然后合并成一个单独的Observable一个一个发射出来
        //注意，相对于flatMap来说，concatMap发射出来的数据是有序的
        Observable.fromArray(1, 2, 3, 4, 5)
            .flatMapIterable { listOf("a", it) }
            .subscribe { println(it) }
    }

    @Test
    fun rxJava18() {
        //switchMap,将Observable发射的数据集合变换为Observables集合，然后只发射这些Observables最近发射的数据
        //在同一线程中，switchMap与contactMap没有任何区别，都是先跑的先到
        //在不同的线程中，如果前一个任务，尚未执行结束，就会被后一个任务给取消
        Observable.fromArray(1, 2, 3, 4, 5)
            .switchMap { Observable.just(it) }
            .subscribe { println(it) }
    }

    @Test
    fun rxJava19() {
        //scan,将Observable发射的每一个数据应用一个函数，然后按顺序依次发送每一个值,比如累加操作
        Observable.fromArray(1, 2, 3, 4, 5)
            .scan { t1, t2 ->
                println("t1:$t1")
                println("t2:$t2")
                t1 + t2
            }
            .subscribe {
                println("result:$it")
            }
    }

    @Test
    fun rxJava20() {
        //groupBy,将Observable分拆为Observable集合，将原始Observable发射的数据按key分组，每一个Observable发射一组不同的数据
        Observable.fromArray(1, 2, 3, 4, 5)
            .groupBy {
                if (it % 2 == 0) "group A" else "Group B"
            }
            .subscribe { groupedObservable ->
                groupedObservable.subscribe {
                    println("${groupedObservable.key} :: $it")
                }
            }
    }

    @Test
    fun rxJava21() {
        //window,与buffer类似，区别在于window将结果集合封装成了Observable
        Observable.just("one", "two", "three", "four", "five")
            .window(1, 2)
            .subscribe { observable ->
                observable.subscribe {
                    print(it)
                }
            }
    }

    @Test
    fun rxJava22() {
        //cast,在发射之前强制将Observable发射的所有数据转换为指定类型
        Observable.just(1, 2, 3, 4, 5)
            .cast(String::class.java)
            .subscribe {
                println(it)
            }
    }
    /**
     * ====================================RxJava2 变换操作符 end=========================================
     */

    /**
     * ====================================RxJava2 过滤操作符 start=========================================
     */
    @Test
    fun rxJava23() {
        //filter,对数据进行过滤
        Observable.just(1, 2, 3, 4, 5)
            .filter { it % 2 == 0 }
            .subscribe {
                println(it)
            }
    }

    @Test
    fun rxJava24() {
        //takeLast,只发射最后的N项数据
        Observable.just(1, 2, 3, 4, 5)
            .takeLast(2)
            .subscribe {
                println(it)
            }
    }

    @Test
    fun rxJava25() {
        //last,只发射最后的一项数据，如果Observable为空就发射默认值
        Observable.just(1, 2, 3, 4, 5)
            .last(0)
            .subscribe { it1 ->
                println(it1)
            }
    }

    @Test
    fun rxJava26() {
        //lastElement,只发射最后的一项数据
        Observable.just(1, 2, 3, 4, 5)
            .lastElement()
            .subscribe {
                println(it)
            }
    }

    @Test
    fun rxJava27() {
        //skip,跳过开始的N项数据
        Observable.just(1, 2, 3, 4, 5)
            .skip(3)
            .subscribe {
                println(it)
            }
    }

    @Test
    fun rxJava28() {
        //skipLast,跳过最后的N项数据
        Observable.just(1, 2, 3, 4, 5)
            .skipLast(3)
            .subscribe {
                println(it)
            }
    }

    @Test
    fun rxJava29() {
        //take,只发射开始的N项数据
        Observable.just(1, 2, 3, 4, 5)
            .take(3)
            .subscribe {
                println(it)
            }
    }

    @Test
    fun rxJava30() {
        //firstElement,只发射第一项数据
        Observable.just(1, 2, 3, 4, 5)
            .firstElement()
            .subscribe {
                println(it)
            }
    }

    @Test
    fun rxJava31() {
        //first,只发射第一项数据,如果Observable为空就发射默认值
        Observable.just(1, 2, 3, 4, 5)
            .first(0)
            .subscribe { it1 ->
                println(it1)
            }
    }

    @Test
    fun rxJava32() {
        //elementAt,发射第N项数据
        Observable.just(1, 2, 3, 4, 5)
            .elementAt(2)
            .subscribe { it1 ->
                println(it1)
            }
    }

    @Test
    fun rxJava33() {
        //throttleFirst,定期发射Observable发射的第一项数据
        Observable.just(1, 2, 3, 4, 5)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe { it1 ->
                println(it1)
            }
    }

    @Test
    fun rxJava34() {
        //throttleLast | sample,定期发射Observable最近的数据
        Observable.just(1, 2, 3, 4, 5)
            .sample(1, TimeUnit.SECONDS)
            .subscribe { it1 ->
                println(it1)
            }
    }

    @Test
    fun rxJava35() {
        //throttleWithTimeout | debounce,只有在空闲了一段时间后才发射数据，通俗的说，就是如果一段时间没有操作，就执行一次操作
        //即如果在等待时间内，有新数据则抛弃旧数据，直到这段时间内没有新数据，则发射该数据
        Observable.just(1, 2, 3, 4, 5)
            .throttleWithTimeout(1, TimeUnit.SECONDS)
            .subscribe { it1 ->
                println(it1)
            }
    }

    @Test
    fun rxJava36() {
        //timeout,如果在一个指定的时间段后还没发射数据，就发射一个异常
        Observable.just(1, 2, 3, 4, 5)
            .timeout(1, TimeUnit.SECONDS)
            .subscribe { it1 ->
                println(it1)
            }
    }

    @Test
    fun rxJava37() {
        //distinct,过滤掉重复数据
        Observable.just(1, 2, 2, 1, 5)
            .distinct()
            .subscribe { it1 ->
                println(it1)
            }
    }

    @Test
    fun rxJava38() {
        //distinctUntilChanged,过滤掉连续重复的数据
        Observable.just(1, 2, 2, 1, 5)
            .distinctUntilChanged()
            .subscribe { it1 ->
                println(it1)
            }
    }

    @Test
    fun rxJava39() {
        //ofType,只发射指定类型的数据
        Observable.just(1, 2, "double", 1, 5)
            .ofType(String::class.java)
            .subscribe { it1 ->
                println(it1)
            }
    }

    @Test
    fun rxJava40() {
        //ignoreElements,丢弃所有的正常数据，只发射错误或完成通知
        Observable.create<Int> {
            it.onNext(1)
            it.onError(RuntimeException("custom"))
            it.onNext(2)
            it.onComplete()
        }.ignoreElements()
            .doOnError{
                println("do on error work.")
            }
            .subscribe({
                println("onComplete")
            }, {
                println("error work.")
            })
    }


    /**
     * ====================================RxJava2 过滤操作符 end=========================================
     */
}