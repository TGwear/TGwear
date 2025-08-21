/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.demo.shortform.viewpager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.demo.shortform.MainActivity
import androidx.media3.demo.shortform.MediaItemDatabase
import androidx.media3.demo.shortform.R
import androidx.viewpager2.widget.ViewPager2

class ViewPagerActivity : AppCompatActivity() {
    private lateinit var viewPagerView: ViewPager2
    private lateinit var onPageChangeCallback: ViewPager2.OnPageChangeCallback
    private var numberOfPlayers = 3
    private var mediaItemDatabase = MediaItemDatabase()

    companion object {
        private const val TAG = "ViewPagerActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)
        numberOfPlayers = intent.getIntExtra(MainActivity.NUM_PLAYERS_EXTRA, numberOfPlayers)
        Log.d(TAG, "Using a pool of $numberOfPlayers players")
        viewPagerView = findViewById(R.id.viewPager)
        viewPagerView.offscreenPageLimit = 1
    }

    override fun onStart() {
        super.onStart()
        val adapter = ViewPagerMediaAdapter(mediaItemDatabase, numberOfPlayers, applicationContext)
        viewPagerView.adapter = adapter
        onPageChangeCallback =
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    adapter.onPageSelected(position)
                }
            }
        viewPagerView.registerOnPageChangeCallback(onPageChangeCallback)
    }

    override fun onStop() {
        viewPagerView.unregisterOnPageChangeCallback(onPageChangeCallback)
        viewPagerView.adapter = null
        super.onStop()
    }
}
