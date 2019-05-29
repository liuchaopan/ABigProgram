package tech.soit.quiet.ui.item

import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_setting_switch.view.*
import tech.soit.quiet.R
import tech.soit.typed.adapter.TypedBinder
import tech.soit.typed.adapter.ViewHolder

data class SettingSwitch(
        val key: String,
        val title: String,
        val isChecked: Boolean,
        val subTitle: String? = null
)


class SettingSwitchBinder : TypedBinder<SettingSwitch>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder.from(R.layout.item_setting_switch, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: SettingSwitch) {
        holder.itemView.switch1.apply {
            text = item.title
            isChecked = item.isChecked
        }
    }

}

