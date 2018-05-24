import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

public class DocumentSearcherTest {
    DocumentSearcher searcher;
    ISearchEngine searchEngine;

    List<String> searchResults;

    @Before
    public void setUp() {
        searchResults = new ArrayList<String>();
        searchEngine = Mockito.mock(ISearchEngine.class);
    }

    @Test
    public void testDocumentSearcherReturnsResults() {
        // Given
        searchResults = new ArrayList<String>();
        searchResults.add("");
        searchResults.add("");
        searchResults.add("");

        Mockito.doReturn(searchResults).when(searchEngine).search("one two three four");

        // When
        List<Document> resultsAsDocuments = searcher.search(new KeywordCollection("one two three four"));

        // Then
        assertTrue(3, resultsAsDocuments.size());
    }

    @Test
    public void testSearchWithNoKeywords() {
        // Given
        KeywordCollection keywords = null;

        // When
        List<Document> resultsAsDocuments = searcher.search(keywords);

        // Then
        assertTrue(0, resultsAsDocuments.size());
    }
}
