package se.a3;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import se.a3.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CategoryPopularityTest {
    IClusterer _clusterer;
    IKeywordExtractor _extractor;
    ITextSummariser _summariser;

    @Before
    public void setUp() {
        //Given
        _clusterer = Mockito.mock(IClusterer.class);
        _extractor = Mockito.mock(IKeywordExtractor.class);
        _summariser = Mockito.mock(ITextSummariser.class);
    }

    @Test
    public void shouldHavePopularityOfOneWhenOnlyOneCluster() {
        //Given
        // Search results of one document
        List<Document> searchResults = new ArrayList<Document>();
        Document docOne = new Document("one");
        searchResults.add(docOne);

        DocumentManager documentManager = new DocumentManager(searchResults, _clusterer, _extractor, _summariser);

        // One cluster returned from one document as search results
        Category clusterOne = new Category();
        clusterOne.add(docOne);
        List<Category> oneCluster = new ArrayList<Category>();
        oneCluster.add(clusterOne);
        Mockito.doReturn(oneCluster).when(_clusterer).createClusters(searchResults);

        //When
        // create clusters and assign popularity based on number of documents per cluster
        documentManager.createClusters();
        documentManager.assignPopularityToClusters();

        // Then
        // clusters should only have one cluster with popularity of 1
        List<Category> clusters = documentManager.getClusters();
        assertEquals(1,clusters.size());
        Category onlyCluster = clusters.get(0);
        assertEquals(1,onlyCluster.getPopularity(),Double.POSITIVE_INFINITY);
    }

    @Test
    public void shouldProportionatePopularitiesWhenMoreThanOneCluster() {
        //Given
        // Search results of ten documents
        List<Document> searchResults = new ArrayList<Document>();
        // 1 document of clearly 1 category
        Document docOne = new Document("one");
        // 6 documents of clearly another category
        Document docSix1 = new Document("six");
        Document docSix2 = new Document("six");
        Document docSix3 = new Document("six");
        Document docSix4 = new Document("six");
        Document docSix5 = new Document("six");
        Document docSix6 = new Document("six");
        // 3 documents of clearly another category
        Document docThree1 = new Document("three");
        Document docThree2 = new Document("three");
        Document docThree3 = new Document("three");

        searchResults.add(docOne);
        searchResults.add(docSix1);
        searchResults.add(docSix2);
        searchResults.add(docSix3);
        searchResults.add(docSix4);
        searchResults.add(docSix5);
        searchResults.add(docSix6);
        searchResults.add(docThree1);
        searchResults.add(docThree2);
        searchResults.add(docThree3);

        DocumentManager documentManager = new DocumentManager(searchResults, _clusterer, _extractor, _summariser);

        // Three clusters returned from ten documents as search results
        Category clusterOne = new Category();
        clusterOne.add(docOne);

        Category clusterTwo = new Category();
        clusterTwo.add(docSix1);
        clusterTwo.add(docSix2);
        clusterTwo.add(docSix3);
        clusterTwo.add(docSix4);
        clusterTwo.add(docSix5);
        clusterTwo.add(docSix6);

        Category clusterThree = new Category();
        clusterThree.add(docThree1);
        clusterThree.add(docThree2);
        clusterThree.add(docThree3);

        List<Category> threeClusters = new ArrayList<Category>();
        threeClusters.add(clusterTwo);
        threeClusters.add(clusterThree);
        threeClusters.add(clusterOne);
        Mockito.doReturn(threeClusters).when(_clusterer).createClusters(searchResults);

        //When
        // create clusters and assign popularity based on number of documents per cluster
        documentManager.createClusters();
        documentManager.assignPopularityToClusters();

        // Then
        // clusters should only have three cluster with popularity of 0.6, 0.3 and 0.1
        List<Category> clusters = documentManager.getClusters();
        Category cluster1 = clusters.get(2);
        Category cluster2 = clusters.get(0);
        Category cluster3 = clusters.get(1);
        assertEquals(0.6,cluster2.getPopularity(),Double.POSITIVE_INFINITY);
        assertEquals(0.3,cluster3.getPopularity(),Double.POSITIVE_INFINITY);
        assertEquals(0.1,cluster1.getPopularity(),Double.POSITIVE_INFINITY);

    }
}
