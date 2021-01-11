package com.syscode.aeudio.data.source

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.syscode.aeudio.data.model.AudioModel
import com.syscode.aeudio.data.model.ScanProgressModel
import java.io.File


class AudioDataSourceImpl(
    private val context: Context
): AudioDataSource {

    private val _localAudio = MutableLiveData<MutableList<AudioModel>>()
    override val localAudioModel: LiveData<MutableList<AudioModel>>
        get() = _localAudio

    private val _scanLocalAudioProgress = MutableLiveData<ScanProgressModel>()
    override val scanLocalAudioProgressModel: LiveData<ScanProgressModel>
        get() = _scanLocalAudioProgress

    override suspend fun scanLocalAudio() {
        val tempAudioModelList: MutableList<AudioModel> = ArrayList()

        val mainDirectory = File("storage/emulated/0/Music")
        val files = mainDirectory.listFiles()
        val fileDirectories = Array(999){ File("") }
        var count = 0
        if (files != null) {
            for (file in files) {
                Log.d("file", file.absolutePath)
                if (file.name.endsWith(".mp3") //||
//                    file.name.endsWith(".MP3") ||
//                    file.name.endsWith(".M4A") ||
//                    file.name.endsWith(".m4a") ||
//                    file.name.endsWith(".AAC") ||
//                    file.name.endsWith(".aac")
                ) {

                    tempAudioModelList.add(AudioModel(file.name, file.name, file.name, file.absolutePath))
                    _localAudio.postValue(tempAudioModelList)
                    _scanLocalAudioProgress.postValue(ScanProgressModel(tempAudioModelList.size, 999))
//                    databaseHandler.addSong(file.name, file.path, file.toURI())
                } else if (file.isDirectory) {
                    fileDirectories[count] = file
                    count++
                }
            }
        }
    }
}