/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.demo.shortform

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import androidx.media3.demo.shortform.viewpager.ViewPagerActivity
import java.lang.Integer.max
import java.lang.Integer.min

class MainActivity : AppCompatActivity() {

    @androidx.annotation.OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var numberOfPlayers = 3
        val numPlayersFieldView = findViewById<EditText>(R.id.num_players_field)
        numPlayersFieldView.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) =
                    Unit

                override fun afterTextChanged(s: Editable) {
                    val newText = numPlayersFieldView.text.toString()
                    if (newText != "") {
                        numberOfPlayers = max(1, min(newText.toInt(), 5))
                    }
                }
            }
        )

        findViewById<View>(R.id.view_pager_button).setOnClickListener {
            startActivity(
                Intent(this, ViewPagerActivity::class.java).putExtra(
                    NUM_PLAYERS_EXTRA,
                    numberOfPlayers
                )
            )
        }
    }

    companion object {
        const val NUM_PLAYERS_EXTRA = "number_of_players"
    }
}
