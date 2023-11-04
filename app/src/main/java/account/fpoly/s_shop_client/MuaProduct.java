package account.fpoly.s_shop_client;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

import account.fpoly.s_shop_client.GiaoDien.ChitietProduct;

public class MuaProduct extends AppCompatActivity {

    LinearLayout oder;
    ImageView comeback;
    TextView namePro,pricePro,quantityPro,sizePro,totalQuantity,thanhtien,totalprice,thanhtoan,tongPrice;
    String name,price,quantity,size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mua_product);

        anhxa();
        layDulieu();

        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getBaseContext(), ChitietProduct.class));
                onBackPressed();
            }
        });
        oder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.dialog_thongbao,null);
                builder.setView(view);
                AlertDialog dialog = builder.create();

                ImageView imageView = view.findViewById(R.id.imageView);
// Tạo hiệu ứng chuông rung
                ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", 0f, -15f, 20f, -15f, 0f);
                animator.setDuration(1000);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setRepeatCount(ObjectAnimator.INFINITE);
                animator.start();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        startActivity(new Intent(getBaseContext(),Tab_Giaodien_Activity.class));
                    }
                }, 2500);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Thực hiện cập nhật dữ liệu ở đây
        layDulieu();
    }
    private void layDulieu() {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        SharedPreferences sharedPreferences = getSharedPreferences("product", MODE_PRIVATE);
        SharedPreferences sharedPreferencesSize = getSharedPreferences("size", MODE_PRIVATE);
        SharedPreferences sharedPreferencesQuantity = getSharedPreferences("quantityProduct", MODE_PRIVATE);

        name = sharedPreferences.getString("tenProduct", null);
        price = sharedPreferences.getString("giaProduct", null);
        size = sharedPreferencesSize.getString("size", null);
        quantity = sharedPreferencesQuantity.getString("quantity", null);
        quantityPro.setText("x"+ quantity);
        totalQuantity.setText(quantity);
        sizePro.setText(" " + size);
        namePro.setText(name);
        int priceFormat = Integer.parseInt(price);
        String Price = decimalFormat.format(priceFormat);
        pricePro.setText("đ" + Price);

        if (price != null && quantity != null) {
            int priceValue = Integer.parseInt(price);
            int quantityValue = Integer.parseInt(quantity);
            int totalPrice = priceValue * quantityValue;
//thành tiền
            String formattedthanhtienPrice = decimalFormat.format(totalPrice);
            thanhtien.setText( "đ" + formattedthanhtienPrice);
            totalprice.setText( "đ" + formattedthanhtienPrice);
// tổng thanh toán
            int shipValue = Integer.parseInt("23400");
            int totalThanhtoan = totalPrice + shipValue;
            String formattedPrice = decimalFormat.format(totalThanhtoan);
            thanhtoan.setText( "đ" + formattedPrice);
            tongPrice.setText( "đ" + formattedPrice);
        }
    }
    private void anhxa() {
        oder = findViewById(R.id.oder);
        comeback = findViewById(R.id.comeback);
        pricePro = findViewById(R.id.pricePro);
        quantityPro = findViewById(R.id.quantityPro);
        namePro = findViewById(R.id.namePro);
        sizePro = findViewById(R.id.sizePro);
        totalQuantity = findViewById(R.id.totalQuantity);
        thanhtien = findViewById(R.id.thanhtien);
        totalprice = findViewById(R.id.totalPrice);
        thanhtoan = findViewById(R.id.thanhtoan);
        tongPrice = findViewById(R.id.tongPrice);

    }

}