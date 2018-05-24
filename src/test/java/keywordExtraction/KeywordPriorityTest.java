package keywordExtraction;

import org.junit.Assert;
import org.junit.Test;

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
        Assert.assertEquals("are Here five key words", swappedCollection.toString());
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
        Assert.assertEquals("words Here are five key", swappedCollection.toString());
    }
}