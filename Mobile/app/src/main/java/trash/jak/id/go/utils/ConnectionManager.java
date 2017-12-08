package trash.jak.id.go.utils;

import java.io.IOException;
import java.util.Map;

/**
 * Created by itp on 06/12/17.
 */

public class ConnectionManager {
    /*
   |-----------------------------------------------------------------------------------------------
   | Static URL
   |-----------------------------------------------------------------------------------------------
   */
    public static final String CM_URL_LOGIN             = Config.makeUrlString("api/login_mobile/");
    public static final String CM_FIREBASE              = Config.makeUrlString("api/set_firebase_key");
    public static final String CM_UPLOAD                = Config.makeUrlStringAlpha("api/aH4r164nt3Ng719bffae3e130/");
    public static final String CM_GET_LIST_MUSTAHIK     = Config.makeUrlString("v1/rakornas/firebase/");
    public static final String CM_GET_LIST_MUZAKI       = Config.makeUrlString("api/cFAaD3eBdF780e1c93d5eAdFf4aE4/");
    public static final String CM_FORM_KAS_MASUK        = Config.makeUrlStringKasMasuk("api/ajax_transaksi_simpan");
    public static final String CM_FORM_ADD_MUZAKI       = Config.makeUrlStringKasMasuk("api/ajax_muzaki_register");
    public static final String tesssss                  = Config.makeUrlStringKasMasuk("/api/sdsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdf/");
    public static final String CM_GET_USER_ONLINE       = Config.makeUrlStringApi("v1/dashboard/nasional/ajax_users_online.php");
    public static final String CM_GET_NEWS              = "http://myshaf.com/alif/newstrash.php";
    public static final String CM_GET_VERSION           = "http://myshaf.com/alif/versiontrashjak.php";
    public static final String CM_URL_TRACKLOG          = Config.makeUrlStringApi("v1/tracklog/add.php");
    public static final String CM_GET_LIST_Building     = "http://ppid.jakarta.go.id/json?url=http://data.jakarta.go.id/dataset/f3ee79da-ff89-4ba7-be4c-3f090d4f77f7/resource/663b7b5c-4cee-45d6-b7cb-0ed265699f81/download/Perusaaan-Angkutan-Sampah-Berizin-2016.csv";

    /*
    |----------------------------------------------------------------------------------------------------------------
    | URL Compiler For GET Method Resquest Link URL
    |----------------------------------------------------------------------------------------------------------------
    */
    public static String requestUrl (String url, Map<String, String> params) throws IOException {
        StringBuilder param = new StringBuilder(url);
        param.append("?");
        int a =0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            a = a+1;
            param.append(entry.getKey());
            param.append("=");
            param.append(entry.getValue());
            if (a<params.size()) {
                param.append("&");
            }
        }
        return param.toString();
    }
}
