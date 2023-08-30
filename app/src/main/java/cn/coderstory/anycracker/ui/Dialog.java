package cn.coderstory.anycracker.ui;

import android.content.Context;
import cn.coderstory.anycracker.hook.SevenDegreeHack;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Dialog {
    @SneakyThrows
    public Dialog(Context context,String text) {
        Class clazz = SevenDegreeHack.classLoader.loadClass(("c.m.a.f.m0"));
        Constructor constructor = clazz.getConstructor(Context.class,String.class);

        Object content = constructor.newInstance(context, text);
        Method method = clazz.getMethod("show");
        method.invoke(content);
    }
}
