package account.fpoly.s_shop_client.GiaoDien;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.API.API_Product;
import account.fpoly.s_shop_client.Message;
import account.fpoly.s_shop_client.Modal.ProductModal;
import account.fpoly.s_shop_client.Modal.UserModal;
import account.fpoly.s_shop_client.MuaProduct;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Service.ServiceProduct;
import account.fpoly.s_shop_client.adapter.ProductAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChitietProduct extends AppCompatActivity {

    LinearLayout clickmua,chat;
    ImageView back;
    TextView chitiet_tenProduct,chitiet_giaProduct,chitiet_description;

    private  int sol = 1;
    ProductModal productModal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_product);

        SharedPreferences sharedPreferences = getSharedPreferences("product", MODE_PRIVATE);


        clickmua = findViewById(R.id.clickmua);
        back = findViewById(R.id.back);
        chat = findViewById(R.id.chat);
        chitiet_giaProduct = findViewById(R.id.chitiet_giaProduct);
        chitiet_tenProduct = findViewById(R.id.chitiet_tenProduct);
        chitiet_description = findViewById(R.id.chitiet_description);


        String priceProduct = sharedPreferences.getString("giaProduct", null);
        chitiet_giaProduct.setText(priceProduct);
        String tenProduct = sharedPreferences.getString("tenProduct", null);
        chitiet_tenProduct.setText(tenProduct);
        String chitietProduct = sharedPreferences.getString("descriptionPro", null);
        chitiet_description.setText(chitietProduct);


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

                RadioButton checkBoxSize40 = bottomView.findViewById(R.id.checkBoxSize40);
                RadioButton checkBoxSize41 = bottomView.findViewById(R.id.checkBoxSize41);
                TextView buttonMinus = bottomView.findViewById(R.id.buttonMinus );
                TextView buttonPlus = bottomView.findViewById(R.id.buttonPlus);
                TextView totalQuantity = bottomView.findViewById(R.id.totalQuantity);
                EditText edsoluong = bottomView.findViewById(R.id.numberPickerQuantity);

                ImageView checkboxImgProduct = bottomView.findViewById(R.id.checkbox_ImageProduct);
                TextView checkboxPriceProduct = bottomView.findViewById(R.id.checkbox_PriceProduct);

                String priceProduct = sharedPreferences.getString("giaProduct", null);
                checkboxPriceProduct.setText(priceProduct);
                String totalQuantityProduct = sharedPreferences.getString("quantityPro", null);
                totalQuantity.setText(totalQuantityProduct);



                String imageProduct = sharedPreferences.getString("anhProduct", null);
                Glide.with(getApplicationContext()).load(imageProduct).into(checkboxImgProduct);


//                numberPicker.setText(String.valueOf(sol)); // Giá trị mặc định

                buttonMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentValue = Integer.parseInt(edsoluong.getText().toString());
                        if (currentValue > 1) {
                            edsoluong.setText(String.valueOf(currentValue - 1));
                        }
                    }
                });

                buttonPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentValue = Integer.parseInt(edsoluong.getText().toString());
                        edsoluong.setText(String.valueOf(currentValue + 1));
                    }
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

//                checkBoxSize40.setText(sizePro);

//--------------


                LinearLayout layout = bottomView.findViewById(R.id.checkboxLayout);

                ArrayList<String> selectedSizes = getIntent().getStringArrayListExtra("selectedSizes");
                Toast.makeText(ChitietProduct.this, "[] Size: "+ selectedSizes, Toast.LENGTH_SHORT).show();
                if (selectedSizes != null) {
                    // Tạo một mảng CheckBox để lưu trữ các ô checkbox
                    CheckBox[] checkBoxes = new CheckBox[selectedSizes.size()];

                    // Hiển thị danh sách size lên các ô checkbox
                    for (int i = 0; i < selectedSizes.size(); i++) {
                        String size = selectedSizes.get(i);
                        CheckBox checkBox = new CheckBox(getBaseContext());
                        checkBox.setText(size);
                        checkBoxes[i] = checkBox;
                        // Thêm checkBox vào layout của bạn
                        layout.addView(checkBox);
                    }

                    // Xử lý sự kiện khi người dùng chọn/deselect ô checkbox
                    for (CheckBox checkBox : checkBoxes) {
                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                // Xử lý logic khi người dùng chọn/deselect ô checkbox
                                // Ví dụ: lưu trạng thái của ô checkbox vào một danh sách khác
                            }
                        });
                    }
                } else {
                    // Xử lý khi danh sách size không tồn tại
                    // Ví dụ: Hiển thị thông báo lỗi
                    Toast.makeText(getBaseContext(), "Không có danh sách size", Toast.LENGTH_SHORT).show();
                }




//-------------


                checkBoxSize40.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            buttonView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.custom_checkbox));
                        } else {
                            buttonView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.custom_canlecheckbox)); // Hình ảnh cho trạng thái chưa chọn
                        }
                    }
                });
                checkBoxSize41.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            buttonView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.custom_checkbox));
                        } else {
                            buttonView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.custom_canlecheckbox)); // Hình ảnh cho trạng thái chưa chọn
                        }
                    }
                });
                bottomView.findViewById(R.id.imageclose).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.cancel();
                    }
                });
                bottomView.findViewById(R.id.muasp).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getBaseContext(), MuaProduct.class));
                    }
                });
                bottomSheetDialog.setContentView(bottomView);
                bottomSheetDialog.show();
            }


        });
    }


}