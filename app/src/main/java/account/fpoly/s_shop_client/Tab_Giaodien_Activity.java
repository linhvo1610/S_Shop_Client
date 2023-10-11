package account.fpoly.s_shop_client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import account.fpoly.s_shop_client.fragment.GiohangFragment;
import account.fpoly.s_shop_client.fragment.HomeFragment;
import account.fpoly.s_shop_client.fragment.SettingsFragment;
import account.fpoly.s_shop_client.fragment.LichsuFragment;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class Tab_Giaodien_Activity extends AppCompatActivity {

    SmoothBottomBar navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_giaodien);

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


        navigationView = findViewById(R.id.bottom_navigation);

        replaceFragment(new HomeFragment());

        navigationView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                if (i == 0){
                    replaceFragment(homeFragment);
                }

                if (i == 1){
                    replaceFragment(giohangFragment);
                }


        
                if (i == 3){
                    replaceFragment(infoUserFragment);
                }

                return false;
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