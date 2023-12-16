package account.fpoly.s_shop_client.GiaoDien;

import static java.lang.String.valueOf;

import static account.fpoly.s_shop_client.API.API_Product.gson;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.API.API_Product;
import account.fpoly.s_shop_client.CommentActivity;
import account.fpoly.s_shop_client.ContactUsActivity;
import account.fpoly.s_shop_client.ImageBig;
import account.fpoly.s_shop_client.Message;
import account.fpoly.s_shop_client.Modal.BillMore;
import account.fpoly.s_shop_client.Modal.Cart;
import account.fpoly.s_shop_client.Modal.ProductModal;
import account.fpoly.s_shop_client.Modal.UserModal;
import account.fpoly.s_shop_client.MuaProduct;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Service.ApiService;
import account.fpoly.s_shop_client.Service.ServiceProduct;
import account.fpoly.s_shop_client.Size_Chart;
import account.fpoly.s_shop_client.SplassActivity;
import account.fpoly.s_shop_client.TabCartActivity;
import account.fpoly.s_shop_client.Tab_Giaodien_Activity;
import account.fpoly.s_shop_client.Tools.ACCOUNT;
import account.fpoly.s_shop_client.adapter.ProductAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChitietProduct extends AppCompatActivity {

    LinearLayout clickmua, chat, btn_add_cart,hotro;
    ImageView back, chitiet_imgProduct,img_chuyen_gh;
    TextView chitiet_tenProduct, chitiet_giaProduct, chitiet_description,trademark,namecat,sizechart;
    String idProduct;
    String imagePro;
    Button add_Cart;



    private int sol = 1;
    int currentValue;
    int newValue = 1;
    private boolean isDialogOpen = false;
    private boolean isRadioButtonSelected = false;
    DecimalFormat decimalFormat;
    TextView sluongMuaText;
    String sluongMuaSP;
    int totalQuantityBills;
    LinearLayout imageContainer;
    String imagesJson;
    TextView testname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_product);

        testname =  findViewById(R.id.testname);
        testname.setVisibility(View.GONE);
//        SharedPreferences sharedPreferencesCart = getSharedPreferences("cartpro", MODE_PRIVATE);
//        String namecart = sharedPreferencesCart.getString("tenProduct",null);
//        // Check for null before setting the value to the TextView
//        if (namecart != null) {
//            testname.setText(namecart);
//        } else {
//            // Handle the case where the value is null, for example, set a default value or show an error message
//            testname.setText("Default Value"); // or testname.setText("Error: Value is null");
//        }



        SharedPreferences sharedPreferences = getSharedPreferences("product", MODE_PRIVATE);
        SharedPreferences sharedPreferencesSluong = getSharedPreferences("billPro", MODE_PRIVATE);
        sluongMuaSP = sharedPreferencesSluong.getString("sluongMua", null);
        imagesJson = sharedPreferences.getString("images",null);
        decimalFormat = new DecimalFormat("#,###");

        clickmua = findViewById(R.id.clickmua);
        img_chuyen_gh=findViewById(R.id.img_chuyen_gh);
        back = findViewById(R.id.back);
        chat = findViewById(R.id.chat);
        sizechart = findViewById(R.id.sizechart);
        hotro = findViewById(R.id.hotro);
        imageContainer = findViewById(R.id.imageContainer);

        chitiet_giaProduct = findViewById(R.id.chitiet_giaProduct);
        chitiet_tenProduct = findViewById(R.id.chitiet_tenProduct);
        chitiet_description = findViewById(R.id.chitiet_description);
        chitiet_imgProduct = findViewById(R.id.chitiet_imgProduct);
        trademark = findViewById(R.id.trademark);
        namecat = findViewById(R.id.namecat);
        LinearLayout linearLayout = findViewById(R.id.Ln_danhgia);
        sluongMuaText = findViewById(R.id.sluongMua);

        hotro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ContactUsActivity.class));
            }
        });

        sizechart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Size_Chart.class));
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChitietProduct.this, CommentActivity.class);
                startActivity(intent);
            }
        });
        img_chuyen_gh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChitietProduct.this, TabCartActivity.class));
            }
        });
        btn_add_cart = findViewById(R.id.btn_add_detail);

        listQuantity();

        String priceProduct = sharedPreferences.getString("giaProduct", null);
        idProduct = sharedPreferences.getString("idProduct", null);

