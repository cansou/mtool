# logcat命令
### 打印vxv标签日志保存到gan.java文件中
`adb logcat -s "vxv" > gan.java`

# apktool命令
### `解包`
`apktool d wechat.apk -r -o out`
### `打包`
`apktool b out`

# apk签名命令
### `mac`
`jarsigner -verbose -keystore company.jks -signedjar signed.apk unsigned.apk javaer`
### `windows`
`jarsigner -verbose -keystore company.jks -signedjar signed.apk unsigned.apk javaer`

# ssh远程登录
`ssh -p 29166 -u root@74.82.208.54`

# mysql命令
### 登录数据库
`mysql -u root -p test`
**===> test为数据库名**
### 切换数据库
`use test`
**===> 切换到test数据库**
### 查询表中所有数据
`select * from tb_user`
**===> tb_user为数据库表名**
### 查询某个字段包含某个字符
`select * from tb_user where user_name like '%下%';`