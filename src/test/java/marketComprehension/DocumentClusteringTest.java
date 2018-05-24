import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import se.a3.Category;
import se.a3.Document;
import se.a3.DocumentManager;
import se.a3.IClusterer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DocumentClusteringTest {
    List<Document> searchResults;
    List<Category> clusters;

    DocumentManager documentManager;

    IClusterer clusterer;

    @Before
    public void setUp() {
        searchResults = new ArrayList<Document>();
        clusters = new ArrayList<Category>();
        clusterer = Mockito.mock(IClusterer.class);

        Mockito.doReturn(clusters).when(clusterer).createClusters(searchResults);
    }

    @Test
    public void testDocumentsAreClustered() {
        // Given
        searchResults.add(new Document("one"));
        searchResults.add(new Document("one"));
        searchResults.add(new Document("two"));
        searchResults.add(new Document("two"));
        searchResults.add(new Document("three"));

        clusters.add(new Category());
        clusters.add(new Category());
        clusters.add(new Category());

        documentManager = new DocumentManager(searchResults, clusterer, null, null);

        // When
        documentManager.createClusters();

        // Then
        assertEquals(3, documentManager.getClusters().size());
    }

    @Test
    public void testNoDocumentsToCluster() {
        // Given
        documentManager = new DocumentManager(searchResults, clusterer, null, null);

        // When
        documentManager.createClusters();

        // Then
        assertEquals(0, documentManager.getClusters().size());
    }
}
