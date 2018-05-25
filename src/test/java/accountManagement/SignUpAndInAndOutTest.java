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
import se.a3.*;

import static se.a3.AccountManager.EMAIL;
import static se.a3.AccountManager.TYPE;

public class SignUpAndInAndOutTest {
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

    // REGISTER
    @Test(expected = InvalidCredentialException.class)
    public void shouldThrowExceptionWhenRegisteringWithNullEmail() {
        _accountManager.register(null,_password);
    }

    @Test(expected = InvalidCredentialException.class)
    public void shouldThrowExceptionWhenRegisteringWithNullPassword() {
        _accountManager.register(_email,null);
    }

    @Test(expected = InvalidCredentialException.class)
    public void shouldThrowExceptionWhenRegisteringWithEmptyEmail() {
        _accountManager.register("",_password);
    }

    @Test(expected = InvalidCredentialException.class)
    public void shouldThrowExceptionWhenRegisteringWithEmptyPassword() {
        _accountManager.register(_email,"");
    }

    @Test(expected = AccountExistException.class)
    public void shouldThrowExceptionWhenRegisteringWithExistingEmail() {
        // Given
        // account with the email exists
        FindIterable iterable = Mockito.mock(FindIterable.class);
        Mockito.doReturn(new Document()).when(iterable).first();
        Mockito.doReturn(iterable).when(_collection).find(createSearchQuery(_email));

        // When
        _accountManager.register(_email,_password);
    }

    @Test
    public void shouldRegisterWhenEmailDoesNotExist() {
        // Given
        // account with the email doesn't exist
        FindIterable iterable = Mockito.mock(FindIterable.class);
        Mockito.doReturn(iterable).when(_collection).find(createSearchQuery(_email));

        // When
        _accountManager.register(_email, _password);

        // Then
        Mockito.verify(_collection, Mockito.times(1)).insertOne(createDocumentForAccount(_email,_password,Account.AccountType.USER,0));
    }

    // SIGN IN
    @Test(expected = InvalidCredentialException.class)
    public void shouldThrowExceptionWhenSigningInWithNullEmail() {
        _accountManager.signIn(null,_password);
    }

    @Test(expected = InvalidCredentialException.class)
    public void shouldThrowExceptionWhenSigningInWithNullPassword() {
        _accountManager.signIn(_email,null);
    }

    @Test(expected = InvalidCredentialException.class)
    public void shouldThrowExceptionWhenSigningInWithEmptyEmail() {
        _accountManager.signIn("",_password);
    }

    @Test(expected = InvalidCredentialException.class)
    public void shouldThrowExceptionWhenSigningInWithEmptyPassword() {
        _accountManager.signIn(_email,"");
    }

    @Test
    public void shouldSignInWhenSigningInWithCorrectDetails() {
        // Given
        FindIterable iterable = Mockito.mock(FindIterable.class);
        Mockito.doReturn(createDocumentForAccount(_email,_password,Account.AccountType.USER,0)).when(iterable).first();
        Mockito.doReturn(iterable).when(_collection).find(createSearchQuery(_email));

        // When
        _accountManager.signIn(_email, _password);

        // Then
        Assert.assertTrue(_accountManager.accountSignedIn());
    }

    @Test(expected = InvalidCredentialException.class)
    public void shouldThrowExceptionWhenSigningInWithIncorrectPassword(){
        // Given
        FindIterable iterable = Mockito.mock(FindIterable.class);
        Mockito.doReturn(createDocumentForAccount(_email,_password,Account.AccountType.USER,0)).when(iterable).first();
        Mockito.doReturn(iterable).when(_collection).find(createSearchQuery(_email));

        // When
        _accountManager.signIn(_email, "incorrect password");
    }

    @Test(expected = InvalidCredentialException.class)
    public void shouldThrowExceptionWhenSigningInWithNonExistingEmail(){
        // Given
        FindIterable iterable = Mockito.mock(FindIterable.class);
        Mockito.doReturn(iterable).when(_collection).find(createSearchQuery(_email));

        // When
        _accountManager.signIn(_email, _password);
    }


    // SIGN OFF
    @Test
    public void shouldSignOffWhenAccountIsSignedIn() {
        // Given
        FindIterable iterable = Mockito.mock(FindIterable.class);
        Mockito.doReturn(createDocumentForAccount(_email,_password,Account.AccountType.USER,0)).when(iterable).first();
        Mockito.doReturn(iterable).when(_collection).find(createSearchQuery(_email));

        _accountManager.signIn(_email, _password);
        Assert.assertTrue(_accountManager.accountSignedIn());

        // When
        _accountManager.signOff();

        // Then
        Assert.assertFalse(_accountManager.accountSignedIn());
    }

    // ADMIN UPGRADE USER
    @Test
    public void shouldUpgradeAccountToAdminWhenDoneByAdmin(){
        String testingEmail = "testing@email.com";

        // Given
        FindIterable iterable = Mockito.mock(FindIterable.class);
        Mockito.doReturn(createDocumentForAccount(_email,_password,Account.AccountType.ADMIN,0)).when(iterable).first();
        Mockito.doReturn(iterable).when(_collection).find(createSearchQuery(_email));
        _accountManager.signIn(_email,_password);

        // When
        _accountManager.changeAccountType(testingEmail,Account.AccountType.ADMIN);

        // Then
        Document searchQuery = new Document();
        searchQuery.put(EMAIL, testingEmail);
        Document updateQuery = new Document();
        updateQuery.put(TYPE, Account.AccountType.ADMIN);
        Mockito.verify(_collection, Mockito.times(1)).findOneAndUpdate(searchQuery,updateQuery);
    }

    @Test(expected = InvalidOperationException.class)
    public void shouldThrowExceptionWhenUserTriesToUpgradeUserToAdmin(){
        // Given
        FindIterable iterable = Mockito.mock(FindIterable.class);
        Mockito.doReturn(createDocumentForAccount(_email,_password,Account.AccountType.USER,0)).when(iterable).first();
        Mockito.doReturn(iterable).when(_collection).find(createSearchQuery(_email));
        _accountManager.signIn(_email,_password);

        // When
        _accountManager.changeAccountType("testing@email.com",Account.AccountType.ADMIN);
    }

    @Test(expected = NotSignedInException.class)
    public void shouldThrowExceptionWhenTryingToChangeAccountTypeWhenNotSignedIn(){
        _accountManager.changeAccountType("testing@email.com",Account.AccountType.ADMIN);
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
