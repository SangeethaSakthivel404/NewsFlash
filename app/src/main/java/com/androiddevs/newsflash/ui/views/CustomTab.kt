package com.androiddevs.newsflash.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF
import com.androiddevs.newsflash.R
import kotlin.math.roundToInt

class CustomTab @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    companion object {
        inline fun getChip(context: Context, initBlock: CustomTab.() -> Unit = {}): CustomTab =
            CustomTab(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1F
                ).also {
                    it.leftMargin = 12
                    it.rightMargin = 12
                }
                initBlock()
            }
    }


    private var strokeWidth: Float = 0F
    private var strokeColour: Int = 0


    private val backgroundPadding = 4

    private var shouldStroke: Boolean = false
    val defaultPadding by lazy {
        16 * context.resources.displayMetrics.density.roundToInt()
    }

    private val backgroundRect by lazy {
        Rect(
            backgroundPadding,
            backgroundPadding,
            measuredWidth - backgroundPadding,
            measuredHeight - backgroundPadding
        )
    }

    private val backgroundPaint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            color = Color.TRANSPARENT
            isAntiAlias = true
        }
    }


    fun setStrokeColor(color: Int, strokeWidth: Float = 1F) {
        shouldStroke = true
        this.strokeWidth = strokeWidth
        this.strokeColour = color
    }

    fun setSelectedBackgroundColor(selectedColor: Int) {
        backgroundPaint.apply { color = selectedColor }
    }

    fun setSelectedTabWidth(width: Int) {
        backgroundRect.right = width
        requestLayout()
    }

    init {
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        setBackgroundColor(Color.TRANSPARENT)
        setTextColor(ContextCompat.getColorStateList(context, R.color.tablayout_tab_text))
        setPadding(defaultPadding, defaultPadding / 2, defaultPadding, defaultPadding / 2)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            it.drawRoundRect(
                backgroundRect.toRectF(),
                measuredHeight.toFloat(),
                measuredHeight.toFloat(),
                backgroundPaint
            )
            if (shouldStroke) {
                backgroundPaint.apply {
                    style = Paint.Style.STROKE
                    strokeJoin = Paint.Join.ROUND
                    strokeCap = Paint.Cap.ROUND
                    strokeWidth = this@CustomTab.strokeWidth
                    color = strokeColour
                }
                it.drawRoundRect(
                    backgroundRect.toRectF(),
                    measuredHeight.toFloat(),
                    measuredHeight.toFloat(),
                    backgroundPaint
                )
            }
        }

        super.onDraw(canvas)
    }


}