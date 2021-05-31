package com.example.dieter.ui.screen.workout

import android.os.CountDownTimer
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dieter.USER_REP_ID
import com.example.dieter.application.DieterApplication
import com.example.dieter.data.source.DieterRepository
import com.example.dieter.data.source.domain.BurnCalorieModel
import com.example.dieter.data.source.domain.SaveWorkoutModel
import com.example.dieter.data.source.domain.WorkoutModel
import com.example.dieter.dataStore
import com.example.dieter.vo.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val dieterRepository: DieterRepository
) : ViewModel() {
    private val userRepKey = stringPreferencesKey(USER_REP_ID)
    var userRepId: String = ""

    val workoutModels =
        listOf(
            WorkoutModel("1", "Pushup", 5),
            WorkoutModel("2", "Pullup", 10),
            WorkoutModel("3", "Pulldown", 3),
            WorkoutModel("4", "Sleeping", 8)
        )

    // now/peak = 0,844
    // 0,844 * 5 = 4,22

    private val _calories = MutableStateFlow<DataState<BurnCalorieModel>>(DataState.Empty)
    val calories: StateFlow<DataState<BurnCalorieModel>>
        get() = _calories

    private val _dones = MutableStateFlow<Set<WorkoutModel>>(emptySet())
    val dones: StateFlow<Set<WorkoutModel>>
        get() = _dones

    fun dones(workoutModel: WorkoutModel) {
        _dones.value += workoutModel
    }

    private val _isTicking = MutableStateFlow(false)
    val isTicking: StateFlow<Boolean>
        get() = _isTicking

    private val _todos = MutableStateFlow<Map<WorkoutModel, Int>>(emptyMap())
    val todos: StateFlow<Map<WorkoutModel, Int>>
        get() = _todos

    val timers = mutableMapOf<WorkoutModel, MutableStateFlow<Long>>()

    private val _saveWorkoutState = MutableStateFlow<DataState<Boolean>>(DataState.Loading(null))
    val saveWorkoutState: StateFlow<DataState<Boolean>>
        get() = _saveWorkoutState

    private val _toAdd = MutableStateFlow<Float>(0f)
    val toAdd: StateFlow<Float>
        get() = _toAdd

    init {
        viewModelScope.launch {
            userRepId()!!.collect {
                userRepId = it!!
                calories(it)
            }
        }
    }

    fun inc(item: WorkoutModel) {
        if (_todos.value.containsKey(item)) {
            var current = _todos.value[item]!!
            current++
            removeItem(item)
            _todos.value += Pair(item, current)
        } else {
            _todos.value += Pair(item, 1)
        }
        var burned = 0f
        _todos.value.forEach { (t, u) ->
            burned += u * t.calorieBurnedPerMinute
        }
        _toAdd.value = burned
    }

    fun dec(item: WorkoutModel) {
        var current = _todos.value[item]
        if (current == null || current == 1) {
            removeItem(item)
        } else {
            current--
            _todos.value += Pair(item, current)
        }
        var burned = 0f
        _todos.value.forEach { (t, u) ->
            burned += u * t.calorieBurnedPerMinute
        }
        _toAdd.value = burned
    }

    private fun removeItem(item: WorkoutModel) {
        _todos.value -= item
    }

    // TODO: Clear this mess
    fun saveBurnedCalories(prorate: Pair<WorkoutModel?, Float?>, now: Long?) {
        var burned = prorate.second ?: 0f
        dones.value.forEach {
            val res = todos.value[it]?.toFloat()?.times(it.calorieBurnedPerMinute) ?: 0f
            burned += res
        }
        val workouts = todos.value.map { (t, u) ->
            val actual =
                when {
                    dones.value.contains(t) -> ((u * 60 * 1000L).toInt() - t.calorieBurnedPerMinute).absoluteValue
                    prorate.first == t -> (
                        (
                            (u * 60 * 1000L).minus(
                                now ?: 0
                            ).absoluteValue
                            ).toInt() - t.calorieBurnedPerMinute
                        ).absoluteValue
                    else -> 0
                }
            SaveWorkoutModel.WorkoutModel(
                t.id,
                t.name,
                t.calorieBurnedPerMinute,
                u * 60 * 1000,
                actual
            )
        }
        val save = SaveWorkoutModel(workouts, burned.toInt())
        _dones.value = emptySet()
        viewModelScope.launch {
            dieterRepository.saveWorkouts(userRepId, save).collect {
                _saveWorkoutState.value = it
            }
        }
    }

    fun startQueue() {
        val timeInMillis = todos.value.map { it.key to (it.value * 60 * 1000L) }.toMap()
        todos.value.forEach { (t, u) ->
            timers[t] = MutableStateFlow(u.toLong())
        }
        timer(timeInMillis.iterator())
    }

    private fun timer(
        iterable:
            Iterator<Map.Entry<WorkoutModel, Long>>
    ) {
        val item = iterable.next()
        val timer = object : CountDownTimer(item.value, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timers[item.key]?.value = millisUntilFinished
                if (!isTicking.value) {
                    cancel()
                    timers[item.key]?.value = 0
                }
            }

            override fun onFinish() {
                if (iterable.hasNext()) {
                    timer(iterable)
                } else {
                    _isTicking.value = false
                    return
                }
            }
        }
        timer.start()
    }

    fun tick(value: Boolean) {
        _isTicking.value = value
    }

    private fun calories(userRepId: String) {
        val nowString = SimpleDateFormat(
            "dd-MM-yyyy",
            Locale.UK
        ).format(java.util.Date(System.currentTimeMillis()))
        viewModelScope.launch {
            dieterRepository.caloriesBurned(userRepId, nowString).collect {
                _calories.value = it
                Log.d(TAG, "calories: $it")
            }
        }
    }

    private fun userRepId() = DieterApplication.applicationContext()?.dataStore?.data
        ?.map { preferences ->
            preferences[userRepKey]
        }

    companion object {
        private val TAG = WorkoutViewModel::class.java.simpleName
    }
}
