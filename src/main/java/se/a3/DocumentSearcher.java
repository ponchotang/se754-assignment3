package se.a3;

import java.util.ArrayList;
import java.util.List;

public class DocumentSearcher {
    ISearchEngine _searchEngine;

    public DocumentSearcher(ISearchEngine searchEngine) {
        _searchEngine = searchEngine;
    }

    public List<Document> search(KeywordCollection keywords) {
        if (keywords == null) {
            return new ArrayList<Document>();
        }

        List<String> searchResults = _searchEngine.search(keywords.toString());
        List<Document> resultsAsDocuments = new ArrayList<Document>();

        for (String searchResult: searchResults) {
            resultsAsDocuments.add(new Document(searchResult));
        }

        return resultsAsDocuments;
    }
}
