package io.github.fairyxh.ZhangSystemHook.hook

import android.os.Build
import androidx.annotation.RequiresApi
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.android.IntentClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import io.github.fairyxh.ZhangSystemHook.application.SystemNotifier

object ColorOSHomeHooker : YukiBaseHooker() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onHook() {
        registerLifecycle()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun hookColorOSHome() {
        val context = this.appContext
        SystemNotifier.sendUserMsg(msg = "ColorOSHomeHooker 执行Hook", context)

        "com.oplus.quickstep.applock.AppLockModel".toClass().apply {
            method {
                name = "isExceededLockLimit"
                param(StringClass, StringClass)
                returnType = BooleanType
            }.hook {
                before {
                    resultFalse()
                    SystemNotifier.sendUserMsg("修改方法 isExceededLockLimit() 的返回值为False", context)
                }
            }
        }

        "com.oplus.quickstep.applock.AppLockInfo".toClass().apply {
            method{
                name="isAllowDefaultLock"
                emptyParam()
                returnType=BooleanType
            }.hook{
                after {
                    result=true
                    SystemNotifier.sendUserMsg("修改方法 isAllowDefaultLock() 的返回值为True", context)
                }
            }
            method{
                name="isUseless"
                emptyParam()
                returnType=BooleanType
            }.hook{
                after {
                    result=false
                    SystemNotifier.sendUserMsg("修改方法 isUseless() 的返回值为False", context)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun registerLifecycle() {
        onAppLifecycle {
            onCreate {
                hookColorOSHome()
            }
        }
    }
}