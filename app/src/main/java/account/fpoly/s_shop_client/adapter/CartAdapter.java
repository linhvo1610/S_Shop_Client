package account.fpoly.s_shop_client.adapter;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.GiaoDien.ChitietProduct;
import account.fpoly.s_shop_client.Modal.Cart;
import account.fpoly.s_shop_client.Modal.ProductModal;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Service.ApiService;
import account.fpoly.s_shop_client.Tools.LIST;
import account.fpoly.s_shop_client.Tools.TOOLS;
import account.fpoly.s_shop_client.fragment.GiohangFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Cart> list;
    private final Context context;
    private final boolean type1;
    private static final int VIEW_TYPE_TYPE = 0;
    private static final int VIEW_TYPE_TYPE1 = 1;
    String name,id,trademark,dec;


    public CartAdapter(Context context, boolean type1) {
        this.context = context;
        this.type1 = type1;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Cart> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (type1) {
            return VIEW_TYPE_TYPE1;
        } else {
            return VIEW_TYPE_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_TYPE) {
            View view = inflater.inflate(R.layout.iteam_giohang, parent, false);
            return new CartHolderView(view);
        } else {
            View view = inflater.inflate(R.layout.iteam_trangthai_bill, parent, false);
            return new CartHolderView1(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        Cart cart = list.get(position);
        if (cart != null) {
            int lastPrice = (cart.getPrice_product()*cart.getQuantity());
            if (holder1 instanceof CartHolderView) {
                CartHolderView holder = (CartHolderView) holder1;

//                Glide.with(context).load(TOOLS.doMainDevice + cart.getImage()).into(holder.imv_image);
                Picasso.get().load(API.api_image + cart.getImage()).into(holder.imv_image);
                holder.tv_name.setText(cart.getName_product());
                holder.tv_price.setText(TOOLS.convertPrice(lastPrice));
                holder.tv_quantity.setText(String.valueOf(cart.getQuantity()));
                holder.cbox_add.setChecked(TOOLS.checkAllCarts);
                holder.tv_size.setText(String.valueOf(cart.getSize()));
                if (TOOLS.checkAllCarts) {
                    LIST.listBuyCart.add(cart);
                }

                holder.imv_image.setOnClickListener(v -> {


                    SharedPreferences sharedPreferences = context.getSharedPreferences("product", context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.api + "product?_id=" + cart.getId_product(),
                            null, new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length() > 0) {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                JSONObject jsonObjectCat = jsonObject.getJSONObject("id_cat");

                                    String namecat = jsonObjectCat.getString("name");

                                    // Extract data from the jsonObject
                                    String names = jsonObject.getString("name");
                                    id = jsonObject.getString("_id");
                                    String decs = jsonObject.getString("description");
                                    String trademarks = jsonObject.getString("trademark");

                                    JSONArray imagesArray = jsonObject.getJSONArray("images");
                                    List<ProductModal.ImageItem> imageItemsList = new ArrayList<>();

                                    for (int j = 0; j < imagesArray.length(); j++) {
                                        JSONObject jsonObjectImage = imagesArray.getJSONObject(j);
                                        String imageUrl = jsonObjectImage.getString("image");

                                        // Tạo một đối tượng ImageItem và thêm vào danh sách
                                        ProductModal.ImageItem imageItem = new ProductModal.ImageItem();
                                        imageItem.setImage(imageUrl);
                                        imageItemsList.add(imageItem);
                                    }

                                    editor.putString("images", new Gson().toJson(imageItemsList));
                                    editor.putString("descriptionPro", decs);
                                    editor.putString("trademark", trademarks);
                                    editor.putString("tenProduct", names);
                                    editor.putString("namecat", namecat);
                                    // Save data to SharedPreferences
                                    editor.apply();
                                    startDetailActivity(context);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    editor.putString("idProduct", cart.getId_product());
                    editor.putString("giaProduct", cart.getPrice_product() + "");
                    editor.apply();
                    requestQueue.add(jsonObjectRequest);
                });



                holder.cbox_add.setOnClickListener(v -> {
                    if (holder.cbox_add.isChecked()) {
                        LIST.listBuyCart.add(cart);
                        if (LIST.listBuyCart.size() == list.size()) {
                            TOOLS.checkAllCarts = true;
                            GiohangFragment.setCheckByItem();
                        }
                    } else {
                        for (int i = 0; i < LIST.listBuyCart.size(); i++) {
                            if (LIST.listBuyCart.get(i).get_id().equals(cart.get_id())) {
                                LIST.listBuyCart.remove(i);
                                break;
                            }
                        }
                        if (LIST.listBuyCart.size() < list.size()) {
                            TOOLS.checkAllCarts = false;
                            GiohangFragment.setCheckByItem();
                        }
                    }
                    GiohangFragment.showLayoutPay(LIST.listBuyCart);
                });
                holder.imv_subtract.setOnClickListener(v -> {
                    if (cart.getQuantity() == 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Xóa sản phẩm " + cart.getName_product() + " khỏi giỏ hàng?");
                        builder.setPositiveButton("Xóa", (dialog, which) -> deleteCart(holder, cart.get_id()));
                        builder.setNegativeButton("Hủy", null);
                        builder.create().show();
                        return;
                    }
                    cart.setQuantity(cart.getQuantity() - 1);
                    holder.tv_quantity.setText(String.valueOf(cart.getQuantity()));
                    holder.tv_price.setText(TOOLS.convertPrice(cart.getQuantity() * cart.getPrice_product()));
                    replaceCartItem(cart);
                });
                holder.imv_add.setOnClickListener(v -> {
                    cart.setQuantity(cart.getQuantity() + 1);
                    holder.tv_quantity.setText(String.valueOf(cart.getQuantity()));
                    holder.tv_price.setText(TOOLS.convertPrice(cart.getQuantity() * cart.getPrice_product()));
                    replaceCartItem(cart);
                });
                holder.ln_delete.setOnClickListener(v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Xóa sản phẩm " + cart.getName_product() + " khỏi giỏ hàng?");
                    builder.setPositiveButton("Xóa", (dialog, which) -> deleteCart(holder, cart.get_id()));
                    builder.setNegativeButton("Hủy", null);
                    builder.create().show();
                });
            } else if (holder1 instanceof CartHolderView1) {
                CartHolderView1 holder = (CartHolderView1) holder1;
                Picasso.get().load(API.api_image + cart.getImage()).into(holder.imv_image);
                holder.tv_name.setText(cart.getName_product());
                holder.tv_price.setText("$"+TOOLS.convertPrice(lastPrice));
                holder.tv_quantity.setText("x"+cart.getQuantity());
                holder.tv_size.setText(String.valueOf(cart.getSize()));
            }
        }

    }
    private void startDetailActivity(Context context) {
        Intent intent = new Intent(context, ChitietProduct.class);
        context.startActivity(intent);
    }
    private void replaceCartItem(Cart cart) {
        for (int i = 0; i < LIST.listBuyCart.size(); i++) {
            if(LIST.listBuyCart.get(i).get_id().equals(cart.get_id())){
                LIST.listBuyCart.get(i).setQuantity(cart.getQuantity());
                break;
            }
        }
        GiohangFragment.showLayoutPay(LIST.listBuyCart);
    }

    private void deleteCart(CartHolderView holder, String id) {
        Dialog dialog = TOOLS.createDialog(context);
        dialog.show();
        ApiService.apiService.deleteCart(id).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body() == 1) {
                        for (int i = 0; i < LIST.listBuyCart.size(); i++) {
                            if (LIST.listBuyCart.get(i).get_id().equals(id)) {
                                LIST.listBuyCart.remove(i);
                                GiohangFragment.showLayoutPay(LIST.listBuyCart);
                                break;
                            }
                        }
                        list.remove(holder.getAdapterPosition());
                        if (list.size() == 0) {
                            GiohangFragment.ln_cart_emty.setVisibility(View.VISIBLE);
                        }
                        notifyItemRemoved(holder.getAdapterPosition());
                    } else {
                        Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list != null) ? list.size() : 0;
    }

    public static class CartHolderView extends RecyclerView.ViewHolder {
        private final TextView tv_name;
        private final TextView tv_quantity;
        private final TextView tv_price;
        private final ImageView imv_image;
        private final ImageView imv_add;
        private final ImageView imv_subtract;
        private final CheckBox cbox_add;
        private final LinearLayout ln_delete;
        private final TextView tv_size;


        public CartHolderView(@NonNull View itemView) {
            super(itemView);
            ln_delete = itemView.findViewById(R.id.ln_delete);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            tv_price = itemView.findViewById(R.id.tv_price);
            imv_image = itemView.findViewById(R.id.imv_image);
            imv_add = itemView.findViewById(R.id.imv_add);
            imv_subtract = itemView.findViewById(R.id.imv_subtract);
            cbox_add = itemView.findViewById(R.id.cbox_add);
            tv_size = itemView.findViewById(R.id.tv_size);
        }
    }

    public static class CartHolderView1 extends RecyclerView.ViewHolder {
        private final TextView tv_name;
        private final TextView tv_quantity;
        private final TextView tv_price;
        private final ImageView imv_image;
        private final TextView tv_size;


        public CartHolderView1(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            tv_price = itemView.findViewById(R.id.tv_price);
            imv_image = itemView.findViewById(R.id.imv_image);
            tv_size = itemView.findViewById(R.id.tv_size);

        }
    }
}
