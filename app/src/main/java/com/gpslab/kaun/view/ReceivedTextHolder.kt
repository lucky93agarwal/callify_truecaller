package com.gpslab.kaun.view

import android.content.Context
import android.view.View
import com.gpslab.kaun.R
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView

class ReceivedTextHolder(context: Context, itemView: View) : BaseReceivedHolder(context,itemView) {

    private var tvMessageContent: EmojiconTextView = itemView.findViewById(R.id.tv_message_content)

    override fun bind(message: Message,user: User) {
        super.bind(message,user)
        tvMessageContent.text = message.content
    }


}