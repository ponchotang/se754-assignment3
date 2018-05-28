package accountManagement;

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

public class SessionCountTest {
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

    @Test
    public void shouldGetCurrentSessionSearchCountWhenUserSignedIn(){
        // Given
        FindIterable iterable = Mockito.mock(FindIterable.class);
        Mockito.doReturn(createDocumentForAccount(_email,_password,Account.AccountType.USER, 0)).when(iterable).first();
        Mockito.doReturn(iterable).when(_collection).find(createSearchQuery(_email));

        // When
        _accountManager.signIn(_email, _password);

        // Then
        Assert.assertEquals(_accountManager.getCurrSessionCount(), 0 );
    }

    @Test
    public void shouldGetCurrentSessionSearchCountWhenAdminSignedIn(){
        // Given
        FindIterable iterable = Mockito.mock(FindIterable.class);
        Mockito.doReturn(createDocumentForAccount(_email,_password,Account.AccountType.ADMIN, 0)).when(iterable).first();
        Mockito.doReturn(iterable).when(_collection).find(createSearchQuery(_email));

        // When
        _accountManager.signIn(_email, _password);

        // Then
        Assert.assertEquals(_accountManager.getCurrSessionCount(), 0 );
    }

    @Test(expected = NotSignedInException.class)
    public void shouldThrowExceptionTryingToGetCurrentSessionSearchCountWhenNotSignedIn() {
        // Then
        _accountManager.getCurrSessionCount();
    }

    @Test
    public void shouldIncrementSessionCountWhenUserIsSignedIn(){
        // Given
        FindIterable iterable = Mockito.mock(FindIterable.class);
        Mockito.doReturn(createDocumentForAccount(_email,_password,Account.AccountType.USER, 0)).when(iterable).first();
        Mockito.doReturn(iterable).when(_collection).find(createSearchQuery(_email));

        // When
        _accountManager.signIn(_email, _password);
        _accountManager.incrementSearchCount();

        // Then
        Assert.assertEquals(_accountManager.getCurrSessionCount(), 1);
    }

    @Test
    public void shouldIncrementSessionCountWhenAdminIsSignedIn(){
        // Given
        FindIterable iterable = Mockito.mock(FindIterable.class);
        Mockito.doReturn(createDocumentForAccount(_email,_password,Account.AccountType.ADMIN, 0)).when(iterable).first();
        Mockito.doReturn(iterable).when(_collection).find(createSearchQuery(_email));

        // When
        _accountManager.signIn(_email, _password);
        _accountManager.incrementSearchCount();

        // Then
        Assert.assertEquals(_accountManager.getCurrSessionCount(), 1);
    }

    @Test(expected = NotSignedInException.class)
    public void shouldThrowExceptionWhenIncrementSearchCountWhenNotSignedIn(){
        // Then
        _accountManager.incrementSearchCount();
    }


    // Get current session count that is zero and not zero for user
    @Test
    public void shouldGetCurrentSessionCountOfZeroWhenSessionCountIsZero(){
        // Given
        FindIterable iterable = Mockito.mock(FindIterable.class);
        Mockito.doReturn(createDocumentForAccount(_email,_password,Account.AccountType.USER, 0)).when(iterable).first();
        Mockito.doReturn(iterable).when(_collection).find(createSearchQuery(_email));

        // When
        _accountManager.signIn(_email, _password);

        // Then
        Assert.assertEquals(_accountManager.getCurrSessionCount(), 0 );
    }

    @Test
    public void shouldGetCurrentSessionCountWhenSessionCountIsNotZero(){
        // Given
        FindIterable iterable = Mockito.mock(FindIterable.class);
        Mockito.doReturn(createDocumentForAccount(_email,_password,Account.AccountType.USER, 0)).when(iterable).first();
        Mockito.doReturn(iterable).when(_collection).find(createSearchQuery(_email));

        // When
        _accountManager.signIn(_email, _password);
        _accountManager.incrementSearchCount();
        _accountManager.incrementSearchCount();

        // Then
        Assert.assertEquals(_accountManager.getCurrSessionCount(), 2);
    }


    // Get TOTAL session count for user, admin, not signed in
    @Test(expected = InvalidOperationException.class)
    public void shouldThrowExceptionWhenGetTotalSessionCountWhenUserSignedIn(){
        // Given
        FindIterable iterable = Mockito.mock(FindIterable.class);
        Mockito.doReturn(createDocumentForAccount(_email,_password,Account.AccountType.USER, 0)).when(iterable).first();
        Mockito.doReturn(iterable).when(_collection).find(createSearchQuery(_email));
        _accountManager.signIn(_email, _password);

        // When
        Mockito.doReturn(1L).when(_collection).count();
        _accountManager.getTotalSessionCount("test@email.com");
    }
    @Test
    public void shouldGetTotalSessionCountWhenAdminSignedIn(){
        // Given
        FindIterable iterable = Mockito.mock(FindIterable.class);
        Mockito.doReturn(createDocumentForAccount(_email,_password,Account.AccountType.ADMIN, 0)).when(iterable).first();
        Mockito.doReturn(iterable).when(_collection).find(createSearchQuery(_email));
        _accountManager.signIn(_email, _password);

        // When
        FindIterable iterableOtherAccount = Mockito.mock(FindIterable.class);
        Mockito.doReturn(createDocumentForAccount("test@email.com",_password,Account.AccountType.ADMIN, 1)).when(iterableOtherAccount).first();
        Mockito.doReturn(iterableOtherAccount).when(_collection).find(createSearchQuery("test@email.com"));

        // Then
        Assert.assertEquals(_accountManager.getTotalSessionCount("test@email.com"),1);
    }

    @Test(expected = NotSignedInException.class)
    public void shouldThrowExceptionWhenGet(){
        // When
        _accountManager.getRegisteredUsersCount();
    }

    private Document createSearchQuery(String email){
        Document searchQuery = new Document();
        searchQuery.put("EMAIL", email);
        return searchQuery;
    }

    private Document createDocumentForAccount(String email, String password, Account.AccountType accountType, int count){
        Document document = new Document();
        document.put("EMAIL", email);
        document.put("PASSWORD", password);
        document.put("TYPE", accountType);
        document.put("COUNT", count);
        return document;
    }
}
