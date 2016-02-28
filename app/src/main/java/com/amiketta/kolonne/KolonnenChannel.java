package com.amiketta.kolonne;

/**
 * Created by AMiketta on 21.02.2016.
 */
public class KolonnenChannel {
    private String author;
    private String title;
    private String password;
    private Boolean go;

    public KolonnenChannel() {

    }

    public KolonnenChannel(String author, String title, String password) {
        this.author = author;
        this.title = title;
        this.password = password;
        this.go = true;


    }

    public String getAuthor() {
        return author;
    }
    public String getTitle() {
        return title;
    }

    public Boolean hasPassword() {
        return (this.password != null && !this.password.equals(""));
    }

    public Boolean validatePassword(String password) {
        return this.password.equals(password);
    }

    // Gibt zurück ob gefahren werden kann oder ob gewartet werden soll. false falls gewartet werden soll...
    public Boolean getGo(){
        return go;
    }
}
