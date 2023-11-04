package account.fpoly.s_shop_client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import account.fpoly.s_shop_client.Modal.Address;
import account.fpoly.s_shop_client.Service.ApiService;
import account.fpoly.s_shop_client.Tools.ACCOUNT;
import account.fpoly.s_shop_client.Tools.ADDRESS;
import account.fpoly.s_shop_client.Tools.LIST;
import account.fpoly.s_shop_client.Tools.TOOLS;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tv_err_fullname_adr, tv_err_numberphone_adr, tv_err_select_address_adr, tv_err_name_address_adr;
    private EditText edt_fullname_adr, edt_numberphone_adr, edt_name_address_adr, edt_select_address_adr;
    private SwitchCompat sw_select_adr;
    private Button btn_add_new_address_adr, btn_del_address_adr;

    public static int REQUSET_CODE_ADDRESS = 888;
    public static Address address1;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        mapping();
        createDialog();
        setToolbar();
        selectAddress();
        editAddress();
        deleteAddress();
        addNewAddress();
    }
    private void createDialog() {
        dialog = TOOLS.createDialog(AddAddressActivity.this);
    }

    private void deleteAddress() {
        btn_del_address_adr.setOnClickListener(v -> {
            if(LIST.listAddress.size()<=1){
                Toast.makeText(AddAddressActivity.this, "Cần ít nhất một địa chỉ", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(AddAddressActivity.this);
            builder.setTitle("Xác nhận xóa địa chỉ này?");
            builder.setPositiveButton("Xóa", (dialog1, which) -> {
                dialog.show();
                ApiService.apiService.deleteAdsress(address1.get_id()).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()&&response.body()!=null) {
                            if (response.body() == 1) {
                                for (int i = 0; i < LIST.listAddress.size(); i++) {
                                    if (LIST.listAddress.get(i).get_id().equals(address1.get_id())) {
                                        LIST.listAddress.remove(i);
                                        break;
                                    }
                                }
                                if (TOOLS.getDefaulAddress(AddAddressActivity.this).equals(address1.get_id())) {
                                    TOOLS.clearDefaulAddress(AddAddressActivity.this);
                                }}
                            clearAddress();
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                        Toast.makeText(AddAddressActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            });
            builder.setNegativeButton("Hủy",null);
            builder.create().show();

        });
    }

    @SuppressLint("SetTextI18n")
    private void editAddress() {
        address1 = (Address) getIntent().getSerializableExtra("address");
        if (address1 != null) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_uppdate_address);
            btn_del_address_adr.setVisibility(View.VISIBLE);
            edt_fullname_adr.setText(address1.getFullname());
            edt_numberphone_adr.setText(address1.getNumberphone());
            edt_name_address_adr.setText(address1.getAddress());
            edt_select_address_adr.setText(address1.getWards() + ", " + address1.getDistrict() + ", " + address1.getProvince());
            sw_select_adr.setChecked((TOOLS.getDefaulAddress(AddAddressActivity.this).equals(address1.get_id())));
        }
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbar_add_address);
        tv_err_fullname_adr = findViewById(R.id.tv_err_fullname_adr);
        tv_err_numberphone_adr = findViewById(R.id.tv_err_numberphone_adr);
        tv_err_select_address_adr = findViewById(R.id.tv_err_select_address_adr);
        tv_err_name_address_adr = findViewById(R.id.tv_err_name_address_adr);
        edt_select_address_adr = findViewById(R.id.edt_select_address_adr);
        edt_select_address_adr.setInputType(InputType.TYPE_NULL);
        edt_fullname_adr = findViewById(R.id.edt_fullname_adr);
        edt_numberphone_adr = findViewById(R.id.edt_numberphone_adr);
        edt_name_address_adr = findViewById(R.id.edt_name_address_adr);
        sw_select_adr = findViewById(R.id.sw_select_adr);
        btn_add_new_address_adr = findViewById(R.id.btn_add_new_address_adr);
        btn_del_address_adr = findViewById(R.id.btn_del_address_adr);
    }

    private void addNewAddress() {
        btn_add_new_address_adr.setOnClickListener(v -> {
            if (checkFielEmty(edt_fullname_adr) || checkFielEmty(edt_numberphone_adr) || checkFielEmty(edt_select_address_adr) || checkFielEmty(edt_name_address_adr)) {
                return;
            }
            dialog.show();
            if (address1 != null) {
                address1.setFullname(edt_fullname_adr.getText().toString().trim());
                address1.setNumberphone(edt_numberphone_adr.getText().toString().trim());
                if (ADDRESS.province != null) {
                    address1.setProvince(ADDRESS.province.getName());
                }
                if (ADDRESS.district != null) {
                    address1.setDistrict(ADDRESS.district.getName());
                }
                if (ADDRESS.ward != null) {
                    address1.setWards(ADDRESS.ward.getName());
                }
                address1.setAddress(edt_name_address_adr.getText().toString());
                ApiService.apiService.updateAddress(address1).enqueue(new Callback<Address>() {
                    @Override
                    public void onResponse(@NonNull Call<Address> call, @NonNull Response<Address> response) {
                        if (response.isSuccessful()&&response.body()!=null) {
                            for (int i = 0; i < LIST.listAddress.size(); i++) {
                                if (LIST.listAddress.get(i).get_id().equals(response.body().get_id())) {
                                    LIST.listAddress.set(i, response.body());
                                    break;
                                }
                            }
                            if (sw_select_adr.isChecked()) {
                                TOOLS.saveDefaulAddress(AddAddressActivity.this, response.body().get_id());
                            } else {
                                if (TOOLS.getDefaulAddress(AddAddressActivity.this).equals(response.body().get_id())) {
                                    TOOLS.clearDefaulAddress(AddAddressActivity.this);
                                }else if(LIST.listAddress.isEmpty()){
                                    TOOLS.saveDefaulAddress(AddAddressActivity.this, response.body().get_id());
                                }
                            }
                            clearAddress();
                            dialog.dismiss();
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Address> call, @NonNull Throwable t) {
                        Toast.makeText(AddAddressActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                return;
            }
            Address address = new Address();
            SharedPreferences sharedPreferences = getSharedPreferences("infoUser", MODE_PRIVATE);
            String idUser = sharedPreferences.getString("iduser", null);
            address.setId_user(idUser);
            address.setFullname(edt_fullname_adr.getText().toString().trim());
            address.setNumberphone(edt_numberphone_adr.getText().toString().trim());
            address.setProvince(ADDRESS.province.getName());
            address.setDistrict(ADDRESS.district.getName());
            address.setWards(ADDRESS.ward.getName());
            address.setAddress(edt_name_address_adr.getText().toString());
            ApiService.apiService.saveNewAddress(idUser, address).enqueue(new Callback<Address>() {
                @Override
                public void onResponse(@NonNull Call<Address> call, @NonNull Response<Address> response) {
                    if (response.isSuccessful()) {
                        LIST.listAddress.add(response.body());
                        if (sw_select_adr.isChecked()) {
                            assert response.body() != null;
                            TOOLS.saveDefaulAddress(AddAddressActivity.this, response.body().get_id());
                        }
                        clearAddress();
                        onBackPressed();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Address> call, @NonNull Throwable t) {
                    Toast.makeText(AddAddressActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void selectAddress() {
        edt_select_address_adr.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                onClickEditText();
            }
            return false;
        });
    }

    private void clearAddress() {
        ADDRESS.province = null;
        ADDRESS.district = null;
        ADDRESS.ward = null;
    }

    private void onClickEditText() {
        Intent intent = new Intent(AddAddressActivity.this, ChooseAddressActivity.class);
        startActivityForResult(intent, REQUSET_CODE_ADDRESS);
        overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_add_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private boolean checkFielEmty(EditText editText) {
        if (editText.getText().toString().trim().length() == 0) {
            if (editText == edt_fullname_adr) {
                tv_err_fullname_adr.setVisibility(View.VISIBLE);
                edt_fullname_adr.requestFocus();
            } else if (editText == edt_numberphone_adr) {
                tv_err_numberphone_adr.setVisibility(View.VISIBLE);
                edt_numberphone_adr.requestFocus();
            } else if (editText == edt_select_address_adr) {
                tv_err_select_address_adr.setVisibility(View.VISIBLE);
                edt_select_address_adr.requestFocus();
            } else {
                tv_err_name_address_adr.setVisibility(View.VISIBLE);
                edt_name_address_adr.requestFocus();
            }
            return true;
        }
        if (editText == edt_fullname_adr) {
            tv_err_fullname_adr.setVisibility(View.GONE);
            edt_fullname_adr.clearFocus();
        } else if (editText == edt_numberphone_adr) {
            if (TOOLS.isValidPhoneNumber(editText.getText().toString().trim())) {
                tv_err_numberphone_adr.setVisibility(View.VISIBLE);
                edt_numberphone_adr.requestFocus();
                return true;
            }
            tv_err_numberphone_adr.setVisibility(View.GONE);
            edt_numberphone_adr.clearFocus();
        } else if (editText == edt_select_address_adr) {
            tv_err_select_address_adr.setVisibility(View.GONE);
        } else {
            tv_err_name_address_adr.setVisibility(View.GONE);
            edt_name_address_adr.clearFocus();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUSET_CODE_ADDRESS && resultCode == RESULT_OK) {
            if (data != null) {
                String s = data.getStringExtra("data");
                edt_select_address_adr.setText(s);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("fullname", edt_fullname_adr.getText().toString());
        outState.putString("numberphone", edt_numberphone_adr.getText().toString());
        outState.putString("nameaddres", edt_name_address_adr.getText().toString());
        outState.putBoolean("default", sw_select_adr.isChecked());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        edt_fullname_adr.setText(savedInstanceState.getString("fullname", ""));
        edt_numberphone_adr.setText(savedInstanceState.getString("numberphone", ""));
        edt_name_address_adr.setText(savedInstanceState.getString("nameaddres", ""));
        sw_select_adr.setChecked(savedInstanceState.getBoolean("default", false));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.prev_enter, R.anim.prev_exit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}