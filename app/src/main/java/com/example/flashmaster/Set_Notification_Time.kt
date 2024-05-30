package com.example.flashmaster

import com.example.flashmaster.databinding.ActivitySetNotificationTimeBinding
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import java.util.Calendar
import java.util.Date


class Set_Notification_Time : AppCompatActivity() {
    private lateinit var binding: ActivitySetNotificationTimeBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetNotificationTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.extras?.getString("user") ?: "No message found"
        val refId = intent.extras?.getString("refid") ?: "No message found"
        val category = intent.extras?.getString("category") ?: "No message found"

        createNotificationChannel()
        binding.setNotification.setOnClickListener {
            // Check if notification permissions are granted
            if (checkNotificationPermissions(this)) {
                // Schedule a notification
                scheduleNotification()
            }
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification() {
        // Create an intent for the Notification BroadcastReceiver
        val intent = Intent(applicationContext, Notification::class.java)
        // Extract title and message from user input
        val title = binding.notificationTitle.text.toString()
        val message = binding.message.text.toString()
        // Add title and message as extras to the intent
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)
        // Create a PendingIntent for the broadcast
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        // Get the AlarmManager service
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // Get the selected time and schedule the notification
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        // Show an alert dialog with information
        // about the scheduled notification
        showAlert(time, title, message)
    }

    private fun showAlert(time: Long, title: String, message: String) {
        // Format the time for display
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)
        // Create and show an alert dialog with notification details
        AlertDialog.Builder(this)
            .setIcon(R.drawable.logo)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: $title\nMessage: $message\nAt: ${dateFormat.format(date)} ${
                    timeFormat.format(
                        date
                    )
                }"
            )
            .setPositiveButton("Okay") { _, _ ->
                val email = intent.extras?.getString("user") ?: "No message found"
                val refId = intent.extras?.getString("refid") ?: "No message found"
                val category = intent.extras?.getString("category") ?: "No message found"
                val i = Intent(this, Add_FlashCard::class.java)
                i.putExtra("user", email)
                i.putExtra("refid", refId)
                i.putExtra("category", category)
                startActivity(i)
            }
            .show()
    }

    private fun getTime(): Long {
        // Get selected time from TimePicker and DatePicker
        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year
        // Create a Calendar instance and set the selected date and time
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        // Create a notification channel for devices running
        // Android Oreo (API level 26) and above
        val name = "Notify Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        // Get the NotificationManager service and create the channel
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun checkNotificationPermissions(context: Context): Boolean {
        // Check if notification permissions are granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val isEnabled = notificationManager.areNotificationsEnabled()
            if (!isEnabled) {
                // Open the app notification settings if notifications are not enabled
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                context.startActivity(intent)
                return false
            }
        } else {
            val areEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()
            if (!areEnabled) {
                // Open the app notification settings if notifications are not enabled
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                context.startActivity(intent)
                return false
            }
        }
        // Permissions are granted
        return true
    }
}

