package account.fpoly.s_shop_client.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.ChitietProduct;
import account.fpoly.s_shop_client.Modal.ProductModal;
import account.fpoly.s_shop_client.R;

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
        holder.NameProduct.setText(""+sp.getName());
        holder.PriceProduct.setText(""+sp.getPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChitietProduct.class);
                context.startActivity(intent);

            }
        });

        Glide.with(context)
                .load( API.api_reg + sp.getImage())
                .into(holder.ImageProduct);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductViewHoder extends RecyclerView.ViewHolder {
        private TextView NameProduct, PriceProduct;
        private ImageView ImageProduct;


        public ProductViewHoder(@NonNull View itemView) {
            super(itemView);
            NameProduct=itemView.findViewById(R.id.txt_nameproduct);
            PriceProduct=itemView.findViewById(R.id.txt_price_product);
            ImageProduct=itemView.findViewById(R.id.img_product);
        }
    }
}
