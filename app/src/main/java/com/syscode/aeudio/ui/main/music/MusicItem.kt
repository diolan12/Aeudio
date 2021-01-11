package com.syscode.aeudio.ui.main.music

import android.view.View
import com.syscode.aeudio.R
import com.syscode.aeudio.data.model.AudioModel
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_music.*

class MusicItem(
    private val audioModel: AudioModel,
    private val musicContainer: MusicContainer
): Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            R.id.item_music_name
            item_music_name.text = audioModel.title
            item_music_path.text = audioModel.path
            item_music.setOnClickListener {
                musicContainer.onClick(audioModel, arrayListOf(
                    item_music_name,
                    item_music_path
                ))
            }
        }
    }

    override fun getLayout() = R.layout.item_music
    interface MusicContainer {
        fun onClick(audioModel: AudioModel, views: ArrayList<View>)
    }
}