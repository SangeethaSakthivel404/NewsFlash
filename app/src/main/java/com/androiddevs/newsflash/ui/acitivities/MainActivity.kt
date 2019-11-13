package com.androiddevs.newsflash.ui.acitivities

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.androiddevs.newsflash.R
import com.androiddevs.newsflash.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)

        textView("Hello") {
            this textColor Color.BLACK
            textSize = 24f
        }
    }


    private fun textView(text: String, block: TextView.() -> Unit) {
        val textView = TextView(this)
        textView.apply {
            this.text = text
            block()
        }
        binding.root.addView(textView)
    }

    private infix fun TextView.textColor(color:Int){
        this.setTextColor(color)
    }
}
