package com.example.operatorapp


import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.operatorapp.ui.theme.LogicGame
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LogicGame {
                QuizAppActivitys().QuizApp()
            }
        }
    }
}
