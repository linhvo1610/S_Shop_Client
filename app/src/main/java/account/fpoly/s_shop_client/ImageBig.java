package account.fpoly.s_shop_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import account.fpoly.s_shop_client.API.API;

public class ImageBig extends AppCompatActivity {
    ImageView imageBig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_big);


        imageBig = findViewById(R.id.imageBig);
        ImageView back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        SharedPreferences sharedPreferencesImage = getSharedPreferences("image", MODE_PRIVATE);
        String link = sharedPreferencesImage.getString("link", null);

        Glide.with(getBaseContext()).load(API.api_image + link)
                .into(imageBig);

    }
}