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
    public static final String CM_UPLOAD                = Config.makeUrlStringAlpha("api/supload/");
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
