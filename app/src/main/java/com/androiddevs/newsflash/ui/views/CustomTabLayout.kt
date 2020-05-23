package com.androiddevs.newsflash.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.androiddevs.newsflash.R
import com.androiddevs.newsflash.ui.views.CustomTab.Companion.getChip


class CustomTabLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener,
    DynamicAnimation.OnAnimationUpdateListener {


    private var isScrollable: Boolean = false
    private val chipList: MutableList<CustomTab> = ArrayList()
    private var selectedIndex = 0

    private var onTabPressCallback: ((String) -> Unit)? = null

    private val parentViewGroup by lazy {
        LinearLayout(context)
    }

    private val parentViewGroupScrollable by lazy {
        HorizontalScrollView(context)
    }

    private var globalPositionOffsetPixels: Float = 0F

    private val backgroundChip by lazy {
        getChip(context) {
            isClickable = false
            isFocusable = false
            visibility = View.GONE
            setSelectedBackgroundColor(ContextCompat.getColor(context, R.color.colorSecondaryDark))
        }
    }

    private val dotIndicatorSpring by lazy {
        SpringAnimation(backgroundChip, SpringAnimation.TRANSLATION_X).apply {
            val springForce = SpringForce(0F)
            springForce.dampingRatio = 0.75f
            springForce.stiffness = 300F
            spring = springForce
            addUpdateListener(this@CustomTabLayout)
        }
    }

    init {
        setWillNotDraw(false)
        parentViewGroupScrollable.apply {
            isHorizontalScrollBarEnabled = false
        }
    }


    fun initTabs(initBlock: CustomTabLayout.() -> Unit) {
        apply {
            initBlock()
        }
    }

    fun buildTabsWithText(vararg titleList: String) {
        titleList.forEachIndexed { index, title ->
            addTab(title)
        }
    }

    fun addTab(titleList: String) {
        val chip = getChip(context) {
            id = View.generateViewId()
            text = titleList
            isSelected = if (chipList.isEmpty()) {
                selectedIndex = id
                true
            } else {
                false
            }
            isClickable = true
            setOnClickListener(this@CustomTabLayout)
        }
        backgroundChip.visibility = View.VISIBLE
        chipList.add(chip)
    }

    private fun setLayoutParams() {
        var width = 0
        val display = display
        display?.let {
            val pointWidth = Point()
            val pointHeight = Point()
            display.getSize(pointWidth)
            display.getSize(pointHeight)

            chipList.forEach {
                it.measure(pointWidth.x, pointHeight.y)
                width += it.measuredWidth
            }
            parentViewGroup.removeAllViews()

            chipList.forEach {
                parentViewGroup.addView(it)
            }
            isScrollable = width + 100 > context.resources.displayMetrics.widthPixels

            if (isScrollable) {
                removeView(parentViewGroup)
                val frameLayout = FrameLayout(context)
                frameLayout.addView(backgroundChip)
                frameLayout.addView(parentViewGroup)
                parentViewGroupScrollable.addView(frameLayout)
                addView(parentViewGroupScrollable)
            } else {
                addView(backgroundChip)
                addView(parentViewGroup)
            }
        }
    }

    override fun onClick(v: View?) {
        v?.let {
            if (selectedIndex != v.id) {
                backgroundChip.updateLayoutParams {
                    width = v.boundingBox.width()
                    backgroundChip.setSelectedTabWidth(v.boundingBox.width())
                }
                globalPositionOffsetPixels = v.left.toFloat()
                if (isScrollable)
                    parentViewGroupScrollable.smoothScrollTo(
                        v.boundingBox.left - backgroundChip.defaultPadding,
                        0
                    )
                dotIndicatorSpring.spring.finalPosition = globalPositionOffsetPixels
                dotIndicatorSpring.animateToFinalPosition(globalPositionOffsetPixels)
                selectedIndex = v.id
                chipList.forEach {
                    it.isSelected = it.id == selectedIndex
                }
            }
        }
    }

    fun setOnTabChangeCallback(onTabPressCallback: (String) -> Unit) {
        this.onTabPressCallback = onTabPressCallback
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setLayoutParams()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        chipList.find { it.id == selectedIndex }?.let {
            backgroundChip.updateLayoutParams {
                width = it.boundingBox.width()
                backgroundChip.setSelectedTabWidth(it.boundingBox.width())
                height = it.measuredHeight
            }
        }
    }


    override fun onAnimationUpdate(
        animation: DynamicAnimation<out DynamicAnimation<*>>?,
        value: Float,
        velocity: Float
    ) {
    }
}

val View.screenLocation
    get(): IntArray {
        val point = IntArray(2)
        getLocationOnScreen(point)
        return point
    }

val View.boundingBox
    get(): Rect {
        val (x, y) = screenLocation
        return Rect(x, y, x + width, y + height)
    }
