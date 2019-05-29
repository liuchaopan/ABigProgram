package tech.soit.quiet.ui.item

import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_setting_header.view.*
import tech.soit.quiet.R
import tech.soit.typed.adapter.TypedBinder
import tech.soit.typed.adapter.ViewHolder

data class SettingHeader(
        val title: String
)

class SettingHeaderBinder : TypedBinder<SettingHeader>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder.from(R.layout.item_setting_header, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: SettingHeader) {
        holder.itemView.textHeader.text = item.title
    }
}