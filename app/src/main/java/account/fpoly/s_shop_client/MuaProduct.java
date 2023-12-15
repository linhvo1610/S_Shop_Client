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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.API.API_Bill;
import account.fpoly.s_shop_client.Modal.Address;
import account.fpoly.s_shop_client.Modal.Bill;
import account.fpoly.s_shop_client.Modal.BillMore;
import account.fpoly.s_shop_client.Service.ApiService;
import account.fpoly.s_shop_client.Tools.ACCOUNT;
import account.fpoly.s_shop_client.Tools.ADDRESS;
import account.fpoly.s_shop_client.Tools.LIST;
import account.fpoly.s_shop_client.Tools.TOOLS;
import account.fpoly.s_shop_client.adapter.CartAdapter;
import account.fpoly.s_shop_client.fragment.GiohangFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.momo.momo_partner.AppMoMoLib;

public class MuaProduct extends AppCompatActivity {
    private BillMore billMore;
    private int total_product;
    LinearLayout oder;
    ImageView comeback;
    TextView tv_name, tv_phonenumber, tv_address;

    String name,price,quantity,size,fullname,phone,addressU,iduser,idPro,imagePro;
    TextView namePro,pricePro,quantityPro,sizePro,thanhtoanmomo,totalQuantity,thanhtien,totalprice,thanhtoan,tongPrice;
    ImageView listAdd;
    ImageView imageProduct;
    private Address address;
    LinearLayout btn_listAdd;
    private final int REQUESR_ADDRESS_CHOOSE = 777;
    int totalPrice;
    String formattedthanhtienPrice;

    private RecyclerView recyclerView;

    private String amount = "10000";
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "HoangNgoc";
    private String merchantCode = "MOMOC2IC20220510";
    private String merchantNameLabel = "Nhà cung cấp";
    private String description = "Thanh toán ";
    private boolean isChoosingMomo = false;


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
        totalQuantity = findViewById(R.id.totalQuantity);
        thanhtien = findViewById(R.id.thanhtien);
        totalprice = findViewById(R.id.totalPrice);
        thanhtoan = findViewById(R.id.thanhtoan);
        tongPrice = findViewById(R.id.tongPrice);
        thanhtoanmomo = findViewById(R.id.thanhtoanmomo);
        thanhtoanmomo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (billMore.getAddress() == null) {
                    Toast.makeText(MuaProduct.this, "Vui lòng chọn địa chỉ", Toast.LENGTH_SHORT).show();
                    return;
                }
                requestPayment(billMore.get_id());

            }
        });
         // AppMoMoLib.ENVIRONMENT.PRODUCTION

        SharedPreferences sharedPreferences1 = getSharedPreferences("size", MODE_PRIVATE);
        String sizeRadio = sharedPreferences1.getString("size",null);

        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(MuaProduct.this, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MuaProduct.this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
            }
        }
        recyclerView = findViewById(R.id.rcv_pay);
        getBill();
        showAddress();
        chooseAddress();
        layDulieu();
        showList();
        buy();
        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    AlertDialog dialog;
    private boolean isReturningFromMomo = false;

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            if (!isChoosingMomo) {
                isReturningFromMomo = true;
                startActivity(new Intent(getBaseContext(), Tab_Giaodien_Activity.class));
            }
        }


    }

    private SharedPreferences sharedPreferences;

    //Get token through MoMo app
    private void requestPayment(String iddonhang) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
            int tongtien = tongtienHang;


        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount",tongtien); //Kiểu integer
        eventValue.put("orderId", iddonhang); //uniqueue id cho BillId, giá trị duy nhất cho mỗi BILL
        eventValue.put("orderLabel", iddonhang); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
        eventValue.put("fee", "0"); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId",  merchantCode+"merchant_billId_"+System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);

    }
