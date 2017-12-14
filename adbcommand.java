

D:\je>adb shell dumpsys activity | findstr "mFoc"
  mFocusedActivity: ActivityRecord{e3627fb u0 com.huawei.hwid/.ui.extend.setting.StartUpGuideLoginActivity t76}
  mFocusedStack=ActivityStack{ddba4c0 stackId=1, 3 tasks} mLastFocusedStack=ActivityStack{ddba4c0 stackId=1, 3 tasks}



D:\je>adb shell pm list package -f | findstr "com.huawei.hwid"
package:/data/app/com.huawei.hwid-1/base.apk=com.huawei.hwid

覆盖安装
adb install -r d:/a.apk

adb pull /data/app/com.huawei.hwid-1/base.apk com.huawei.hwid.apk

start . 打开当前目录


/data/system/caller === su

/data/data/club.vxv.virtual.wechat10/virtual/data/user/0/com.tencent.mm

files/wx*
databases/WECHAT_*
databases/WX_*
shared_prefs/com.fkzhang*

adb shell content query --uri content://club.vxv.virtual.wechat1/vclub/qcode  --where /sdcard/qcode.jpg

com.mfashiongallery.emag/.app.main.AppMainActivity


dumpsys activity | grep mFo

