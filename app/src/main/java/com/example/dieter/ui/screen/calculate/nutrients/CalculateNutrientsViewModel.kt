package com.example.dieter.ui.screen.calculate.nutrients

import androidx.lifecycle.ViewModel
import com.example.dieter.data.source.DieterRepository
import javax.inject.Inject

class CalculateNutrientsViewModel @Inject constructor(
    private val dieterRepository: DieterRepository
) : ViewModel()
