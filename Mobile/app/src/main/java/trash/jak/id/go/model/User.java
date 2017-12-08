package trash.jak.id.go.model;

import java.io.Serializable;

/**
 * Created by itp on 17/10/17.
 */

public class User implements Serializable {

    private String nama, email, foto, nia, phone;

    public User() {

    }

    public User(String nama, String email, String foto, String nia, String phone) {
        this.nama = nama;
        this.email = email;
        this.foto = foto;
        this.nia = nia;
        this.phone = phone;

    }



    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAmilEmail() {
        return email;
    }

    public void setAmilEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNia() {
        return nia;
    }

    public void setNia(String nia) {
        this.nia = nia;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
