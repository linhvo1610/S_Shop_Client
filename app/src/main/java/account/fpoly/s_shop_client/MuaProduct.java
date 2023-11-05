package account.fpoly.s_shop_client;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import account.fpoly.s_shop_client.Modal.Address;
import account.fpoly.s_shop_client.Tools.ADDRESS;

public class MuaProduct extends AppCompatActivity {

    LinearLayout oder;
    ImageView comeback;
    TextView tv_name, tv_phonenumber, tv_address;

    ImageView listAdd;
    private Address address;
    private final int REQUESR_ADDRESS_CHOOSE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mua_product);

        oder = findViewById(R.id.oder);
        comeback = findViewById(R.id.comeback);
        listAdd = findViewById(R.id.btn_listAdd);
        tv_name = findViewById(R.id.tv_name);
        tv_phonenumber = findViewById(R.id.tv_phonenumber);
        tv_address = findViewById(R.id.tv_address);

        showAddress();
        chooseAddress();

        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        oder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                }, 2500);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showAddress() {
        address = ADDRESS.aDefault(MuaProduct.this);
        if (address != null) {
            tv_name.setText(address.getFullname());
            tv_phonenumber.setText(address.getNumberphone());

            tv_address.setText(address.getAddress()+", "+address.getWards() + ", " + address.getDistrict() + ", " + address.getProvince());
        }
    }
    private void chooseAddress() {
        listAdd.setOnClickListener(v -> {
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
            tv_name.setText(address.getFullname());
            tv_phonenumber.setText(address.getNumberphone());

            tv_address.setText(address.getAddress()+", "+address.getWards() + ", " + address.getDistrict() + ", " + address.getProvince());

        }
    }
}

}