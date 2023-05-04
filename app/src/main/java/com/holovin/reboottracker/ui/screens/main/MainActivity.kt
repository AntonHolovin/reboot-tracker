package com.holovin.reboottracker.ui.screens.main

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.holovin.reboottracker.R
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.uiState.observe(this) {
            when (it) {
                is MainScreenUiState.Loading -> {
                    // TODO: show loading
                }

                is MainScreenUiState.Content -> render(it.reboots)
            }
        }
    }

    private fun render(reboots: List<Instant>) {
        findViewById<TextView>(R.id.text_view).text = buildString(reboots)
    }

    private fun buildString(reboots: List<Instant>): String {
        if (reboots.isEmpty()) {
            return "No boots detected"
        }

        return buildString {
            reboots.forEachIndexed { index, instant ->
                append(index + 1)
                append(" - ")
                append(instant.toEpochMilli())
                append("\n")
            }
        }
    }
}