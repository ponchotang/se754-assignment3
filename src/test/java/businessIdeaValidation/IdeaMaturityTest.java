package businessIdeaValidation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import se.a3.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IdeaMaturityTest {
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
        Document docOne = new Document("one");
        Document docTwo1 = new Document("two");
        Document docTwo2 = new Document("two");
        Document docTwo3 = new Document("two");
        searchResults.add(docOne);
        searchResults.add(docTwo1);
        searchResults.add(docTwo2);
        searchResults.add(docTwo3);

        _documentManager = new DocumentManager(searchResults, _clusterer, _extractor, _summariser);

        // Two clusters returned from two documents as search results
        Category clusterOne = new Category();
        clusterOne.add(docOne);
        Category clusterTwo = new Category();
        clusterTwo.add(docTwo1);
        clusterTwo.add(docTwo2);
        clusterTwo.add(docTwo3);

        List<Category> twoClusters = new ArrayList<Category>();
        twoClusters.add(clusterTwo);
        twoClusters.add(clusterOne);
        Mockito.doReturn(twoClusters).when(_clusterer).createClusters(searchResults);

        // create clusters and assign popularity
        _documentManager.createClusters();
        _documentManager.assignPopularityToClusters();
    }

    @Test
    public void testMaturityWithAllRelevanceAssigned() {
        // When
        _documentManager.setClusterRelevance(0,1);
        _documentManager.setClusterRelevance(1,0.1);

        // Then
        assertEquals(0.775,_documentManager.computeIdeaMaturity(),Double.POSITIVE_INFINITY);
    }


    @Test(expected = InvalidOperationException.class)
    public void testMaturityWithNotAllRelevanceAssigned() {
        // When
        // only relevance of one of two clusters set
        _documentManager.setClusterRelevance(0, 1);
        _documentManager.computeIdeaMaturity();
    }


}
