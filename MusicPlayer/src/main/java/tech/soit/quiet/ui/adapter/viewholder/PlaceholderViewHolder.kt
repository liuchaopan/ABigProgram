package tech.soit.quiet.ui.adapter.viewholder

import android.view.View
import kotlinx.android.synthetic.main.item_placeholder.view.*

/**
 * RecyclerView 空白占位
 */
class PlaceholderViewHolder(itemView: View) : BaseViewHolder(itemView) {


    override fun applyPrimaryColor(colorPrimary: Int) {
        itemView.progressBar.indeterminateDrawable.setTint(colorPrimary)
    }

}