package account.fpoly.s_shop_client.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import account.fpoly.s_shop_client.Modal.Cart;
import account.fpoly.s_shop_client.R;
//import account.fpoly.s_shop_client.adapter.CartAdapter;


public class GiohangFragment extends Fragment {

    public static final String TAG = GiohangFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        setHasOptionsMenu(true);
//        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.title_cart);
//        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        return inflater.inflate(R.layout.fragment_giohang, container, false);
    }
//    private RecyclerView recyclerView;
//    private CartAdapter adapter;
//    private LinearLayout ln_speed, ln_check_cart;
//    @SuppressLint("StaticFieldLeak")
//    public static LinearLayout ln_cart_emty;
//    @SuppressLint("StaticFieldLeak")
//    private static TextView tv_check_all, tv_price_pay;
//    @SuppressLint("StaticFieldLeak")
//    private static CheckBox cbox_check_all;
//
//    @SuppressLint("StaticFieldLeak")
//    private static LinearLayout ln_pay;
//    private Button btn_pay, btn_login_cart,btn_buy_cart;
//    private static int price_pay;
//    public static List<Cart> cartList;
}