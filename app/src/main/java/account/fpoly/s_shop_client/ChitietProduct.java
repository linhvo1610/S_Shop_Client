package account.fpoly.s_shop_client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ChitietProduct extends AppCompatActivity {

    LinearLayout clickmua,chat;
    ImageView back;
    private  int sol = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_product);

        clickmua = findViewById(R.id.clickmua);
        back = findViewById(R.id.back);
        chat = findViewById(R.id.chat);
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
                EditText edsoluong = bottomView.findViewById(R.id.numberPickerQuantity);


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