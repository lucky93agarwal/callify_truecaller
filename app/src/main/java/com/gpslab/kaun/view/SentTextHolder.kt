package com.gpslab.kaun.view

import android.content.Context
import android.view.View
import com.gpslab.kaun.R
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView

class SentTextHolder(context: Context, itemView: View) : BaseSentHolder(context,itemView) {
    private var tvMessageContent: EmojiconTextView = itemView.findViewById(R.id.tv_message_content)

    override fun bind(message: Message, user: User) {
        super.bind(message,user)

//        val emojiInformation = EmojiUtils.emojiInformation(message.content)
//        val res: Int
//
//        res = if (emojiInformation.isOnlyEmojis && emojiInformation.emojis.size == 1) {
//            R.dimen.emoji_size_single_emoji
//        } else if (emojiInformation.isOnlyEmojis && emojiInformation.emojis.size > 1) {
//            R.dimen.emoji_size_only_emojis
//        } else {
//            R.dimen.emoji_size_default
//        }
//
//        tvMessageContent.setEmojiconSize(res)
        tvMessageContent.text = message.content
    }

}

