package account.fpoly.s_shop_client.GiaoDien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.API.API_User;
import account.fpoly.s_shop_client.Modal.UserModal;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Service.ServiceUser;
import account.fpoly.s_shop_client.Tools.TOOLS;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DangKyActivity extends AppCompatActivity {
    Button btnDangKy;
    private TextInputEditText dangky_username, dangky_phone, dangky_password, dangky_email;
    private EditText dangky_dob, dangky_sex, dangky_fullname,register_img;
    private Retrofit retrofit;
    private ServiceUser serviceUser;
    private String url = API.api_reg;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        AnhXa();

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DangKy();

            }
        });
    }

    private void AnhXa() {
        dangky_username = findViewById(R.id.register_username);
        dangky_phone = findViewById(R.id.register_phone);
        dangky_password = findViewById(R.id.register_password);
        dangky_email = findViewById(R.id.register_email);

        dangky_dob = findViewById(R.id.register_dob);
        dangky_sex = findViewById(R.id.register_sex);
        dangky_fullname = findViewById(R.id.register_fullname);
        register_img = findViewById(R.id.register_img);

        btnDangKy = findViewById(R.id.btn_DangKy);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void DangKy() {
        retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();

        String username = dangky_username.getText().toString().trim();
        String phone = dangky_phone.getText().toString().trim();
        String password = dangky_password.getText().toString().trim();
        String email = dangky_email.getText().toString().trim();

        String dob = dangky_dob.getText().toString().trim();
        String sex = dangky_sex.getText().toString().trim();
        String fullname = dangky_fullname.getText().toString().trim();
        String image = register_img.getText().toString().trim();
        String token = TOOLS.getToken(DangKyActivity.this);;
        Pattern patternemail = Patterns.EMAIL_ADDRESS;


        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Không được để trống Username", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Không được để số điện thoại", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Không được để trống password", Toast.LENGTH_SHORT).show();
        }
        else if (password.length() < 6){
            Toast.makeText(this, "Password yếu vui lòng nhập lại!!!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Không được để trống email", Toast.LENGTH_SHORT).show();
        }
        else if (!patternemail.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
        }
        else if (!isValidPhoneNumber(phone)) {
            Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
        }
        else {
            serviceUser = retrofit.create(ServiceUser.class);
            Call<UserModal> call = serviceUser.dangkiUser(new UserModal(username, password, email, phone, dob, fullname, token));
            call.enqueue(new Callback<UserModal>() {
                @Override
                public void onResponse(Call<UserModal> call, Response<UserModal> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(DangKyActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }else{
                        if (response.code() == 409) {
                            Toast.makeText(DangKyActivity.this, "Tên tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 401) {
                            Spannable centeredText = new SpannableString("Đã có tài khoản sử dụng bằng email này");
                            centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, centeredText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            Toast toast = Toast.makeText(DangKyActivity.this,centeredText , Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        else if (response.code() == 403) {
                            Spannable centeredText = new SpannableString("Số điện thoại tài khoản đã tồn tại");
                            centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, centeredText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            Toast toast = Toast.makeText(DangKyActivity.this, centeredText, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        else {
                            Toast.makeText(DangKyActivity.this, "Đăng ký thất bại, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserModal> call, Throwable t) {

                }
            });
        }
    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Biểu thức chính quy cho số điện thoại bắt đầu bằng 0 và có tổng cộng 10 chữ số
        String phoneRegex = "^0[0-9]{9}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        return pattern.matcher(phoneNumber).matches();
    }

}