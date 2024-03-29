package se.a3;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class AccountManager {
    public static final String EMAIL = "EMAIL";
    public static final String PASSWORD = "PASSWORD";
    public static final String TYPE = "TYPE";
    public static final String COUNT = "COUNT";

    private String _signedInAccountEmail;
    private int _currentSessionSearchCount;

    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection<Document> mongoCollection;

    public AccountManager(MongoClient mongoClient,
                             String accountDatabaseName, String accountCollectionName) {
        this.mongoClient = mongoClient;
        this.mongoDatabase = this.mongoClient.getDatabase(accountDatabaseName);
        this.mongoCollection = this.mongoDatabase.getCollection(accountCollectionName);

        _currentSessionSearchCount = 0;
        _signedInAccountEmail = null;
    }

    public boolean isMongoDBClientNull() {
        return this.mongoClient == null;
    }

    public String getDBName() {
        return this.mongoDatabase.getName();
    }

    public void register(String email, String password) {
        this.checkStringNotEmpty(email);
        this.checkStringNotEmpty(password);

        Document document = new Document();
        document.put(EMAIL, email);
        document.put(PASSWORD, password);
        document.put(TYPE, Account.AccountType.USER);
        document.put(COUNT, 0);

        if(this.emailAlreadyUsed(email)) throw new AccountExistException();

        this.mongoCollection.insertOne(document);
    }

    private boolean emailAlreadyUsed(String email) {
        Document searchQuery = new Document();
        searchQuery.put(EMAIL, email);
        FindIterable<Document> cursor = this.mongoCollection.find(searchQuery);

        return cursor.first() != null;
    }

    public void changeAccountType(String email, Account.AccountType accountType){
        if(!this.isAccountAdmin()) throw new InvalidOperationException();

        Document searchQuery = new Document();
        searchQuery.put(EMAIL, email);
        Document updateQuery = new Document();
        updateQuery.put(TYPE, accountType);
        this.mongoCollection.findOneAndUpdate(searchQuery, updateQuery);
    }

    public void signIn(String submittedEmail, String submittedPassword){
        this.checkStringNotEmpty(submittedEmail);
        this.checkStringNotEmpty(submittedPassword);

        Document searchQuery = new Document();
        searchQuery.put(EMAIL, submittedEmail);
        FindIterable<Document> cursor = this.mongoCollection.find(searchQuery);

        Document userInfo = cursor.first();

        if(userInfo == null) throw new InvalidCredentialException();

        String userPassword = userInfo.get(PASSWORD,String.class);

        if(!userPassword.equals(submittedPassword)) throw new InvalidCredentialException();

        _signedInAccountEmail = submittedEmail;
        _currentSessionSearchCount = 0;
    }
    public void signOff(){
        _signedInAccountEmail = null;
    }

    public boolean isAccountSignedIn(){
        return _signedInAccountEmail != null;
    }

    public int getRegisteredUsersCount(){
        if(!this.isAccountAdmin()) throw new InvalidOperationException();
        return (int)mongoCollection.count();
    }

    private void checkStringNotEmpty(String str){
        if(str == null || str.equals("")) throw new InvalidCredentialException();
    }

    public void incrementSearchCount(){
        this.checkAccountSignedIn();
        Document userInfo = this.getDocument(_signedInAccountEmail);

        int count = userInfo.getInteger(COUNT);
        count++;

        userInfo.put(COUNT, count);

        Document searchQuery = new Document();
        searchQuery.put(EMAIL, _signedInAccountEmail);
        this.mongoCollection.findOneAndUpdate(searchQuery, userInfo);

        _currentSessionSearchCount++;
    }

    private Document getDocument(String email){
        Document searchQuery = new Document();
        searchQuery.put(EMAIL, email);
        FindIterable<Document> cursor = this.mongoCollection.find(searchQuery);
        if(cursor == null) throw new AccountDoesNotExistException();
        return cursor.first();
    }

    public int getCurrSessionCount(){
        this.checkAccountSignedIn();
        return _currentSessionSearchCount;
    }

    private boolean isAccountAdmin(){
        this.checkAccountSignedIn();
        Document accountInfo = this.getDocument(_signedInAccountEmail);
        return accountInfo.get(TYPE) == Account.AccountType.ADMIN;
    }

    private void checkAccountSignedIn(){
        if(_signedInAccountEmail == null) throw new NotSignedInException();
    }

    public int getTotalSessionCount(String email){
        this.checkAccountSignedIn();
        if(!isAccountAdmin()) throw new InvalidOperationException();

        Document userInfo = this.getDocument(email);

        return userInfo.getInteger(COUNT);
    }
}
