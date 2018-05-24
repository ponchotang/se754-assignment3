package se.a3;

import java.util.List;

public class Category {
    private List<Document> _documents;

    private String _label;
    private String _summary;


    public Category() {

    }

    public Category(List<Document> documents) {
        _documents = documents;
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
