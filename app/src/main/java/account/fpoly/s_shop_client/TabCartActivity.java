package account.fpoly.s_shop_client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.WindowManager;

import account.fpoly.s_shop_client.fragment.GiohangFragment;
import account.fpoly.s_shop_client.fragment.HomeFragment;
import account.fpoly.s_shop_client.fragment.LichsuFragment;
import account.fpoly.s_shop_client.fragment.SettingsFragment;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class TabCartActivity extends AppCompatActivity {
    SmoothBottomBar navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_cart);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle bundle = new Bundle();
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);

        SettingsFragment settingsFragment = new SettingsFragment();
        settingsFragment.setArguments(bundle);

        LichsuFragment lichsuFragment = new LichsuFragment();
        lichsuFragment.setArguments(bundle);

        GiohangFragment giohangFragment = new GiohangFragment();
        giohangFragment.setArguments(bundle);


        navigationView = findViewById(R.id.bottom_navigations);

        replaceFragment(giohangFragment);
        navigationView.setItemActiveIndex(1);

        navigationView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                switch (i) {
                    case 0:
                        replaceFragment(homeFragment);
                        break;
                    case 1:
                        replaceFragment(giohangFragment);
                        break;
                    case 2:
                        replaceFragment(lichsuFragment);
                        break;
                    case 3:
                        replaceFragment(settingsFragment);
                        break;
                }
                return true;
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}