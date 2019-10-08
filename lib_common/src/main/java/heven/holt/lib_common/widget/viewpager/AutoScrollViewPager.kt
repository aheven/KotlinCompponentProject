package heven.holt.lib_common.widget.viewpager

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import heven.holt.lib_common.R
import heven.holt.lib_common.utils.setImageUrl
import java.lang.ref.WeakReference


/**
 *Time:2019/9/30
 *Author:HevenHolt
 *Description:自动无限循环滚动的Viewpager
 */
@SuppressLint("CustomViewStyleable")
class AutoScrollViewPager(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    //Point位置
    private val CENTER = 0
    private val LEFT = 1
    private val RIGHT = 2
    private val WHAT_AUTO_PLAY = 5000

    private lateinit var mPointRealContainerLl: LinearLayout

    private lateinit var mViewPager: ViewPager
    //本地图片资源
    private var mImages: List<Int>? = null
    //网络图片资源
    private var mImageUrls: List<String>? = null
    //是否是网络图片
    private var mIsImageUrl = false
    //是否只有一张图片
    private var mIsOneImg = false
    //是否可以自动播放
    private var mAutoPlayAble = true
    //是否正在播放
    private var mIsAutoPlaying = false
    //自动播放时间
    private val mAutoPalyTime = 5000
    //当前页面位置
    private var mCurrentPositon: Int = 0
    //指示点位置
    private var mPointPosition = CENTER
    //指示点资源
    private val mPointDrawableResId = R.drawable.common_selector_bgabanner_point
    //指示容器背景
    private var mPointContainerBackgroundDrawable: Drawable? = null
    //指示容器布局规则
    private lateinit var mPointRealContainerLp: LayoutParams
    //指示点是否可见
    private var mPointsIsVisible = true

    private val mAutoPlayHandler =
        MyHandler(this)

    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.common_AutoScrollViewPager,
            0,
            0
        )
        mPointsIsVisible =
            a.getBoolean(R.styleable.common_AutoScrollViewPager_common_points_visibility, true)
        mPointPosition =
            a.getInt(R.styleable.common_AutoScrollViewPager_common_points_position, CENTER)
        mPointContainerBackgroundDrawable =
            a.getDrawable(R.styleable.common_AutoScrollViewPager_common_points_container_background)
        mAutoPlayAble =
            a.getBoolean(R.styleable.common_AutoScrollViewPager_common_isAutoScroll, true)
        a.recycle()

        setLayout(context)
    }

    private fun setLayout(context: Context) {
        //关闭view的OverScroll
        overScrollMode = View.OVER_SCROLL_NEVER
        //设置指示器背景
        if (mPointContainerBackgroundDrawable == null) {
            mPointContainerBackgroundDrawable = ColorDrawable(Color.parseColor("#00aaaaaa"))
        }
        //添加ViewPager
        mViewPager = ViewPager(context)
        addView(mViewPager, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        //设置指示器背景容器
        val pointContainerRl = RelativeLayout(context)
        pointContainerRl.background = mPointContainerBackgroundDrawable
        //设置内边距
        pointContainerRl.setPadding(0, 10, 0, 10)
        //设定指示器容器布局及位置
        val pointContainerLp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        pointContainerLp.addRule(ALIGN_PARENT_BOTTOM)
        addView(pointContainerRl, pointContainerLp)
        //设置指示器容器
        mPointRealContainerLl = LinearLayout(context)
        mPointRealContainerLl.orientation = LinearLayout.HORIZONTAL
        mPointRealContainerLp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        pointContainerRl.addView(mPointRealContainerLl, mPointRealContainerLp)
        //设置指示器容器是否可见
        mPointRealContainerLl.visibility = if (mPointsIsVisible) View.VISIBLE else View.GONE
        //设置指示器布局位置
        when (mPointPosition) {
            CENTER -> mPointRealContainerLp.addRule(CENTER_HORIZONTAL)
            LEFT -> mPointRealContainerLp.addRule(ALIGN_PARENT_LEFT)
            RIGHT -> mPointRealContainerLp.addRule(ALIGN_PARENT_RIGHT)
        }
    }

    /**
     *设置本地图片
     */
    fun setImages(images: List<Int>) {
        if (images.isNullOrEmpty()) throw RuntimeException("urls must not be null")
        //加载本地图片
        mIsImageUrl = false
        this.mImages = images
        if (images.size <= 1)
            mIsOneImg = true
        //初始化ViewPager
        initViewPager()
    }

    /**
     *设置网络图片
     */
    fun setImageUrls(urls: List<String>) {
        if (urls.isNullOrEmpty()) throw RuntimeException("urls must not be null")
        //加载网络图片
        mIsImageUrl = true
        this.mImageUrls = urls
        if (urls.size <= 1)
            mIsOneImg = true
        //初始化ViewPager
        initViewPager()
    }

    private fun initViewPager() {
        //当图片多于1张时添加指示点
        if (!mIsOneImg) {
            addPoints()
        }
        //设置ViewPager
        val adapter = AutoScrollPagerAdapter()
        mViewPager.adapter = adapter
        mViewPager.addOnPageChangeListener(mOnPageChangeListener)
        //跳转到首页
        mViewPager.setCurrentItem(1, false)
        //当图片多于1张时开始轮播
        if (!mIsOneImg) {
            startAutoPlay()
        }
    }

    /**
     * 开始播放
     */
    private fun startAutoPlay() {
        if (mAutoPlayAble && !mIsAutoPlaying) {
            mIsAutoPlaying = true
            mAutoPlayHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, mAutoPalyTime.toLong())
        }
    }

    /**
     * 停止播放
     */
    fun stopAutoPlay() {
        if (mAutoPlayAble && mIsAutoPlaying) {
            mIsAutoPlaying = false
            mAutoPlayHandler.removeMessages(WHAT_AUTO_PLAY)
        }
    }

    class MyHandler(autoScrollViewPager: AutoScrollViewPager) : Handler() {
        private var weakReference: WeakReference<AutoScrollViewPager> =
            WeakReference(autoScrollViewPager)

        override fun handleMessage(msg: Message?) {
            weakReference.get()?.handlerMessage()
        }
    }

    private fun handlerMessage() {
        mCurrentPositon++
        mViewPager.currentItem = mCurrentPositon
        mAutoPlayHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, mAutoPalyTime.toLong())
    }

    /**
     * 返回真实的位置
     * @param position
     * @return
     */
    private fun toRealPosition(position: Int): Int {
        var realPosition: Int
        if (mIsImageUrl) {
            realPosition = (position - 1) % mImageUrls!!.size
            if (realPosition < 0)
                realPosition += mImageUrls!!.size
        } else {
            realPosition = (position - 1) % mImages!!.size
            if (realPosition < 0)
                realPosition += mImages!!.size
        }

        return realPosition
    }

    private val mOnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                val current = mViewPager.currentItem
                val lastReal = mViewPager.adapter!!.count - 2
                if (current == 0) {
                    mViewPager.setCurrentItem(lastReal, false)
                } else if (current == lastReal + 1) {
                    mViewPager.setCurrentItem(1, false)
                }
            }
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            mCurrentPositon = if (mIsImageUrl) {
                position % (mImageUrls!!.size + 2)
            } else {
                position % (mImages!!.size + 2)
            }
            switchToPoint(toRealPosition(mCurrentPositon))
        }

    }

    inner class AutoScrollPagerAdapter : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

        override fun getCount(): Int {
            //当只有一张图片时返回1
            if (mIsOneImg) {
                return 1
            }
            //当为网络图片，返回网页图片长度
            return if (mIsImageUrl) {
                mImageUrls!!.size + 2
            } else {
                mImages!!.size + 2
            }
            //当为本地图片，返回本地图片长度
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val imageView = ImageView(context)
            imageView.setOnClickListener {
                mOnItemClickListener?.onItemClick(toRealPosition(position))
            }
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            if (mIsImageUrl) {
                imageView.setImageUrl(mImageUrls!![toRealPosition(position)])
            } else {
                imageView.setImageResource(mImages!![toRealPosition(position)])
            }
            container.addView(imageView)

            return imageView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    /**
     * 添加指示点
     */
    private fun addPoints() {
        mPointRealContainerLl.removeAllViews()
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(10, 10, 10, 10)
        var imageView: ImageView
        val length: Int = (if (mIsImageUrl) mImageUrls?.size else mImages?.size) ?: 0
        repeat((0 until length).count()) {
            imageView = ImageView(context)
            imageView.layoutParams = lp
            imageView.setImageResource(mPointDrawableResId)
            mPointRealContainerLl.addView(imageView)
        }

        switchToPoint(0)
    }

    /**
     * 切换指示器
     */
    private fun switchToPoint(currentPoint: Int) {
        for (i in 0 until mPointRealContainerLl.childCount) {
            mPointRealContainerLl.getChildAt(i).isEnabled = false
        }
        mPointRealContainerLl.getChildAt(currentPoint).isEnabled = true
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (mAutoPlayAble && !mIsOneImg) {
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> stopAutoPlay()
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> startAutoPlay()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private var mOnItemClickListener: OnItemClickListener? = null

    fun getViewPager() = mViewPager

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mOnItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}