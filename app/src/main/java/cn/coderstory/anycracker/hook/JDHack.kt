package cn.coderstory.anycracker.hook

import cn.coderstory.anycracker.bean.JDUser
import cn.coderstory.anycracker.util.Tool
import com.google.gson.Gson
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class JDHack : IXposedHookLoadPackage {
    @Throws(Throwable::class)
    override fun handleLoadPackage(param: LoadPackageParam) {
        var gson = Gson()
        val classLoader = param.classLoader
        if (!param.packageName.contains("jdvideo")) {
            return
        }
        XposedBridge.log("成功Hook京东")
        XposedHelpers.findAndHookMethod("c.o.a.a", classLoader, "a", String::class.java, String::class.java, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {

                super.beforeHookedMethod(param)
            }

            @Throws(Throwable::class)
            override fun afterHookedMethod(param: MethodHookParam) {
                var origin = param.result as String
                XposedBridge.log("origin内容 $origin")
                try {
                    var fromJson = gson.fromJson(origin, JDUser::class.java)
                    XposedBridge.log("成功解析")
                    param.result = gson.toJson(Tool.processUserBean(fromJson))
                } catch (e:Exception) {
                    return
                }
                super.afterHookedMethod(param)
            }
        })
    }
}
