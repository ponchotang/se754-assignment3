package se.a3;

import java.util.ArrayList;
public class KeywordManager {

    private KeywordCollection _collection;

    public KeywordManager(KeywordCollection collection){
        _collection = collection;
    }

    public KeywordCollection addWord(String name){
        Keyword keyword = new Keyword(name);
        ArrayList<Keyword> list = _collection.getList();
        list.add(keyword);
        KeywordCollection newCollection = new KeywordCollection(list);
        return newCollection;
    }

    public KeywordCollection removeWord(int index){
        ArrayList<Keyword> list = _collection.getList();
        list.remove(index);
        KeywordCollection newCollection = new KeywordCollection(list);
        return newCollection;
    }

    public KeywordCollection changePriority(int index, int pos){
        ArrayList<Keyword> list = _collection.getList();
        Keyword word = list.get(index);
        list.remove(index);
        list.add(pos,word);
        KeywordCollection newCollection = new KeywordCollection(list);
        return newCollection;
    }
}
