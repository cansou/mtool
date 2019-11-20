### 简单记录一下使用`DexClassloader`
****
```java
public static void load(Context context)throws Throwable{
        Vlog.log("files====" +context.getFilesDir().getAbsolutePath());
        DexClassLoader dexClassLoader = new DexClassLoader(
                "/sdcard/test.dex",  //填写dex的绝对路径
                context.getFilesDir().getAbsolutePath(),  //填写优化后后的dex存放路径(只需写目录,无需写文件名)
                null,  //lib目录,不加载可写null
                context.getClassLoader()  //父加载器
        );
        Class<?> test = dexClassLoader.loadClass("Test");
        Object instance = test.newInstance();
        String str = (String) test.getDeclaredMethod("accessDex").invoke(instance);
        Vlog.log("test" + str);
}
```
### 如何生成`.class`文件
`javac Test.java`
### 如何生成`dex`
`dx --dex --output=test.dex Test.class UserInfo.class Category.class`
##### dx命令在`sdk/build-tools/27.0.3/`目录,最后的java源文件,如果源码写在对应的包里,需要预先创建对应的文件夹,`将生成的.class文件放入与包对应的文件夹中`,才可编译成功.