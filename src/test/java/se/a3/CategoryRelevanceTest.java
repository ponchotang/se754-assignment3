package se.a3;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import se.a3.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class CategoryRelevanceTest {
    DocumentManager _documentManager;
    IClusterer _clusterer;
    IKeywordExtractor _extractor;
    ITextSummariser _summariser;

    @Before
    public void setUpTwoClusters() {
        //Given
        _clusterer = Mockito.mock(IClusterer.class);
        _extractor = Mockito.mock(IKeywordExtractor.class);
        _summariser = Mockito.mock(ITextSummariser.class);


        // Search results of two documents
        List<Document> searchResults = new ArrayList<Document>();
        Document docOne1 = new Document("one");
        Document docOne2 = new Document("one");
        Document docTwo = new Document("two");
        searchResults.add(docOne1);
        searchResults.add(docOne2);
        searchResults.add(docTwo);

        _documentManager = new DocumentManager(searchResults, _clusterer, _extractor, _summariser);

        // Two clusters returned from two documents as search results
        Category clusterOne = new Category();
        clusterOne.add(docOne1);
        clusterOne.add(docOne2);
        Category clusterTwo = new Category();
        clusterTwo.add(docTwo);

        List<Category> twoClusters = new ArrayList<Category>();
        twoClusters.add(clusterOne);
        twoClusters.add(clusterTwo);
        Mockito.doReturn(twoClusters).when(_clusterer).createClusters(searchResults);

        // create clusters
        _documentManager.createClusters();
    }

    @Test
    public void shouldSetRelevanceWhenGivenValidValues() {
        // When
        _documentManager.setClusterRelevance(0,0.5);
        _documentManager.setClusterRelevance(1,0.1);

        // Then
        List<Category> clusters = _documentManager.getClusters();

        Category clusterOne = clusters.get(0);
        Category clusterTwo = clusters.get(1);
        assertEquals(0.5,clusterOne.getRelevance(),Double.POSITIVE_INFINITY);
        assertEquals(0.1,clusterTwo.getRelevance(),Double.POSITIVE_INFINITY);
    }

    @Test
    public void shouldStillSetRelevanceWhenGivenValidBoundaryValues() {
        // When
        _documentManager.setClusterRelevance(0,1);
        _documentManager.setClusterRelevance(1,0);

        // Then
        List<Category> clusters = _documentManager.getClusters();

        Category clusterOne = clusters.get(0);
        Category clusterTwo = clusters.get(1);
        assertEquals(1,clusterOne.getRelevance(),Double.POSITIVE_INFINITY);
        assertEquals(0,clusterTwo.getRelevance(),Double.POSITIVE_INFINITY);
    }

    @Test(expected = InvalidOperationException.class)
    public void shouldThrowExceptionWhenRelevanceValueIsAboveValidRange() {
        // When
        _documentManager.setClusterRelevance(0,3);
    }

    @Test(expected = InvalidOperationException.class)
    public void shouldThrowExceptionWhenRelevanceIsNegativeNumber() {
        // When
        _documentManager.setClusterRelevance(0,-1);
    }

    @Test(expected = InvalidOperationException.class)
    public void shouldThrowExceptionWhenSettingRelevanceForOverRangePositionCluster() {
        // When
        _documentManager.setClusterRelevance(0,0.8);
        _documentManager.setClusterRelevance(1,0.1);
        // invalid third cluster being set a valid relevance
        _documentManager.setClusterRelevance(2,0.1);
    }

    @Test(expected = InvalidOperationException.class)
    public void shouldThrowExceptionWhenSettingRelevanceForNegativeIndexPositionCluster() {
        // When
        _documentManager.setClusterRelevance(0,0.8);
        _documentManager.setClusterRelevance(1,0.1);
        // invalid third cluster being set a valid relevance
        _documentManager.setClusterRelevance(-1,0.1);
    }
}
