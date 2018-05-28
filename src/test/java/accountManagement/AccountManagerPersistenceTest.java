package accountManagement;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import se.a3.AccountManager;

public class AccountManagerPersistenceTest {
    MongoClient _mongoClient;
    String _accountDBName;
    String _accountCollectionName;
    MongoDatabase _mongoDatabase;

    @Before
    public void setUp(){
        _mongoClient = Mockito.mock(MongoClient.class);
        _accountDBName = "account-db";
        _accountCollectionName = "account-collection";
        _mongoDatabase =  Mockito.mock(MongoDatabase.class);
    }

    @Test
    public void shouldInitializeMongoDBClientWhenCreating() {

        // Given
        Mockito.doReturn(_mongoDatabase).when(_mongoClient).getDatabase(_accountDBName);

        // When
        AccountManager accountManager  = new AccountManager(_mongoClient, _accountDBName, _accountCollectionName);

        // Then
        Assert.assertFalse(accountManager.isMongoDBClientNull());
    }

    @Test
    public void shouldHaveDatabaseWithANameAfterCreatingMyCash() {
        // Given
        Mockito.doReturn(_accountDBName).when(_mongoDatabase).getName();
        Mockito.doReturn(_mongoDatabase).when(_mongoClient).getDatabase(_accountDBName);

        // When
        AccountManager accountManager = new AccountManager(_mongoClient, _accountDBName, _accountCollectionName);
        String dbName = accountManager.getDBName();

        // Then
        Assert.assertEquals(_accountDBName, dbName);
    }
}
