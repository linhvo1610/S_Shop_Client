package account.fpoly.s_shop_client.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import account.fpoly.s_shop_client.Interface.ProvinceOnClick;
import account.fpoly.s_shop_client.Modal.Province;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Tools.ADDRESS;

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ProvinceViewHolder> {

    private final Context context;
    private List<Province> list;

    private final ProvinceOnClick provinceOnClick;

    public ProvinceAdapter(Context context, ProvinceOnClick provinceOnClick) {
        this.context = context;
        this.provinceOnClick = provinceOnClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Province> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProvinceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.province_layout, parent, false);
        return new ProvinceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProvinceViewHolder holder, int position) {
        if (ADDRESS.province != null) {
            holder.btn_choose.setText(ADDRESS.province.getName());
            holder.btn_choose.setOnClickListener(v -> provinceOnClick.ItemClick(ADDRESS.province));
            return;
        }
        Province province = list.get(position);
        if (province != null) {
            holder.btn_choose.setText(province.getName());
            holder.btn_choose.setOnClickListener(v -> provinceOnClick.ItemClick(province));
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            if (ADDRESS.province != null) {
                return 1;
            }
            return list.size();
        }
        return 0;
    }

    public static class ProvinceViewHolder extends RecyclerView.ViewHolder {

        private final Button btn_choose;

        public ProvinceViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_choose = itemView.findViewById(R.id.btn_choose);
        }
    }
}
