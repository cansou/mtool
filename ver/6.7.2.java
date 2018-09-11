final void chm() {
    if (this.rsn.isEmpty()) {
        this.rtY = true;
        g.Df().dAN.a(881, this);
        g.Df().dAN.a(new com.tencent.mm.openim.b.h((String) this.rtW.getFirst(), "", ""), 0);
        return;
    }
    g.Df().dAN.a(new o((String) this.rtW.getFirst(), this.content, (String) this.rsn.getFirst()), 0);
}

public final void onSceneEnd(int i, int i2, String str, m mVar) {
    if (mVar.getType() == 881) {
        g.Df().dAN.b(881, this);
        if (i != 0 || i2 != 0) {
            Toast.makeText(this.context, this.context.getString(a.h.sendrequest_send_fail), 1).show();
        } else if (this.rtY) {
            g.Df().dAN.a(new o((String) this.rtW.getFirst(), this.content, ((com.tencent.mm.openim.b.h) mVar).eHC.rMO), 0);
        }
        this.rtY = false;
    } else if (mVar.getType() == 30 || mVar.getType() == com.tencent.mm.plugin.appbrand.jsapi.i.g.CTRL_INDEX) {
        y.d("MicroMsg.SendVerifyRequest", "onSceneEnd, errType = " + i + ", errCode = " + i2);
        if (this.tipDialog != null) {
            this.tipDialog.dismiss();
            this.tipDialog = null;
        }
        onStop();
        if (i == 0 && i2 == 0) {
            if (this.rtX) {
                com.tencent.mm.ui.base.h.by(this.context, this.context.getString(a.h.sendrequest_send_success));
            }
            this.rtV.bl(true);
            return;
        }
        if (i == 4 && i2 == -34) {
            str = this.context.getString(a.h.fmessage_request_too_offen);
        } else if (i == 4 && i2 == -94) {
            str = this.context.getString(a.h.fmessage_user_not_support);
        } else if (!(i == 4 && i2 == -24 && !bj.bl(str)) && (i != 4 || bj.bl(str))) {
            str = this.context.getString(a.h.sendrequest_send_fail);
        }
        if (this.rtX) {
            Toast.makeText(this.context, str, 1).show();
        }
        this.rtV.bl(false);
    } else {
        y.w("MicroMsg.SendVerifyRequest", "not expected scene,  type = " + mVar.getType());
    }
}