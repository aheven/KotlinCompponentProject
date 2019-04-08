package heven.holt.model.camera.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class SurfaceViewHandWriting(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs),
    SurfaceHolder.Callback,
    Runnable {
    private var isDrawing = false
    private var mCanvas: Canvas? = null
    private var path: Path? = null

    private lateinit var paint: Paint

    private var startY = 0f

    init {
        holder.addCallback(this)
        initPaint()
        path = Path()
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
            val start = System.currentTimeMillis()
            drawSomething()
            val end = System.currentTimeMillis()
            if (end - start < 100) {
                Thread.sleep(100 - (end - start))
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> path?.moveTo(x, y)
            MotionEvent.ACTION_MOVE -> path?.lineTo(x, y)
        }
        return true
    }
}