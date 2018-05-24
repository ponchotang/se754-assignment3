package se.a3;

public class KeywordCollection {
    String _originalString;

    public KeywordCollection(String keywords) {
        _originalString = keywords;
    }

    @Override
    public String toString() {
        return _originalString;
    }
}
