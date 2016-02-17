package br.com.saturno.android.util;

/**
 * Created by andre on 16/02/2016.
 */
public class Bank {

    private String name;
    private String imageResource;

    public Bank(String name, String imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }
}