//        chitiet_giaProduct.setText(priceProduct);
        int priceFormatPro = Integer.parseInt(priceProduct);
        String PricePro = decimalFormat.format(priceFormatPro);
        chitiet_giaProduct.setText(PricePro);

        String tenProduct = sharedPreferences.getString("tenProduct", null);
        chitiet_tenProduct.setText(tenProduct);
        String chitietProduct = sharedPreferences.getString("descriptionPro", null);
        chitiet_description.setText(chitietProduct);
        imagePro = sharedPreferences.getString("image", null);

        String trademarks = sharedPreferences.getString("trademark",null);
        String nameCat = sharedPreferences.getString("namecat",null);

        trademark.setText(trademarks);
        namecat.setText(nameCat);

        Picasso.get().load(API.api_image + imagePro).into(chitiet_imgProduct);

// lay anhhh
        Type type = new TypeToken<List<ProductModal.ImageItem>>(){}.getType();
        List<ProductModal.ImageItem> imageItems = gson.fromJson(imagesJson, type);
        if (imageItems != null) {
            for (ProductModal.ImageItem imageItem : imageItems) {
                String imageUrl = imageItem.getImage();
                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                int width = 3900;
                int height = 850;
                imageView.setLayoutParams(layoutParams);
                Glide.with(getBaseContext()).load(API.api_image + imageUrl)
                        .override(width, height)
                        .into(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getBaseContext(), ImageBig.class));
                        SharedPreferences sharedPreferencesImage = getSharedPreferences("image", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferencesImage.edit();
                        editor.putString("link", imageUrl);
                        editor.apply();
                    }
                });
                imageContainer.addView(imageView);
            }
        }
// --------------

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ContactUsActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        clickmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext(), R.style.BottomSheetDialogTheme);
                View bottomView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_dialog,
                        (LinearLayout) findViewById(R.id.bottomSheetDialog));
                decimalFormat = new DecimalFormat("#,###");
                TextView buttonMinus = bottomView.findViewById(R.id.buttonMinus);
                TextView buttonPlus = bottomView.findViewById(R.id.buttonPlus);
