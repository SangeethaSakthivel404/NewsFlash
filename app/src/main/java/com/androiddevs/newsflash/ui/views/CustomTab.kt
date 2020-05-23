package com.androiddevs.newsflash.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.roundToInt

class CustomTab @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val defaultPadding by lazy {
        16 * context.resources.displayMetrics.density.roundToInt()
    }

    private val backgroundRect by lazy {
        RectF(
            defaultPadding.toFloat(),
            defaultPadding.toFloat(),
            measuredWidth.toFloat(),
            measuredHeight.toFloat()
        )
    }

    private val backgroundPaint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            color = Color.TRANSPARENT
            isAntiAlias = true
        }

    }


    init {
        setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.drawRoundRect(
                backgroundRect,
                measuredHeight / 2.toFloat(),
                measuredHeight / 2.toFloat(),
                backgroundPaint
            )
        }
    }


}