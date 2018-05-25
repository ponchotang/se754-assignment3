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

public class DocumentManagerClusterTest {
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
    public void shouldClusterDocumentsWhenDocumentsAreRetrieved() {
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
    public void shouldCreateNoClustersWhenNoDocumentsRetrieved() {
        // Given
        documentManager = new DocumentManager(searchResults, clusterer, null, null);

        // When
        documentManager.createClusters();

        // Then
        assertEquals(0, documentManager.getClusters().size());
    }

    @Test
    public void shouldContainAssociatedDocumentsWhenClustersAreMade() {
        // Given
        String documentContentsString = "one one";

        searchResults.add(new Document("one"));
        searchResults.add(new Document("one"));
        searchResults.add(new Document("two"));
        searchResults.add(new Document("two"));
        searchResults.add(new Document("three"));

        clusters.add(new Category(searchResults.subList(0, 2)));
        clusters.add(new Category(searchResults.subList(2, 4)));
        clusters.add(new Category(searchResults.subList(4, 5)));

        documentManager = new DocumentManager(searchResults, clusterer, null, null);

        // When
        documentManager.createClusters();
        Category cluster = documentManager.getClusters().get(0);

        // Then
        assertEquals(documentContentsString, cluster.getDocumentContents());
    }
}
