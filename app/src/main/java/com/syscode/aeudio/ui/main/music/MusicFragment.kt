package com.syscode.aeudio.ui.main.music

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.syscode.aeudio.R
import com.syscode.aeudio.data.model.AudioModel
import com.syscode.aeudio.internal.ScopedFragment
import com.syscode.aeudio.ui.main.MainActivity
import com.syscode.aeudio.ui.player.PlayerActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class MusicFragment : ScopedFragment(), KodeinAware, MusicItem.MusicContainer {
    override val kodein by closestKodein()

    private val musicViewModelFactory: MusicViewModelFactory by instance()
    private lateinit var musicViewModel: MusicViewModel

    private lateinit var musics: LiveData<MutableList<AudioModel>>
    private lateinit var root: View

    private val musicGroupAdapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        musicViewModel =
                ViewModelProvider(this, musicViewModelFactory).get(MusicViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        musicViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

        loadData()

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        music_recyclerView.adapter = musicGroupAdapter
        music_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        text_home.setOnClickListener {
            loadData()
        }
    }
    private fun loadData(){
        if (checkPermission()) await(root)
        else requestPermission()
    }
    private fun await(root: View) = launch {
        musics = musicViewModel.musics.await()
        musics.observe(viewLifecycleOwner, {
            musicViewModel.setText(it.size.toString())
            musicGroupAdapter.clear()
            it.mapIndexed{index, audioModel ->
                musicGroupAdapter.add(index, MusicItem(audioModel, this@MusicFragment))
            }
            return@observe
        })
    }
    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext().applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext().applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        , MainActivity.PERMISSION_CODE)
    }

    override fun onClick(audioModel: AudioModel, views: ArrayList<View>) {
        Intent(requireContext(), PlayerActivity::class.java).apply {
            putExtra("musicName", audioModel.title)
            putExtra("musicPath", audioModel.path)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(),
                Pair.create(views[0], "tn_music_name"),
                Pair.create(views[1], "tn_music_path")
            )
            startActivity(this, options.toBundle())
        }
    }


}