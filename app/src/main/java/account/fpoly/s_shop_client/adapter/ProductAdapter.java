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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import account.fpoly.s_shop_client.GiaoDien.ChitietProduct;
import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.Modal.ProductModal;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Service.IClickItemListener;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHoder> {
    private final List<ProductModal> list;
    private final Context context;
    private final IClickItemListener iClickItemListener;
    public ProductAdapter(List<ProductModal> list, Context context, IClickItemListener iClickItemListener) {
        this.list = list;
        this.context=context;
        this.iClickItemListener = iClickItemListener;
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
        ProductViewHoder productViewHoder = holder;
        ProductModal productModal = list.get(position);
        productViewHoder.NameProduct.setText("" + productModal.getName());
        productViewHoder.PriceProduct.setText("" + productModal.getPrice());

        String id = productModal.getId();

        Glide.with(context).load(productModal.getImage()).into(holder.ImageProduct);

        productViewHoder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("product", context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("idProduct", id);
                editor.putString("tenProduct", productModal.getName());
                editor.putString("giaProduct", productModal.getPrice());
                editor.putString("anhProduct", productModal.getImage());
                editor.apply();
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

                iClickItemListener.onCLickItemProduct(productModal);
            }
        });


        int totalQuantity = 0;
        for (ProductModal.Size size : productModal.getSizes()) {
            totalQuantity += size.getQuantity();
        }
        StringBuilder sizesBuilder = new StringBuilder();
        for (ProductModal.Size size : productModal.getSizes()) {
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
        private TextView NameProduct, PriceProduct,totalQuantity,sizePro,Description;
        private ImageView ImageProduct;


        public ProductViewHoder(@NonNull View itemView) {
            super(itemView);
            NameProduct=itemView.findViewById(R.id.txt_nameproduct);
            PriceProduct=itemView.findViewById(R.id.txt_price_product);
            totalQuantity=itemView.findViewById(R.id.totalQuantity);
            ImageProduct=itemView.findViewById(R.id.img_product);
            sizePro=itemView.findViewById(R.id.sizePro);
            Description=itemView.findViewById(R.id.chitiet_description);
        }
    }
}
