package com.example.dieter.ui.screen.account

import androidx.lifecycle.ViewModel
import com.example.dieter.data.source.DieterRepository
import javax.inject.Inject

class AccountViewModel @Inject constructor(private val dieterRepository: DieterRepository) : ViewModel()
