package com.easyctrl.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.easyctrl.ldy.activity.LoginActivity;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.ldy.util.FileUtil;
import com.easyctrl.ldy.util.ZipUtil;
import com.easyctrl.manager.OrderManage;
import com.easyctrl.ui.base.BaseDialog;
import java.util.ArrayList;

public class FileListDialog extends BaseDialog implements OnItemClickListener {
    private ListView filelist;
    private Handler handler = new Handler();
    private ArrayList<String> paths;
    private ProgressDialog progressDialog;
    private SettingActivity settingActivity;

    private class FileAdapter extends BaseAdapter {

        public class ViewHolder {
            public TextView fileName;
        }

        private FileAdapter() {
        }

        public int getCount() {
            return FileListDialog.this.paths == null ? 0 : FileListDialog.this.paths.size();
        }

        public Object getItem(int position) {
            return FileListDialog.this.paths.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            String path = (String) FileListDialog.this.paths.get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = MainApplication.mInflater.inflate(R.layout.file_list_item, parent, false);
                viewHolder.fileName = (TextView) convertView.findViewById(R.id.fileName);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.fileName.setText(path);
            return convertView;
        }
    }

    public FileListDialog(Context context) {
        super(context);
        this.settingActivity = (SettingActivity) context;
        setContentView((int) R.layout.file_list);
        this.filelist = (ListView) findViewById(R.id.filelist);
        String[] files = this.settingActivity.baseFile.list();
        this.paths = new ArrayList();
        int i = 0;
        while (i < files.length) {
            int start = files[i].indexOf(46);
            if (start != -1 && files[i].substring(start + 1, files[i].length()).equals("zip")) {
                this.paths.add(files[i]);
            }
            i++;
        }
        this.progressDialog = new ProgressDialog(this.settingActivity);
        this.filelist.setAdapter(new FileAdapter());
        this.filelist.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
        this.progressDialog.show();
        this.progressDialog.setMessage("\u6b63\u5728\u6062\u590d\uff0c\u8bf7\u8010\u5fc3\u7b49\u5f85");
        try {
            MainApplication.threadPool.submit(new Runnable() {
                public void run() {
                    String fileName = (String) FileListDialog.this.paths.get(position);
                    ZipUtil.unZip(new StringBuilder(String.valueOf(FileListDialog.this.settingActivity.baseFile.getAbsolutePath())).append("/").append(fileName).toString(), FileListDialog.this.settingActivity.baseFile.getAbsolutePath());
                    String folder = fileName.substring(0, fileName.length() - 4);
                    try {
                        Thread.sleep(1000);
                        FileUtil.backupDatatoServerDATA(new StringBuilder(String.valueOf(FileListDialog.this.settingActivity.baseFile.getAbsolutePath())).append("/").append(folder).toString(), "ET_APP_DATA.DB");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(2000);
                        FileUtil.backupDatatoServerName(new StringBuilder(String.valueOf(FileListDialog.this.settingActivity.baseFile.getAbsolutePath())).append("/").append(folder).toString(), "ET_APP_NAME.DB");
                        Thread.sleep(2000);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    FileListDialog.this.handler.post(new Runnable() {
                        public void run() {
                            FileListDialog.this.progressDialog.dismiss();
                            FileListDialog.this.dismiss();
                            MainApplication.userManager.setCurrentBackStata(false);
                            FileListDialog.this.settingActivity.sendData(OrderManage.recovery());
                            FileListDialog.this.settingActivity.startActivity(new Intent(FileListDialog.this.settingActivity, LoginActivity.class));
                            Toast.makeText(FileListDialog.this.settingActivity, "\u6570\u636e\u6062\u590d\u5b8c\u6210", 0).show();
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
