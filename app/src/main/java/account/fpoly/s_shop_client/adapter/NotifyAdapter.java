package account.fpoly.s_shop_client.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import account.fpoly.s_shop_client.DaGiao_activity;
import account.fpoly.s_shop_client.DangGiao_Activity;
import account.fpoly.s_shop_client.HuyBill;
import account.fpoly.s_shop_client.Modal.Notify;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Tab_Giaodien_Activity;
import account.fpoly.s_shop_client.Xacnhan_Bill;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.NotifyViewHolder>{
    private final Context context;
    private List<Notify> list;

    public NotifyAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Notify> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotifyAdapter.NotifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_layout,parent,false);
        return new NotifyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyAdapter.NotifyViewHolder holder, int position) {
        Notify notify = list.get(position);
        if(notify!=null){
            holder.ln_notify.setOnClickListener(v -> {
                int status = notify.getStatus();
                if (status == 0){
                    Intent intent = new Intent(context, Xacnhan_Bill.class);
                    context.startActivity(intent);
                }
                if (status == 2){
                    Intent intent = new Intent(context, DangGiao_Activity.class);
                    context.startActivity(intent);
                }
                if (status == 3){
                    Intent intent = new Intent(context, DaGiao_activity.class);
                    context.startActivity(intent);
                }
                if (status == 4){
                    Intent intent = new Intent(context, HuyBill.class);
                    context.startActivity(intent);
                }
                if (status == 10){
                    Intent intent = new Intent(context, Tab_Giaodien_Activity.class);
                    context.startActivity(intent);
                }
                ((Activity)context).overridePendingTransition(R.anim.next_enter,R.anim.next_exit);
                ((Activity)context).finish();
            });
            holder.tv_time.setText(String.valueOf(notify.getTime()));
            holder.tv_body.setText(notify.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return (list!=null)? list.size():0;
    }

    public static class NotifyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_time,tv_body;
        private final LinearLayout ln_notify;
        public NotifyViewHolder(@NonNull View itemView) {
            super(itemView);
            ln_notify = itemView.findViewById(R.id.ln_notify);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_body = itemView.findViewById(R.id.tv_body);
        }
    }
}
