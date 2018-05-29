package se.a3;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        AccountManagerPersistenceTest.class,
        AddRemoveKeywordTest.class,
        CategoryPopularityTest.class,
        CategoryRelevanceTest.class,
        DocumentManagerClusterTest.class,
        DocumentManagerLabelTest.class,
        DocumentManagerSummaryTest.class,
        DocumentSearcherTest.class,
        IdeaMaturityTest.class,
        KeywordCollectionTest.class,
        KeywordPriorityTest.class,
        RegisteredUserCountTest.class,
        SessionCountTest.class,
        SignUpAndInAndOutTest.class
})
public class TestSuite {
}
