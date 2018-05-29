package se.a3;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import se.a3.Account;
import se.a3.AccountManager;
import se.a3.InvalidOperationException;
import se.a3.NotSignedInException;

public class RegisteredUserCountTest {
    MongoClient _mongoClient;
    String _accountDBName;
    String _accountCollectionName;
    MongoDatabase _mongoDatabase;
    MongoCollection<Document> _collection;
    AccountManager _accountManager;

    private String _email;
    private String _password;

    @Before
    public void setUp(){
        _mongoClient = Mockito.mock(MongoClient.class);
        _accountDBName = "account-db";
        _accountCollectionName = "account-collection";
        _mongoDatabase =  Mockito.mock(MongoDatabase.class);

        Mockito.doReturn(_mongoDatabase).when(_mongoClient).getDatabase(_accountDBName);

        _collection = Mockito.mock(MongoCollection.class);
        Mockito.doReturn(_collection).when(_mongoDatabase).getCollection(_accountCollectionName);

        _accountManager = new AccountManager(_mongoClient, _accountDBName, _accountCollectionName);

        _email = "james.bond@gmail.com";
        _password = "hunter2";
    }

    @Test(expected = InvalidOperationException.class)
    public void shouldThrowExceptionWhenTryingToGetNumberOfRegisteredUsersWhenUserSignedIn(){
        // Given
        FindIterable<Document> iterable = Mockito.mock(FindIterable.class);
        Mockito.doReturn(getDocumentForAccount(_email,_password,Account.AccountType.USER, 0)).when(iterable).first();
        Mockito.doReturn(iterable).when(_collection).find(getSearchQuery(_email));

        // When
        _accountManager.signIn(_email, _password);
        Mockito.doReturn(1L).when(_collection).count();
        _accountManager.getRegisteredUsersCount();
    }

    @Test
    public void shouldGetNumberOfRegisteredUsersWhenAdminSignedIn(){
        // Given
        FindIterable iterable = Mockito.mock(FindIterable.class);
        Mockito.doReturn(getDocumentForAccount(_email,_password,Account.AccountType.ADMIN, 0)).when(iterable).first();
        Mockito.doReturn(iterable).when(_collection).find(getSearchQuery(_email));

        // When
        Mockito.doReturn(1L).when(_collection).count();
        _accountManager.signIn(_email, _password);

        // Then
        Assert.assertEquals(_accountManager.getRegisteredUsersCount(), 1);
    }

    @Test(expected = NotSignedInException.class)
    public void shouldThrowExceptionWhenGetNumberOfRegisteredUsersWhenNotSignedIn(){
        _accountManager.getRegisteredUsersCount();
    }

    private Document getSearchQuery(String email){
        Document searchQuery = new Document();
        searchQuery.put("EMAIL", email);
        return searchQuery;
    }

    private Document getDocumentForAccount(String email, String password, Account.AccountType accountType, int count){
        Document document = new Document();
        document.put("EMAIL", email);
        document.put("PASSWORD", password);
        document.put("TYPE", accountType);
        document.put("COUNT", count);
        return document;
    }
}
