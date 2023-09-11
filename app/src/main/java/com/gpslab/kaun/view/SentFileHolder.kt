package com.gpslab.kaun.view

import android.content.Context
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.github.abdularis.buttonprogress.DownloadButtonProgress
import com.gpslab.kaun.R

class SentFileHolder(context: Context, itemView: View) : BaseSentHolder(context, itemView) {

    private val tvFileSize: TextView = itemView.findViewById(R.id.tv_file_size)
    var progressbar: DownloadButtonProgress = itemView.findViewById(R.id.progress_button)
    private val tvFileName: TextView = itemView.findViewById(R.id.tv_file_name)
    private val tvFileExtension: TextView = itemView.findViewById(R.id.tv_file_extension)
    private var progresscheckclick: Boolean = false
    var progress_in_persentage: Int = 0
    private val fileIcon: ImageView = itemView.findViewById(R.id.file_icon)
    override fun bind(message: Message, user: User) {
        super.bind(message, user)
        val fileExtension = Util.getFileExtensionFromPath(message.metadata).toUpperCase()
        tvFileExtension.text = fileExtension
        //set file name
        tvFileName.text = message.metadata

        //file size
        tvFileSize.text = message.fileSize









        progressbar.setOnClickListener {
            val sharedPre: SharedPreferences = context.getSharedPreferences("ChatData", 0)
            val edit = sharedPre.edit()
            progresscheckclick = sharedPre.getBoolean("check_progress_doc", false)
            Log.d("PlayButtonclickFile", "progresscheckclick 2   = " + progresscheckclick)
            if (progresscheckclick) {
                Log.d("PlayButtonclickFile", "progresscheckclick 4   = " + progresscheckclick)
                progresscheckclick = false
                edit.putBoolean("check_progress_doc", false)
                edit.putBoolean("check_pro_doc", false)
                edit.putInt("pause_and_resume_doc", 2)
                edit.apply()
                progressbar.setDeterminate()

            } else {
                Log.d("PlayButtonclickFile", "progresscheckclick 3   = " + progresscheckclick)

                progresscheckclick = true
                progressbar.setIdle()
                edit.putInt("pause_and_resume_doc", 1)
                edit.putBoolean("check_progress_doc", true)
                edit.putBoolean("check_pro_doc", true)
                edit.apply()

            }


        }
        object : CountDownTimer(5000, 2000) {
            override fun onTick(millisUntilFinished: Long) {
                val sharedPre: SharedPreferences = context.getSharedPreferences("ChatData", 0)
                val edit = sharedPre.edit()

                progress_in_persentage = sharedPre.getInt("progress_in_per", 100)
//                Log.d("PlayButtonclick", "CountDownTimer check_pro 3   = " + progress_in_persentage)
                Log.d("PlayButtonclickFile", "progress_in_per  11    =     "+progress_in_persentage)
                Log.d("PlayButtonclickFile", "CountDownTimer check_pro 3   = " + sharedPre.getBoolean("check_pro_doc", false))
                Log.d("PlayButtonclickFile", "CountDownTimer check_progress 3   = " + sharedPre.getBoolean("check_progress_doc", false))
                if(progress_in_persentage == 100){
                    edit.putBoolean("check_progress_doc", false)
                    edit.apply()
                    progressbar.setIdle()
                    progresscheckclick = true

//                    Log.d("PlayButtonclick", "CountDownTimer progress_in_persentage 2   = " + progress_in_persentage)

                }else {
                    if(!sharedPre.getBoolean("check_pro_doc", false)){
//                        if(!sharedPre.getBoolean("check_progress",false)){
                        Log.d("PlayButtonclickFile", "CountDownTimer progress_in_persentage 4   = " + progress_in_persentage)
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
       fileIcon.setHidden(message.downloadUploadStat != DownloadUploadStat.SUCCESS, true)

    }


}