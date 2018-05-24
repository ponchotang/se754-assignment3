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

    public void createClusters(){
        _clusters = _clusterer.createClusters(_searchResults);
    }

    public List<Category> getClusters(){
        return _clusters;
    }

    public void assignPopularityToClusters(){
        int totalNoOfDocuments = 0;
        for(Category c : _clusters){
            totalNoOfDocuments += c.getNoOfDocuments();
        }
        for(Category c : _clusters){
            c.setPopularity(c.getNoOfDocuments()/totalNoOfDocuments);
        }
    }

    public void setClusterRelevance(int position, double relevance){
        if(relevance < 0 || relevance > 1){
            throw new InvalidOperationException();
        }

        try {
            _clusters.get(position).setRelevance(relevance);
        } catch (IndexOutOfBoundsException e){
            throw new InvalidOperationException();
        }
    }

    public double computeIdeaMaturity(){
        double maturity = 0;

        for(Category c : _clusters){
            maturity += c.getPopularity() * c.getRelevance();
        }

        return maturity;
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
}
