package com.syscode.aeudio.data.repository

import androidx.lifecycle.LiveData
import com.syscode.aeudio.data.model.AudioModel
import com.syscode.aeudio.data.model.ScanProgressModel

interface AudioRepository {
//    suspend fun scanAudio(): LiveData<ScanProgressModel>
    suspend fun getLocalAudio(): LiveData<MutableList<AudioModel>>

}