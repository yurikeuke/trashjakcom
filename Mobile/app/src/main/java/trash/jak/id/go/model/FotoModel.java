package trash.jak.id.go.model;

/**
 * Created by itp on 07/12/17.
 */

public class FotoModel {
    private String gambar;
        private String nama;
        private String deskripsi;

        public FotoModel(String gambar, String nama, String deskripsi) {
            this.gambar = gambar;
            this.nama = nama;
            this.deskripsi = deskripsi;
        }

        public FotoModel() {

        }

        public String getGambar() {
            return gambar;
        }

        public void setGambar(String gambar) {
            this.gambar = gambar;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getDeskripsi() {
            return deskripsi;
        }

        public void setDeskripsi(String deskripsi) {
            this.deskripsi = deskripsi;
        }
}
