package com.gpslab.kaun.status

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.gpslab.kaun.R
import com.gpslab.kaun.view.IntentUtils
import com.gpslab.kaun.view.TextStatus
import kotlinx.android.synthetic.main.activity_text_status.*

class TextStatusActivity : AppCompatActivity() {
    private lateinit var fontsNames: Array<String>
    private lateinit var colors: Array<String>
    private lateinit var emojiPopup: EditText

    var currentFontIndex = 0
    var currentBackgroundIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_status)


        initFontsNames()
        setInitialTypeFace()
        colors = resources.getStringArray(R.array.status_bg_colors)

        //set initial background randomly
        val randomColorIndex = colors.indexOf(colors.random())
        currentBackgroundIndex = randomColorIndex
        root.setBackgroundColor(Color.parseColor(colors[currentBackgroundIndex]))


//        btn_emoji.setOnClickListener {
//            emojiPopup.toggle()
//        }

        tv_font.setOnClickListener {
            changeTypeFace()
        }
        btn_background.setOnClickListener {
            changeBackground()
        }

        fab_send.setOnClickListener {
            val textStatus = TextStatus("", et_status.text.toString(), fontsNames[currentFontIndex], colors[currentBackgroundIndex])
            val data = Intent().putExtra(IntentUtils.EXTRA_TEXT_STATUS, textStatus)
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }




    private fun changeTypeFace() {
        if (currentFontIndex + 1 > fontsNames.lastIndex) currentFontIndex = 0 else currentFontIndex++
        val typeface = Typeface.createFromAsset(assets, "fonts/${fontsNames[currentFontIndex]}")
        tv_font.typeface = typeface
        et_status.typeface = typeface
    }

    private fun setInitialTypeFace() {
        if (fontsNames.isEmpty()) return
        val typeface = Typeface.createFromAsset(assets, "fonts/${fontsNames[0]}")
        tv_font.typeface = typeface
        et_status.typeface = typeface
    }

    private fun changeBackground() {
        if (currentBackgroundIndex + 1 > colors.lastIndex) currentBackgroundIndex = 0 else currentBackgroundIndex++
        root.setBackgroundColor(Color.parseColor(colors[currentBackgroundIndex]))
    }

    private fun initFontsNames() {
        fontsNames = assets.list("fonts") as Array<String>
    }

}