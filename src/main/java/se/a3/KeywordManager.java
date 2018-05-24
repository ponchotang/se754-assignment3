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
        keyword.setWeight(list.size()+1);
        list.add(keyword);
        KeywordCollection newCollection = new KeywordCollection(list);
        return newCollection;
    }

    public KeywordCollection removeWord(int index) throws ArrayIndexOutOfBoundsException{
        ArrayList<Keyword> list = _collection.getList();
        list.remove(index);
        for (int i = index; i < list.size(); i++){
            list.get(i).setWeight(list.get(i).getWeight()-1);
        }
        KeywordCollection newCollection = new KeywordCollection(list);
        return newCollection;
    }

    public KeywordCollection changePriority(int index, int pos) throws IndexOutOfBoundsException{
        ArrayList<Keyword> list = _collection.getList();
        Keyword word = list.get(index);
        word.setWeight(pos);
        list.remove(index);
        list.add(pos,word);
        for (int i = 0; i < list.size(); i++){
            list.get(i).setWeight(list.get(i).getWeight()+1);
        }
        KeywordCollection newCollection = new KeywordCollection(list);
        return newCollection;
    }
}
