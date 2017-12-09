package com.selecto.vladrevuk.test.app.Structures;

/**
 * Created by user on 08.12.2017.
 */

public class ContactStructure {

    String _id;
    String img;
    String name;

    public ContactStructure(String _id, String img, String name) {
        this._id = _id;
        this.img = img;
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }
}
