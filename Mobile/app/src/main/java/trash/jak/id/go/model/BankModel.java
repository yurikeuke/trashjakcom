package trash.jak.id.go.model;

/**
 * Created by itp on 08/12/17.
 */

public class BankModel {

    private String  nama_penyedia, alamat, kelurahan, wilayah, nomor_telepon;
    public BankModel(String nama_penyedia, String alamat, String kelurahan, String wilayah, String nomor_telepon) {
        this.nama_penyedia = nama_penyedia;
        this.alamat = alamat;
        this.kelurahan = kelurahan;
        this.wilayah = wilayah;
        this.nomor_telepon = nomor_telepon;

    }
    public BankModel() {

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

    public String getNomor_telepon() {
        return nomor_telepon;
    }

    public void setNomor_telepon(String nomor_telepon) {
        this.nomor_telepon = nomor_telepon;
    }
}
