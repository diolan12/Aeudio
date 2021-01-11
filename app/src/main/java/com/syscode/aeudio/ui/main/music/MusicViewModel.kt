package com.syscode.aeudio.ui.main.music

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.syscode.aeudio.data.repository.AudioRepository
import com.syscode.aeudio.internal.lazyDeferred

class MusicViewModel(
    private val audioRepository: AudioRepository
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val musics by lazyDeferred { audioRepository.getLocalAudio() }
//    suspend fun getMusics() = audioRepository.getLocalAudio()
//    suspend fun getInt() = audioRepository.scanAudio()
    fun setText(data: String){
        _text.postValue(data)
    }
}