package se.a3;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private List<Document> _documents = new ArrayList<Document>();

    private boolean _isRelevanceSet = false;
    private double _relevance;
    private double _popularity;

    public void add(Document document){
        _documents.add(document);
    }

    public int getNoOfDocuments(){
        return _documents.size();
    }

    public void setRelevance(double relevance){
        _relevance = relevance;
        _isRelevanceSet = true;
    }

    public void setPopularity(double popularity){
        _popularity = popularity;
    }

    public double getRelevance(){
        if(_isRelevanceSet){
            return _relevance;
        } else {
            throw new InvalidOperationException();
        }
    }

    public double getPopularity(){
        return _popularity;
    }

}
