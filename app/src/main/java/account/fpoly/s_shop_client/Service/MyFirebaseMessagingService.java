package account.fpoly.s_shop_client.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import account.fpoly.s_shop_client.Application.MyApplication;
import account.fpoly.s_shop_client.DaGiao_activity;
import account.fpoly.s_shop_client.DangGiao_Activity;
import account.fpoly.s_shop_client.HuyBill;
import account.fpoly.s_shop_client.Modal.Notify;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Tab_Giaodien_Activity;
import account.fpoly.s_shop_client.Tools.ACCOUNT;
import account.fpoly.s_shop_client.Xacnhan_Bill;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = MyFirebaseMessagingService.class.getName();
    public static final String  TOKEN= "TOKEN";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.e("TAG", "onMessageReceived: " );
        Map<String,String> stringMap = message.getData();
        String title = stringMap.get("tilte");
        String body = stringMap.get("body");
        String status = stringMap.get("status");
        sendNotification(title,body,(status!=null)?Integer.parseInt(status):0);
        Notify notify = new Notify();
        notify.setId_user(ACCOUNT.user.get_id());
        notify.setContent(body);
        notify.setStatus((status!=null)?Integer.parseInt(status):0);
        ApiService.apiService.addNotify(notify).enqueue(new Callback<Notify>() {
            @Override
            public void onResponse(Call<Notify> call, Response<Notify> response) {
                if (response.isSuccessful()){
                    Toast.makeText(MyFirebaseMessagingService.this, "Đã thêm thông báo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Notify> call, Throwable t) {
                Toast.makeText(MyFirebaseMessagingService.this, t + "", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void sendNotification(String title, String body, int status) {
        Intent intent;
        if (status == 0) {
            intent = new Intent(this, Xacnhan_Bill.class);
        } else if (status == 2) {
            intent = new Intent(this, DangGiao_Activity.class);
        } else if (status == 3) {
            // Chú ý: Bạn có hai trạng thái == 3, nên chỉ một trong chúng sẽ được thực hiện.
            intent = new Intent(this, DaGiao_activity.class);
        } else if (status == 4) {
            intent = new Intent(this, HuyBill.class);
        } else  {
            intent = new Intent(this, Tab_Giaodien_Activity.class);
        }
        intent.putExtra("status",status);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.shopping)

                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(1, builder.build());
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN, token);
        editor.apply();
    }

}
