package io.github.fairyxh.ZhangSystemHook.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import io.github.fairyxh.ZhangSystemHook.bean.AppFiltersBean
import io.github.fairyxh.ZhangSystemHook.bean.AppFiltersType
import io.github.fairyxh.ZhangSystemHook.bean.AppInfoBean
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import io.github.fairyxh.ZhangSystemHook.databinding.ActivityAppsConfigBinding
import io.github.fairyxh.ZhangSystemHook.databinding.AdapterAppInfoBinding
import io.github.fairyxh.ZhangSystemHook.databinding.DiaAppsFilterBinding
import io.github.fairyxh.ZhangSystemHook.ui.activity.base.BaseActivity
import io.github.fairyxh.ZhangSystemHook.utils.factory.appIconOf
import io.github.fairyxh.ZhangSystemHook.utils.factory.bindAdapter
import io.github.fairyxh.ZhangSystemHook.utils.factory.locale
import io.github.fairyxh.ZhangSystemHook.utils.factory.toast
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

    private val createFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
        result.data?.data?.let { uri ->
            val success = ConfigData.exportConfig(this, uri)
            toast(if(success) locale.exportSuccess else locale.exportFailed)
        }
    }

    private val openFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
        result.data?.data?.let { uri ->
            val success = ConfigData.importConfig(this, uri)
            toast(if(success) locale.importSuccess else locale.importFailed)
            if(success){
                lifecycleScope.launch(Dispatchers.Main){
                    refreshData(700)
                }
            }
        }
    }

    override fun onCreate() {
        binding.titleBackIcon.setOnClickListener { finish() }
        binding.exportIcon.setOnClickListener {
            createFileLauncher.launch(
                Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "application/xml"
                    putExtra(Intent.EXTRA_TITLE, "${locale.appName()}.xml")
                }
            )
        }
        binding.importIcon.setOnClickListener {
            openFileLauncher.launch(
                Intent(Intent.ACTION_GET_CONTENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/xml", "text/xml"))
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
            )
        }
        binding.filterIcon.setOnClickListener {

        }
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
        runBlocking {
            refreshData()
        }
    }

    /** 刷新列表数据 */
    private suspend fun refreshData(waitRefreshTime: Long = 0) {
        binding.listProgressView.isVisible = true
        binding.exportIcon.isVisible = false
        binding.importIcon.isVisible = false
        binding.filterIcon.isVisible = false
        binding.listView.isVisible = false
        binding.listNoDataView.isVisible = false
        binding.titleCountText.text = locale.loading
        if(waitRefreshTime > 0){
            delay(waitRefreshTime)
        }
        FrameworkTool.fetchAppListData(context = this, appFilters) {
            lifecycleScope.launch(Dispatchers.IO){
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
                withContext(Dispatchers.Main){
                    listData.clear()
                    listData.addAll(tempsData)
                    notifyDataSetChanged?.invoke()
                    binding.listView.post { binding.listView.setSelection(0) }
                    binding.listProgressView.isVisible = false
                    binding.exportIcon.isVisible = true
                    binding.importIcon.isVisible = true
                    binding.filterIcon.isVisible = true
                    binding.listView.isVisible = listData.isNotEmpty()
                    binding.listNoDataView.isVisible = listData.isEmpty()
                    binding.titleCountText.text = locale.resultCount(listData.size)
                }
            }
        }
    }

}