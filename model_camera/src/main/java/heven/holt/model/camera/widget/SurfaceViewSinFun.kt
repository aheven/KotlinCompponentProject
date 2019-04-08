package heven.holt.model.camera.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils

class SurfaceViewSinFun(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs), SurfaceHolder.Callback,
    Runnable {
    private var isDrawing = false
    private var mCanvas: Canvas? = null
    private var path: Path? = null

    private lateinit var paint: Paint

    private var x: Int = 0
    private var y: Int = 0

    private var startY = 0f

    init {
        holder.addCallback(this)
        initPaint()
    }

    private fun initPaint() {
        paint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeWidth = 5f
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        isDrawing = false
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        isDrawing = true
        startY = (measuredHeight / 2).toFloat()
        Thread(this).start()
    }

    override fun run() {
        while (isDrawing) {
            if (path == null) {
                path = Path()
                path!!.moveTo(0f, startY)
            }
            drawSomething()
            x += 8
            y = (100 * Math.sin(2 * x * Math.PI / 180) + startY).toInt()
            path?.lineTo(x.toFloat(), y.toFloat())
            if (x > ScreenUtils.getScreenWidth()) {
                isDrawing = false
                LogUtils.i("draw x > screen width ,stop draw.")
            }
        }
    }

    private fun drawSomething() {
        mCanvas = holder.lockCanvas()
        mCanvas?.drawColor(Color.WHITE)
        if (path != null)
            mCanvas?.drawPath(path!!, paint)
        if (mCanvas != null)
            holder.unlockCanvasAndPost(mCanvas)
    }
}