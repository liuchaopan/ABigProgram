package tech.soit.quiet.ui.activity.local

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.pan.library.util.AppContext
import kotlinx.android.synthetic.main.activity_local_music.*
import tech.soit.quiet.R
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.fragment.local.LocalAlbumFragment
import tech.soit.quiet.ui.fragment.local.LocalArtistFragment
import tech.soit.quiet.ui.fragment.local.LocalSingleSongFragment
import tech.soit.quiet.utils.annotation.DisableLayoutInject
import tech.soit.quiet.utils.annotation.EnableBottomController

/**
 * local music main activity
 *
 * contain three fragment [LocalAlbumFragment] [LocalSingleSongFragment] [LocalArtistFragment]
 */
@DisableLayoutInject
@EnableBottomController
class LocalMusicActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_music)
        tabLayout.setupWithViewPager(viewPager)
        viewPager.adapter = LocalPagerAdapter()

        //init toolbar
        toolbar.inflateMenu(R.menu.menu_local_home_page)
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.menu_local_home_scan) {
                startActivity(Intent(this, LocalScannerActivity::class.java))
            }
            true
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    private inner class LocalPagerAdapter : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val fragments = Array<Fragment?>(count) { null }

        override fun getItem(position: Int): Fragment = fragments[position]
                ?: when (position) {
                    0 -> LocalSingleSongFragment()
                    1 -> LocalArtistFragment()
                    2 -> LocalAlbumFragment()
                    else -> error("illegal position $position")
                }.also {
                    fragments[position] = it
                }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> AppContext.instance.getString(R.string.single_song)
                1 -> AppContext.instance.getString(R.string.artist)
                2 -> AppContext.instance.getString(R.string.album)
                else -> null
            }
        }


        /**
         * display three fragment: single song , artist , album
         */
        override fun getCount(): Int = 3

    }

}