package se.a3;

import org.junit.Assert;
import org.junit.Test;
import se.a3.*;

import java.util.List;

/**
 * Users can prioritise the keywords by changing the weights or order of the keywords.
 */
public class KeywordPriorityTest {
    @Test
    public void testSwapAdjacentWords(){
        //Given
        String userText = "Here are five key words";

        //When
        KeywordCollection collection = new KeywordCollection(userText);
        KeywordManager keywordManager = new KeywordManager(collection);
        KeywordCollection swappedCollection = keywordManager.changePriority(1,0);

        //Then
        Assert.assertEquals("are Here five key words", swappedCollection.getString());
        Assert.assertTrue(verifyKeywordPriority(swappedCollection));
    }

    @Test
    public void testSwapEndingWords(){
        //Given
        String userText = "Here are five key words";

        //When
        KeywordCollection collection = new KeywordCollection(userText);
        KeywordManager keywordManager = new KeywordManager(collection);
        KeywordCollection swappedCollection = keywordManager.changePriority(4,0);

        //Then
        Assert.assertEquals("words Here are five key", swappedCollection.getString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testChangePriorityOutOfBounds(){
        //Given
        String userText = "Here are five key words";

        //When
        KeywordCollection collection = new KeywordCollection(userText);
        KeywordManager keywordManager = new KeywordManager(collection);
        KeywordCollection swappedCollection = keywordManager.changePriority(4,-1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testChangePriorityArrayOutOfBounds(){
        //Given
        String userText = "Here are five key words";

        //When
        KeywordCollection collection = new KeywordCollection(userText);
        KeywordManager keywordManager = new KeywordManager(collection);
        KeywordCollection swappedCollection = keywordManager.changePriority(-1,0);
    }

    private boolean verifyKeywordPriority(KeywordCollection keywordCollection) {
        List<Keyword> keywordList = keywordCollection.getList();

        for (int i = 0; i < keywordList.size(); i++) {
            if (keywordList.get(i).getWeight() != i + 1) {
                return false;
            }
        }
        return true;
    }
}
