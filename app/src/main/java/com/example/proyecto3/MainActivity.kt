package com.example.proyecto3

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.MotionEvent
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.collections.HashSet


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        val view = CirclesDrawingView(this)
        setContentView(view)
    }

}

public class CirclesDrawingView : FrameLayout {
    /** Main bitmap  */
    private var mBitmap: Bitmap? = null
    private var mMeasuredRect: Rect? = null


    /** Stores data about single circle  */
    private class CircleArea internal constructor(
        var centerX: Int,
        var centerY: Int,
        var radius: Int
    ) {
        override fun toString(): String {
            return "Circle[$centerX, $centerY, $radius]"
        }
    }

    /** Paint to draw circles  */
    private var mCirclePaint: Paint? = null
    private val mRadiusGenerator: Random = Random()

    /** All available circles  */
    private val mCircles = HashSet<CircleArea>(CIRCLES_LIMIT)
    private val mCirclePointer = SparseArray<CircleArea?>(CIRCLES_LIMIT)

    lateinit var btn : Button

    /**
     * Default constructor
     *
     * @param ct [android.content.Context]
     */
    constructor(ct: Context) : super(ct) {
        this.setWillNotDraw(false)
        init(ct)
    }

    constructor(ct: Context, attrs: AttributeSet?) : super(ct, attrs) {
        this.setWillNotDraw(false)
        init(ct)
    }

    constructor(ct: Context, attrs: AttributeSet?, defStyle: Int) : super(ct, attrs, defStyle) {
        this.setWillNotDraw(false)
        init(ct)
    }

    private fun init(ct: Context) {
        // Generate bitmap used for background

        mCirclePaint = Paint()
        mCirclePaint!!.color = Color.BLUE
        mCirclePaint!!.strokeWidth = 40f
        mCirclePaint!!.style = Paint.Style.FILL
        btn = Button(ct)
        btn.x = 100F
        btn.y = 200F
        btn.setLayoutParams(FrameLayout.LayoutParams(200, 200))
        btn.text = "up"
        var btn2 = Button(ct)
        btn2.x = 100F
        btn2.y = 400F
        btn2.setLayoutParams(FrameLayout.LayoutParams(200, 200))
        btn2.text = "down"
        var btn3 = Button(ct)
        btn3.x = 400F
        btn3.y = 300F
        btn3.setLayoutParams(FrameLayout.LayoutParams(200, 200))
        btn3.text = "left"
        var btn4 = Button(ct)
        btn4.x = 0F
        btn4.y = 300F
        btn4.setLayoutParams(FrameLayout.LayoutParams(200, 200))
        btn4.text = "right"
        btn.setOnClickListener {
            Log.w(TAG, "btn")
            for(element in mCircles) {
                element.centerY -= 100
            }
            invalidate()
        }
        btn2.setOnClickListener {
            Log.w(TAG, "btn2")
            var touchedCircle: CircleArea?
            for(element in mCircles) {
                element.centerY += 100
            }
            invalidate()

        }
        btn3.setOnClickListener {
            Log.w(TAG, "btn3")
            var touchedCircle: CircleArea?
            for(element in mCircles) {
                element.centerX += 100
            }
            invalidate()
        }
        btn4.setOnClickListener {
            Log.w(TAG, "btn4")
            var touchedCircle: CircleArea?
            for(element in mCircles) {
                element.centerX -= 100
            }
            invalidate()
        }

        this.addView(btn)
        this.addView(btn2)
        this.addView(btn3)
        this.addView(btn4)
    }

