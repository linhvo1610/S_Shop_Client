package account.fpoly.s_shop_client.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;

import account.fpoly.s_shop_client.Modal.Bill;
import account.fpoly.s_shop_client.R;

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
        View view = LayoutInflater.from(context).inflate(R.layout.iteam_history_bill,parent,false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill bill = list.get(position);

        holder.totalPrice.setText(bill.getTotalPrice()+"");
        holder.totalQuantity.setText(bill.getTotalQuantity()+"");

        int totalQuantityBill = 0;
        for (Bill billQuan : list) {
            totalQuantityBill += billQuan.getTotalQuantity();
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class BillViewHolder extends ViewHolder {
        TextView totalQuantity,totalPrice;
        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            totalQuantity = itemView.findViewById(R.id.totalQuantity);
            totalPrice = itemView.findViewById(R.id.totalPrice);
        }
    }
}
