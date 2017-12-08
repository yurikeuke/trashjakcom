package trash.jak.id.go.model;

/**
 * Created by itp on 08/11/17.
 */

public class MapsMainModel {

    private String  nama, lat, lang;
    public MapsMainModel(String nama, String lat, String lang) {
        this.nama = nama;
        this.lat = lat;
        this.lang = lang;
    }
    public MapsMainModel() {

    }
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
