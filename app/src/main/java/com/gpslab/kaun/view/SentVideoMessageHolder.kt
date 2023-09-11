package com.gpslab.kaun.view

import android.R.attr.path
import android.content.Context
import android.content.SharedPreferences
import android.media.ThumbnailUtils
import android.os.CountDownTimer
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.github.abdularis.buttonprogress.DownloadButtonProgress
import com.gpslab.kaun.R


class SentVideoMessageHolder(context: Context, itemView: View) : BaseSentHolder(context, itemView) {

    private val progressbar: DownloadButtonProgress = itemView.findViewById(R.id.progress_button)
    private val thumbImg: ImageView = itemView.findViewById(R.id.thumb_img)
    private val btnPlayVideo: ImageButton = itemView.findViewById(R.id.btn_play_video)
    private var progresscheckclick: Boolean = false
    private val tvMediaDuration: TextView = itemView.findViewById(R.id.tv_media_duration)
    var progress_in_persentage: Int = 0
    override fun bind(message: Message, user: User) {
        super.bind(message, user)
        if (message.downloadUploadStat != DownloadUploadStat.SUCCESS) {
            tvMediaDuration.visibility = View.GONE
//            progressbar.visibility = View.VISIBLE
        } else {
            progressbar.visibility = View.GONE
            tvMediaDuration.visibility = View.VISIBLE
            tvMediaDuration.text = message.mediaDuration
        }

        Log.d("createVideoMessage", "message.videoThumb ==  " + message.videoThumb)
//        val thumb = ThumbnailUtils.createVideoThumbnail(message.localPath,
//                MediaStore.Images.Thumbnails.MINI_KIND)
//        thumbImg.setImageBitmap(thumb)
        Glide.with(context).load(message.localPath).into(thumbImg)

        btnPlayVideo?.setOnClickListener { interaction?.onContainerViewClick(adapterPosition, itemView, message) }




        progressbar.setOnClickListener {


//            progressbar.setCurrentProgress(100)
//            progressbar.setFinish()
            val sharedPre: SharedPreferences = context.getSharedPreferences("ChatData", 0)
            val edit = sharedPre.edit()
            progresscheckclick = sharedPre.getBoolean("check_progress_video", false)
            Log.d("PlayButtonclickVideo", "progresscheckclick 2   = " + progresscheckclick)
            if(progresscheckclick){
                Log.d("PlayButtonclickVideo", "progresscheckclick 4   = " + progresscheckclick)
                progresscheckclick = false
                edit.putBoolean("check_progress_video", false)
                edit.putBoolean("check_pro_video", false)
                edit.putInt("pause_and_resume_video", 2)
                edit.apply()
                progressbar.setDeterminate()

            }else {
                Log.d("PlayButtonclickVideo", "progresscheckclick 3   = " + progresscheckclick)

                progresscheckclick = true
                progressbar.setIdle()
                edit.putInt("pause_and_resume_video", 1)
                edit.putBoolean("check_progress_video", true)
                edit.putBoolean("check_pro_video", true)
                edit.apply()

            }


        }



        object : CountDownTimer(5000, 2000) {
            override fun onTick(millisUntilFinished: Long) {
                val sharedPre: SharedPreferences = context.getSharedPreferences("ChatData", 0)
                val edit = sharedPre.edit()

                progress_in_persentage = sharedPre.getInt("progress_in_per", 100)
                Log.d("PlayButtonclick", "CountDownTimer check_pro 3   = " + progress_in_persentage)
                Log.d("PlayButtonclickVideo", "CountDownTimer check_pro 3   = " + sharedPre.getBoolean("check_pro_video", false))
                Log.d("PlayButtonclickVideo", "CountDownTimer check_progress 3   = " + sharedPre.getBoolean("check_progress_video", false))
                if(progress_in_persentage == 100){
                    edit.putBoolean("check_progress_video", false)
                    edit.apply()
                    progressbar.visibility = View.GONE
                    progresscheckclick = true

//                    Log.d("PlayButtonclick", "CountDownTimer progress_in_persentage 2   = " + progress_in_persentage)

                }else {
                    if(!sharedPre.getBoolean("check_pro_video", false)){
//                        if(!sharedPre.getBoolean("check_progress",false)){
                        Log.d("PlayButtonclickVideo", "CountDownTimer progress_in_persentage 4   = " + progress_in_persentage)
                        progressbar.setIdle()
//                        }
                    }else {
//                        Log.d("PlayButtonclick", "CountDownTimer progress_in_persentage 1  = " + progress_in_persentage)
                        progressbar.setDeterminate()
                        progressbar.setCurrentProgress(progress_in_persentage)
                    }
                }
            }

            override fun onFinish() {

            }
        }.start()

    }


}