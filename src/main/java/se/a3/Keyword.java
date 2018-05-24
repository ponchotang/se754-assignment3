package se.a3;

public class Keyword {

    private String _word;
    private int _weight;

    public Keyword(String word){
        _word = word;
        _weight = -1;
    }

    public String getWord(){
        return _word;
    }

    public int getWeight(){
        return _weight;
    }
}
