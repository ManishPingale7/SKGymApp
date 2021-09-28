package com.example.skgym.utils

import android.content.Context
import android.graphics.Color.green
import android.view.View
import android.view.animation.Animation
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.skgym.R

class ProgressBtn(context: Context, view: View) {
    private var cardView: CardView = view.findViewById(R.id.cardViewLayout)
    private var layout: ConstraintLayout = view.findViewById(R.id.constraintLayBtn)
    private var progressBar: ProgressBar = view.findViewById(R.id.progressBar)
    private var textView: TextView = view.findViewById(R.id.textView)

    lateinit var fade_in: Animation


    fun buttonActivated(){
        progressBar.visibility=View.VISIBLE
        textView.text = "Please Wait"
    }


    fun buttonfinished(){
        layout.setBackgroundColor(cardView.resources.getColor(R.color.green))
        progressBar.visibility= View.GONE
        textView.text = "Done"
    }


}