package com.syscode.aeudio.ui.player

import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import com.syscode.aeudio.R
import com.syscode.aeudio.internal.ScopedActivity
import com.syscode.aeudio.ui.main.music.MusicViewModel
import com.syscode.aeudio.ui.main.music.MusicViewModelFactory
import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


class PlayerActivity : ScopedActivity(), KodeinAware {
    override val kodein by closestKodein()

    private val musicViewModelFactory: MusicViewModelFactory by instance()
    private lateinit var musicViewModel: MusicViewModel

    private lateinit var title: String
    private lateinit var path: String

    private val mediaPlayer = MediaPlayer()
    private val mediaMetadata = MediaMetadataRetriever()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        title = intent.getStringExtra("musicName")
        path = intent.getStringExtra("musicPath")
        player_music_name.text = title
        player_music_path.text = path
        val uri: Uri = Uri.parse(path)
        loadMetaData()
        mediaPlayer.apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(applicationContext, uri)
            this.setOnPreparedListener {
                player_music_path.text = "Loading..."
            }
            this.
            prepare()
//            prepareAsync()
            toggleMusic()
            start()
            run()
        }
        player_music_play.setOnClickListener {
            toggleMusic()
        }
        progressBar.max = mediaPlayer.duration
        progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isSeeking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isSeeking = false
                mediaPlayer.seekTo(progressBar.progress)
            }

        })
    }
    private var isSeeking = false
    private fun loadMetaData(){
        mediaMetadata.setDataSource(path)
        try {
            val art = mediaMetadata.embeddedPicture
            val songImage = BitmapFactory
                .decodeByteArray(art, 0, art.size)
            player_music_art.setImageBitmap(songImage)
            player_music_name.text = mediaMetadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            player_music_path.text = mediaMetadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        } catch (e: Exception){
            Log.e("meta data ", e.message, e)
        }
    }
    private fun toggleMusic(){
        mediaPlayer.apply {
            if (!this.isPlaying) {
                start()
                player_music_path.text = "Playing"
                run()
            }
            else {
                pause()
                player_music_path.text = "Paused"
            }
        }
    }
    private fun run() = launch(Dispatchers.IO) {
        while (mediaPlayer.isPlaying){
            if(!isSeeking) progressBar.progress = mediaPlayer.currentPosition
        }
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        mediaPlayer.release()
        super.onDestroy()
    }
}