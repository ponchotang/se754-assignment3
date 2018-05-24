package se.a3;

import java.util.ArrayList;
public class KeywordCollection {
    private ArrayList<Keyword> _list = new ArrayList<Keyword>();

    public KeywordCollection(String text){
        String[] keywords = text.split(" ");

        for (String keyword : keywords){
            Keyword word = new Keyword(keyword);
            _list.add(word);
        }
    }

    public KeywordCollection(ArrayList<Keyword> list){
        _list = list;
    }

    public int getLength(){
        return _list.size();
    }

    public String getString(){
        String collectionString = "";
        for (Keyword word: _list){
            String string = word.getWord();
            collectionString = collectionString.concat(string) + " ";
        }
        String result;
        result = collectionString.trim();
        return result;
    }

    public ArrayList<Keyword> getList(){
        return _list;
    }
}
