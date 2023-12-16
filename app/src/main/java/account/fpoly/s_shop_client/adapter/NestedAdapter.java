package account.fpoly.s_shop_client.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.AddCommentActivity;
import account.fpoly.s_shop_client.GiaoDien.ChitietProduct;
import account.fpoly.s_shop_client.Modal.Cart;
import account.fpoly.s_shop_client.Modal.ProductModal;
import account.fpoly.s_shop_client.R;

public class NestedAdapter extends RecyclerView.Adapter<NestedAdapter.NestedViewHolder>{
    private List<Cart> itemList;
    Context context;


    public NestedAdapter(List<Cart> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public NestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteam_nested,parent,false);

        return new NestedViewHolder(view);
    }
    String id;
    @Override
    public void onBindViewHolder(@NonNull NestedViewHolder holder, int position) {
        Cart cart = itemList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        if (holder.namePro != null) {
            holder.namePro.setText(cart.getName_product());
        }
        int price = cart.getPrice_product();
        int priceFormat = Integer.parseInt(String.valueOf(price));
        String Price = decimalFormat.format(priceFormat);
        holder.totalQuantity.setText("x"+cart.getQuantity());

        holder.totalPrice.setText("đ"+ Price);
        holder.sizePro.setText("Size:"+ cart.getSize());
        // truyen du lieu sang comment



        Picasso.get().load(API.api_image + cart.getImage()).into(holder.imageBill);


        holder.danhgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddCommentActivity.class);

                SharedPreferences sharedPreferences = context.getSharedPreferences("id_product", context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("idP", cart.getId_product());
                Toast.makeText(context, "id: "+ cart.getId_product(), Toast.LENGTH_SHORT).show();
                editor.apply();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                id = itemList.get(position).getId_product();
                Toast.makeText(context, "id: "+ id, Toast.LENGTH_SHORT).show();

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
                                String id = jsonObject.getString("_id");
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

            }
        });
    }
    private void startDetailActivity(Context context) {
        Intent intent = new Intent(context, ChitietProduct.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class NestedViewHolder extends RecyclerView.ViewHolder {
        TextView totalQuantity,totalPrice,statusPro,namePro,sizePro;
        LinearLayout danhgia;
        ImageView imageBill;
        public NestedViewHolder(@NonNull View itemView) {
            super(itemView);
            totalQuantity = itemView.findViewById(R.id.totalQuantitys);
            totalPrice = itemView.findViewById(R.id.totalPrices);
            namePro = itemView.findViewById(R.id.nameProbills);
            imageBill = itemView.findViewById(R.id.imageBills);
            sizePro = itemView.findViewById(R.id.sizePros);
            danhgia=itemView.findViewById(R.id.btn_danhgia);
        }
    }
}
