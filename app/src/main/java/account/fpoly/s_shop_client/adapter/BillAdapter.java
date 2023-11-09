package account.fpoly.s_shop_client.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.Modal.Bill;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Xacnhan_Bill;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder>{

    Context context;
    List<Bill> list;

    public BillAdapter(Context context, List<Bill> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.iteam_xacnhan_bill,parent,false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill bill = list.get(position);

        holder.totalPrice.setText("đ"+bill.getTotalPrice());
        holder.totalQuantity.setText("x"+bill.getTotalQuantity());
        holder.statusPro.setText(bill.getStatus());
        holder.sizePro.setText("Size "+bill.getSize());
        holder.namePro.setText(bill.getName());
        Glide.with(holder.itemView).load(API.api_image + bill.getImage()).into(holder.imageBill);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class BillViewHolder extends ViewHolder {
        TextView totalQuantity,totalPrice,statusPro,namePro,sizePro;
        ImageView imageBill;
        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            totalQuantity = itemView.findViewById(R.id.totalQuantity);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            statusPro = itemView.findViewById(R.id.statusPro);
            namePro = itemView.findViewById(R.id.nameProbill);
            imageBill = itemView.findViewById(R.id.imageBill);
            sizePro = itemView.findViewById(R.id.sizePro);
        }
    }
}
