package account.fpoly.s_shop_client;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import account.fpoly.s_shop_client.API.API_User;
import account.fpoly.s_shop_client.Modal.UserModal;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Update_PassWord extends AppCompatActivity {

    Button btnupdate;
    TextInputEditText ed_passcu,ed_passnew,nhaplai;
    String password,iduser;
    ImageView img_back_updatePass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pass_word);

        nhaplai = findViewById(R.id.nhaplai);
        btnupdate = findViewById(R.id.btnupdate);
        ed_passnew = findViewById(R.id.ed_passnew);
        ed_passcu = findViewById(R.id.ed_passcu);
        img_back_updatePass=findViewById(R.id.img_back_updatePass);

        SharedPreferences preferences = getSharedPreferences("infoUser", MODE_PRIVATE);
        iduser = preferences.getString("iduser", null);
        password = preferences.getString("password", null);
        img_back_updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

            btnupdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String currentPassword = ed_passcu.getText().toString();
                    String newPassword = ed_passnew.getText().toString();
                    String confirmPassword = nhaplai.getText().toString();
                    UserModal userModal = new UserModal();
                    userModal.setCurrentPassword(currentPassword);
                    userModal.setNewPassword(newPassword);
                    userModal.setConfirmPassword(confirmPassword);
                    if (!currentPassword.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty()) {
                        API_User.apiUser.updatePassword(iduser, userModal).enqueue(new Callback<UserModal>() {
                            @Override
                            public void onResponse(Call<UserModal> call, Response<UserModal> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(Update_PassWord.this, "Cập nhật mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                } else {
                                    Toast.makeText(Update_PassWord.this, "Cập nhật mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                                    Log.e("passs", "Cập nhật mật khẩu thất bại: " + response.message());

                                }
                            }

                            @Override
                            public void onFailure(Call<UserModal> call, Throwable t) {
                                Toast.makeText(Update_PassWord.this, "Cập nhật mật khẩu thất bại" + t, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        // Hiển thị thông báo lỗi khi dữ liệu không hợp lệ
                        Toast.makeText(Update_PassWord.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
}