package account.fpoly.s_shop_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import account.fpoly.s_shop_client.GiaoDien.DangNhapActivity;
import account.fpoly.s_shop_client.Service.ApiService;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassActivity extends AppCompatActivity {
    TextInputEditText ed_username, ed_otp, ed_newPass, ed_reNewPass;
    TextInputLayout ed_layoutName, ed_layoutOTP;
    LinearLayout ln_resetPass;
    Button btn_getOTP;
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        ed_username = findViewById(R.id.ed_username);
        ed_otp = findViewById(R.id.ed_otp);
        ed_layoutName = findViewById(R.id.ed_layoutname);
        ed_layoutOTP = findViewById(R.id.ed_layoutotp);
        ed_newPass = findViewById(R.id.ed_passnew);
        ed_reNewPass = findViewById(R.id.nhaplai);
        ln_resetPass = findViewById(R.id.ln_resetPass);
        btn_getOTP = findViewById(R.id.btnupdate);
        btn_back = findViewById(R.id.img_back_forgetPass);
        btn_back.setOnClickListener(view -> {
            onBackPressed();
        });

        btn_getOTP.setOnClickListener(view -> {
            if (btn_getOTP.getText().toString().equals("Lấy mã")){
                SharedPreferences sharedPreferences = getSharedPreferences("namerepass", getBaseContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", ed_username.getText().toString());
                editor.apply();

                String usernameJson = "{\"username\": \"" + ed_username.getText().toString() + "\"}";

                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), usernameJson);

                ApiService.apiService.sendOTP(requestBody).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(ForgetPassActivity.this, "Hãy kiểm tra email!", Toast.LENGTH_SHORT).show();
                            ed_layoutName.setVisibility(View.GONE);
                            ed_layoutOTP.setVisibility(View.VISIBLE);
                            btn_getOTP.setText("Tiếp tục");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ForgetPassActivity.this, t+"", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (btn_getOTP.getText().toString().equals("Tiếp tục")) {
                String tokenJson = "{\"otp\": \"" + ed_otp.getText().toString() + "\"}";

                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), tokenJson);

                ApiService.apiService.verifyOTP(requestBody).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(ForgetPassActivity.this, "OPT đúng!!!", Toast.LENGTH_SHORT).show();
                            ed_layoutOTP.setVisibility(View.GONE);
                            ln_resetPass.setVisibility(View.VISIBLE);
                            btn_getOTP.setText("Cập nhật");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ForgetPassActivity.this, t+"", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("namerepass", getBaseContext().MODE_PRIVATE);
                String username = sharedPreferences.getString("name", null);

                String newPassJson = "{\"username\": \"" + username + "\"," +
                        " \"newPassword\": \"" + ed_newPass.getText().toString() + "\"}";
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), newPassJson);

                ApiService.apiService.resetPassword(requestBody).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(ForgetPassActivity.this, "Đã đổi thành công!!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgetPassActivity.this, DangNhapActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ForgetPassActivity.this, t+"", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }
}