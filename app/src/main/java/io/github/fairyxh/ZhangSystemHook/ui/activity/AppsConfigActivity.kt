package io.github.fairyxh.ZhangSystemHook.ui.activity

import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import io.github.fairyxh.ZhangSystemHook.bean.AppFiltersBean
import io.github.fairyxh.ZhangSystemHook.bean.AppInfoBean
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import io.github.fairyxh.ZhangSystemHook.databinding.ActivityAppsConfigBinding
import io.github.fairyxh.ZhangSystemHook.databinding.AdapterAppInfoBinding
import io.github.fairyxh.ZhangSystemHook.ui.activity.base.BaseActivity
import io.github.fairyxh.ZhangSystemHook.utils.factory.appIconOf
import io.github.fairyxh.ZhangSystemHook.utils.factory.bindAdapter
import io.github.fairyxh.ZhangSystemHook.utils.factory.locale
import io.github.fairyxh.ZhangSystemHook.utils.tool.FrameworkTool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class AppsConfigActivity : BaseActivity<ActivityAppsConfigBinding>() {

    private var appFilters = AppFiltersBean()
    private var notifyDataSetChanged: (() -> Unit)? = null
    private val listData = ArrayList<AppInfoBean>()
    private val allData = ArrayList<AppInfoBean>()

    override fun onCreate() {
        binding.titleBackIcon.setOnClickListener { finish() }
        binding.listView.apply {
            bindAdapter {
                onBindDatas { listData }
                onBindViews<AdapterAppInfoBinding> { binding, position ->
                    listData[position].also { bean ->
                        binding.appIcon.setImageDrawable(bean.icon)
                        binding.appNameText.text = bean.name
                        binding.pkgNameText.text = bean.packageName
                        binding.appCheck.isChecked = ConfigData.blockApps.contains(bean.packageName)
                    }
                }
            }.apply {
                notifyDataSetChanged = this::notifyDataSetChanged
            }
            setOnItemClickListener { _, _, position, _ ->
                listData[position].also { bean ->
                    ConfigData.blockApps.switch(bean.packageName)
                    notifyDataSetChanged?.invoke()
                }
            }
        }
        binding.AppSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterList(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        runBlocking {
            refreshData()
        }
    }

    /** 刷新列表数据 */
    private suspend fun refreshData(waitRefreshTime: Long = 0) {
        binding.listProgressView.isVisible = true
        binding.AppSearch.isVisible = true
        binding.listView.isVisible = false
        binding.listNoDataView.isVisible = false
        binding.titleCountText.text = locale.loading
        if (waitRefreshTime > 0) {
            delay(waitRefreshTime)
        }
        FrameworkTool.fetchAppListData(context = this, appFilters) {
            lifecycleScope.launch(Dispatchers.IO) {
                val tempsData = ArrayList<AppInfoBean>()
                runCatching {
                    it.takeIf { e -> e.isNotEmpty() }?.forEach { e ->
                        e.icon = appIconOf(e.packageName)
                        tempsData.add(e)
                    }
                }
                tempsData.sortBy { bean ->
                    !ConfigData.blockApps.contains(bean.packageName)
                }
                withContext(Dispatchers.Main) {
                    allData.clear()
                    allData.addAll(tempsData)

                    listData.clear()
                    listData.addAll(tempsData)

                    notifyDataSetChanged?.invoke()
                    binding.listView.post { binding.listView.setSelection(0) }
                    binding.listProgressView.isVisible = false
                    binding.listView.isVisible = listData.isNotEmpty()
                    binding.listNoDataView.isVisible = listData.isEmpty()
                    binding.titleCountText.text = locale.resultCount(listData.size)
                }
            }
        }
    }


    private fun filterList(keyword: String) {
        val key = keyword.trim().lowercase()
        listData.clear()
        if (key.isEmpty()) {
            listData.addAll(allData)
        } else {
            listData.addAll(allData.filter { bean ->
                bean.name.lowercase().contains(key) ||
                        bean.packageName.lowercase().contains(key)
            })
        }
        notifyDataSetChanged?.invoke()
        binding.titleCountText.text = locale.resultCount(listData.size)
        binding.listView.isVisible = listData.isNotEmpty()
        binding.listNoDataView.isVisible = listData.isEmpty()
    }

}