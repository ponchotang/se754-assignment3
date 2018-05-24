package se.a3;

import java.util.ArrayList;
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
        _clusters = _clusterer.createClusters(_searchResults);
    }

    public void generateLabels() {
        if (_clusters == null) {
            throw new DocumentsNotClusteredException();
        }

        for (Category cluster: _clusters) {
            String label = _extractor.extractKeywords(cluster.getDocumentContents());
            cluster.setLabel(label);
        }
    }

    public void generateSummaries() {
        if (_clusters == null) {
            throw new DocumentsNotClusteredException();
        }

        for (Category cluster: _clusters) {
            String summary = _summariser.summarise(cluster.getDocumentContents());
            cluster.setSummary(summary);
        }
    }

    public List<Category> getClusters() {
        return _clusters;
    }

}
