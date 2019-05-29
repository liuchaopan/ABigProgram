package tech.soit.quiet.ui.fragment.local

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_local_scanner_setting.*
import tech.soit.quiet.R
import tech.soit.quiet.ui.fragment.base.BaseFragment
import tech.soit.quiet.ui.item.*
import tech.soit.quiet.utils.component.support.Status
import tech.soit.quiet.viewmodel.LocalMusicScannerSettingViewModel
import tech.soit.typed.adapter.TypedAdapter
import tech.soit.typed.adapter.withBinder

class LocalMusicScannerSettingFragment : BaseFragment() {

    companion object {
        const val TAG = "LocalMusicScannerSettingFragment"
    }

    private val viewModel by lazyViewModelInternal<LocalMusicScannerSettingViewModel>()

    private val adapter by lazy {
        TypedAdapter()
                .withEmptyBinder()
                .withLoadingBinder()
                .withBinder(SettingScannerFolderFilterBinder())
                .withBinder(SettingHeaderBinder())
                .withBinder(SettingSwitchBinder())
    }

    private val items = ArrayList<Any>()

    init {
        items.add(SettingHeader("大小过滤"))
        items.add(SettingSwitch("过滤小于60s的音频文件",
                "过滤小于60s的音频文件", true))
        items.add(SettingHeader("扫描目录过滤"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.isFilterByDuration().observe(this, Observer { b ->
            b ?: return@Observer
            items[1] = (items[1] as SettingSwitch).copy(isChecked = b)
            adapter.notifyItemChanged(1)
        })
        viewModel.getFileFilterData().observe(this, Observer { result ->
            result ?: return@Observer
            items.remove(Empty)
            if (result.status == Status.LOADING) {
                items.add(Loading)
            } else if (result.status == Status.SUCCESS) {
                items.remove(Loading)
                val filters = result.requireData()
                if (filters.isEmpty()) {
                    items.add(Empty)
                } else {
                    items.add(filters)
                }
            }
            adapter.notifyDataSetChanged()
        })
    }

    override fun onCreateView2(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_local_scanner_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = adapter
        adapter.setList(items, false)
    }

}