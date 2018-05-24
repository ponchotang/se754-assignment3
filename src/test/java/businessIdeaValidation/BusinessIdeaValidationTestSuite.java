package businessIdeaValidation;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CategoryPopularityTest.class, CategoryRelevanceTest.class, IdeaMaturityTest.class })
public class BusinessIdeaValidationTestSuite {
}
