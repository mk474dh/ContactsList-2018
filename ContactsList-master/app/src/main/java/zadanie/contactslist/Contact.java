package zadanie.contactslist;

public class Contact {
    private String uid,name,surname, number, gender,email,foto,date;

    public Contact() {
    }

    public Contact(String uid, String name, String surname, String date, String gender, String number, String email, String foto) {
        this.uid = uid;   // Primary key
        this.name = name;
        this.surname = surname;
        this.date = date;
        this.gender = gender;
        this.number = number;
        this.email = email;
        this.foto = foto;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFoto(){return foto;}

    public void setFoto(String foto){this.foto = foto;}

    public String getDate(){return date;}

    public void setDate(String date){this.date = date;}
}
