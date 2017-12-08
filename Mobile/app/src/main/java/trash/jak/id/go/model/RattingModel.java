package trash.jak.id.go.model;

/**
 * Created by itp on 08/12/17.
 */

public class RattingModel {
    private String  nama_penyedia, alamat, kelurahan, wilayah, ratting;
    public RattingModel(String nama_penyedia, String alamat, String kelurahan, String wilayah, String ratting) {
        this.nama_penyedia = nama_penyedia;
        this.alamat = alamat;
        this.kelurahan = kelurahan;
        this.wilayah = wilayah;
        this.ratting = ratting;

    }
    public RattingModel() {

    }

    public String getNama_penyedia() {
        return nama_penyedia;
    }

    public void setNama_penyedia(String nama_penyedia) {
        this.nama_penyedia = nama_penyedia;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
    }

    public String getWilayah() {
        return wilayah;
    }

    public void setWilayah(String wilayah) {
        this.wilayah = wilayah;
    }

    public String getRatting() {
        return ratting;
    }

    public void setRatting(String ratting) {
        this.ratting = ratting;
    }
}
