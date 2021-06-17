package com.kyleduo.aladdin.genie.info

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.kyleduo.aladdin.api.manager.genie.AladdinViewGenie
import com.kyleduo.aladdin.genie.info.data.InfoSection
import com.kyleduo.aladdin.genie.info.databinding.AladdinGenieInfoPanelBinding
import com.kyleduo.aladdin.genie.info.view.AppInfoItemDelegate
import com.kyleduo.aladdin.genie.info.view.AppInfoSectionTitleItemDelegate
import com.kyleduo.aladdin.genie.info.view.InfoItemViewData
import com.kyleduo.aladdin.genie.info.view.InfoSectionTitleViewData
import com.kyleduo.aladdin.ui.dp2px

/**
 * Display basic information of this Application.
 *
 * @author kyleduo on 2021/6/11
 */
abstract class InfoGenie : AladdinViewGenie() {
    private val infoProviders = mutableListOf<InfoProvider>()

    private lateinit var binding: AladdinGenieInfoPanelBinding
    private val adapter by lazy {
        MultiTypeAdapter().also {
            it.register(InfoSectionTitleViewData::class.java, AppInfoSectionTitleItemDelegate())
            it.register(InfoItemViewData::class.java, AppInfoItemDelegate())
        }
    }

    override fun createPanel(container: ViewGroup): View {
        return LayoutInflater.from(container.context)
            .inflate(R.layout.aladdin_genie_info_panel, container, false).also {
                binding = AladdinGenieInfoPanelBinding.bind(it)
                binding.aladdinInfoList.adapter = adapter
                binding.aladdinInfoList.layoutManager =
                    LinearLayoutManager(container.context, LinearLayoutManager.VERTICAL, false)
                binding.aladdinInfoList.addItemDecoration(object :
                    RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        val position =
                            (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
                        val item = adapter.items[position]
                        val unit = 8.dp2px()
                        val isLast = position == adapter.items.size - 1
                        val bottom = if (isLast) unit else 0
                        if (item is InfoSectionTitleViewData) {
                            outRect.set(unit, unit, unit, bottom)
                        } else if (item is InfoItemViewData) {
                            outRect.set(unit * 2, unit, unit, bottom)
                        }
                    }
                })
            }
    }

    override fun onSelected() {
        reload()
    }

    override fun onDeselected() {
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    private fun reload() {
        val viewItems = infoProviders.mapNotNull {
            it.provideAppInfo()
        }.toList().toViewItems()
        adapter.items = viewItems
        adapter.notifyDataSetChanged()
    }

    @MainThread
    fun registerInfoProvider(provider: InfoProvider) {
        if (provider !in infoProviders) {
            infoProviders.add(provider)
        }
    }

    @MainThread
    fun unregisterInfoProvider(provider: InfoProvider) {
        infoProviders.remove(provider)
    }

    private fun List<InfoSection>.toViewItems(): List<Any> {
        val list = mutableListOf<Any>()
        this.forEach {
            list.add(InfoSectionTitleViewData(it.title, it.desc))
            it.items.forEach { item -> list.add(InfoItemViewData(item.name, item.value)) }
        }
        return list
    }
}
