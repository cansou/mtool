微信(6.5.10)版本逆向记录

//数据库密码(Javaer):4926f5f
//数据库密码(苦瓜):9c3142d
//苦瓜新:a06286d
//数据库密码(南城):d99d2bd
//数据库密码(w):73ef89b

//发送视频  "send sight to %s, videopath %s, thumbpath %s url: %s time: %d, duration: %s"

//朋友圈下载视频？ "start online download video %s isPlayMode %b"  prepare stream download sns video %s mkDir %b

//解析朋友圈对象 "MicroMsg.SnsInfoStorageLogic", "SnsObject parseFrom error"

//ChattingFootEditMaxHeigh   "MicroMsg.SnsVideoLogic", "get sns video thumb path %s [%b]"

ChattingUI intent参数
{
    key: Chat_Mode
    value: 1
    class: java.lang.Integer
},
{
    key: Chat_User
    value: wxid_flcwurtc3qgc22
    class: java.lang.String
},


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


----6.6.7

06-25 15:49:22.841 31992-31992/? I/vxv: com.tencent.mm.ui.x>>>>>>>>>>class com.tencent.mm.ui.conversation.j  //微信
06-25 15:49:22.844 31992-31992/? I/vxv: com.tencent.mm.ui.x>>>>>>>>>>class com.tencent.mm.ui.contact.AddressUI$a  //通讯录
06-25 15:49:22.847 31992-31992/? I/vxv: com.tencent.mm.ui.x>>>>>>>>>>class com.tencent.mm.ui.h
06-25 15:49:22.850 31992-31992/? I/vxv: com.tencent.mm.ui.x>>>>>>>>>>class com.tencent.mm.ui.ab
06-25 15:49:23.456 31992-31992/? I/vxv: com.tencent.mm.ui.x>>>>>>>>>>class com.tencent.mm.ui.chatting.y