    public override fun onDraw(canv: Canvas) {
        // background bitmap to cover all area
        mBitmap?.let { mMeasuredRect?.let { it1 -> canv.drawBitmap(it, null, it1, null) } }
        for (circle in mCircles) {
            canv.drawCircle(
                circle.centerX.toFloat(), circle.centerY.toFloat(), circle.radius.toFloat(),
                mCirclePaint!!
            )
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        var handled = false
        var touchedCircle: CircleArea?
        var xTouch: Int
        var yTouch: Int
        var pointerId: Int
        var actionIndex = event.actionIndex
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // it's the first pointer, so clear all existing pointers data
                clearCirclePointer()
                xTouch = event.getX(0).toInt()
                yTouch = event.getY(0).toInt()

                // check if we've touched inside some circle
                touchedCircle = obtainTouchedCircle(xTouch, yTouch)
                touchedCircle.centerX = xTouch
                touchedCircle.centerY = yTouch
                mCirclePointer.put(event.getPointerId(0), touchedCircle)
                invalidate()
                handled = true
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                Log.w(TAG, "Pointer down")
                // It secondary pointers, so obtain their ids and check circles
                pointerId = event.getPointerId(actionIndex)
                xTouch = event.getX(actionIndex).toInt()
                yTouch = event.getY(actionIndex).toInt()

                // check if we've touched inside some circle
                touchedCircle = obtainTouchedCircle(xTouch, yTouch)
                mCirclePointer.put(pointerId, touchedCircle)
                touchedCircle.centerX = xTouch
                touchedCircle.centerY = yTouch
                invalidate()
                handled = true
            }
            MotionEvent.ACTION_MOVE -> {
                val pointerCount = event.pointerCount
                Log.w(TAG, "Move")
                actionIndex = 0
                while (actionIndex < pointerCount) {

                    // Some pointer has moved, search it by pointer id
                    pointerId = event.getPointerId(actionIndex)
                    xTouch = event.getX(actionIndex).toInt()
                    yTouch = event.getY(actionIndex).toInt()
                    touchedCircle = mCirclePointer[pointerId]
                    if (null != touchedCircle) {
                        touchedCircle.centerX = xTouch
                        touchedCircle.centerY = yTouch
                    }
                    actionIndex++
                }
                invalidate()
                handled = true
            }
            MotionEvent.ACTION_UP -> {
                clearCirclePointer()
                invalidate()
                handled = true
            }
            MotionEvent.ACTION_POINTER_UP -> {
                // not general pointer was up
                pointerId = event.getPointerId(actionIndex)
                mCirclePointer.remove(pointerId)
                invalidate()
                handled = true
            }
            MotionEvent.ACTION_CANCEL -> handled = true
            else -> {
            }
        }
        return super.onTouchEvent(event) || handled
    }

    /**
     * Clears all CircleArea - pointer id relations
     */
    private fun clearCirclePointer() {
        Log.w(TAG, "clearCirclePointer")
        mCirclePointer.clear()
    }

    /**
     * Search and creates new (if needed) circle based on touch area
     *
     * @param xTouch int x of touch
     * @param yTouch int y of touch
     *
     * @return obtained [CircleArea]
     */
    private fun obtainTouchedCircle(xTouch: Int, yTouch: Int): CircleArea {
        var touchedCircle = getTouchedCircle(xTouch, yTouch)
        if (null == touchedCircle) {
            touchedCircle =
                CircleArea(xTouch, yTouch, mRadiusGenerator.nextInt(RADIUS_LIMIT) + RADIUS_LIMIT)
            if (mCircles.size === CirclesDrawingView.Companion.CIRCLES_LIMIT) {
                Log.w(TAG, "Clear all circles, size is " + mCircles.size)
                // remove first circle
                mCircles.clear()
            }
            Log.w(TAG, "Added circle $touchedCircle")
            mCircles.add(touchedCircle)
        }
        return touchedCircle
    }

    /**
     * Determines touched circle
     *
     * @param xTouch int x touch coordinate
     * @param yTouch int y touch coordinate
     *
     * @return [CircleArea] touched circle or null if no circle has been touched
     */
    private fun getTouchedCircle(xTouch: Int, yTouch: Int): CircleArea? {
        var touched: CircleArea? = null
        for (circle in mCircles) {
            if ((circle.centerX - xTouch) * (circle.centerX - xTouch) + (circle.centerY - yTouch) * (circle.centerY - yTouch) <= circle.radius * circle.radius) {
                touched = circle
                break
            }
        }
        return touched
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mMeasuredRect = Rect(0, 0, measuredWidth, measuredHeight)
    }

    companion object {
        private const val TAG = "CirclesDrawingView"

        // Radius limit in pixels
        private const val RADIUS_LIMIT = 50
        private const val CIRCLES_LIMIT = 1
    }
}