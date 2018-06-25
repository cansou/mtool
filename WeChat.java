微信(6.5.10)版本逆向记录

//数据库密码(Javaer):4926f5f
//数据库密码(苦瓜):9c3142d
//苦瓜新:a06286d
//数据库密码(南城):d99d2bd
//数据库密码(w):73ef89b

字符串资源:
微信运动类:
R.l.epN = 分享给朋友;
R.l.epO = 分享到朋友圈;
R.l.epM = 捐赠步数;

微信设置类:
R.l.dSF = 提示;

R.l.fBa = 正在退出...;
R.l.fAZ = 正在退出...;

R.l.dRF = New;

R.l.eXN = 已保护;

R.l.feM = 设置;

R.l.fdP = 已启用;
R.l.fdO = 未启用;

R.l.fde = 退出后不会删除任何历史数据，下次登录依然可以使用本帐号。

R.l.eGN = 关闭微信;

R.l.eTX = 为了你下次能够顺利登录微信，在退出前，请为你的微信帐号先设置一个登录密码。

布局
R.i.dio = 红包详情页ListView的item项,最外层线性布局

Visibility属性:
	0=VISIBLE;
	4=INVISIBLE;
	8=GONE;
	


摸索中的正则表达式:
public class Matchers {

    public static String match(String p, String str){
        Pattern pattern=Pattern.compile(p);
        Matcher m=pattern.matcher(str);
        if (m.find()){
            return m.group(1);
        }
        return null;
    }
}

[6.5.10]
String cls1 = "com.tencent.mm.ui.chatting.En_5b8fbb1e.a";
    findAndHookConstructor(cls1, loadPackageParam.classLoader, new XC_MethodHook() {
        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            Object object=param.thisObject;
            String fieldName="oGY";//弹出菜单
            View.OnCreateContextMenuListener listener= (View.OnCreateContextMenuListener) XposedHelpers.getObjectField(object,fieldName);
            XposedHelpers.setObjectField(object,fieldName,new MyMenuListener(listener,param));
        }
    });
		
public class MyMenuListener implements View.OnCreateContextMenuListener{

    private XC_MethodHook.MethodHookParam param;
    private View.OnCreateContextMenuListener listener;
    
    public MyMenuListener(View.OnCreateContextMenuListener listener, XC_MethodHook.MethodHookParam param){
        this.param=param;
        this.listener=listener;
    }
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, final View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        listener.onCreateContextMenu(contextMenu,view,contextMenuInfo);
        int intField= XposedHelpers.getIntField(view.getTag(),"position");
        contextMenu.add(intField,1000,0,"模块").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                XposedBridge.log("itemId: "+menuItem.getItemId());
                return true;
            }
        });
    }
}




String default_str="<url><![CDATA[http://support.weixin.qq.com/cgi-bin/mmsupport-bin/addchatroombyinvite?ticket=A%2BquAamrCmJPZ7qYK5bKCg%3D%3D]]></url>";
String match= Matchers.match("<url><!\\[CDATA\\[(.*?)\\]\\]></url>",default_str);
num.setText("匹配到:"+match);


我的钱包Activity: com.tencent.mm.plugin.mall.ui.MallIndexUI

微信运动Activity: com.tencent.mm.plugin.exdevice.ui.ExdeviceRankInfoUI

微信运动ViewHolder:  com.tencent.mm.plugin.exdevice.ui.b   //适配器的ViewHolder


微信某重要类: com.tencent.mm.plugin.exdevice.f.b.a.d (classes2.dex)

				  com.tencent.mm.plugin.exdevice.ui.b (classes3.dex)
				  com.tencent.mm.plugin.exdevice.ui.ExdeviceLikeView (classes3.dex)//微信运动点赞
				  com.tencent.mm.plugin.exdevice.ui.d (classes3.dex)
				  com.tencent.mm.plugin.exdevice.ui.ExdeviceRankInfoUI (classes3.dex)
				  com.tencent.mm.ui.transmit.SelectConversationUI (classes.dex)//微信运动分享选择联系人
				  
				  com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI (classes3.dex)//红包详情
				  com.tencent.mm.plugin.luckymoney.ui.i (classes3.dex)//红包详情adapter
				  
				  com.tencent.mm.plugin.webwx.ui.ExtDeviceWXLoginUI (classes3.dex)//电脑登陆确认
				  
				  com.tencent.mm.sdk.e.e (classes3.dex)//重要接口
				  
				  com.tencent.mm/.plugin.remittance.ui.RemittanceUI
				  com.tencent.mm/.plugin.remittance.ui.RemittanceUI
				  
				  com.tencent.mm.sdk.platformtools.bh (classes.dex)
				  com.tencent.mm.sdk.platformtools.bi (classes.dex)
				  com.tencent.mm.sdk.platformtools.aw (classes.dex)
				  
				  com.tencent.mm.sdk.platformtools.aa (classes.dex)
				  
				  com.tencent.mm.storage.s (classes.dex)//疑似微信运动数据读写类
				  
				  com.tencent.mm.s.ao
				  
				  
				  com.tencent.mm.plugin.remittance.ui.SelectRemittanceContactUI (classes.dex)//选择联系人转账
				  
				  com.tencent.mm.plugin.setting.ui.setting.SettingsUI (classes3.dex)//微信设置类
				  
				  com.tencent.mm.plugin.setting.ui.setting.PersonalPreference 继承0  (classes3.dex)
				  
				  com.tencent.mm.plugin.setting.ui.setting.SettingsNotificationUI (classes3.dex)//新消息提醒
				  com.tencent.mm.plugin.setting.ui.setting.SettingsPrivacyUI (classes3.dex)//隐私
				  com.tencent.mm.plugin.setting.ui.setting.SettingsPersonalInfoUI (classes3.dex)//个人信息
				  com.tencent.mm.plugin.setting.ui.setting.SettingsAccountInfoUI (classes3.dex)//账号与安全
				  
				  com.tencent.mm.ui.s (classes.dex)//我
				  com.tencent.mm.ui.base.preference.f (classes.dex)//接口
				  com.tencent.mm.ui.base.preference.Preference (classes.dex)//0
				  com.tencent.mm.ui.base.preference.a (classes.dex)//
				  com.tencent.mm.ui.base.preference.j (classes.dex)//
				  
				  com.tencent.mm.ui.chatting.gallery.ImageGalleryUI ()//聊天界面查看大图Activity
				  com.tencent.mm.plugin.scanner.a (classes.dex)//
				  
				  com.tencent.mm.plugin.scanner.ui.BaseScanUI ()//扫一扫
				  com.tencent.mm.plugin.profile.ui.ContactInfoUI ()//好友资料页面
				  
				  com.tencent.mm.plugin.remittance.ui.RemittanceAdapterUI (classes3.dex)//
				  
				  com.tencent.mm.plugin.remittance.ui.RemittanceResultNewUI 
				  
				  com.tencent.mm.plugin.remittance.ui.RemittanceUI (classes.dex)//转账类
				  
				  wxp://f2f1ahwsW4-_1Cyba3uIYPE_Lf8of4hM3wOd;
				  
				  d4q
				  aya
				  c8d
				  
				  
				  
