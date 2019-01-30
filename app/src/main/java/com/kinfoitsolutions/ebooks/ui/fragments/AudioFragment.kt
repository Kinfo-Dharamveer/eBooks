package com.kinfoitsolutions.ebooks.ui.fragments


import android.media.AudioManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.kinfoitsolutions.ebooks.R
import android.media.MediaPlayer
import android.net.Uri
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_audio.view.*
import java.io.IOException
import java.util.*




class AudioFragment : Fragment() {

    private lateinit var viewOfLayout: View

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_audio, container, false)


        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer = MediaPlayer()

        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                viewOfLayout.seek.setProgress(mediaPlayer.currentPosition)
            }
        }, 0, 1000)
        viewOfLayout.playbutton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val mediaPath = Uri.parse("android.resource://" + activity!!.packageName + "/" + R.raw.haha)

                try {
                    mediaPlayer.stop()
                    mediaPlayer.reset()
                    Toast.makeText(context, "Again Playing", Toast.LENGTH_LONG).show()
                    mediaPlayer.setDataSource(context, mediaPath)
                    mediaPlayer.prepare()

                    mediaPlayer.start()

                } catch (e: IOException) {
                    e.printStackTrace()
                }


            }
        })

        viewOfLayout.seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewOfLayout.seekTime.setVisibility(View.VISIBLE)
                val x = Math.ceil((progress / 1000f).toDouble()).toInt()

                if (x < 10)
                    viewOfLayout.seekTime.setText("0:0$x")
                else
                    viewOfLayout.seekTime.setText("0:$x")

                val percent = progress / seekBar.max.toDouble()
                val offset = seekBar.thumbOffset
                val seekWidth = seekBar.width

                val `val` = Math.round(percent * (seekWidth - 2 * offset))

                val labelWidth = viewOfLayout.seekTime.getWidth()


                viewOfLayout.seekTime.setX(
                    offset.toFloat() + seekBar.x + `val`.toFloat()
                            - Math.round(percent * offset).toFloat()
                            - Math.round(percent * labelWidth / 2).toFloat()
                )

                if (progress > 0 && mediaPlayer != null && !mediaPlayer.isPlaying) {
                    //clearMediaPlayer();
                    viewOfLayout.playbutton.setImageDrawable(
                        ContextCompat.getDrawable(
                            context!!,
                            android.R.drawable.ic_media_play
                        )
                    )
                    viewOfLayout.seek.setProgress(0)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                viewOfLayout.seekTime.setVisibility(View.VISIBLE)

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (mediaPlayer != null && mediaPlayer.isPlaying) {
                    mediaPlayer.seekTo(seekBar.progress)
                }
            }
        })



        return viewOfLayout
    }


}


