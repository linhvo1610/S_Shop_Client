package account.fpoly.s_shop_client.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.Modal.Bill;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.adapter.BillAdapter;


public class LichsuFragment extends Fragment {

    RecyclerView rcv;
    List<Bill> list;
    BillAdapter adapter;
    String iduser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_trangthaidon, container, false);

        rcv = view.findViewById(R.id.rcv);
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rcv.setLayoutManager(manager);
        SharedPreferences preferences = getContext().getSharedPreferences("infoUser",getContext().MODE_PRIVATE);
        iduser = preferences.getString("iduser", null);

        hienthiHistoty();

        return view;
    }

    private void hienthiHistoty() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.api + "bill?id_user=" + iduser + "&status=Xác nhận", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Bill bill = new Bill();
                        bill.setTotalPrice(jsonObject.getDouble("totalPrice"));
                        bill.setTotalQuantity(jsonObject.getInt("totalQuantity"));

                        list.add(bill);
                    }
                        adapter = new BillAdapter(getContext(),list);
                        rcv.setAdapter(adapter);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Call API Fail", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}