package com.marlem.mixm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.RequestManager
import com.marlem.mixm.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
//playing logic in background service or foreground

@AndroidEntryPoint //proviene de android component
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var glideModule:RequestManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
