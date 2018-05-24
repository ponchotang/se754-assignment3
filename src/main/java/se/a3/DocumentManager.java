package se.a3;

import java.util.List;

public class DocumentManager {
    List<Document> _searchResults;
    List<Category> _clusters;

    IClusterer _clusterer;
    IKeywordExtractor _extractor;
    ITextSummariser _summariser;

    public DocumentManager(List<Document> searchResults, IClusterer clusterer, IKeywordExtractor extractor, ITextSummariser summariser){
        _searchResults = searchResults;
        _clusterer = clusterer;
        _extractor = extractor;
        _summariser = summariser;
    }

    public void createClusters() {
        
    }

    public void generateLabels() {

    }

    public void generateSummaries() {

    }

    public List<Category> getClusters() {
        return null;
    }

}
