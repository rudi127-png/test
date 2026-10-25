package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ai.AppCheckHelper
import com.example.ui.screens.MainContainer
import com.example.ui.theme.Only4AlineTheme
import com.example.ui.viewmodel.JobViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize App Check foundation
        AppCheckHelper.configureAppCheck(this, intent)

        enableEdgeToEdge()
        setContent {
            Only4AlineTheme {
                val viewModel: JobViewModel = viewModel()
                MainContainer(viewModel = viewModel)
            }
        }
    }
}
