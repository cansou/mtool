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
### `登录数据库`
`mysql -u root -p test`
**====> test 为数据库名**
### `切换数据库`
`use test`
**切换到test数据库**
### `查询表中所有数据`
`select * from tb_user`
**tb_user为数据库表名**