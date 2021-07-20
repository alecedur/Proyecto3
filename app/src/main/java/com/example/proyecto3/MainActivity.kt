package com.example.proyecto3

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.MotionEvent
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs
import kotlin.random.Random


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
    var endPointX = 1
    var endPointY = 1
    var startPointX = 1
    var startPointY = 1
    var drawEnd = true
    var gameCircle : CircleArea? = null
    var endCircle : CircleArea? = null
    var tStart : Long = 1




    /** Stores data about single circle  */
    class CircleArea internal constructor(
        var centerX: Int,
        var centerY: Int,
        var radius: Int,
        var id: Int,
        var color: Paint
    ) {
        override fun toString(): String {
            return "Circle[$centerX, $centerY, $radius]"
        }
    }

    /** Paint to draw circles  */
    private var mCirclePaint: Paint? = null

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

    private fun generaStartEndPoints() {
        var width = Resources.getSystem().getDisplayMetrics().widthPixels
        var height = Resources.getSystem().getDisplayMetrics().heightPixels
        endPointX = (Random.nextInt(Resources.getSystem().getDisplayMetrics().widthPixels)/100)*100
        endPointY = (Random.nextInt(14)+2)*100
        startPointX = (Random.nextInt(Resources.getSystem().getDisplayMetrics().widthPixels)/100)*100
        startPointY = (Random.nextInt(14)+2)*100
    }

    private fun init(ct: Context) {
        // Generate bitmap used for background
        mCirclePaint = Paint()
        mCirclePaint!!.color = Color.BLUE
        mCirclePaint!!.strokeWidth = 40f
        mCirclePaint!!.style = Paint.Style.FILL
        btn = Button(ct)
        btn.x = 200F+200F+20F+10F+10F
        btn.y = 1400F
        btn.setLayoutParams(FrameLayout.LayoutParams(200, 200))
        btn.text = "up"
        var btn2 = Button(ct)
        btn2.x = 200F+200F+20F+10F+10F
        btn2.y = 1600F
        btn2.setLayoutParams(FrameLayout.LayoutParams(200, 200))
        btn2.text = "down"
        var btn3 = Button(ct)
        btn3.x = 400F+200F+20F+10F+10F
        btn3.y = 1500F
        btn3.setLayoutParams(FrameLayout.LayoutParams(200, 200))
        btn3.text = "right"
        var btn4 = Button(ct)
        btn4.x = 0F+200F+20F+10F+10F
        btn4.y = 1500F
        btn4.setLayoutParams(FrameLayout.LayoutParams(200, 200))
        btn4.text = "left"
        btn.setOnClickListener {
            Log.w(TAG, "btn")
            for(element in mCircles) {
                if(element.id == 1) {
                    if(element.centerY - 100 < 100) {
                    } else {
                        element.centerY -= 100
                    }
                }
            }
            invalidate()
        }
        btn2.setOnClickListener {
            Log.w(TAG, "btn2")
            var touchedCircle: CircleArea?
            for(element in mCircles) {
                if(element.id == 1 ) {
                    if(element.centerY + 100 > 1400) {
                    } else {
                        element.centerY += 100
                    }
                }
            }
            invalidate()

        }
        btn3.setOnClickListener {
            Log.w(TAG, "btn3")
            var touchedCircle: CircleArea?
            for(element in mCircles) {
                if(element.id == 1 ) {
                    if(element.centerX + 100 > 1080) {

                    }
                    else {
                        element.centerX += 100
                    }
                }
            }
            invalidate()
        }
        btn4.setOnClickListener {
            Log.w(TAG, "btn4")
            var touchedCircle: CircleArea?
            for(element in mCircles) {
                if(element.id == 1) {
                    if(element.centerX - 100 < 0) {

                    } else {
                        element.centerX -= 100
                    }
                }
            }
            invalidate()
        }

        this.addView(btn)
        this.addView(btn2)
        this.addView(btn3)
        this.addView(btn4)
        tStart = System.currentTimeMillis()
//        for(element in mCircles) {
//            element.centerX = 540
//            element.centerY = 1036
//        }

        //var touchedCircle =
            //CircleArea(536, 864, RADIUS_LIMIT)
        generaStartEndPoints()
        gameCircle =
            CircleArea(startPointX, startPointY, RADIUS_LIMIT, 1, mCirclePaint!!)
        mCircles.add(gameCircle!!)
        var mCirclePaint2 = Paint()
        mCirclePaint2!!.color = Color.RED
        mCirclePaint2!!.strokeWidth = 40f
        mCirclePaint2!!.style = Paint.Style.FILL
        endCircle = CircleArea(endPointX, endPointY, RADIUS_LIMIT,2, mCirclePaint2)
        mCircles.add(endCircle!!)
        invalidate()
    }

    public fun didWeWin() {
        var gameCenterX = gameCircle?.centerX
        var gameCenterY = gameCircle?.centerY
        var endCenterX = endCircle?.centerX
        var endCenterY = endCircle?.centerY
        if (gameCenterX != null) {
            if (gameCenterY != null) {
                if((abs(gameCenterX - endCenterX!!) < 70) && abs(gameCenterY - endCenterY!!) < 70) {
                    val tEnd = System.currentTimeMillis()
                    val tDelta: Long = tEnd - tStart
                    val elapsedSeconds = tDelta / 1000.0
                    val str: String = elapsedSeconds.toString()
                    val i = Intent(context, InsertName::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK //add this line
                    i.putExtra("score_key", str)
                    context.startActivity(i)
                    (context as? MainActivity)?.finish()
                }
            }
        }
    }

    public override fun onDraw(canv: Canvas) {
        // background bitmap to cover all area
        didWeWin()
        mBitmap?.let { mMeasuredRect?.let { it1 -> canv.drawBitmap(it, null, it1, null) } }
        for (circle in mCircles) {
                canv.drawCircle(
                    circle.centerX.toFloat(), circle.centerY.toFloat(), circle.radius.toFloat(),
                    circle.color!!
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
//                // it's the first pointer, so clear all existing pointers data
//                clearCirclePointer()
//                xTouch = event.getX(0).toInt()
//                yTouch = event.getY(0).toInt()
//
//                // check if we've touched inside some circle
//                touchedCircle = obtainTouchedCircle(xTouch, yTouch)
//                touchedCircle.centerX = xTouch
//                touchedCircle.centerY = yTouch
//                mCirclePointer.put(event.getPointerId(0), touchedCircle)
//                invalidate()
//                handled = true
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
//                Log.w(TAG, "Pointer down")
//                // It secondary pointers, so obtain their ids and check circles
//                pointerId = event.getPointerId(actionIndex)
//                xTouch = event.getX(actionIndex).toInt()
//                yTouch = event.getY(actionIndex).toInt()
//
//                // check if we've touched inside some circle
//                //touchedCircle = obtainTouchedCircle(xTouch, yTouch)
//                mCirclePointer.put(pointerId, touchedCircle)
//                touchedCircle.centerX = xTouch
//                touchedCircle.centerY = yTouch
//                invalidate()
//                handled = true
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



//    private fun obtainTouchedCircle(xTouch: Int, yTouch: Int): CircleArea {
//        var touchedCircle = getTouchedCircle(xTouch, yTouch)
//        if (null == touchedCircle) {
//            touchedCircle =
//                CircleArea(xTouch, yTouch, RADIUS_LIMIT, 1, mCirclePaint)
//            if (mCircles.size === CirclesDrawingView.Companion.CIRCLES_LIMIT) {
//                Log.w(TAG, "Clear all circles, size is " + mCircles.size)
//                // remove first circle
//                mCircles.clear()
//            }
//            Log.w(TAG, "Added circle $touchedCircle")
//            mCircles.add(touchedCircle)
//        }
//        return touchedCircle
//    }

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