package com.gpslab.kaun.view


import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.gpslab.kaun.R
import java.io.File

class SentImageHolder(context: Context, itemView: View) : BaseSentHolder(context, itemView) {

    var imgMsg: ImageView = itemView.findViewById(R.id.img_msg)

    override fun bind(message: Message, user: User) {
        super.bind(message, user)
        //if the image is not downloaded show thumb img
        if (message.localPath == null) {
            try {
                //                Bitmap bm = BitmapFactory.decodeFile(path);
//                val imgFile: File = File(message.thumb)
//                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
//                val orientedBitmap = ExifUtil.rotateBitmap(message.thumb, myBitmap)
//                imgMsg.setImageBitmap(orientedBitmap)
                Glide.with(context).load(message.thumb).into(imgMsg)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (!File(message.localPath).exists()) {
            // if image deleted from device then show the blurred thumbnail
            try {
//                val imgFile: File = File(message.thumb)
//                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
//                val orientedBitmap = ExifUtil.rotateBitmap(message.thumb, myBitmap)
//                imgMsg.setImageBitmap(orientedBitmap)
                Glide.with(context).load(message.thumb).into(imgMsg)
//                imgMsg.setImageBitmap()
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