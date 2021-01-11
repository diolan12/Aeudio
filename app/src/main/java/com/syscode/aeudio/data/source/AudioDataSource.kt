package com.syscode.aeudio.data.source

import androidx.lifecycle.LiveData
import com.syscode.aeudio.data.model.AudioModel
import com.syscode.aeudio.data.model.ScanProgressModel

interface AudioDataSource {
    val localAudioModel: LiveData<MutableList<AudioModel>>

    val scanLocalAudioProgressModel: LiveData<ScanProgressModel>
    suspend fun scanLocalAudio()
}