//                TextView totalQuantity = bottomView.findViewById(R.id.totalQuantity);
                EditText edsoluong = bottomView.findViewById(R.id.numberPickerQuantity);
                LinearLayout checkboxLayout = bottomView.findViewById(R.id.checkboxLayout);
                RadioGroup radioGroup = bottomView.findViewById(R.id.radioGroup);
                LinearLayout layoutQuantity = bottomView.findViewById(R.id.layoutQuantity);
                ImageView imageBotom = bottomView.findViewById(R.id.checkbox_ImageProduct);
                showBottomSheet(true);
            }
        });
        btn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet(false);
                add_Cart.setText("Thêm vào giỏ hàng");
            }
        });
    }
    private void listQuantity() {
        SharedPreferences sharedPreferencesQuantity = getSharedPreferences("product", MODE_PRIVATE);
        String id = sharedPreferencesQuantity.getString("idProduct",null);
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.api + "billQu?id_product=" + id + "&status=5", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    int totalQuantityBill = 0;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject billObject = jsonArray.getJSONObject(i);
                        JSONArray productList = billObject.getJSONArray("list");
                        for (int j = 0; j < productList.length(); j++) {
                            JSONObject product = productList.getJSONObject(j);

                            String idProductbill = product.getString("id_product");
                            if (idProductbill.equalsIgnoreCase(idProduct)) {
                                int quantity = product.getInt("quantity");
                                totalQuantityBill += quantity;
                            }
                        }
                    }
                    totalQuantityBills = totalQuantityBill;
                    sluongMuaText.setText(String.valueOf(totalQuantityBill));
                    soluongdamua = totalQuantityBill;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void saveValueToSharedPreferences(int value) {
// SharedPreferences ( quantityProduct )
        SharedPreferences sharedPreferences = getSharedPreferences("quantityProduct", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("quantity", valueOf(value));
        editor.apply();
    }
    int soluong,size;
    int soluongdamua;
    private void showBottomSheet(boolean buyNow) {
        SharedPreferences sharedPreferences = getSharedPreferences("product", MODE_PRIVATE);
        decimalFormat = new DecimalFormat("#,###");



        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ChitietProduct.this, R.style.BottomSheetDialogTheme);
        View bottomView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_dialog,
                (LinearLayout) findViewById(R.id.bottomSheetDialog));
        decimalFormat = new DecimalFormat("#,###");
        LinearLayout layoutSize = bottomView.findViewById(R.id.layoutSize);
        TextView totalQuantitySizes = bottomView.findViewById(R.id.totalQuantitySize);

        TextView buttonMinus = bottomView.findViewById(R.id.buttonMinus);
        TextView buttonPlus = bottomView.findViewById(R.id.buttonPlus);
        TextView totalQuantity = bottomView.findViewById(R.id.totalQuantitys);
        EditText edsoluong = bottomView.findViewById(R.id.numberPickerQuantity);
        LinearLayout checkboxLayout = bottomView.findViewById(R.id.checkboxLayout);
        RadioGroup radioGroup = bottomView.findViewById(R.id.radioGroup);
        LinearLayout layoutQuantity = bottomView.findViewById(R.id.layoutQuantity);
        ImageView imageBotom = bottomView.findViewById(R.id.checkbox_ImageProduct);

        ImageView checkboxImgProduct = bottomView.findViewById(R.id.checkbox_ImageProduct);
        TextView checkboxPriceProduct = bottomView.findViewById(R.id.checkbox_PriceProduct);

        Picasso.get().load(API.api_image + imagePro).into(imageBotom);

        String priceProduct = sharedPreferences.getString("giaProduct", null);
        int priceFormat = Integer.parseInt(priceProduct);
        String Price = decimalFormat.format(priceFormat);
        checkboxPriceProduct.setText(Price);

        String priceImport = sharedPreferences.getString("importPrice", null);
        int priceFormatImport = Integer.parseInt(priceImport);

        String totalQuantityProduct = sharedPreferences.getString("quantityPro", null);
        int qantityFormat = Integer.parseInt(totalQuantityProduct);



        int resul = qantityFormat ;
        String qantity = decimalFormat.format(resul);
        totalQuantity.setText(qantity);

