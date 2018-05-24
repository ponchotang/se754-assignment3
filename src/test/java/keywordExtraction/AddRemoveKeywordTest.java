package keywordExtraction;

import org.junit.Assert;
import org.junit.Test;

/**
 * Users can inject or remove keywords from the keyword list.
 */
public class AddRemoveKeywordTest {
    @Test
    public void testAddWordsToKeywordCollection(){
        //Given
        String userText = "Here are five key words";

        //When
        KeywordCollection collection = new KeywordCollection(userText);
        KeywordManager keywordManager = new KeywordManager(collection);
        KeywordCollection largerCollection = keywordManager.addWord("now");

        //Then
        Assert.assertEquals(6,largerCollection.getLength());
    }

    @Test
    public void testRemoveWordsFromKeywordCollection(){
        //Given
        String userText = "Here are five key words";

        //When
        KeywordCollection collection = new KeywordCollection(userText);
        KeywordManager keywordManager = new KeywordManager(collection);
        KeywordCollection smallerCollection = keywordManager.removeWord(0);

        //Then
        Assert.assertEquals(4,smallerCollection.getLength());
    }

}
