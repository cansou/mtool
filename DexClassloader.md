# 简单记录一下DexClassloader
****
```java
public static void load(Context context)throws Throwable{
        Vlog.log("files====" +context.getFilesDir().getAbsolutePath());
        DexClassLoader dexClassLoader = new DexClassLoader(
                "/sdcard/test.dex",//填写dex的绝对路径
                context.getFilesDir().getAbsolutePath(),//填写优化后后的dex存放路径(只需写目录,无需写文件名)
                null,//lib目录,不加载可写null
                context.getClassLoader()//父加载器
        );
        Class<?> test = dexClassLoader.loadClass("Test");
        Object instance = test.newInstance();
        String str = (String) test.getDeclaredMethod("accessDex").invoke(instance);
        Vlog.log("test" + str);
}
```