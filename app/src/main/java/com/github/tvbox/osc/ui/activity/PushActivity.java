package com.github.tvbox.osc.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.base.BaseActivity;
import com.github.tvbox.osc.server.ControlManager;
import com.github.tvbox.osc.ui.tv.QRCodeGen;
import com.github.tvbox.osc.util.DefaultConfig;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class PushActivity extends BaseActivity {
    private ImageView ivQRCode;
    private TextView tvAddress;
    private String wdName = "";
    @Override
    protected int getLayoutResID() {
        return R.layout.activity_push;
    }

    @Override
    protected void init() {
        initData();
        initView();
    }

    private void initView() {
        ivQRCode = findViewById(R.id.ivQRCode);
        tvAddress = findViewById(R.id.tvAddress);
        refreshQRCode();
        findViewById(R.id.pushLocal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushdo("push_agent");
            }
        });

        findViewById(R.id.pushLocalYiso).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushdo("ali_Yiso");
            }
        });
    }

    private void pushdo(String key){
        try {
            ClipboardManager manager = (ClipboardManager) PushActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
            if (manager != null) {
                if (manager.hasPrimaryClip() && manager.getPrimaryClip() != null && manager.getPrimaryClip().getItemCount() > 0) {
                    ClipData.Item addedText = manager.getPrimaryClip().getItemAt(0);
                    String clipText = addedText.getText().toString().trim();
                    clipText = DefaultConfig.getHttpUrl(clipText);
                    Intent newIntent = new Intent(mContext, DetailActivity.class);
                    newIntent.putExtra("id", clipText);
                    if (!wdName.isEmpty()) {
                        newIntent.putExtra("wdName", wdName);
                    }
                    newIntent.putExtra("sourceKey", key);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PushActivity.this.startActivity(newIntent);
                }
            }
        } catch (Throwable th) {

        }
    }

    private void refreshQRCode() {
        String address = ControlManager.get().getAddress(false);
        tvAddress.setText(String.format("手机/电脑扫描上方二维码或者直接浏览器访问地址\n%s", address));
        ivQRCode.setImageBitmap(QRCodeGen.generateBitmap(address, AutoSizeUtils.mm2px(this, 300), AutoSizeUtils.mm2px(this, 300), 4));
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            wdName = bundle.getString("wdName", "");
        }
    }
}