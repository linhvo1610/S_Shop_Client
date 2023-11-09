package account.fpoly.s_shop_client.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
    BillAdapter adapter;String iduser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_trangthaidon, container, false);
        rcv=view.findViewById(R.id.rcv_lichsu);
        SharedPreferences preferences = getActivity().getSharedPreferences("infoUser", Context.MODE_PRIVATE);
        iduser = preferences.getString("iduser", null);
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rcv.setLayoutManager(manager);
        hienthiHistoty();

        return view;
    }

    String idpro;
    private void hienthiHistoty() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.api + "billDone?id_user=" + iduser + "&status=Done", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Bill bill = new Bill();
                        bill.setTotalPrice(jsonObject.getDouble("totalPrice"));
                        bill.setTotalQuantity(jsonObject.getInt("totalQuantity"));
                        bill.setStatus(jsonObject.getString("status"));
                        bill.setSize(jsonObject.getInt("size"));

                        JSONArray jsonArrayPro = jsonObject.getJSONArray("product");
                        for (int j = 0; j < jsonArrayPro.length(); j++) {
                            JSONObject jsonObjectPro = jsonArrayPro.getJSONObject(j);
                            try {
                                JSONObject idProductObject  = jsonObjectPro.getJSONObject("id_product");
                                bill.setName(idProductObject .getString("name"));
                                bill.setImage(idProductObject .getString("image"));

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
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