package Service;

import DAO.AccountDAO;
import Model.Account;


public class AccountService {
    private final AccountDAO accountDAO = new AccountDAO();

    public Account registerUser(Account account) {
        if (accountDAO.getAccountByUsername(account.getUsername()) != null) {
            throw new RuntimeException("Username already exists.");
        }
        return accountDAO.createAccount(account);
    }

    public Account loginUser(String username, String password) {
        Account account = accountDAO.getAccountByUsername(username);
        if (account != null && account.getPassword().equals(password)) {
            return account;
        }
        return null;
    }

    public Account getAccountById(int accountId) {
        return accountDAO.getAccountById(accountId);
    }

    public Account getAccountByUsername(String username) {
        return accountDAO.getAccountByUsername(username);
    }

    public boolean updateAccount(Account account) {
        return accountDAO.updateAccount(account);
    }

    public boolean deleteAccount(int accountId) {
        return accountDAO.deleteAccount(accountId);
    }

   
}
