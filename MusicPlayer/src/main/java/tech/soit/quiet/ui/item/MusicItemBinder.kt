package tech.soit.quiet.ui.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_music.view.*
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.MusicPlayerManager
import tech.soit.quiet.utils.KItemViewBinder
import tech.soit.quiet.utils.KViewHolder
import tech.soit.quiet.utils.component.ImageLoader


/**
 * item music
 *
 * @param token the token of playlist, to check music if playing
 * @param onClick callback of music been clicked
 * @param onPlayingItemShowHide callback of playing music item show/hide
 */

open class MusicItemViewBinder(
        private val token: String,
        private val onClick: (view: View, music: Music) -> Unit,
        private val onPlayingItemShowHide: ((show: Boolean) -> Unit)? = null
) : KItemViewBinder<Music>() {



    override fun getLayoutRes(): Int {
        return R.layout.item_music
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): KViewHolder {
        return MusicViewHolder(inflater.inflate(R.layout.item_music, parent, false))
    }

    override fun onBindViewHolder(holder: KViewHolder, item: Music) {
        holder as MusicViewHolder

        val isPlaying = isPlaying(item)
        holder.setIsPlaying(isPlaying)

        holder.itemView.setOnClickListener {
            onClick(holder.itemView, item)
        }

        holder.bind(item)
    }

    private fun isPlaying(music: Music): Boolean {
        return MusicPlayerManager.musicPlayer.playlist.token == token
                && MusicPlayerManager.musicPlayer.playlist.current == music
    }

    /**
     * 设置当前播放的音乐
     */
    fun setCurrentPlaying(music: Music?, recyclerView: RecyclerView) {
        recyclerView.children
                .map {
                    recyclerView.getChildViewHolder(it)
                }
                .filterIsInstance<MusicViewHolder>()
                .forEach {
                    it.setIsPlaying(it.musicId == music?.getId())
                }
    }

    class MusicViewHolder(itemView: View) : KViewHolder(itemView) {

        var musicId: Long = 0

        fun bind(item: Music) = with(itemView) {

            ImageLoader.with(this).load(item.getAlbum().getCoverImageUrl()).centerCrop().into(image)

            popup_menu.setOnClickListener {
                //
            }
            text_item_title.text = item.getTitle()
            text_item_subtitle.text = item.getArtists().joinToString("/") { it.getName() }
            text_item_subtitle_2.text = item.getAlbum().getName()

            musicId = item.getId()
        }

        fun setIsPlaying(isPlaying: Boolean) {
            itemView.indicatorPlaying.isGone = !isPlaying
        }


    }

}