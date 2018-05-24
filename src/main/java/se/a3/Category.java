package se.a3;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private List<Document> _documents = new ArrayList<Document>();
    private String _label;
    private String _summary;

    private boolean _isRelevanceSet = false;
    private double _relevance;
    private double _popularity;

    public Category() {

    }

    public Category(List<Document> documents) {
        _documents = documents;
    }

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

    public void setLabel(String label) {
        _label = label;
    }

    public void setSummary(String summary) {
        _summary = summary;
    }

    public String getLabel() {
        return _label;
    }

    public String getSummary() {
        return _summary;
    }

    public String getDocumentContents() {
        String contents = "";

        for (Document document: _documents) {
            contents += document.getContent() + " ";
        }

        return contents.trim();
    }
}
