package com.syscode.aeudio.data.repository

import androidx.lifecycle.LiveData
import com.syscode.aeudio.data.model.AudioModel
import com.syscode.aeudio.data.model.ScanProgressModel
import com.syscode.aeudio.data.source.AudioDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AudioRepositoryImpl(
    private val audioDataSource: AudioDataSource
) : AudioRepository {
//    override suspend fun scanAudio(): LiveData<ScanProgressModel> {
//        return withContext(Dispatchers.IO){
//            audioDataSource.scanLocalAudio()
//            return@withContext audioDataSource.scanLocalAudioProgressModel
//        }
//    }

    override suspend fun getLocalAudio(): LiveData<MutableList<AudioModel>> {
        return withContext(Dispatchers.IO){
            audioDataSource.scanLocalAudio()
            return@withContext audioDataSource.localAudioModel
        }
    }
}