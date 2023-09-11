package com.gpslab.kaun.view

import com.gpslab.kaun.R;

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

internal class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var header: TextView = itemView.findViewById<View>(R.id.tv_day) as TextView

}