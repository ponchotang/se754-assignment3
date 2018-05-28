package se.a3;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import se.a3.Document;
import se.a3.DocumentSearcher;
import se.a3.ISearchEngine;
import se.a3.KeywordCollection;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DocumentSearcherTest {
    DocumentSearcher searcher;
    ISearchEngine searchEngine;

    List<String> searchResults;

    @Before
    public void setUp() {
        searchResults = new ArrayList<String>();
        searchEngine = Mockito.mock(ISearchEngine.class);
        searcher = new DocumentSearcher(searchEngine);
    }

    @Test
    public void shouldReturnDocumentsWhenKeywordsAreGiven() {
        // Given
        KeywordCollection keywords = new KeywordCollection("one two three four");
        searchResults = new ArrayList<String>();
        searchResults.add("");
        searchResults.add("");
        searchResults.add("");

        Mockito.doReturn(searchResults).when(searchEngine).search(keywords);

        // When
        List<Document> resultsAsDocuments = searcher.search(keywords);

        // Then
        assertEquals(3, resultsAsDocuments.size());
        Mockito.verify(searchEngine, Mockito.times(1));
    }

    @Test
    public void shouldReturnNoDocumentsWhenNoKeywordsAreGiven() {
        // Given
        KeywordCollection keywords = null;

        // When
        List<Document> resultsAsDocuments = searcher.search(keywords);

        // Then
        assertEquals(0, resultsAsDocuments.size());
    }
}
