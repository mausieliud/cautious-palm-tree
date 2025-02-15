package com.example.eliud2

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var timeProgressBar: ProgressBar
    private lateinit var dayMonthDate: TextView
    private lateinit var scrollShow: ImageView
    private lateinit var progressText: TextView // Added progressText
    private val handler = Handler(Looper.getMainLooper())

    private val images = arrayOf(
        R.drawable.em1, R.drawable.em2, R.drawable.em3, R.drawable.em4,
        R.drawable.em5, R.drawable.em6, R.drawable.em7, R.drawable.em8,
        R.drawable.em9, R.drawable.em10, R.drawable.em11, R.drawable.em12,
        R.drawable.em13, R.drawable.em14, R.drawable.em15, R.drawable.em16,
        R.drawable.em17, R.drawable.em18, R.drawable.em19
    )
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        timeProgressBar = findViewById(R.id.progressBar2)
        dayMonthDate = findViewById(R.id.dayMonthDate)
        scrollShow = findViewById(R.id.scrollShow)
        progressText = findViewById(R.id.progressText) // Initialize progressText

        updateProgress()
        updateDayProgress()
        displayDate()
        startSlideshow()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun updateProgress() {
        handler.post(object : Runnable {
            override fun run() {
                val calendar = Calendar.getInstance()
                val minutesPassed = (calendar.get(Calendar.HOUR_OF_DAY) * 60) + calendar.get(Calendar.MINUTE)
                timeProgressBar.progress = minutesPassed
                updateDayProgress() // Update progressText with each tick
                handler.postDelayed(this, 60000)
            }
        })
    }

    private fun updateDayProgress() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        val currentSecond = calendar.get(Calendar.SECOND)

        val secondsPassed = (currentHour * 3600) + (currentMinute * 60) + currentSecond
        val totalSecondsInDay = 24 * 3600

        val progressPercentage = (secondsPassed.toDouble() / totalSecondsInDay) * 100

        progressText.text = "Day Progress: %.2f%%".format(progressPercentage)
    }

    private fun displayDate() {
        val calendar = Calendar.getInstance()
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val suffix = getDaySuffix(dayOfMonth)
        val formattedDate = "${dayFormat.format(calendar.time)} ${monthFormat.format(calendar.time)} $dayOfMonth$suffix"

        dayMonthDate.text = formattedDate
    }

    private fun getDaySuffix(day: Int): String {
        return when {
            day in 11..13 -> "th"
            day % 10 == 1 -> "st"
            day % 10 == 2 -> "nd"
            day % 10 == 3 -> "rd"
            else -> "th"
        }
    }

    private fun startSlideshow() {
        handler.post(object : Runnable {
            override fun run() {
                scrollShow.setImageResource(images[currentIndex])
                currentIndex = (currentIndex + 1) % images.size
                handler.postDelayed(this, 10000)
            }
        })
    }
}
