package account.fpoly.s_shop_client.Tools;

import android.content.Context;

import account.fpoly.s_shop_client.Modal.Address;
import account.fpoly.s_shop_client.Modal.District;
import account.fpoly.s_shop_client.Modal.Province;
import account.fpoly.s_shop_client.Modal.Ward;

public class ADDRESS {
    public static Province province = null;
    public static District district = null;
    public static Ward ward = null;

    public static Address aDefault(Context context) {
        Address address = null;
        for (int i = 0; i < LIST.listAddress.size(); i++) {
            if (LIST.listAddress.get(i).get_id().equals(TOOLS.getDefaulAddress(context))) {
                address = LIST.listAddress.get(i);
                break;
            }
        }
        if(address==null&& !LIST.listAddress.isEmpty()){
            address = LIST.listAddress.get(0);
        }
        return address;
    }
}
