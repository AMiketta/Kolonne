package com.amiketta.kolonne;

/**
 * Created by AMiketta on 21.02.2016.
 */
public class KolonnenChannel {
    private String author;
    private String title;
    private Boolean go;
    public KolonnenChannel() {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }

    public KolonnenChannel(String author, String title) {
        this.author = author;
        this.title = title;
        this.go = true;
    }

    public String getAuthor() {
        return author;
    }
    public String getTitle() {
        return title;
    }

    // Gibt zurück ob gefahren werden kann oder ob gewartet werden soll. false falls gewartet werden soll...
    public Boolean getGo(){
        return go;
    }
}
