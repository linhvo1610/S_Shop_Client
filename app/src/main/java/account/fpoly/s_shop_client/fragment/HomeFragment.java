package account.fpoly.s_shop_client.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.security.InvalidParameterException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import account.fpoly.s_shop_client.API.API_Product;
import account.fpoly.s_shop_client.GiaoDien.ChitietProduct;
import account.fpoly.s_shop_client.GiaoDien.CustomDialog;
import account.fpoly.s_shop_client.Modal.ProductModal;
import account.fpoly.s_shop_client.Modal.ReceProduct;
import account.fpoly.s_shop_client.Notification;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Service.IClickItemListener;
import account.fpoly.s_shop_client.adapter.ProductAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    public static final String TAG = HomeFragment.class.getName();
    EditText etd_timkiem;
    ImageView chat_admin,notification,dialog;
    ProductAdapter productAdapter;
    private List<ProductModal> listproduct;
    TextView chuyen;
    RecyclerView recyclerView;
    private String anhProduct, tenProduct, giaProduct;

    CustomDialog dialog1;
    String minPrice = null;
    String maxPrice = null;
    EditText min,max;
    Button locp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home, container, false);

        // Truyền dữ liệu vào Chi Tiết Activity
        Bundle bundle = getArguments();
        if (bundle != null){
            tenProduct = bundle.getString("tenProduct");
            giaProduct = bundle.getString("giaProduct");
            anhProduct = bundle.getString("anhProduct");
        }
        //=====================================
        etd_timkiem=view.findViewById(R.id.edt_timkiem);
        dialog=view.findViewById(R.id.dialog);


        recyclerView=view.findViewById(R.id.rcv_product);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        listproduct= new ArrayList<>();
        callApiSeviceListProduct();
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1=new CustomDialog(getContext());
                ImageView dimiss = dialog1.findViewById(R.id.dimiss);
                dimiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                ArrayList<String> spinnerData = new ArrayList<>();
                spinnerData.add("Tìm kiếm sản phẩm");
                spinnerData.add("All sản phẩm");
                spinnerData.add("Dưới 200k");
                spinnerData.add("200k - 500k");
                spinnerData.add("500k - 1triệu");
                spinnerData.add("1triệu - 3triệu");
                spinnerData.add("3triệu - 5triệu");
                spinnerData.add("Trên 5triệu");
                Spinner spinner = dialog1.findViewById(R.id.spinner);

                spinner.setBackgroundColor(Color.LTGRAY);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, spinnerData);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItem = spinnerData.get(position);

                        // Xử lý sự kiện khi một mục được chọn trong Spinner
                        if (selectedItem.equals("All sản phẩm")) {
                            minPrice = null;
                            maxPrice = null;
                            callApiSeviceListProduct();
                            dialog1.dismiss();
                        }
                        else if (selectedItem.equals("Dưới 200k")) {
                            minPrice = null;
                            filterByPrice(minPrice, "200000");
                        } else if (selectedItem.equals("200k - 500k")) {
                            maxPrice = "500000";
                            minPrice = "200000";
                        } else if (selectedItem.equals("500k - 1triệu")) {
                            maxPrice = "1000000";
                            minPrice = "500000";
                        } else if (selectedItem.equals("1triệu - 3triệu")) {
                            maxPrice = "3000000";
                            minPrice = "1000000";
                        } else if (selectedItem.equals("3triệu - 5triệu")) {
                            maxPrice = "5000000";
                            minPrice = "3000000";
                        } else if (selectedItem.equals("Trên 5triệu")) {
                            minPrice = "5000000";
                            maxPrice = "";
                        }
                        filterByPrice(minPrice, maxPrice);
                        minPrice = null;
                        maxPrice = null;

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Xử lý sự kiện khi không có mục nào được chọn trong Spinner
                    }
                });



                dialog1.show();
            }
        });


//        chat_admin = view.findViewById(R.id.chat_admin);
        notification= view.findViewById(R.id.iconNotification);
        chuyen= view.findViewById(R.id.chuyen);

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Notification.class));
            }
        });


        return view;
    }

    private void filterByPrice(String minPrice, String maxPrice) {
        API_Product.apiProduct.filterProducts(minPrice, maxPrice).enqueue(new Callback<List<ProductModal>>() {
            @Override
            public void onResponse(Call<List<ProductModal>> call, Response<List<ProductModal>> response) {
                if (response.isSuccessful()) {
                    listproduct.clear();
                    listproduct.addAll(response.body());
                    productAdapter.notifyDataSetChanged();
                    dialog1.dismiss();


                }else{
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductModal>> call, Throwable t) {
                if (t instanceof InvalidParameterException) {
                    Toast.makeText(getContext(), "Invalid price range", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void callApiSeviceListProduct() {
        API_Product.apiProduct.listProduct().enqueue(new Callback<ReceProduct>() {
            @Override
            public void onResponse(Call<ReceProduct> call, Response<ReceProduct> response) {
                listproduct = response.body().getData();
                productAdapter = new ProductAdapter(listproduct, getContext(), new IClickItemListener() {
                    @Override
                    public void onCLickItemProduct(ProductModal productModal) {
                        onClickGoToDetailProduct(productModal);
                    }
                });
                recyclerView.setAdapter(productAdapter);
                etd_timkiem.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String keyword=removeVietnameseDiacritics(etd_timkiem.getText().toString().trim());
                        List<ProductModal> serarchResult=searchProducts(keyword);
                        productAdapter= new ProductAdapter(serarchResult, getContext(), new IClickItemListener() {
                            @Override
                            public void onCLickItemProduct(ProductModal productModal) {
                                onClickGoToDetailProduct(productModal);
                            }
                        });
                        recyclerView.setAdapter(productAdapter);
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }

            @Override
            public void onFailure(Call<ReceProduct> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void onClickGoToDetailProduct(ProductModal productModal){
        Intent intent = new Intent(getContext(), ChitietProduct.class);
        intent.putExtra("id", productModal.getId());
        intent.putExtra("tenProduct", tenProduct);
        intent.putExtra("giaProduct", giaProduct);
        intent.putExtra("anhProduct", anhProduct);
        startActivity(intent);
    }
    private List<ProductModal> searchProducts(String keyword) {
        List<ProductModal> filteredProducts = new ArrayList<>();
        String normalizedKeyword = removeVietnameseDiacritics(keyword);
        for (ProductModal product : listproduct) {
            String normalizedProductName = removeVietnameseDiacritics(product.getName());
            if (normalizedProductName.contains(normalizedKeyword)) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }
    @Override
    public void onResume() {
        super.onResume();
        callApiSeviceListProduct();
    }
    private String removeVietnameseDiacritics(String text) {
        String normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalizedText).replaceAll("").toLowerCase();
    }
}