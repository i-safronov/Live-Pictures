package com.safronov.livepictures.di

import com.safronov.livepictures.ui.composable.CanvasViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { CanvasViewModel() }
}