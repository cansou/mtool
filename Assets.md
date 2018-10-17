# Assets
### 在微信的Application初始化之后，提取微信Context,用微信的Context创建新的Context
```java
Context context = wechat_context.createPackageContext("club.javaer.vsetting", Context.CONTEXT_IGNORE_SECURITY)
```
`Context.CONTEXT_IGNORE_SECURITY = 2`
### 然后使用此`context`就可以加载包外部的资源(图片,视频,dexdengde)