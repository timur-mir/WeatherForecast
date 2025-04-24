package ru.presentationpatterns.weatherforecast.presentation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import ru.presentationpatterns.weatherforecast.R
import java.util.Locale

class MusicService : Service() {
    private lateinit var mediaPlayer: MediaPlayer
    private val binder = MusicBinder()
    val CHANNEL_ID = "канал 1"
    val NOTIFICATION_ID = 1000

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d("MS", "onBind()");
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.weather_music)
        mediaPlayer.isLooping = true
    }

    fun startMusic(town: String, temp: Double,historyMode:Boolean) {
        if (!mediaPlayer.isPlaying) {
            if(historyMode){
                mediaPlayer.start()
                Log.d("MSM", "Start MS")
            }
         else {
                createNotification(town, temp)
                mediaPlayer.start()
                Log.d("MSM", "Start MS")
            }
        }
    }

    fun stopMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Music Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun createNotification(town: String, currentTempEx: Double) {
        var state = "0"
        if (currentTempEx > 0) {
            state = "+"
        } else if (currentTempEx < 0) {
            state = "-"
        } else
            state = "0"

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Погода в городе")
            .setContentText("${town.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }}, ${state}${currentTempEx}° С ")
            .setSmallIcon(R.drawable.city)
            .setAutoCancel(true)
            .build()

        val manager = getSystemService(NotificationManager::class.java)
        createNotificationChannel()
        manager.notify(NOTIFICATION_ID, notification)
    }

}