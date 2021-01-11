package com.syscode.aeudio.ui.main.music

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.syscode.aeudio.data.repository.AudioRepository

class MusicViewModelFactory(
    private val audioRepository: AudioRepository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MusicViewModel(audioRepository) as T
    }
}