/*
 * This file is created by fankes on 2022/10/1.
 */
@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.fairyxh.ZhangSystemHook.data

import android.app.Activity
import android.content.Context
import android.net.Uri
import com.highcapable.yukihookapi.hook.factory.prefs
import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.param.PackageParam
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import io.github.fairyxh.ZhangSystemHook.BuildConfig
import io.github.fairyxh.ZhangSystemHook.utils.tool.FrameworkTool
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

/**
 * 全局配置存储控制类
 */
object ConfigData {
    private const val BLOCK_APPS = "_block_apps"
    private const val SHOW_NOTICE = "show_notice"
    val ENABLE_MODULE = PrefsData("_enable_module", true)
    val blockApps by lazy {
        PrefsDataSetString(BLOCK_APPS, hashSetOf(BuildConfig.APPLICATION_ID))
    }

    fun refresh() {
        blockApps.refresh()
    }

    /** 当前实例 - [Context] or [PackageParam] */
    private var instance: Any? = null

    /**
     * 初始化存储控制类
     * @param instance 实例 - 只能是 [Context] or [PackageParam]
     * @throws IllegalStateException 如果类型错误
     */
    fun init(instance: Any) {
        when (instance) {
            is Context, is PackageParam -> this.instance = instance
            else -> error("Unknown type for init ConfigData")
        }
    }

    /**
     * 读取 [Set]<[String]> 数据
     * @param key 键值名称
     * @return [Set]<[String]>
     */
    internal fun getStringSet(key: String, value: Set<String> = hashSetOf()) = when (instance) {
        is Context -> (instance as Context).prefs().getStringSet(key, value)
        is PackageParam -> (instance as PackageParam).prefs.getStringSet(key, value)
        else -> error("Unknown type for get prefs data")
    }

    /**
     * 存入 [Set]<[String]> 数据
     * @param key 键值名称
     * @param value 键值内容
     */
    internal fun putStringSet(key: String, value: Set<String>) {
        when (instance) {
            is Context -> (instance as Context).prefs().edit { putStringSet(key, value) }
            is PackageParam -> YLog.warn("Not support for this method")
            else -> error("Unknown type for put prefs data")
        }
    }

    /**
     * 读取 [Int] 数据
     * @param data 键值数据模板
     * @return [Int]
     */
    internal fun getInt(data: PrefsData<Int>) = when (instance) {
        is Context -> (instance as Context).prefs().get(data)
        is PackageParam -> (instance as PackageParam).prefs.get(data)
        else -> error("Unknown type for get prefs data")
    }

    /**
     * 存入 [Int] 数据
     * @param data 键值数据模板
     * @param value 键值内容
     */
    internal fun putInt(data: PrefsData<Int>, value: Int) {
        when (instance) {
            is Context -> (instance as Context).prefs().edit { put(data, value) }
            is PackageParam -> YLog.warn("Not support for this method")
            else -> error("Unknown type for put prefs data")
        }
    }
    /**
     * 是否启用模块
     * @return [Boolean]
     */
    var isEnableModule
        get() = getBoolean(ENABLE_MODULE)
        set(value) {
            putBoolean(ENABLE_MODULE, value)
        }

    /**
     * 读取 [Boolean] 数据
     * @param data 键值数据模板
     * @return [Boolean]
     */
    internal fun getBoolean(data: PrefsData<Boolean>) = when (instance) {
        is Context -> (instance as Context).prefs().get(data)
        is PackageParam -> (instance as PackageParam).prefs.get(data)
        else -> error("Unknown type for get prefs data")
    }

    /**
     * 存入 [Boolean] 数据
     * @param data 键值数据模板
     * @param value 键值内容
     */
    internal fun putBoolean(data: PrefsData<Boolean>, value: Boolean) {
        when (instance) {
            is Context -> (instance as Context).prefs().edit { put(data, value) }
            is PackageParam -> YLog.warn("Not support for this method")
            else -> error("Unknown type for put prefs data")
        }
    }

    data class PrefsDataSetString(private val key: String, var data: HashSet<String> = hashSetOf()){
        init {
            refresh()
        }
        internal fun refresh(){
            data = getStringSet(key, data).toHashSet()
        }
        internal fun callRefresh(){
            when (instance) {
                is Context -> FrameworkTool.refreshFrameworkPrefsData(instance as Context)
                is PackageParam -> YLog.warn("Not support for this method")
                else -> error("Unknown type for get prefs data")
            }
        }
        fun contains(element: String) = data.contains(element)
        fun add(element: String) {
            if(data.add(element)){
                putStringSet(key, data)
                callRefresh()
            }
        }
        fun remove(element: String) {
            if(data.remove(element)){
                putStringSet(key, data)
                callRefresh()
            }
        }
        fun switch(element: String){
            if(!contains(element)) add(element)
            else remove(element)
        }
        fun replaceAll(newData: HashSet<String>){
            data.clear()
            data.addAll(newData);
            callRefresh()
        }
    }


    /**
     * 导出配置到用户选择的URI
     * @param context 当前Context
     * @param uri 用户选择的保存位置URI
     */
    fun exportConfig(
        context: Context,
        uri: Uri
    ): Boolean {
        return try {
            // 使用 ConfigData 来获取配置
            val blockApps = ConfigData.blockApps.data
            // 创建XML格式的配置内容
            val xmlContent = StringBuilder().apply {
                appendLine("<?xml version='1.0' encoding='utf-8' standalone='yes' ?>")
                appendLine("<map>")
                appendLine("    <set name=\"_block_apps\">")
                blockApps.forEach { app ->
                    appendLine("        <string>$app</string>")
                }
                appendLine("    </set>")
                appendLine("</map>")
            }.toString()
            // 使用ContentResolver写入文件
            context.contentResolver.openOutputStream(uri)?.use { output ->
                output.write(xmlContent.toByteArray())
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 从用户选择的URI导入配置
     * @param activity 当前Activity实例
     * @param uri 用户选择的文件URI
     * @return 导入是否成功
     */
    fun importConfig(
        activity: Activity,
        uri: Uri
    ): Boolean {
        return try {
            // 读取XML文件
            activity.contentResolver.openInputStream(uri)?.use { input ->
                val parser = XmlPullParserFactory.newInstance().newPullParser().apply {
                    setInput(input, "UTF-8")
                }
                var newBlockApps: HashSet<String> = hashSetOf()
                var eventType = parser.eventType
                // 解析XML文件
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    when (eventType) {
                        XmlPullParser.START_TAG -> {
                            when (parser.name) {
                                "string" -> {
                                    parser.next()
                                    if (parser.eventType == XmlPullParser.TEXT) {
                                        newBlockApps.add(parser.text)
                                    }
                                }
                            }
                        }
                    }
                    eventType = parser.next()
                }
                blockApps.replaceAll(newBlockApps)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}