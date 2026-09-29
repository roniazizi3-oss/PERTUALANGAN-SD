package com.example.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity

object NotificationHelper {

    const val CHANNEL_DAILY_REMINDER = "channel_daily_reminder"
    const val CHANNEL_CLASS_SCHEDULE = "channel_class_schedule"

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val dailyChannel = NotificationChannel(
                CHANNEL_DAILY_REMINDER,
                "Pengingat Belajar Harian SD",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifikasi pengingat latihan kuis dan petualangan harian"
                enableVibration(true)
            }

            val classChannel = NotificationChannel(
                CHANNEL_CLASS_SCHEDULE,
                "Jadwal Kelas & Tugas Sekolah",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Pengingat jadwal kelas virtual dan sinkronisasi tugas sekolah"
                enableVibration(true)
            }

            notificationManager.createNotificationChannel(dailyChannel)
            notificationManager.createNotificationChannel(classChannel)
        }
    }

    fun sendDailyStudyReminder(context: Context, studentName: String = "Juara SD") {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            101,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_DAILY_REMINDER)
            .setSmallIcon(android.R.drawable.star_big_on)
            .setContentTitle("Waktunya Berpetualang, $studentName! ⭐")
            .setContentText("Klaim bonus koin harianmu dan lanjutkan petualangan matematika seru hari ini!")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Halo $studentName! Jaga streak 5 harimu agar tidak putus. Ayo taklukkan 1 level tantangan sains & dapatkan lencana baru!")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(1001, notification)
    }

    fun sendClassScheduleReminder(context: Context, courseName: String, timeText: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            102,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_CLASS_SCHEDULE)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("🔔 Jadwal Kelas: $courseName")
            .setContentText("Kelas akan dimulai $timeText. Siapkan buku catatan dan alat tulismu!")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Pemberitahuan dari guru: Kelas $courseName akan dimulai $timeText. Pastikan koneksi atau modul belajar sudah siap.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(1002, notification)
    }

    fun sendTaskSyncSuccess(context: Context, taskTitle: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_CLASS_SCHEDULE)
            .setSmallIcon(android.R.drawable.checkbox_on_background)
            .setContentTitle("Google Classroom Tersinkronisasi ✅")
            .setContentText("Tugas '$taskTitle' berhasil disinkronkan & nilai otomatis terkirim.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(1003, notification)
    }
}