{.TimelineObject.ContentObject.mediaList.media.type=2, .TimelineObject.streamvideo.streamvideoweburl=, .TimelineObject.ContentObject.mediaList.media.description=, .TimelineObject.ContentObject.title=直播间注册邀请, .TimelineObject.publicUserName=gh_d88e1e52a9d0, .TimelineObject.streamvideo=, .TimelineObject.id=12835081098110644335, .TimelineObject.actionInfo.appMsg=, .TimelineObject.ContentObject.mediaList.media.url=http://szmmsns.qpic.cn/mmsns/TGSic0onZvO0JoewIfdIickzA7A5Utic1QAUz8RddhibgKYPv2em7ahSfU64IicIU0yriaM1W74lQJpeE/0, .TimelineObject.location.$poiName=, .TimelineObject.streamvideo.streamvideourl=, .TimelineObject.statisticsData=, .TimelineObject.ContentObject=, .TimelineObject.ContentObject.mediaList.media.id=12835081098599805047, .TimelineObject.contentDesc=, .TimelineObject.ContentObject.mediaList.media.size=, .TimelineObject.location.$city=, .TimelineObject.appInfo=, .TimelineObject.ContentObject.contentUrl=https://ygkc.qianliao.tv/activity/page/redirect?liveId=360000022151734, .TimelineObject.statExtStr=, .TimelineObject.ContentObject.mediaList.media.title=, .TimelineObject.actionInfo.appMsg.messageAction=, .TimelineObject.private=0, .TimelineObject.ContentObject.mediaList.media.thumb=http://szmmsns.qpic.cn/mmsns/TGSic0onZvO0JoewIfdIickzA7A5Utic1QAUz8RddhibgKYPv2em7ahSfU64IicIU0yriaM1W74lQJpeE/150, .TimelineObject.sourceUserName=, .TimelineObject.ContentObject.description=https://ygkc.qianliao.tv/activity/page/redirect?liveId=360000022151734, .TimelineObject.createTime=1530060899, .TimelineObject.weappInfo.appUserName=, .TimelineObject.ContentObject.mediaList=, .TimelineObject.contentDescScene=0, .TimelineObject.ContentObject.mediaList.media.thumb.$type=1, .TimelineObject.contentattr=0, .TimelineObject.weappInfo.pagePath=, .TimelineObject.username=wxid_idbcv1rykc9u22, .TimelineObject.actionInfo=, .TimelineObject=, .TimelineObject.location.$poiAddress=, .TimelineObject.ContentObject.mediaList.media.url.$type=1, .TimelineObject.ContentObject.mediaList.media.size.$height=150.0, .TimelineObject.ContentObject.mediaList.media.size.$width=150.0, .TimelineObject.location.$poiClassifyId=, .TimelineObject.sourceNickName=, .TimelineObject.location.$poiClassifyType=0, .TimelineObject.ContentObject.mediaList.media=, .TimelineObject.ContentObject.contentStyle=3, .TimelineObject.ContentObject.mediaList.media.private=0, .TimelineObject.actionInfo.appMsg.mediaTagName=, .TimelineObject.canvasInfoXml=, .TimelineObject.contentDescShowType=0, .TimelineObject.appInfo.id=, .TimelineObject.actionInfo.appMsg.messageExt=, .TimelineObject.weappInfo=, .TimelineObject.location=, .TimelineObject.streamvideo.streamvideothumburl=, .TimelineObject.ContentObject.mediaList.media.size.$totalSize=17638.0, .TimelineObject.ContentObject.contentSubStyle=0}

.TimelineObject.username



06-28 09:47:27.286 23430-23430/? I/vxv: 消息信息:
类型:16777265
发送者:7384479817@chatroom
是否自发:0
内容:wxid_9x2a4rncnih522:
<msg><appmsg appid="" sdkver="0"><title>[踢] 文青翘楚　</title><des>[踢] 文青翘楚　</des><username></username><action>view</action><type>1</type><showtype>0</showtype><content>#b xintufendou*~无关链接~*</content><url></url><lowurl></lowurl><dataurl></dataurl><lowdataurl></lowdataurl><contentattr>0</contentattr><streamvideo><streamvideourl></streamvideourl><streamvideototaltime>0</streamvideototaltime><streamvideotitle></streamvideotitle><streamvideowording></streamvideowording><streamvideoweburl></streamvideoweburl><streamvideothumburl></streamvideothumburl><streamvideoaduxinfo></streamvideoaduxinfo><streamvideopublishid></streamvideopublishid></streamvideo><canvasPageItem><canvasPageXml><![CDATA[]]></canvasPageXml></canvasPageItem><appattach><attachid></attachid><cdnthumburl>305c020100045530530201000204ac4d40c002032f540602042e89903a02045b2a3fa8042e6175706170706d73675f373762656463633064653235313366395f313532393439353432383233375f36303437350204010800030201000400</cdnthumburl><cdnthumbmd5>707d2fb503ef9ef398b2deb5739f3b29</cdnthumbmd5><cdnthumblength>5749</cdnthumblength><cdnthumbheight>120</cdnthumbheight><cdnthumbwidth>120</cdnthumbwidth><cdnthumbaeskey>c2a2b394748f4322ae03b47c23152b90</cdnthumbaeskey><aeskey>c2a2b394748f4322ae03b47c23152b90</aeskey><encryver>1</encryver><fileext></fileext><islargefilemsg>0</islargefilemsg></appattach><extinfo></extinfo><androidsource>3</androidsource><thumburl></thumburl><mediatagname></mediatagname><messageaction><![CDATA[]]></messageaction><messageext><![CDATA[]]></messageext><emoticongift><packageflag>0</packageflag><packageid></packageid></emoticongift><emoticonshared><packageflag>0</packageflag><packageid></packageid></emoticonshared><designershared><designeruin>0</designeruin><designername>null</designername><designerrediretcturl>null</designerrediretcturl></designershared><emotionpageshared><tid>0</tid><title>null</title><desc>null</desc><iconUrl>null</iconUrl><secondUrl></secondUrl><pageType>0</pageType></emotionpageshared><webviewshared><shareUrlOriginal></shareUrlOriginal><shareUrlOpen></shareUrlOpen><jsAppId></jsAppId><publisherId></publisherId></webviewshared><template_id>0</template_id><md5>707d2fb503ef9ef398b2deb5739f3b29</md5><weappinfo><username></username><appid></appid><appservicetype>0</appservicetype></weappinfo><statextstr></statextstr><websearch></websearch></appmsg><fromusername>wxid_9x2a4rncnih522</fromusername><scene>0</scene><appinfo><version>1</version><appname></appname></appinfo><commenturl></commenturl></msg>
图片路径:THUMBNAIL_DIRPATH://th_7b21bcdf55db7c52b3c2a96690a48640
消息ID:25926
消息svrID:5951890282986866998


MMActivity

ChattingUI

LauncherUI

ui.chatting.y

com.tencent.mm.ui.chatting.j (6.6.7-1320) 链接解析 //(parse app message content fail)

com.tencent.mm.app.WorkerProfile$10 (6.6.7-1320) 消息重发 //(resendVideoMsg, msgId:%d, msgtime: %d, infotime:%d)

com.tencent.mm.modelcdntran.f (6.6.7-1320) //(download finish. totalLen %d)

(/cgi-bin/micromsg-bin/checkbigfiledownload)

msgSvrId
o.Pf().br(

/cgi-bin/micromsg-bin/mmsnscomment
/cgi-bin/micromsg-bin/mmsnsadcomment
/cgi-bin/micromsg-bin/uploadvideo
/cgi-bin/micromsg-bin/sendsight

/cgi-bin/micromsg-bin/getopenimcontact

com.tencent.mm.modelcdntran.f.g()//请求视频

com.tencent.mm.modelvoice.q
com.tencent.mm.modelvoice.f
com.tencent.xweb.WebView
com.tencent.mm.plugin.webwx.a.e

com.tencent.mm.plugin.sns.model.aq
com.tencent.mm.plugin.sns.model.u
com.tencent.mm.plugin.sns.model.y
com.tencent.mm.plugin.sns.model.r
