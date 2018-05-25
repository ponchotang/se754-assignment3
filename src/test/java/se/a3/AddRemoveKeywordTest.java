package se.a3;

import org.junit.Assert;
import org.junit.Test;
import se.a3.*;
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
        Assert.assertEquals(6, largerCollection.getLength());
        Assert.assertEquals(6, largerCollection.getList().get(5).getWeight());
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
        Assert.assertEquals(4, smallerCollection.getList().get(3).getWeight());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRemoveWordsOutOfBounds(){
        //Given
        String userText = "Here are five key words";

        //When
        KeywordCollection collection = new KeywordCollection(userText);
        KeywordManager keywordManager = new KeywordManager(collection);
        KeywordCollection smallerCollection = keywordManager.removeWord(-1);

    }

}
