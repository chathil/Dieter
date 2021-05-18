package com.example.dieter.ui.screen.add.ingredients

import androidx.lifecycle.ViewModel
import com.example.dieter.data.source.DieterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddIngredientsViewModel @Inject constructor(
    private val dieterRepository: DieterRepository
) : ViewModel()
