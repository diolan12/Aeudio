package com.syscode.aeudio

import android.app.Application
import com.syscode.aeudio.data.repository.AudioRepository
import com.syscode.aeudio.data.repository.AudioRepositoryImpl
import com.syscode.aeudio.data.source.AudioDataSource
import com.syscode.aeudio.data.source.AudioDataSourceImpl
import com.syscode.aeudio.ui.main.music.MusicViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class AeudioApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@AeudioApplication))

        // data source
        bind<AudioDataSource>() with singleton { AudioDataSourceImpl(instance()) }

        // repository
        bind<AudioRepository>() with singleton { AudioRepositoryImpl(instance()) }

        // view-model factory
        bind() from provider { MusicViewModelFactory(instance()) }
    }
}