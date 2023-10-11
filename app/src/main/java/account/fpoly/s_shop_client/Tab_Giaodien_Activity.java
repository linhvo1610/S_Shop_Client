package account.fpoly.s_shop_client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import account.fpoly.s_shop_client.fragment.GiohangFragment;
import account.fpoly.s_shop_client.fragment.HomeFragment;
import account.fpoly.s_shop_client.fragment.SettingsFragment;
import account.fpoly.s_shop_client.fragment.LichsuFragment;

public class Tab_Giaodien_Activity extends AppCompatActivity {

    BottomNavigationView navigationView;
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
        getSupportFragmentManager().beginTransaction().replace(R.id.body_contaiber, new HomeFragment()).commit();
        navigationView.setSelectedItemId(R.id.nav_home);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.nav_home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.body_contaiber,homeFragment).commit();

                }else if (item.getItemId() == R.id.nav_giohang) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.body_contaiber,giohangFragment).commit();

                }else if (item.getItemId() == R.id.nav_tym) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.body_contaiber,lichsuFragment).commit();

                }else if (item.getItemId() == R.id.nav_setting) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.body_contaiber, settingsFragment).commit();

                }
                return true;

            }
        });
    }
}