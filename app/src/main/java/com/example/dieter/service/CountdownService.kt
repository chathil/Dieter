package com.example.dieter.service

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.dieter.R
import com.example.dieter.data.source.DieterRepository
import com.example.dieter.data.source.domain.ActiveCounterModel
import com.example.dieter.data.source.domain.SaveWorkoutModel
import com.example.dieter.data.source.domain.WorkoutModel
import com.example.dieter.vo.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class CountdownService : Service() {

    @Inject
    lateinit var dieterRepository: DieterRepository

    val bi = Intent(COUNTDOWN_BR)
    var timer: CountDownTimer? = null
    var todos = emptyMap<WorkoutModel, Int>()
    var userRepId = ""
    lateinit var notificationManagerCompat: NotificationManagerCompat
    lateinit var builder: NotificationCompat.Builder

    override fun onCreate() {
        super.onCreate()
        notificationManagerCompat = NotificationManagerCompat.from(this)
        builder = NotificationCompat.Builder(this, COUNTDOWN_BR)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        todos = intent?.extras?.get("todos") as Map<WorkoutModel, Int>
        userRepId = intent.extras?.getString("user_rep_id") ?: ""

        startQueue()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startQueue() {
        val timeInMillis = todos.map { it.key to (it.value * 60 * 1000L) }.toMap()
        if (!timeInMillis.isNullOrEmpty()) {
            timer(timeInMillis.iterator())
        }
    }

    private fun timer(
        iterable:
            Iterator<Map.Entry<WorkoutModel, Long>>
    ) {
        val item = iterable.next()
        builder.setOnlyAlertOnce(true)
        timer = object : CountDownTimer(item.value, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                bi.putExtra(
                    "active_counter",
                    ActiveCounterModel(millisUntilFinished, item.value, item.key)
                )
                sendBroadcast(bi)
                builder.setOngoing(true)
                builder.setContentTitle("Your doing it!")
                builder.setContentText(
                    "${item.key.name.capitalize(Locale.ROOT)}: ${
                    toSecond(
                        millisUntilFinished
                    )
                    } "
                )
                notificationManagerCompat.notify(COUNTDOWN_BR_COUNTER, builder.build())
            }

            override fun onFinish() {
                if (iterable.hasNext()) {
                    timer(iterable)
                } else {
                    builder.setOngoing(false)
                    builder.setOnlyAlertOnce(false)
                    saveBurnedCalories()
                    notificationManagerCompat.cancel(COUNTDOWN_BR_COUNTER)
                    return
                }
            }
        }
        timer?.start()
    }

    private fun saveBurnedCalories() {
        var burned = 0
        todos.forEach { (k, v) ->
            burned += v * k.calorieBurnedPerMinute
        }
        val workouts = todos.map { (t, u) ->
            SaveWorkoutModel.WorkoutModel(
                t.id,
                t.name,
                t.calorieBurnedPerMinute,
                u * 60 * 1000,
                u * 60 * 1000
            )
        }
        val save = SaveWorkoutModel(workouts, burned)

        CoroutineScope(Dispatchers.IO).launch {
            if (userRepId.isNotEmpty()) {
                dieterRepository.saveWorkouts(userRepId, save).collect {
                    when (it) {
                        is DataState.Success -> {
                            bi.putExtra("is_done", true)
                            sendBroadcast(bi)
                            builder.setContentTitle("You did it!")
                                .setContentText("you burned $burned kcal")
                            notificationManagerCompat.notify(COUNTDOWN_BR_SAVE, builder.build())
                        }
                        is DataState.Error -> {

                            builder.setContentTitle("Error saving workouts")
                                .setContentText("hmm, sorry :)")
                            notificationManagerCompat.notify(COUNTDOWN_BR_SAVE, builder.build())
                        }
                        is DataState.Loading -> {
                            // show loading notif
                            builder.setContentTitle("Saving workouts")
                                .setContentText("Saving your workouts")
                            notificationManagerCompat.notify(COUNTDOWN_BR_SAVE, builder.build())
                        }
                        is DataState.Empty -> {
                        }
                    }
                }
            } else
                Log.e(TAG, "saveBurnedCalories: Missing user id")
        }
    }

    private fun toSecond(time: Long): String {
        return String.format(
            "%d:%d",
            TimeUnit.MILLISECONDS.toMinutes(time),
            TimeUnit.MILLISECONDS.toSeconds(time) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
        )
    }

    override fun onDestroy() {
        timer?.cancel()
        Log.i(TAG, "onDestroy: Stop timer")
        notificationManagerCompat.cancel(COUNTDOWN_BR_COUNTER)
        super.onDestroy()
    }

    companion object {
        private val TAG = CountdownService::class.java.simpleName
        const val COUNTDOWN_BR = "com.example.dieter.countdown_br"
        const val COUNTDOWN_BR_SAVE = 1001
        const val COUNTDOWN_BR_COUNTER = 1002
    }
}
