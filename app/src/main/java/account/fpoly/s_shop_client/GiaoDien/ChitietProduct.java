package account.fpoly.s_shop_client.GiaoDien;

import static java.lang.String.valueOf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.API.API_Product;
import account.fpoly.s_shop_client.CommentActivity;
import account.fpoly.s_shop_client.Message;
import account.fpoly.s_shop_client.Modal.ProductModal;
import account.fpoly.s_shop_client.Modal.UserModal;
import account.fpoly.s_shop_client.MuaProduct;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Service.ServiceProduct;
import account.fpoly.s_shop_client.adapter.ProductAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChitietProduct extends AppCompatActivity {

    LinearLayout clickmua,chat;
    ImageView back,chitiet_imgProduct;
    TextView chitiet_tenProduct,chitiet_giaProduct,chitiet_description;
    String idProduct;
    String imagePro;

    private  int sol = 1;
    int currentValue;
    int newValue;
    private boolean isDialogOpen = false;
    private boolean isRadioButtonSelected = false;
    DecimalFormat decimalFormat;
    TextView sluongMuaText;
    String sluongMuaSP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_product);


        SharedPreferences sharedPreferences = getSharedPreferences("product", MODE_PRIVATE);
        SharedPreferences sharedPreferencesSluong = getSharedPreferences("billPro", MODE_PRIVATE);
        sluongMuaSP = sharedPreferencesSluong.getString("sluongMua",null);
        decimalFormat = new DecimalFormat("#,###");

        clickmua = findViewById(R.id.clickmua);
        back = findViewById(R.id.back);
        chat = findViewById(R.id.chat);
        chitiet_giaProduct = findViewById(R.id.chitiet_giaProduct);
        chitiet_tenProduct = findViewById(R.id.chitiet_tenProduct);
        chitiet_description = findViewById(R.id.chitiet_description);
        chitiet_imgProduct = findViewById(R.id.chitiet_imgProduct);
        LinearLayout linearLayout= findViewById(R.id.Ln_danhgia);
        sluongMuaText = findViewById(R.id.sluongMua);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChitietProduct.this, CommentActivity.class);
                startActivity(intent);
            }
        });

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

        Picasso.get().load(API.api_image + imagePro).into(chitiet_imgProduct);


        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Message.class));
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
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext(),R.style.BottomSheetDialogTheme);
                View bottomView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_dialog,
                        (LinearLayout)findViewById(R.id.bottomSheetDialog));
                decimalFormat = new DecimalFormat("#,###");
                TextView buttonMinus = bottomView.findViewById(R.id.buttonMinus );
                TextView buttonPlus = bottomView.findViewById(R.id.buttonPlus);
                TextView totalQuantity = bottomView.findViewById(R.id.totalQuantity);
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

                String totalQuantityProduct = sharedPreferences.getString("quantityPro", null);
//                totalQuantity.setText(totalQuantityProduct);
                int qantityFormat = Integer.parseInt(totalQuantityProduct);
                String qantity = decimalFormat.format(qantityFormat);
                totalQuantity.setText(qantity);



