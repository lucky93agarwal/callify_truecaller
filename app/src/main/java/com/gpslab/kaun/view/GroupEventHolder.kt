package com.gpslab.kaun.view
import com.gpslab.kaun.R;
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GroupEventHolder(context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvGroupEvent: TextView = itemView.findViewById(R.id.tv_group_event)

    fun bind(message: Message,user: User){
        tvGroupEvent.text = GroupEvent.extractString(message.content, user.group.users)
    }


}