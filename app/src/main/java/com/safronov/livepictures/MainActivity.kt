package com.safronov.livepictures

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.safronov.livepictures.ui.composable.CanvasScreen
import com.safronov.livepictures.ui.composable.CanvasViewModel
import com.safronov.livepictures.ui.theme.Colors
import com.safronov.livepictures.ui.theme.LivePicturesTheme
import com.safronov.livepictures.ui.theme.SetStatusBarColor
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LivePicturesTheme {
                SetStatusBarColor(
                    statusBar = Colors.Background,
                    navigationBar = Colors.Background
                )

                val canvasViewModel: CanvasViewModel = getViewModel()

                CanvasScreen(
                    state = canvasViewModel.state,
                    dispatch = canvasViewModel::dispatch,
                    events = canvasViewModel.events
                )
            }
        }
    }
}