//    Get token callback from MoMo app an submit to server side





    @Override
    public void onResume() {
        super.onResume();

        // Thực hiện cập nhật dữ liệu ở đây
        layDulieu();
        if (isReturningFromMomo) {
            // Thực hiện các hành động cần thiết khi quay lại từ Momo
            // Ví dụ: Hiển thị thanh toán hoặc trang liên quan
            isReturningFromMomo = false; // Đặt lại trạng thái
        }
    }
 int tongtienHang;
    @SuppressLint("SetTextI18n")
    private void showAddress() {

        address = ADDRESS.aDefault(MuaProduct.this);
        if (address != null) {

            billMore.setName(address.getFullname());
            billMore.setPhone(address.getNumberphone());
            billMore.setAddress(address.getAddress() + ", " + address.getWards() + ", " + address.getDistrict() + ", " + address.getProvince() + ".");
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
                billMore.setName(address.getFullname());
                billMore.setPhone(address.getNumberphone());
                billMore.setAddress(address.getAddress() + ", " + address.getWards() + ", " + address.getDistrict() + ", " + address.getProvince() + ".");
                tv_name.setText(address.getFullname());
                tv_phonenumber.setText(address.getNumberphone());

                tv_address.setText(address.getAddress()+", "+address.getWards() + ", " + address.getDistrict() + ", " + address.getProvince());

            }
        } else if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getIntExtra("status", -1) == 0) {
                    // TOKEN IS AVAILABLE
                    Log.d("Thành công", data.getStringExtra("message"));
                    String token = data.getStringExtra("data"); // Token response
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if (env == null) {
                        env = "app";
                    }

                    if (token != null && !token.equals("")) {
                        // TODO: gửi phoneNumber và token đến phía máy chủ để xử lý thanh toán với máy chủ MoMo
                        // Nếu Momo nạp tiền thành công, tiếp tục xử lý đơn hàng của bạn
                        Log.d("Thành công", "onActivityResult: " );
                        ApiService.apiService.createBill(ACCOUNT.user.getToken(),billMore).enqueue(new Callback<BillMore>() {
                            @Override
                            public void onResponse(@NonNull Call<BillMore> call, @NonNull Response<BillMore> response) {
                                if (response.isSuccessful() ) {
                                    isChoosingMomo = true;
                                    if (LIST.listBuyCart != null && GiohangFragment.cartList != null) {
                                        if (LIST.listBuyCart.size() != GiohangFragment.cartList.size()) {
                                            for (int i = 0; i < GiohangFragment.cartList.size(); i++) {
                                                for (int j = 0; j < LIST.listBuyCart.size(); j++) {
                                                    if (GiohangFragment.cartList.get(i).get_id().equals(LIST.listBuyCart.get(j).get_id())) {
                                                        GiohangFragment.cartList.remove(i);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (LIST.listBuyCart != null) {
                                        LIST.listBuyCart.clear();
                                    }
                                    TOOLS.checkAllCarts = false;
//


//

                                }
                                else {

                                    Toast.makeText(MuaProduct.this, response.message()+"", Toast.LENGTH_SHORT).show();
                                }
                            }


                            @Override
                            public void onFailure(@NonNull Call<BillMore> call, @NonNull Throwable t) {
                                Toast.makeText(MuaProduct.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        AlertDialog.Builder builder = new AlertDialog.Builder(MuaProduct.this);
                        View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.dialog_thongbao, null);
                        builder.setView(view);
                        dialog = builder.create();

                        ImageView imageView = view.findViewById(R.id.imageView);
                        TextView title = view.findViewById(R.id.title);
                        title.setText("Bạn đã thanh toán thành công");
// Tạo hiệu ứng chuông rung
                        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", 0f, -15f, 20f, -15f, 0f);
                        animator.setDuration(3000);
                        animator.setInterpolator(new AccelerateDecelerateInterpolator());
                        animator.setRepeatCount(ObjectAnimator.INFINITE);
                        animator.start();
//                        requestPayment(billMore.get_id());
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                startActivity(new Intent(getBaseContext(), Tab_Giaodien_Activity.class));
                            }
                        }, 3000);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MuaProduct.this);
                        View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.dialog_thongbao, null);
                        builder.setView(view);
                        dialog = builder.create();

                        ImageView imageView = view.findViewById(R.id.imageView);
                        TextView title = view.findViewById(R.id.title);
                        title.setText("Thanh toán bị hủy bỏ");
// Tạo hiệu ứng chuông rung
                        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", 0f, -15f, 20f, -15f, 0f);
                        animator.setDuration(3000);
                        animator.setInterpolator(new AccelerateDecelerateInterpolator());
                        animator.setRepeatCount(ObjectAnimator.INFINITE);
                        animator.start();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                } else if (data.getIntExtra("status", -1) == 1) {
                    // TOKEN FAIL
                    AlertDialog.Builder builder = new AlertDialog.Builder(MuaProduct.this);
                    View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.dialog_thongbao, null);
                    builder.setView(view);
                    dialog = builder.create();

                    ImageView imageView = view.findViewById(R.id.imageView);
                    TextView title = view.findViewById(R.id.title);
                    title.setText("Thanh toán bị hủy bỏ");

// Tạo hiệu ứng chuông rung
                    ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", 0f, -15f, 20f, -15f, 0f);
                    animator.setDuration(3000);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setRepeatCount(ObjectAnimator.INFINITE);
                    animator.start();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                } else if (data.getIntExtra("status", -1) == 2) {
                    // TOKEN FAIL
                    AlertDialog.Builder builder = new AlertDialog.Builder(MuaProduct.this);
                    View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.dialog_thongbao, null);
                    builder.setView(view);
                    dialog = builder.create();

                    ImageView imageView = view.findViewById(R.id.imageView);
                    TextView title = view.findViewById(R.id.title);
                    title.setText("Thanh toán bị hủy bỏ");
// Tạo hiệu ứng chuông rung
                    ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", 0f, -15f, 20f, -15f, 0f);
                    animator.setDuration(3000);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setRepeatCount(ObjectAnimator.INFINITE);
                    animator.start();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                } else {
                    // TOKEN FAIL
                    AlertDialog.Builder builder = new AlertDialog.Builder(MuaProduct.this);
                    View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.dialog_thongbao, null);
                    builder.setView(view);
                    dialog = builder.create();

                    ImageView imageView = view.findViewById(R.id.imageView);
                    TextView title = view.findViewById(R.id.title);
                    title.setText("Thanh toán bị hủy bỏ");
// Tạo hiệu ứng chuông rung
                    ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", 0f, -15f, 20f, -15f, 0f);
                    animator.setDuration(3000);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setRepeatCount(ObjectAnimator.INFINITE);
                    animator.start();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            } else {
                Log.d(" ko Thành công", "Data là null");
            }
        } else {
            Log.d(" ko Thành công", "Result code hoặc request code không khớp");
        }

}

