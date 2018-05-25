package se.a3;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import se.a3.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DocumentManagerLabelTest {
    List<Document> searchResults;
    List<Category> clusters;

    DocumentManager documentManager;

    IClusterer clusterer;
    IKeywordExtractor extractor;

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
        extractor = Mockito.mock(IKeywordExtractor.class);

        Mockito.doReturn(clusters).when(clusterer).createClusters(searchResults);
    }

    @Test
    public void shouldGenerateLabelsWhenClustersAreCreated() {
        // Given
        documentManager = new DocumentManager(searchResults, clusterer, extractor, null);
        Mockito.doReturn("one").when(extractor).extractKeywords("one one");

        // When
        documentManager.createClusters();
        documentManager.generateLabels();
        Category category = documentManager.getClusters().get(0);

        // Then
        assertEquals("one", category.getLabel());
    }

    @Test(expected = DocumentsNotClusteredException.class)
    public void shouldThrowExceptionWhenGeneratingLabelsWithoutCreatingCluster() {
        // Given
        documentManager = new DocumentManager(searchResults, clusterer, extractor, null);

        // When
        documentManager.generateLabels();
    }
}