//        String imageProduct = sharedPreferences.getString("anhProduct", null);
//        Glide.with(getApplicationContext()).load(imageProduct).into(checkboxImgProduct);

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.api + "product?_id=" + idProduct, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray dataArray = response.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject item = dataArray.getJSONObject(i);

                                List<Integer> sizeList = new ArrayList<>(); // Sử dụng List để lưu trữ các size

                                JSONArray sizesArray = item.getJSONArray("sizes");
                                for (int j = 0; j < sizesArray.length(); j++) {
                                    JSONObject sizeItem = sizesArray.getJSONObject(j);
                                    size = sizeItem.getInt("size");
                                    soluong = sizeItem.getInt("quantity");
                                    if (!sizeList.contains(size)) {
                                        sizeList.add(size); // Thêm size vào danh sách
                                    }
                                }

                                // Sắp xếp danh sách size từ thấp đến cao
                                Collections.sort(sizeList);

                                for (int k = 0; k < sizeList.size(); k++) {
                                    final int currentSize = sizeList.get(k);
                                    int currentQuantity = 0;
                                    String currentID = null;
                                    for (int m = 0; m < sizesArray.length(); m++) {
                                        JSONObject sizeItem = sizesArray.getJSONObject(m);
                                        if (sizeItem.getInt("size") == currentSize) {
                                            currentQuantity = sizeItem.getInt("quantity");
                                            currentID = sizeItem.getString("_id");
                                            break;
                                        }
                                    }

                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );
                                    params.setMargins(10, 0, 0, 0);
                                    RadioButton radioButton = new RadioButton(getBaseContext());
                                    radioButton.setId(View.generateViewId());
                                    radioButton.setText(String.valueOf(currentSize));
                                    radioButton.setButtonDrawable(null);
                                    radioButton.setHeight(90);
                                    radioButton.setWidth(160);
                                    radioButton.setBackgroundResource(R.color.colorAccrnt);
                                    radioButton.setLayoutParams(params);
                                    radioButton.setTextSize(15);
                                    radioButton.setGravity(Gravity.CENTER);
                                    radioButton.setButtonTintList(ColorStateList.valueOf(Color.GRAY));

                                    SharedPreferences sharedPreferences1 = getSharedPreferences("size", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                                    editor.clear();
                                    editor.apply();

                                    int finalCurrentQuantity = currentQuantity;
                                    String finalCurrentID = currentID;

                                    radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                        if (isChecked) {
                                            layoutSize.setVisibility(View.VISIBLE);
                                            totalQuantitySizes.setText(String.valueOf(finalCurrentQuantity));

                                            SharedPreferences sharedPreferencesCheckQuantity = getSharedPreferences("soluongSize", MODE_PRIVATE);
                                            SharedPreferences.Editor editors = sharedPreferencesCheckQuantity.edit();
                                            editors.putInt("soluongSize", finalCurrentQuantity);

                                            editors.apply();
                                            editor.putString("size", valueOf(currentSize));
                                            editor.putString("totalQuantitySize", valueOf(finalCurrentQuantity));
                                            editor.putString("totalSizeID", valueOf(finalCurrentID));
                                            editor.apply();
                                            buttonView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.custom_checkbox));
                                        } else{
                                            buttonView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.custom_canlecheckbox));
                                        }

                                        radioButton.setLayoutParams(params);
                                    });
                                    if (!radioButton.isChecked()) {
                                        radioButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.custom_checkbox));
                                    }
                                    radioGroup.addView(radioButton);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, error -> {
        });
        requestQueue.add(jsonObjectRequest);
        buttonMinus.setOnClickListener(v -> {
            currentValue = Integer.parseInt(edsoluong.getText().toString());
            if (currentValue > 1) {
                edsoluong.setText(valueOf(currentValue - 1));
                newValue = currentValue - 1;
                saveValueToSharedPreferences(newValue);
            }
        });
        String soluong = edsoluong.getText().toString();
        if (soluong.isEmpty()) {
            currentValue = 1; // Đặt giá trị mặc định là 1 nếu không có số được nhập vào
        }

        buttonPlus.setOnClickListener(v -> {
            currentValue = Integer.parseInt(edsoluong.getText().toString());
            newValue = currentValue + 1;
            edsoluong.setText(valueOf(currentValue + 1));
            saveValueToSharedPreferences(newValue);
        });

        // ản con trỏ khi nhập xong
        edsoluong.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                edsoluong.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edsoluong.getWindowToken(), 0);
                return true;
            }
            return false;
        });

        bottomView.findViewById(R.id.imageclose).setOnClickListener(view -> {
            bottomSheetDialog.cancel();
            isDialogOpen = false;
        });
        bottomSheetDialog.setContentView(bottomView);
        bottomSheetDialog.show();
        isDialogOpen = true;

        add_Cart = bottomView.findViewById(R.id.muasp);

        add_Cart.setOnClickListener(v -> {
//            runOnUiThread(() -> {
//                add_Cart.setText("Thêm vào giỏ hàng");
//            });
            Cart cart = new Cart();

            if (ACCOUNT.user == null){
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.dialog_yeucau_login, null);
                builder.setView(view);
                AlertDialog dialog = builder.create();
                TextView dong,login;
                ImageView imageView = view.findViewById(R.id.imageView);
                dong = view.findViewById(R.id.dongs);
                login = view.findViewById(R.id.login);
// Tạo hiệu ứng chuông rung
                ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", 0f, -15f, 20f, -15f, 0f);
                animator.setDuration(1000);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setRepeatCount(ObjectAnimator.INFINITE);
                animator.start();

                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getBaseContext(), SplassActivity.class));
                    }
                });
                dong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return;
            }

                cart.setId_user(ACCOUNT.user.get_id());
                cart.setId_product(idProduct);
                SharedPreferences sharedPreferences1 = getSharedPreferences("size", MODE_PRIVATE);
                String sizeString = sharedPreferences1.getString("size", null);

                if (sizeString != null && !sizeString.isEmpty()) {
                    int size = Integer.parseInt(sizeString);
                    SharedPreferences sharedPreferencesCheckQuantity = getSharedPreferences("soluongSize", MODE_PRIVATE);
                    int soluongSize = sharedPreferencesCheckQuantity.getInt("soluongSize",0);
                    Log.e( "showBottomSheet: ", newValue+"");
                    currentValue = Integer.parseInt(edsoluong.getText().toString());
                    newValue = currentValue;
                    saveValueToSharedPreferences(newValue);
                    if (soluongSize == 0){
                        Toast.makeText(this, "Sản phẩm đã hết hàng!!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (newValue > soluongSize){
                        Toast.makeText(this, "Sản phẩm quá số lượng ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!buyNow) {
                        cart.setName_product(sharedPreferences.getString("tenProduct", null));
                        cart.setPrice_product(priceFormat);
                        cart.setImage(sharedPreferences.getString("anhProduct", null));
                        cart.setQuantity(newValue);
                        cart.setSize(size);
                        cart.setImportPrice(priceFormatImport);
                        ApiService.apiService.addCart(cart).enqueue(new Callback<Cart>() {
                            @Override
                            public void onResponse(@NonNull Call<Cart> call, @NonNull retrofit2.Response<Cart> response) {
                                if (response.isSuccessful()) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                                    View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.dialog_check, null);
                                    builder.setView(view);
                                    AlertDialog dialog = builder.create();
                                    ImageView imageView = view.findViewById(R.id.imageView);
                                    TextView title = view.findViewById(R.id.title);

                                    title.setText("Đã thêm vào giỏ hàng!");
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
                                        }
                                    }, 1700);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.show();
                                    bottomSheetDialog.dismiss();
                                }

                            }

                            @Override
                            public void onFailure(@NonNull Call<Cart> call, @NonNull Throwable t) {
                                bottomSheetDialog.dismiss();
                            }
                        });
                    } else {
                        BillMore billMore = new BillMore();
                        billMore.setId_user(ACCOUNT.user.get_id());
                        cart.setName_product(sharedPreferences.getString("tenProduct", null));
                        cart.setPrice_product(priceFormat);
                        cart.setImage(sharedPreferences.getString("anhProduct", null));
                        cart.setQuantity(newValue);
                        cart.setSize(size);
                        cart.setImportPrice(priceFormatImport);
                        List<Cart> list = new ArrayList<>();
                        list.add(cart);
                        billMore.setList(list);
                        billMore.setStatus(0);
                        billMore.setImportPrice(priceFormatImport);
                        billMore.setTotal(cart.getQuantity() * cart.getPrice_product());
                        Intent intent = new Intent(ChitietProduct.this, MuaProduct.class);
                        intent.putExtra("billmore", billMore);
                        intent.putExtra("buy",true);
                        startActivity(intent);
                        overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
                        bottomSheetDialog.dismiss(); // Đóng bottomSheetDialog sau khi chọn một kích cỡ
                        isDialogOpen = false;
                    }
                } else {
                    Toast.makeText(this, "Hãy chon size", Toast.LENGTH_SHORT).show();
            }

        });
        bottomSheetDialog.setContentView(bottomView);
        bottomSheetDialog.show();
        isDialogOpen = true;
    }

}