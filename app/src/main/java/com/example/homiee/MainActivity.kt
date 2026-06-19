package com.example.homiee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.homiee.navigation.HomieeNavGraph
import com.example.homiee.ui.theme.HomieeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Lets the gradient backgrounds draw behind the status bar
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            HomieeTheme {
                HomieeNavGraph()
            }
        }
    }
}