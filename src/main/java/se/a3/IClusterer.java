package se.a3;

import java.util.List;

public interface IClusterer {

    List<Category> createClusters(List<Document> documents);

}
