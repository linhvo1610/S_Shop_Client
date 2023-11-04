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

import account.fpoly.s_shop_client.Interface.DistrictOnClick;
import account.fpoly.s_shop_client.Modal.District;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Tools.ADDRESS;

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.DistrictViewHolder> {

    private final Context context;
    private List<District> list;

    private final DistrictOnClick districtOnClick;

    public DistrictAdapter(Context context, DistrictOnClick districtOnClick) {
        this.context = context;
        this.districtOnClick = districtOnClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<District> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DistrictViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.province_layout, parent, false);
        return new DistrictViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistrictViewHolder holder, int position) {
        if (ADDRESS.district != null) {
            holder.btn_choose.setText(ADDRESS.district.getName());
            holder.btn_choose.setOnClickListener(v -> districtOnClick.ItemClick(ADDRESS.district));
            return;
        }
        District district = list.get(position);
        if (district != null) {
            holder.btn_choose.setText(district.getName());
            holder.btn_choose.setOnClickListener(v -> districtOnClick.ItemClick(district));
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            if (ADDRESS.district != null) {
                return 1;
            }
            return list.size();
        }
        return 0;
    }

    public static class DistrictViewHolder extends RecyclerView.ViewHolder {

        private final Button btn_choose;

        public DistrictViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_choose = itemView.findViewById(R.id.btn_choose);
        }
    }
}
