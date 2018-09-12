package com.easyctrl.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ui.base.BaseDialog;

public class UpdateFRDDialog extends BaseDialog implements OnClickListener {
    private static UpdateFRDDialog instance;
    private TextView cancel = ((TextView) findViewById(R.id.cancel));
    private TextView confirm = ((TextView) findViewById(R.id.confirm));
    private EditText content = ((EditText) findViewById(R.id.content));
    private DialogSaveListener onSaveListener;
    private TextView title = ((TextView) findViewById(R.id.title));

    public interface DialogSaveListener {
        void onSaveOrUpdate(String str);
    }

    public UpdateFRDDialog(Context context) {
        super(context);
        setContentView((int) R.layout.add_content);
        this.cancel.setOnClickListener(this);
        this.confirm.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            dismiss();
        } else if (v.getId() == R.id.confirm) {
            this.onSaveListener.onSaveOrUpdate(this.content.getText().toString());
            dismiss();
        }
    }

    public void setHint(String hint) {
        this.content.setHint(hint);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setContent(String str) {
        this.content.setText(str);
    }

    public void setOnSaveListener(DialogSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }
}
