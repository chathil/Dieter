package com.example.dieter.ui.screen.goal

import androidx.lifecycle.ViewModel
import com.example.dieter.data.source.DieterRepository
import com.example.dieter.data.source.domain.GoalType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val dieterRepository: DieterRepository
) : ViewModel() {
    // private _state
    /**
     * Save to firebase
     */
    fun save(selectedGoal: GoalType, age: Int, isMale: Boolean, height: Int, weight: Int) {
    }
}
