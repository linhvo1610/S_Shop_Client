package account.fpoly.s_shop_client.GiaoDien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import account.fpoly.s_shop_client.Modal.UserModal;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Service.ServiceUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DangKyActivity extends AppCompatActivity {
    Button btnDangKy;
    private TextInputEditText dangky_username, dangky_phone, dangky_password, dangky_email;
    private EditText dangky_dob, dangky_sex, dangky_fullname;
    private Retrofit retrofit;
    private ServiceUser serviceUser;
    private String url = "http://192.168.1.13:3000";
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

        btnDangKy = findViewById(R.id.btn_DangKy);
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

        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Không được để trống Username", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Không được để trống họ tên", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Không được để trống password", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Không được để trống email", Toast.LENGTH_SHORT).show();
        }else {
            serviceUser = retrofit.create(ServiceUser.class);
            Call<UserModal> call = serviceUser.dangkiUser(new UserModal(username, password, email, phone, dob, sex, fullname));
            call.enqueue(new Callback<UserModal>() {
                @Override
                public void onResponse(Call<UserModal> call, Response<UserModal> response) {
                    Toast.makeText(DangKyActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
                    startActivity(intent);

                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }

                @Override
                public void onFailure(Call<UserModal> call, Throwable t) {
                    Toast.makeText(DangKyActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}