package com.ad.tibi.lib.helper.inter;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ad.tibi.lib.util.DensityUtils;
import com.ad.tibi.lib.util.UIUtils;

public class CustomDialog extends Dialog {
    private static int default_width = -1; // 默认宽度
    private static int default_height = -1;// 默认高度

    public CustomDialog(Context context, View layout, int style) {
        this(context, default_width, default_height, layout, style);
    }

    public CustomDialog(Context context, int width, int height, View layout, int style) {
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        if (width > 0) {
            params.width = DensityUtils.dp2px(context, width);
        }
        if (height > 0) {
            params.height = DensityUtils.dp2px(context, height);
        }
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }
}
