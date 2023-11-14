package account.fpoly.s_shop_client;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.API.API_Bill;
import account.fpoly.s_shop_client.Modal.Address;
import account.fpoly.s_shop_client.Modal.Bill;
import account.fpoly.s_shop_client.Tools.ADDRESS;
import retrofit2.Call;
import retrofit2.Callback;

public class MuaProduct extends AppCompatActivity {

    LinearLayout oder;
    ImageView comeback;
    TextView tv_name, tv_phonenumber, tv_address;

    String name,price,quantity,size,fullname,phone,addressU,iduser,idPro,imagePro;
    TextView namePro,pricePro,quantityPro,sizePro,totalQuantity,thanhtien,totalprice,thanhtoan,tongPrice;
    ImageView listAdd;
    ImageView imageProduct;
    private Address address;
    LinearLayout btn_listAdd;
    private final int REQUESR_ADDRESS_CHOOSE = 777;
    int totalPrice;
    String formattedthanhtienPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mua_product);

        oder = findViewById(R.id.oder);
        comeback = findViewById(R.id.comeback);
        btn_listAdd = findViewById(R.id.btn_listAdd);
        tv_name = findViewById(R.id.tv_name);
        tv_phonenumber = findViewById(R.id.tv_phonenumber);
        tv_address = findViewById(R.id.tv_address);

        pricePro = findViewById(R.id.pricePro);
        quantityPro = findViewById(R.id.quantityPro);
        namePro = findViewById(R.id.namePro);
        sizePro = findViewById(R.id.sizePro);
        totalQuantity = findViewById(R.id.totalQuantity);
        thanhtien = findViewById(R.id.thanhtien);
        totalprice = findViewById(R.id.totalPrice);
        thanhtoan = findViewById(R.id.thanhtoan);
        tongPrice = findViewById(R.id.tongPrice);
        imageProduct = findViewById(R.id.imagePro);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(MuaProduct.this, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MuaProduct.this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
            }
        }




        showAddress();
        chooseAddress();
        layDulieu();
        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        oder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postBill();

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.dialog_thongbao, null);
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
                        startActivity(new Intent(getBaseContext(), Tab_Giaodien_Activity.class));
                    }
                }, 2500);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                makenotification();

            }
        });
    }
    private SharedPreferences sharedPreferences;
    private void postBill() {
        sharedPreferences = getSharedPreferences("address", MODE_PRIVATE);
        String idAddress = sharedPreferences.getString("idAddress", null);

        String staTer = "Chờ xác nhận";
        String iddiachi = address.get_id();


        // Tạo một danh sách các id_product
        List<String> product = new ArrayList<>();
// Số lượng sản phẩm muốn mua
        int numberOfProducts = 1;
// Vòng lặp để tự động tăng số lượng sản phẩm
        for (int i = 1; i <= numberOfProducts; i++) {
            String idProduct = idPro;
            product.add(idProduct);
        }
        int quntity = Integer.parseInt(quantity);
        int sizebill = Integer.parseInt(size);
        double pricebill = Double.parseDouble(String.valueOf(totalPrice));

        API_Bill.apiBill.addBill(new Bill(staTer,iduser,product,iddiachi,quntity,pricebill,sizebill)).enqueue(new Callback<Bill>() {
            @Override
            public void onResponse(Call<Bill> call, retrofit2.Response<Bill> response) {
                Toast.makeText(getBaseContext(), "Them thanh cong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Bill> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Them that bai", Toast.LENGTH_SHORT).show();
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
        SharedPreferences preferencesUser = getSharedPreferences("infoUser",MODE_PRIVATE);
        iduser = preferencesUser.getString("iduser",null);
        imagePro = sharedPreferences.getString("image", null);
        Picasso.get().load(API.api_image + imagePro).into(imageProduct);


        fullname = preferencesUser.getString("fullname",null);
        phone = preferencesUser.getString("phone",null);
        addressU = preferencesUser.getString("addressUser",null);
//        tv_fullname.setText(fullname);
//        tv_phone.setText(phone);
//        tv_address.setText(address);

        idPro = sharedPreferences.getString("idProduct",null);

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
            totalPrice = priceValue * quantityValue;
//thành tiền
            formattedthanhtienPrice = decimalFormat.format(totalPrice);
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

    @SuppressLint("SetTextI18n")
    private void showAddress() {
        address = ADDRESS.aDefault(MuaProduct.this);
        if (address != null) {
            tv_name.setText(address.getFullname());
            tv_phonenumber.setText(address.getNumberphone());

            SharedPreferences sharedPreferencesAddress = getSharedPreferences("idAddress", MODE_PRIVATE);
            sharedPreferencesAddress.getString("idAddress", address.get_id());
            SharedPreferences.Editor editor = sharedPreferencesAddress.edit();
            editor.apply();

            tv_address.setText(address.getAddress()+", "+address.getWards() + ", " + address.getDistrict() + ", " + address.getProvince());
        }
    }
    private void chooseAddress() {
        btn_listAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MuaProduct.this, AddressActivity.class);
            intent.putExtra("choose", true);
            startActivityForResult(intent, REQUESR_ADDRESS_CHOOSE);
            overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
        });
    }
@SuppressLint("SetTextI18n")
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUESR_ADDRESS_CHOOSE) {
        if (resultCode == RESULT_OK) {
            assert data != null;
            address = (Address) data.getSerializableExtra("address");
            tv_name.setText(address.getFullname());
            tv_phonenumber.setText(address.getNumberphone());

            tv_address.setText(address.getAddress()+", "+address.getWards() + ", " + address.getDistrict() + ", " + address.getProvince());

        }
    }
}
// cloud notification
    public void  makenotification() {
        String chanID = "CHANE_ID";
        Intent intent = getIntent();
        String idproduct = intent.getStringExtra("id_product");
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), chanID);
        builder.setSmallIcon(R.drawable.logo_sshop)
        .setContentText("heloo")
        .setContentTitle("NOtification Title"+idproduct)
        .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
         intent = new Intent(getApplicationContext(), Notification.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0, intent,PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel    notificationChannel = notificationManager.getNotificationChannel(chanID);
            if (notificationChannel==null){
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chanID , "Some DES", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        notificationManager.notify(0,builder.build());

    }

}