//                String imageProduct = sharedPreferences.getString("anhProduct", null);
//                Glide.with(getApplicationContext()).load(imageProduct).into(checkboxImgProduct);

                RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.api +"product?_id="+ idProduct, null,
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
                                            int size = sizeItem.getInt("size");
                                            if (!sizeList.contains(size)) {
                                                sizeList.add(size); // Thêm size vào danh sách
                                            }
                                        }

                                        // Sắp xếp danh sách size từ thấp đến cao
                                        Collections.sort(sizeList);

                                        for (int k = 0; k < sizeList.size(); k++) {
                                            int size = sizeList.get(k);

                                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                            );
                                            params.setMargins(10, 0, 0, 0);

                                            RadioButton radioButton = new RadioButton(getBaseContext());
                                            radioButton.setId(View.generateViewId()); // Tạo ID duy nhất cho RadioButton
                                            radioButton.setText(valueOf(size));
                                            radioButton.setButtonDrawable(null);
                                            radioButton.setHeight(90);
                                            radioButton.setWidth(160);
                                            radioButton.setBackgroundResource(R.color.light_white);
                                            radioButton.setLayoutParams(params);
                                            radioButton.setTextSize(15);
                                            radioButton.setGravity(Gravity.CENTER);
                                            radioButton.setButtonTintList(ColorStateList.valueOf(Color.GRAY));
                                            // Thêm RadioButton vào RadioGroup hoặc ViewGroup tương ứng
                                            // Ví dụ: radioGroup.addView(radioButton);


                                            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                    isRadioButtonSelected = isChecked;
                                                    if (isChecked) {
//   SharedPreferences (size)
                                                        SharedPreferences sharedPreferences = getSharedPreferences("size", MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putString("size", valueOf(size));
                                                        Toast.makeText(ChitietProduct.this, "size: "+ size, Toast.LENGTH_SHORT).show();
                                                        editor.apply();
                                                        buttonView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.custom_checkbox));
                                                    } else {
//                                                            if (!isRadioButtonSelected){
//                                                                Toast.makeText(getBaseContext(), "Vui lòng chọn một kích cỡ !!!", Toast.LENGTH_SHORT).show();
//                                                                return;
//                                                            }
                                                        radioButton.setLayoutParams(params);
                                                        buttonView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.custom_canlecheckbox)); // Hình ảnh cho trạng thái chưa chọn
                                                    }
                                                }
                                            });

                                            radioGroup.addView(radioButton);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(jsonObjectRequest);

//                numberPicker.setText(String.valueOf(sol)); // Giá trị mặc định

                String soluong = edsoluong.getText().toString();
                if (soluong.isEmpty()) {
                    currentValue = 1; // Đặt giá trị mặc định là 1 nếu không có số được nhập vào
                }
                buttonMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (currentValue > 1) {
                            edsoluong.setText(valueOf(currentValue - 1));
                            newValue = currentValue - 1;
                            saveValueToSharedPreferences(newValue);
                        }
                    }
                });

                buttonPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        currentValue = Integer.parseInt(edsoluong.getText().toString());
                        newValue = currentValue + 1;
                        edsoluong.setText(valueOf(currentValue + 1));
                        saveValueToSharedPreferences(newValue);                    }
                });

                // ản con trỏ khi nhập xong
                edsoluong.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            edsoluong.clearFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(edsoluong.getWindowToken(), 0);
                            return true;
                        }
                        return false;
                    }
                });

                bottomView.findViewById(R.id.imageclose).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.cancel();
                        isDialogOpen = false;
                    }
                });
                bottomView.findViewById(R.id.muasp).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getBaseContext(), MuaProduct.class));
                        bottomSheetDialog.dismiss(); // Đóng bottomSheetDialog sau khi chọn một kích cỡ
                        isDialogOpen = false;
                    }
                });
                bottomSheetDialog.setContentView(bottomView);
                bottomSheetDialog.show();
                isDialogOpen = true;
            }
            private void saveValueToSharedPreferences(int value) {
// SharedPreferences ( quantityProduct )
                SharedPreferences sharedPreferences = getSharedPreferences("quantityProduct", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("quantity", valueOf(value));
                editor.apply();
            }


        });

    }

    int totalQuantityBill;
    private void listQuantity() {

        SharedPreferences sharedPreferencesBill = getSharedPreferences("product", MODE_PRIVATE);
        String idProbill = sharedPreferencesBill.getString("idProduct",null);
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.api + "billQu?status=Xác nhận&id_product=" + idProbill, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        totalQuantityBill = 0;
                        // Lặp qua từng đối tượng trong JSONArray
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(j);

                            // Lấy giá trị của totalQuantity từ mỗi đối tượng
                            int quantity = jsonObject.getInt("totalQuantity");

                            // Cộng dồn vào tổng quantity
                            totalQuantityBill += quantity;

                        }
                        sluongMuaText.setText(" "+totalQuantityBill);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Call API Fail", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}