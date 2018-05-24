package se.a3;

import java.util.ArrayList;
public class KeywordManager {

    private KeywordCollection _collection;

    public KeywordManager(KeywordCollection collection){
        _collection = collection;
    }

    // Words are added to collection at the end of the list by default
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
        adjustWeights(index,list,-1);
        KeywordCollection newCollection = new KeywordCollection(list);
        return newCollection;
    }

    public KeywordCollection changePriority(int index, int pos) throws IndexOutOfBoundsException{
        ArrayList<Keyword> list = _collection.getList();
        Keyword word = list.get(index);
        word.setWeight(pos);
        list.remove(index);
        list.add(pos,word);
        adjustWeights(0,list,1);
        KeywordCollection newCollection = new KeywordCollection(list);
        return newCollection;
    }

    public void adjustWeights(int index, ArrayList<Keyword> list, int weight){
        for (int i = index; i < list.size(); i++){
            Keyword word = list.get(i);
            int wordWeight = word.getWeight();
            word.setWeight(wordWeight+weight);
        }
    }
}
