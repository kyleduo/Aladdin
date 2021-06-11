package com.kyleduo.aladdin.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kyleduo.aladdin.R
import com.kyleduo.aladdin.genies.ViewGenie
import com.kyleduo.aladdin.utils.dp2px

/**
 * @author kyleduo on 2021/6/10
 */
class TabAdapter(
    private val onTabSelectedListener: OnTabSelectedListener
) : RecyclerView.Adapter<TabViewHolder>() {

    var selectedGenie: ViewGenie? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var genies = listOf<ViewGenie>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        return TabViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.aladdin_board_tab, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
        holder.bind(genies[position], selectedGenie)
        holder.itemView.setOnClickListener {
            onTabSelectedListener.onTabSelected(position, genies[position])
        }
    }

    override fun getItemCount(): Int = genies.size
}

interface OnTabSelectedListener {
    fun onTabSelected(position: Int, genie: ViewGenie)
}

class TabViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tab = itemView as TextView

    fun bind(viewGenie: ViewGenie, selectedGenie: ViewGenie?) {
        tab.text = viewGenie.title
        tab.isSelected = viewGenie == selectedGenie
        tab.elevation = if (tab.isSelected) {
            2f.dp2px()
        } else {
            0f.dp2px()
        }
    }
}
