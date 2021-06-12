package com.kyleduo.aladdin.genie.appinfo

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.kyleduo.aladdin.api.AladdinContext
import com.kyleduo.aladdin.api.manager.genie.AladdinViewGenie
import com.kyleduo.aladdin.genie.appinfo.data.AppInfoSection
import com.kyleduo.aladdin.genie.appinfo.databinding.AladdinGenieAppInfoPanelBinding
import com.kyleduo.aladdin.genie.appinfo.providers.ApplicationInfoProvider
import com.kyleduo.aladdin.genie.appinfo.providers.DeviceInfoProvider
import com.kyleduo.aladdin.genie.appinfo.providers.DisplayInfoProvider
import com.kyleduo.aladdin.genie.appinfo.view.AppInfoItem
import com.kyleduo.aladdin.genie.appinfo.view.AppInfoItemDelegate
import com.kyleduo.aladdin.genie.appinfo.view.AppInfoSectionTitle
import com.kyleduo.aladdin.genie.appinfo.view.AppInfoSectionTitleItemDelegate
import com.kyleduo.aladdin.ui.dp2px

/**
 * Display basic information of this Application.
 *
 * @author kyleduo on 2021/6/11
 */
class AppInfoGenie(context: AladdinContext) : AladdinViewGenie(context) {
    companion object {
        const val KEY = "aladdin-app-info"
    }

    override val title: String = "App Info"
    override val key: String = KEY

    private val appInfoProviders = mutableListOf<AppInfoProvider>().also {
        it.add(ApplicationInfoProvider(context.app))
        it.add(DeviceInfoProvider(context.app))
        it.add(DisplayInfoProvider(context.app))
    }

    private lateinit var binding: AladdinGenieAppInfoPanelBinding
    private val adapter by lazy {
        MultiTypeAdapter().also {
            it.register(AppInfoSectionTitle::class.java, AppInfoSectionTitleItemDelegate())
            it.register(AppInfoItem::class.java, AppInfoItemDelegate())
        }
    }

    override fun createPanel(container: ViewGroup): View {
        return LayoutInflater.from(container.context)
            .inflate(R.layout.aladdin_genie_app_info_panel, container, false).also {
                binding = AladdinGenieAppInfoPanelBinding.bind(it)
                binding.aladdinAppinfoList.adapter = adapter
                binding.aladdinAppinfoList.layoutManager =
                    LinearLayoutManager(container.context, LinearLayoutManager.VERTICAL, false)
                binding.aladdinAppinfoList.addItemDecoration(object :
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
                        if (item is AppInfoSectionTitle) {
                            outRect.set(unit, unit, unit, bottom)
                        } else if (item is AppInfoItem) {
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
        val viewItems = appInfoProviders.mapNotNull {
            it.provideAppInfo()
        }.toList().toViewItems()
        adapter.items = viewItems
        adapter.notifyDataSetChanged()
    }

    @MainThread
    fun registerAppInfoProvider(provider: AppInfoProvider) {
        if (provider !in appInfoProviders) {
            appInfoProviders.add(provider)
        }
    }

    @MainThread
    fun unregisterAppInfoProvider(provider: AppInfoProvider) {
        appInfoProviders.remove(provider)
    }

    fun List<AppInfoSection>.toViewItems(): List<Any> {
        val list = mutableListOf<Any>()
        this.forEach {
            list.add(AppInfoSectionTitle(it.title, it.desc))
            it.items.forEach { item -> list.add(AppInfoItem(item.name, item.value)) }
        }
        return list
    }
}
