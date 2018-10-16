# 简单记录一下DexClassloader
****
```java
public static void load(Context context)throws Throwable{
        Vlog.log("files====" +context.getFilesDir().getAbsolutePath());
        DexClassLoader dexClassLoader = new DexClassLoader(
                "/sdcard/test.dex",
                context.getFilesDir().getAbsolutePath(),
                null,
                context.getClassLoader());
        Class<?> test = dexClassLoader.loadClass("Test");
        Object instance = test.newInstance();
        String str = (String) test.getDeclaredMethod("accessDex").invoke(instance);
        Vlog.log("test" + str);
    }
```