package com.gpslab.kaun.view

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.gpslab.kaun.R
import java.io.File

class ReceivedImageHolder(context: Context, itemView: View) : BaseReceivedHolder(context,itemView) {

    var imgMsg: ImageView = itemView.findViewById(R.id.img_msg)


    override fun bind(message: Message,user: User) {
        super.bind(message,user)


        //if the image is not downloaded show thumb img
        if (message.localPath == null) {
            try {
                Glide.with(context).load(message.thumb).into(imgMsg)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (!File(message.localPath).exists()) {
            try {
                Glide.with(context).load(message.thumb).into(imgMsg)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            //these try catch exceptions because glide does not support set tag to an image view
            try {
                Glide.with(context).load(Uri.fromFile(File(message.localPath))).into(imgMsg)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
            ViewCompat.setTransitionName(imgMsg, message.messageId)
        }

    }


}

