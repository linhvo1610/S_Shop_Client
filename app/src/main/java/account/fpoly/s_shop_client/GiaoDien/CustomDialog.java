package account.fpoly.s_shop_client.GiaoDien;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import account.fpoly.s_shop_client.R;

public class CustomDialog extends Dialog {
    public CustomDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_custom_dialog);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT; // Đặt độ lớn theo chiều rộng
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT; // Đặt độ lớn theo chiều cao
        layoutParams.gravity = Gravity.RIGHT | Gravity.TOP; // Đặt dialog ở bên trái
        layoutParams.x = 0; // Đặt vị trí theo trục X
        layoutParams.y = 0;
        window.setAttributes(layoutParams);
        // Set dialog mở ra theo hướng từ trái sang phải
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

    }
}
