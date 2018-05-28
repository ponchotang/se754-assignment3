package se.a3;

public interface IAccountManager {

        public boolean isMongoDBClientNull();

        public String getDBName();

        public void register(String email, String password);

        public void changeAccountType(String email, Account.AccountType accountType);

        public void signIn(String email, String password);

        public void signOff();

        public boolean isAccountSignedIn();

        public int getRegisteredUsersCount();

        public void incrementSearchCount();

        public int getCurrSessionCount();

        public int getTotalSessionCount(String email);
}

