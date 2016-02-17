package br.com.saturno.android.database.entities;

/**
 * Created by andre on 13/01/2016.
 */
public abstract class Entity {

    private int _id;

    public Entity(){
    }

    public Entity(int id){
        this._id = id;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
}
