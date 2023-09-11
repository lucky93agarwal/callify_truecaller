package com.gpslab.kaun.view

import com.gpslab.kaun.R;

import ak.sh.ay.musicwave.MusicWave
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import pl.droidsonroids.gif.GifImageView

class ReceivedAudioHolder(context: Context, itemView: View) : BaseReceivedHolder(context, itemView), AudibleBase {
    val waveView: MusicWave = itemView.findViewById(R.id.wave_view)
    val playBtn: ImageView = itemView.findViewById(R.id.voice_play_btn)

    var pauseBtn: ImageView = itemView.findViewById(R.id.voice_puse_btn)
    val seekBar: SeekBar = itemView.findViewById(R.id.voice_seekbar)
    private val tvAudioSize: TextView = itemView.findViewById(R.id.tv_audio_size)
    val tvDuration: TextView = itemView.findViewById(R.id.tv_duration)
    val imgHeadset: ImageView = itemView.findViewById(R.id.img_headset)
    var gifimg: GifImageView = itemView.findViewById(R.id.wave_viewgif)


    private lateinit var runnable: Runnable
    var playcheck: Boolean = false
    private var handler: Handler = Handler()
    private lateinit var mediaPlayers: MediaPlayer

    private var pause: Boolean = false
    private var check_pause: Boolean = false


    private var checkclick: Boolean = false

    override var audibleState: LiveData<Map<String, AudibleState>>? = null
    override var audibleInteraction: AudibleInteraction? = null

    val MediaPlayer.seconds: Int
        get() {
            return this.duration / 1000
        }

    // Creating an extension property to get media player current position in seconds
    val MediaPlayer.currentSeconds: Int
        get() {
            return this.currentPosition / 1000
        }

    override fun bind(message: Message, user: User) {
        super.bind(message, user)


        //Set Initial Values
        seekBar.progress = 0
        playBtn.setImageResource(AdapterHelper.getPlayIcon(false))

        //if it's sending set the audio size
//        if (message.downloadUploadStat != DownloadUploadStat.SUCCESS) {
            tvAudioSize.visibility = View.VISIBLE
            tvAudioSize.text = message.metadata
//        } else {
//            //otherwise hide the audio textview
//            tvAudioSize.visibility = View.GONE
//        }

        tvDuration.text = message.mediaDuration

//        playBtn.setHidden(message.downloadUploadStat != SUCCESS, true)
        lifecycleOwner?.let {


            audibleState?.observe(it, Observer { audioRecyclerStateMap ->
                if (audioRecyclerStateMap.containsKey(message.messageId)) {
                    audioRecyclerStateMap[message.messageId]?.let { mAudioRecyclerState ->


                        if (mAudioRecyclerState.currentDuration != null)
                            tvDuration.text = mAudioRecyclerState.currentDuration

                        if (mAudioRecyclerState.progress != -1) {
                            seekBar.progress = mAudioRecyclerState.progress
                        }

                        if (mAudioRecyclerState.max != -1) {
                            val max = mAudioRecyclerState.max
                            seekBar.max = max
                        }

                        if (mAudioRecyclerState.isPlaying) {
                            imgHeadset.visibility = View.GONE
                            waveView.visibility = View.VISIBLE
                        } else {
                            imgHeadset.visibility = View.VISIBLE
                            waveView.visibility = View.GONE
                        }

                        if (mAudioRecyclerState.waves != null)
                            waveView.updateVisualizer(mAudioRecyclerState.waves)

                        playBtn.setImageResource(AdapterHelper.getPlayIcon(mAudioRecyclerState.isPlaying))

                    }
                } else {
                    tvDuration.text = message.mediaDuration
                    imgHeadset.visibility = View.VISIBLE
                    waveView.visibility = View.GONE
                }
            })
        }
        pauseBtn.setOnClickListener {
            if (mediaPlayers.isPlaying) {
                mediaPlayers.pause()

                imgHeadset.visibility = View.VISIBLE
                gifimg.visibility = View.GONE
                pauseBtn.visibility = View.GONE
                pause = true

            }
        }
        playBtn.setOnClickListener {
            val myUri: Uri = Uri.parse(message.localPath) // initialize Uri here
            if (pause) {
                mediaPlayers.seekTo(mediaPlayers.currentPosition)
                mediaPlayers.start()
                pause = false
                imgHeadset.visibility = View.VISIBLE
                gifimg.visibility = View.GONE
            } else {

                if (checkclick) {
//                    message.fromPhone ="1"
                    mediaPlayers.stop()
                    mediaPlayers.release()

                    mediaPlayers = MediaPlayer.create(context, myUri)
                    mediaPlayers.start()
                    Log.d("PlayButtonclick", "seekBar 2   = " + check_pause)
                    imgHeadset.visibility = View.GONE
                    gifimg.visibility = View.VISIBLE
                } else {
                    Log.d("PlayButtonclick", "seekBar 0   = " + check_pause)
                    checkclick = true
                    Log.d("PlayButtonclick", "seekBar 1   = " + check_pause)

                    mediaPlayers = MediaPlayer.create(context, myUri)
                    mediaPlayers.start()

                    imgHeadset.visibility = View.GONE
                    gifimg.visibility = View.VISIBLE
                }


            }

            pauseBtn.visibility = View.VISIBLE
            mediaPlayers.setOnCompletionListener {

//                mediaPlayers.release()
                seekBar.progress = 0
                imgHeadset.visibility = View.VISIBLE
                gifimg.visibility = View.GONE
                pauseBtn.visibility = View.GONE
                Log.d("PlayButtonclick","new onComplete")

            }
            seekBar.max = mediaPlayers.seconds

            runnable = Runnable {
                seekBar.progress = mediaPlayers.currentSeconds




                handler.postDelayed(runnable, 1000)
            }
            handler.postDelayed(runnable, 1000)

        }


        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                if (fromUser)
//                    audibleInteraction?.onSeek(message, progress, seekBar.max)
                if (fromUser) {
                    Log.d("PlayButtonclick","onComplete")
                    mediaPlayers.seekTo(progress * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })


    }

}
