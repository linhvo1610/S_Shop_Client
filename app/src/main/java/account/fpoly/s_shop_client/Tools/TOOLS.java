package account.fpoly.s_shop_client.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.R;

public class TOOLS {
    public static final String doMainDevice = API.TOOl;
    public static final String  USER= "USER";
    public static final String  DEFAULT_ADDRESS= "DEFAULT_ADDRESS";
    public static final String  TOKEN= "TOKEN";

    private static final Gson gson = new Gson();

    public static String getDefaulAddress(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DEFAULT_ADDRESS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(DEFAULT_ADDRESS,null);
    }

    public static Dialog createDialog(Context context) {
        Dialog dialog = new Dialog(context);
        @SuppressLint("InflateParams")
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.layout_watting, null);
        Glide.with(context).asGif().load(R.drawable.spin).into((ImageView) view.findViewById(R.id.imv_watting));
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    public static void clearDefaulAddress(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DEFAULT_ADDRESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void saveDefaulAddress(Context context,String id_address){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DEFAULT_ADDRESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DEFAULT_ADDRESS, id_address);
        editor.apply();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "\\d{10}";
        return !phoneNumber.matches(regex);
    }
}
