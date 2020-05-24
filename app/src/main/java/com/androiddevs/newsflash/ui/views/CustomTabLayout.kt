package com.androiddevs.newsflash.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.updateLayoutParams
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.androiddevs.newsflash.R
import com.androiddevs.newsflash.ui.views.CustomTab.Companion.getChip


class CustomTabLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener {


    private var isScrollable: Boolean = false
    private val tabsList: MutableList<CustomTab> = ArrayList()

    private var selectedTabID = 0

    private var onTabPressCallback: ((String?) -> Unit)? = null

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

    fun CustomTab.setSelected() {
        val index = tabsList.indexOfFirst { it.id == id }
        selectedTab(index)
    }

    fun addTab(
        titleList: String,
        initBlock: CustomTab.() -> Unit = {}
    ) {
        getChip(context) {
            id = View.generateViewId()
            text = titleList
            isSelected = if (tabsList.isEmpty()) {
                selectedTabID = id
                true
            } else {
                false
            }
            setOnClickListener(this@CustomTabLayout)
            tabsList.add(this)
            initBlock()
        }
        backgroundChip.visibility = View.VISIBLE
    }

    private fun setLayoutParams() {
        var width = 0
        val display = display
        display?.let {
            val pointWidth = Point()
            val pointHeight = Point()
            display.getSize(pointWidth)
            display.getSize(pointHeight)

            tabsList.forEach {
                it.measure(pointWidth.x, pointHeight.y)
                width += it.measuredWidth
            }
            parentViewGroup.removeAllViews()

            tabsList.forEach {
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

    fun selectedTab(selectedIndex: Int) {
        tabsList.forEachIndexed { index, customTab ->
            if (index == selectedIndex) {
                selectedTabID = customTab.id
                customTab.isSelected = true
                post {
                    backgroundChip.x = customTab.left.toFloat()
                }
            } else {
                customTab.isSelected = false
            }
        }
    }


    fun setOnTabChangeCallback(onTabPressCallback: (String?) -> Unit) {
        this.onTabPressCallback = onTabPressCallback
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setLayoutParams()
    }


    private fun measureBackgroundParams() {
        parentViewGroup.children.find { it.id == selectedTabID }?.let { selectedChip ->
            backgroundChip.updateLayoutParams {
                width = selectedChip.measuredWidth
                backgroundChip.setSelectedTabWidth(selectedChip.measuredWidth)
                height = selectedChip.measuredHeight
            }
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        measureBackgroundParams()
    }

    override fun onClick(view: View?) {
        view?.let {
            onTabPressCallback?.invoke((view as CustomTab).text.toString())

            if (selectedTabID != view.id) {
                backgroundChip.updateLayoutParams {
                    width = view.measuredWidth
                    backgroundChip.setSelectedTabWidth(view.measuredWidth)
                }
                selectedTabID = view.id
                tabsList.forEach {
                    it.isSelected = it.id == selectedTabID
                }
                animateViews(view)
            }
        }
    }

    private fun animateViews(view: View) {
        globalPositionOffsetPixels = view.left.toFloat() - 12
        if (isScrollable)
            parentViewGroupScrollable.smoothScrollTo(
                view.boundingBox.left - backgroundChip.defaultPadding,
                0
            )
        dotIndicatorSpring.spring.finalPosition = globalPositionOffsetPixels
        dotIndicatorSpring.animateToFinalPosition(globalPositionOffsetPixels)
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
