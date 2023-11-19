package account.fpoly.s_shop_client.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import account.fpoly.s_shop_client.Modal.BillMore;
import account.fpoly.s_shop_client.Modal.Cart;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.adapter.BillAdapter;
import account.fpoly.s_shop_client.adapter.StatusBillAdapter;


public class LichsuFragment extends Fragment {
    ImageView img_back;
    RecyclerView rcv;
    List<BillMore> list;
    StatusBillAdapter adapter;String iduser;
    TextView title;

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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.api + "billStatus?id_user=" + iduser + "&status=5", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        BillMore bill = new BillMore();
                        bill.set_id(jsonObject.getString("_id"));
                        bill.setName(jsonObject.getString("name"));
                        bill.setPhone(jsonObject.getString("phone"));
                        bill.setAddress(jsonObject.getString("address"));
                        bill.setTotal(jsonObject.getInt("total"));
                        bill.setStatus(Integer.parseInt(jsonObject.getString("status")));

                        JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                        ArrayList<Cart> cartList = new ArrayList<>();
                        for (int j = 0; j < jsonArrayList.length(); j++) {
                            JSONObject jsonObjectList = jsonArrayList.getJSONObject(j);
                            Cart cart = new Cart();
                            cart.setSize(jsonObjectList.getInt("size"));
                            cart.setQuantity(jsonObjectList.getInt("quantity"));
                            cart.setPrice_product(jsonObjectList.getInt("price_product"));
                            cart.setName_product(jsonObjectList.getString("name_product"));
                            cart.setImage(jsonObjectList.getString("image"));
                            // Populate other fields of the Cart object
                            cartList.add(cart);
                        }
                        bill.setList(cartList);
                        list.add(bill);
                    }
                    adapter = new StatusBillAdapter(getContext(),list);
                    rcv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

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