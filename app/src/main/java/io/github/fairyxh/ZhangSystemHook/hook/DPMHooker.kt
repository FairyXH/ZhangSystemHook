package io.github.fairyxh.ZhangSystemHook.hook
import android.content.ComponentName
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringType
import io.github.fairyxh.ZhangSystemHook.application.SystemNotifier

object DPMHooker : YukiBaseHooker() {
    override fun onHook() {
        SystemNotifier.sendUserMsg(msg = "DPMHooker 开始工作")
        registerLifecycle()
    }

    private fun hooksystem() {
        SystemNotifier.sendUserMsg(msg = "DPMHooker 执行Hook")

        "com.android.server.devicepolicy.DevicePolicyManagerService".toClass().apply {
            method {
                name = "hasAccountsOnAnyUser"
                emptyParam()
                returnType = BooleanType
            }.hook {
                after {
                    result = false
                    SystemNotifier.sendUserMsg(msg = "修改方法 hasAccountsOnAnyUser() 的返回值为False")
                }

            }
            method {
                name = "enforceCanSetDeviceOwnerLocked"
                param("com.android.server.devicepolicy.CallerIdentity".toClass(),ComponentName::class.java,IntType,BooleanType)
            }.hook {
                before {
                    resultNull()
                }
                after {
                    SystemNotifier.sendUserMsg("跳过 enforceCanSetDeviceOwnerLocked() 执行")
                }
            }
            method {
                name = "checkDeviceOwnerProvisioningPreConditionLocked"
                param(ComponentName::class.java,IntType ,IntType,BooleanType,BooleanType)
                returnType=IntType
            }.hook {
                after {
                    result = 0
                    SystemNotifier.sendUserMsg("修改了方法 checkDeviceOwnerProvisioningPreConditionLocked() 的返回值为0")
                }
            }
        }
    }

    private fun registerLifecycle() {
        onAppLifecycle {
            onCreate {
                hooksystem()
            }
        }
    }
}