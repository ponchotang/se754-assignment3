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

        if(emailAlreadyUsed(email)){
            throw new AccountExistException();
        }

        this.mongoCollection.insertOne(document);
    }

    private boolean emailAlreadyUsed(String email) {
        Document searchQuery = new Document();
        searchQuery.put(EMAIL, email);
        FindIterable<Document> cursor = this.mongoCollection.find(searchQuery);

        if (cursor.first() != null) {
            return true;
        }
        return false;
    }

    public void changeAccountType(String email, Account.AccountType accountType){
        if(isAccountAdmin()) {
            Document searchQuery = new Document();
            searchQuery.put(EMAIL, email);
            Document updateQuery = new Document();
            updateQuery.put(TYPE, accountType);
            this.mongoCollection.findOneAndUpdate(searchQuery, updateQuery);
        } else {
            throw new InvalidOperationException();
        }
    }

    public void signIn(String email, String password){
        this.checkStringNotEmpty(email);
        this.checkStringNotEmpty(password);

        Document searchQuery = new Document();
        searchQuery.put(EMAIL, email);
        FindIterable<Document> cursor = this.mongoCollection.find(searchQuery);

        Document userInfo = cursor.first();

        if(userInfo == null){
            throw new InvalidCredentialException();
        }

        String truePassword = userInfo.get(PASSWORD,String.class);

        if(!truePassword.equals(password)){
            throw new InvalidCredentialException();
        }

        _signedInAccountEmail = email;
        _currentSessionSearchCount = 0;
    }
    public void signOff(){
        _signedInAccountEmail = null;
    }

    public boolean isAccountSignedIn(){
        return _signedInAccountEmail != null;
    }

    public int getRegisteredUsersCount(){
        if(this.isAccountAdmin()){
            return (int)mongoCollection.count();
        } else {
            throw new InvalidOperationException();
        }
    }


    private void checkStringNotEmpty(String str){
        if(str == null || str.equals("")){
            throw new InvalidCredentialException();
        }
    }

    public void incrementSearchCount(){
        this.checkAccountSignedIn();
        Document currDoc = this.getDocument(_signedInAccountEmail);
        if(currDoc == null) throw new AccountExistException();

        int count = currDoc.getInteger(COUNT);
        count++;

        currDoc.put(COUNT, count);

        Document searchQuery = new Document();
        this.checkAccountSignedIn();
        searchQuery.put(EMAIL, _signedInAccountEmail);
        this.mongoCollection.findOneAndUpdate(searchQuery, currDoc);

        _currentSessionSearchCount++;
    }

    private Document getDocument(String email){
        Document searchQuery = new Document();
        searchQuery.put(EMAIL, email);
        FindIterable<Document> cursor = this.mongoCollection.find(searchQuery);

        return cursor.first();
    }

    public int getCurrSessionCount(){
        this.checkAccountSignedIn();
        return _currentSessionSearchCount;
    }

    private boolean isAccountAdmin(){
        this.checkAccountSignedIn();
        Document currDoc = this.getDocument(_signedInAccountEmail);
        if(currDoc.get(TYPE) == Account.AccountType.ADMIN){
            return true;
        }
        return false;
    }

    private void checkAccountSignedIn(){
        if(_signedInAccountEmail == null) throw new NotSignedInException();
    }

    public int getTotalSessionCount(String email){
        this.checkAccountSignedIn();
        if(!isAccountAdmin()) throw new InvalidOperationException();

        Document currDoc = this.getDocument(email);
        if(currDoc == null) throw new AccountExistException();

        return currDoc.getInteger(COUNT);
    }
}