// cloud notification
    private void getBill() {
        billMore = (BillMore) getIntent().getSerializableExtra("billmore");
        total_product = billMore.getTotal();
//        thanhtien.setText( "đ" + total_product);
//        Toast.makeText(this, "price: "+ total_product, Toast.LENGTH_SHORT).show();
    }
    private void layDulieu() {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        SharedPreferences sharedPreferences = getSharedPreferences("product", MODE_PRIVATE);
        SharedPreferences sharedPreferencesSize = getSharedPreferences("size", MODE_PRIVATE);
        SharedPreferences sharedPreferencesQuantity = getSharedPreferences("quantityProduct", MODE_PRIVATE);
        SharedPreferences preferencesUser = getSharedPreferences("infoUser",MODE_PRIVATE);
        iduser = preferencesUser.getString("iduser",null);
        imagePro = sharedPreferences.getString("image", null);
        Log.d("imaaaaageeeeee",imagePro);
        fullname = preferencesUser.getString("fullname",null);
        phone = preferencesUser.getString("phone",null);
        addressU = preferencesUser.getString("addressUser",null);

        idPro = sharedPreferences.getString("idProduct",null);

        name = sharedPreferences.getString("tenProduct", null);
        price = sharedPreferences.getString("giaProduct", null);
        size = sharedPreferencesSize.getString("size", null);
        quantity = sharedPreferencesQuantity.getString("quantity", null);

        totalQuantity.setText(quantity);

        billMore = (BillMore) getIntent().getSerializableExtra("billmore");
        int total_products = billMore.getTotal();

        formattedthanhtienPrice = decimalFormat.format(total_products);
        thanhtien.setText( "đ" + formattedthanhtienPrice);
        totalprice.setText("đ" + formattedthanhtienPrice);


        int shipValue = Integer.parseInt("23400");
        int totalThanhtoan = total_products + shipValue;
        tongtienHang = totalThanhtoan;
        String formattedPrice = decimalFormat.format(totalThanhtoan);
        thanhtoan.setText( "đ" + formattedPrice);

        tongPrice.setText( "đ" + formattedPrice);
    }
    private void showList() {
        LinearLayoutManager manager = new LinearLayoutManager(MuaProduct.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(MuaProduct.this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        CartAdapter adapter = new CartAdapter(MuaProduct.this, true);
        recyclerView.setAdapter(adapter);
        adapter.setData(billMore.getList());
    }

    private void buy() {
        oder.setOnClickListener(v -> {
            if (billMore.getAddress() == null) {
                Toast.makeText(MuaProduct.this, "Vui lòng chọn địa chỉ", Toast.LENGTH_SHORT).show();
                return;
            }
            ApiService.apiService.createBill(ACCOUNT.user.getToken(),billMore).enqueue(new Callback<BillMore>() {
                @Override
                public void onResponse(@NonNull Call<BillMore> call, @NonNull Response<BillMore> response) {
                    if (response.isSuccessful() ) {
                        if (LIST.listBuyCart != null && GiohangFragment.cartList != null) {
                            if (LIST.listBuyCart.size() != GiohangFragment.cartList.size()) {
                                for (int i = 0; i < GiohangFragment.cartList.size(); i++) {
                                    for (int j = 0; j < LIST.listBuyCart.size(); j++) {
                                        if (GiohangFragment.cartList.get(i).get_id().equals(LIST.listBuyCart.get(j).get_id())) {
                                            GiohangFragment.cartList.remove(i);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (LIST.listBuyCart != null) {
                            LIST.listBuyCart.clear();
                        }
                        TOOLS.checkAllCarts = false;
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
                        }, 1000);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                    }
                    else {

                    }
                }


                @Override
                public void onFailure(@NonNull Call<BillMore> call, @NonNull Throwable t) {
                }
            });
        });
    }

}