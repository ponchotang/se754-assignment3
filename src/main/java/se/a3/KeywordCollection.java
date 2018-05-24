package se.a3;

import java.util.ArrayList;
public class KeywordCollection {
    private ArrayList<Keyword> _list = new ArrayList<Keyword>();

    public KeywordCollection(String text){

    }

    public KeywordCollection(ArrayList<Keyword> list){
        _list = list;
    }

    public int getLength(){
        return 0;
    }

    public String getString(){
        return null;
    }

    public ArrayList<Keyword> getList(){
        return _list;
    }
}
