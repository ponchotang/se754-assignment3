import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import se.a3.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DocumentClusteringSummaryTest {
    List<Document> searchResults;
    List<Category> clusters;

    DocumentManager documentManager;

    IClusterer clusterer;
    ITextSummariser summariser;

    @Before
    public void setUp() {
        searchResults = new ArrayList<Document>();
        searchResults.add(new Document("one"));
        searchResults.add(new Document("one"));
        searchResults.add(new Document("two"));
        searchResults.add(new Document("two"));
        searchResults.add(new Document("three"));

        Category catOne = new Category(searchResults.subList(0, 2));
        Category catTwo = new Category(searchResults.subList(2, 4));
        Category catThree = new Category(searchResults.subList(4, 5));

        clusters = new ArrayList<Category>();
        clusters.add(catOne);
        clusters.add(catTwo);
        clusters.add(catThree);

        clusterer = Mockito.mock(IClusterer.class);
        summariser = Mockito.mock(ITextSummariser.class);

        Mockito.doReturn(clusters).when(clusterer).createClusters(searchResults);
    }

    @Test
    public void testCategoryHasSummary() {
        // Given
        String summaryText = "a lot of ones";
        documentManager = new DocumentManager(searchResults, clusterer, null, summariser);
        Mockito.doReturn(summaryText).when(summariser).summarise("one one");

        // When
        documentManager.createClusters();
        documentManager.generateSummaries();
        Category category = documentManager.getClusters().get(0);

        // Then
        assertEquals(summaryText, category.getSummary());
    }

    @Test(expected = DocumentsNotClusteredException.class)
    public void testGenerateSummaryWithoutFirstClustering() {
        // Given
        documentManager = new DocumentManager(searchResults, clusterer, null, summariser);

        // When
        documentManager.generateSummaries();
    }
}
