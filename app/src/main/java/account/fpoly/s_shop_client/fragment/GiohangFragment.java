package account.fpoly.s_shop_client.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import account.fpoly.s_shop_client.Modal.BillMore;
import account.fpoly.s_shop_client.Modal.Cart;
import account.fpoly.s_shop_client.MuaProduct;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Service.ApiService;
import account.fpoly.s_shop_client.SplassActivity;
import account.fpoly.s_shop_client.Tab_Giaodien_Activity;
import account.fpoly.s_shop_client.Tools.ACCOUNT;
import account.fpoly.s_shop_client.Tools.LIST;
import account.fpoly.s_shop_client.Tools.TOOLS;
import account.fpoly.s_shop_client.adapter.CartAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private LinearLayout ln_speed, ln_check_cart;
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout ln_cart_emty;
    @SuppressLint("StaticFieldLeak")
    private static TextView tv_check_all, tv_price_pay;
    @SuppressLint("StaticFieldLeak")
    private static CheckBox cbox_check_all;

    @SuppressLint("StaticFieldLeak")
    private static LinearLayout ln_pay;
    private Button btn_login_cart,btn_buy_cart;
    private static int price_pay, import_price_pay;

    private TextView btn_pay;
    public static List<Cart> cartList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);
        showCarts();
        checkAll();
        pay();
    }
    private void mapping(View view) {
        btn_buy_cart = view.findViewById(R.id.btn_buy_cart);
        ln_cart_emty = view.findViewById(R.id.ln_cart_emty);
        ln_check_cart = view.findViewById(R.id.ln_check_cart);
        btn_login_cart = view.findViewById(R.id.btn_login_cart);
        btn_pay = view.findViewById(R.id.btn_pay);
        tv_price_pay = view.findViewById(R.id.tv_price_pay);
        ln_pay = view.findViewById(R.id.ln_pay);
        cbox_check_all = view.findViewById(R.id.cbox_check_all);
        tv_check_all = view.findViewById(R.id.tv_check_all);
        ln_speed = view.findViewById(R.id.ln_speed);
        recyclerView = view.findViewById(R.id.rcv_cart);

    }

    private void showCarts() {

        if (ACCOUNT.user == null){
            ln_speed.setVisibility(View.VISIBLE);
            btn_buy_cart.setText("Bạn cần đăng nhập để sử dụng chức năng này ");
            btn_buy_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), SplassActivity.class));
                }
            });
            return;
        }

        btn_buy_cart.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), Tab_Giaodien_Activity.class));
        });

        adapter = new CartAdapter(requireContext(),false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);
        if (TOOLS.getUser(requireContext()) != null) {
            ApiService.apiService.getCarts(TOOLS.getUser(requireContext()).get_id()).enqueue(new Callback<List<Cart>>() {
                @Override
                public void onResponse(@NonNull Call<List<Cart>> call, @NonNull Response<List<Cart>> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().size() > 0) {
                            cartList = response.body();
                            adapter.setData(response.body());
                            ln_speed.setVisibility(View.VISIBLE);
                            recyclerView.setPadding(0, 0, 0, 140);
                            ln_cart_emty.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Cart>> call, @NonNull Throwable t) {

                }
            });
        }

    }
    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void checkAll() {
        cbox_check_all.setOnClickListener(v -> {
            TOOLS.checkAllCarts = cbox_check_all.isChecked();
            if (TOOLS.checkAllCarts) {
                tv_check_all.setText("Bỏ chọn tất cả");
                showLayoutPay(cartList);
            } else {
                LIST.listBuyCart.clear();
                tv_check_all.setText("Chọn tất cả");
                showLayoutPay(LIST.listBuyCart);
            }
            adapter.notifyDataSetChanged();
        });
    }

    public static void showLayoutPay(List<Cart> list) {
        if (list.size() == 0) {
            ln_pay.setVisibility(View.GONE);
            return;
        }
        ln_pay.setVisibility(View.VISIBLE);
        price_pay = 0;
        import_price_pay = 0;
        for (int i = 0; i < list.size(); i++) {
            Cart cart = list.get(i);
            price_pay += (cart.getPrice_product()* cart.getQuantity());
            import_price_pay += (cart.getImportPrice()*cart.getQuantity());
        }
        tv_price_pay.setText(TOOLS.convertPrice(price_pay));
    }

    private void pay() {
        btn_pay.setOnClickListener(v -> {
            BillMore billMore = new BillMore();
            billMore.setStatus(0);
            billMore.setId_user(ACCOUNT.user.get_id());
            billMore.setTotal(price_pay);
            billMore.setImportPrice(import_price_pay);
            billMore.setList(LIST.listBuyCart);
            Intent intent = new Intent(requireContext(), MuaProduct.class);
            intent.putExtra("billmore", billMore);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
        });
    }

    @SuppressLint("SetTextI18n")
    public static void setCheckByItem(){
        if(TOOLS.checkAllCarts){
            if(!cbox_check_all.isChecked()){
                cbox_check_all.setChecked(true);
                tv_check_all.setText("Bỏ chọn tất cả");
            }
        }else {
            if(cbox_check_all.isChecked()){
                cbox_check_all.setChecked(false);
                tv_check_all.setText("Chọn tất cả");
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        cbox_check_all.setChecked(false);
        LIST.listBuyCart.clear();
        TOOLS.checkAllCarts = false;
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
        showLayoutPay(LIST.listBuyCart);
    }
}