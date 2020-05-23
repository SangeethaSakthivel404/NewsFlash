package com.androiddevs.newsflash.ui.views

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.androiddevs.newsflash.R
import com.google.android.material.chip.Chip


fun getChip(context: Context, initBlock: Chip.() -> Unit = {}): Chip {

    val chip = LayoutInflater.from(context).inflate(R.layout.item_custom_tab, null) as Chip
    chip.apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1F
        )
        initBlock()
    }
    return chip
}
