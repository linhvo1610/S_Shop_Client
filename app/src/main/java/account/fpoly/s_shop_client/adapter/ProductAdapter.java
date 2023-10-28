package account.fpoly.s_shop_client.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.API.API_Product;
import account.fpoly.s_shop_client.ChitietProduct;
import account.fpoly.s_shop_client.Modal.ProductModal;
import account.fpoly.s_shop_client.Modal.ReceProduct;
import account.fpoly.s_shop_client.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHoder> {
    List<ProductModal> list;
    Context context;
    public ProductAdapter(List<ProductModal> list, Context context) {
        this.list = list;
        this.context=context;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ProductViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteam_product,parent,false);
        return new ProductViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHoder holder, int position) {
        ProductModal sp = list.get(position);
        if (sp == null){
                    return;
        }
        String id = sp.getId();
        holder.NameProduct.setText(sp.getName());
        holder.PriceProduct.setText(""+sp.getPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChitietProduct.class);
                Toast.makeText(context, "idproduct"+ id, Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = context.getSharedPreferences("product",context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("idproduct", id);
                editor.putString("price", sp.getPrice()+"");
                editor.apply();
                context.startActivity(intent);

            }
        });

        Glide.with(context)
                .load( API.api_reg + sp.getImage())
                .into(holder.ImageProduct);

        int totalQuantity = 0;
            for (ProductModal.Size size : sp.getSizes()) {
                totalQuantity += size.getQuantity();
        }

        StringBuilder sizesBuilder = new StringBuilder();
        for (ProductModal.Size size : sp.getSizes()) {
            sizesBuilder.append(size.getSize()).append(", ");
        }
        String sizes = sizesBuilder.toString();
        if (sizes.length() > 0) {
            sizes = sizes.substring(0, sizes.length() - 2); // Loại bỏ dấu phẩy cuối cùng
        }
        holder.sizePro.setText(String.valueOf(sizes));

        holder.totalQuantity.setText(String.valueOf(totalQuantity));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductViewHoder extends RecyclerView.ViewHolder {
        private TextView NameProduct, PriceProduct,totalQuantity,sizePro;
        private ImageView ImageProduct;


        public ProductViewHoder(@NonNull View itemView) {
            super(itemView);
            NameProduct=itemView.findViewById(R.id.txt_nameproduct);
            PriceProduct=itemView.findViewById(R.id.txt_price_product);
            totalQuantity=itemView.findViewById(R.id.totalQuantity);
            ImageProduct=itemView.findViewById(R.id.img_product);
            sizePro=itemView.findViewById(R.id.sizePro);
        }
    }
}
