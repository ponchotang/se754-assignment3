package se.a3;

import org.junit.Assert;
import org.junit.Test;
public class KeywordCollectionTest {
    @Test
    public void shouldSeparateKeywordsWithUnderscoresToOneString(){
        //Given
        String userText = "one_two three_four";
        //When
        KeywordCollection collection = new KeywordCollection(userText);
        //Then
        Assert.assertEquals(2,collection.getList().size());
        Assert.assertEquals("one two", collection.getList().get(0).getWord());
    }

    @Test
    public void shouldMakeSeparateKeywordsWithSpaces(){
        //Given
        String userText = "one two three four";
        //When
        KeywordCollection collection = new KeywordCollection(userText);
        //Then
        Assert.assertEquals(4,collection.getList().size());
        Assert.assertEquals("four", collection.getList().get(3).getWord());
    }

}