微信撤回:
hashMap:{.sysmsg.revokemsg.session=8118878867@chatroom, .sysmsg.revokemsg.msgid=1062486985, .sysmsg.revokemsg.newmsgid=1992710206626891979, .sysmsg.revokemsg.replacemsg="苦苦苦苦瓜2" 撤回了一条消息, .sysmsg.$type=revokemsg, .sysmsg=, .sysmsg.revokemsg=}
s1:<sysmsg type="revokemsg"><revokemsg><session>8118878867@chatroom</session><msgid>1062486985</msgid><newmsgid>1992710206626891979</newmsgid><replacemsg><![CDATA["苦苦苦苦瓜2" 撤回了一条消息]]></replacemsg></revokemsg></sysmsg> s2:sysmsg


微信付款:
{"retcode":"0",
"retmsg":"ok",
"user_name":"wxid_mj5stkt33kde12",
"true_name":"*勇",
"fee":"0",
"desc":"",
"scene":"32",
"transfer_qrcode_id":"8HMWb9rjnd2Jsvz2DKf0ThesQanqUBrp9E9NIH0WDb5hA4-uoMpOum5i1pgOj5rs9s1869QOpUAl06nejNqDKHD_CFrNmXKvNpMwK-2y0rI5hmTgNIKRanoSyvcc9N10",
"f2f_pay_desc":"向个人用户转账",
"rcvr_desc":"收款备注",
"payer_desc":"付款留言",
"rcvr_ticket":"2UOzPTc6oxGlI8JNnQ0AwyD7p26WgxLGXaYSoBLMhnsE4ioVEcz_v6PrG_ZS1QtY",
"get_pay_wifi":1,
"busi_type":0,
"receiver_openid":"oX2-vjgozY3Nyl0SYcB6iUd5dXdA"}

版本比对

6.3.13												6.5.10
com.tencent.mm.model.ah				dbClass			com.tencent.mm.s.ao
com.tencent.mm.model.c 				返回类型		com.tencent.mm.s.c
com.tencent.mm.storage.h 			storage			com.tencent.mm.storage.t //(数据库)
com.tencent.mm.az.g 				返回类型		com.tencent.mm.bh.g //(数据库)
com.tencent.mm.storage.ah 			返回类型		com.tencent.mm.storage.ax

c:com.tencent.mm.plugin.sns.h.l 
d:com.tencent.mm.sdk.h.d
q:com.tencent.mm.ab.n
o = "Ao";
f:com.tencent.mm.storage.h
g:com.tencent.mm.az.g
r:com.tencent.mm.an.n
m="jM";
t="jL";
p="j";
s:com.tencent.mm.a.g
n = "aC";
i = "fNB";
j = "kSE";
F:com.tencent.mm.ui.chatting.ChattingUI.a
u:com.tencent.mm.plugin.sight.encode.ui.SightCameraView.1
y:com.tencent.mm.plugin.sight.encode.a.b.b
v = "lj";
w = "gEp";
z = "gEd";
A = "awp";
C = "x";
E = "n";
D = "awo";
B = "awq";
x = "awA";
h:com.tencent.mm.ui.chatting.ChattingUI
e:com.tencent.mm.ui.ui.LauncherUI
l:com.tencent.mm.plugin.sns.ui.SightUploadUI
k:com.tencent.mm.plugin.sns.ui.SnsUploadUI


06-25 15:49:22.841 31992-31992/? I/vxv: com.tencent.mm.ui.x>>>>>>>>>>class com.tencent.mm.ui.conversation.j
06-25 15:49:22.844 31992-31992/? I/vxv: com.tencent.mm.ui.x>>>>>>>>>>class com.tencent.mm.ui.contact.AddressUI$a
06-25 15:49:22.847 31992-31992/? I/vxv: com.tencent.mm.ui.x>>>>>>>>>>class com.tencent.mm.ui.h
06-25 15:49:22.850 31992-31992/? I/vxv: com.tencent.mm.ui.x>>>>>>>>>>class com.tencent.mm.ui.ab
06-25 15:49:23.456 31992-31992/? I/vxv: com.tencent.mm.ui.x>>>>>>>>>>class com.tencent.mm.ui.chatting.y
