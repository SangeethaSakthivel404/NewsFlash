package com.androiddevs.newsflash.ui.acitivities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.androiddevs.newsflash.R
import com.androiddevs.newsflash.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        binding.ctCustomTab.setTabs(
            arrayOf(
                "Recent News",
                "Saved News",
                "Profile"
            )
        )
        binding.ctCustomTab.setOnTabChangeCallback { tabTitle ->
            Toast.makeText(this, tabTitle, Toast.LENGTH_SHORT).show()
        }

    }

}