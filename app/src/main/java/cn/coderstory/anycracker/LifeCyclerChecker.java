package cn.coderstory.anycracker;

import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class LifeCyclerChecker implements LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackground() {
        // 应用进入后台
        Log.e("test", "LifecycleChecker onAppBackground ON_STOP");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForeground() {
        // 应用进入前台
        Log.e("test", "LifecycleChecker onAppForeground ON_START");

    }
}
