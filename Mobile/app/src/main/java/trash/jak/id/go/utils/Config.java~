package trash.jak.id.go.utils;

/**
 * Created by itp on 06/12/17.
 */

public final class Config {
    /*
    | -------------------------------------------------------------------------------------------
    | Untuk menentukan jenis request protokol apakah terkatagori http / https.
    | -------------------------------------------------------------------------------------------
    */
    public static final String URL_HTTP_STRING = "http://";
    public static final String URL_HTTPS_STRING = "https://";

    /*
    |-----------------------------------------------------------------------------------------------
    | Directory name to store captured images and videos
    |-----------------------------------------------------------------------------------------------
    */
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";

    /*
    |-----------------------------------------------------------------------------------------------
    | URL server for this application to transfer data.
    |-----------------------------------------------------------------------------------------------
    */

    public static final String url_Server_Alpha = "alpha.simba.baznas.go.id";

        /*
    |-----------------------------------------------------------------------------------------------
    | URL server for this application to transfer data.
    |-----------------------------------------------------------------------------------------------
    */

    public static final String url_Server = "simba.baznas.go.id";

    /*
    |-----------------------------------------------------------------------------------------------
    | URL server for this application to transfer data Form Kas Masuk
    |-----------------------------------------------------------------------------------------------
    */

    public static final String url_kas_masuk = "simba.baznas.go.id";

        /*
    |-----------------------------------------------------------------------------------------------
    | URL server for this application API
    |-----------------------------------------------------------------------------------------------
    */

    public static final String url_api= "api.baznas.go.id";

    /*
    |-----------------------------------------------------------------------------------------------
    | Database name
    |-----------------------------------------------------------------------------------------------
    */
    public static final String DATABASE_NAME = "simba_db";

    /*
    |-----------------------------------------------------------------------------------------------
    | Database path
    |-----------------------------------------------------------------------------------------------
    */
    public static final String DATABASE_PATH = "/data/data/lite.simba.id.go.baznas.simbalite/databases/";

    /*
    |-----------------------------------------------------------------------------------------------
    | Database version
    |-----------------------------------------------------------------------------------------------
    */
    public static final int DATABASE_VERSION = 6;

    /*
    |-----------------------------------------------------------------------------------------------
    | Method for append URL and URI as API
    |-----------------------------------------------------------------------------------------------
    */

    public static String makeUrlString(String uri) {
        StringBuilder url = new StringBuilder(URL_HTTPS_STRING);
        url.append(url_Server);
        url.append("/");
        url.append(uri);
        return url.toString();
    }

        /*
    |-----------------------------------------------------------------------------------------------
    | Method for append URL and URI as API Foto Mustahik
    |-----------------------------------------------------------------------------------------------
    */

    public static String makeUrlStringAlpha(String uri) {
        StringBuilder url = new StringBuilder(URL_HTTP_STRING);
        url.append(url_Server_Alpha);
        url.append("/");
        url.append(uri);
        return url.toString();
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Method for append URL and URI as API Form Kas Masuk
    |-----------------------------------------------------------------------------------------------
    */

    public static String makeUrlStringKasMasuk(String uri) {
        StringBuilder url = new StringBuilder(URL_HTTPS_STRING);
        url.append(url_kas_masuk);
        url.append("/");
        url.append(uri);
        return url.toString();
    }

        /*
    |-----------------------------------------------------------------------------------------------
    | Method for append URL and URI as API
    |-----------------------------------------------------------------------------------------------
    */

    public static String makeUrlStringApi(String uri) {
        StringBuilder url = new StringBuilder(URL_HTTPS_STRING);
        url.append(url_api);
        url.append("/");
        url.append(uri);
        return url.toString();
    }
    /*
    |-----------------------------------------------------------------------------------------------
    | Method for append URL and URI as Image URL
    |-----------------------------------------------------------------------------------------------
    */
    public static String makeUrlStringForImage(String uri) {
        StringBuilder url = new StringBuilder(URL_HTTP_STRING);
        url.append(url_Server);
        url.append("/");
        url.append("files");
        url.append("/");
        url.append("photo");
        url.append("/");
        url.append(uri);
        return url.toString();
    }


}
