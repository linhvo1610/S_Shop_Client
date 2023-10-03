package account.fpoly.s_shop_client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Notification extends AppCompatActivity {
    ImageView comeback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        comeback= findViewById(R.id.comebackNotification);
        comeback.setOnClickListener(new View.OnClickListener(
        ) {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}