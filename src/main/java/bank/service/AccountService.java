package bank.service;

import bank.dao.DaoFactory;
import bank.dao.IAccountDAO;
import bank.dao.Logger;
import bank.dao.MyFactory;
import bank.domain.Account;
import bank.domain.Customer;
import java.util.Collection;

public class AccountService implements IAccountService {

  DaoFactory mainFactory = new DaoFactory();
  MyFactory factory = mainFactory.getFactoryInstance();

  private IAccountDAO accountDAO;
  private EmailService emailService;

  public AccountService() {
    accountDAO = new Logger(factory.getAccountDAO());
    emailService = factory.getEmailService();
  }

  public Account createAccount(long accountNumber, String customerName) {
    Account account = new Account(accountNumber);
    Customer customer = new Customer(customerName);
    account.setCustomer(customer);
    accountDAO.saveAccount(account);
    return account;
  }

  public void deposit(long accountNumber, double amount) {
    Account account = accountDAO.loadAccount(accountNumber);
    account.deposit(amount);
    accountDAO.updateAccount(account);
    emailService.send(amount);
  }

  public Account getAccount(long accountNumber) {
    Account account = accountDAO.loadAccount(accountNumber);
    return account;
  }

  public Collection<Account> getAllAccounts() {
    return accountDAO.getAccounts();
  }

  public void withdraw(long accountNumber, double amount) {
    Account account = accountDAO.loadAccount(accountNumber);
    account.withdraw(amount);
    accountDAO.updateAccount(account);
    emailService.send(amount);
  }

  public void transferFunds(long fromAccountNumber, long toAccountNumber, double amount, String description) {
    Account fromAccount = accountDAO.loadAccount(fromAccountNumber);
    Account toAccount = accountDAO.loadAccount(toAccountNumber);
    fromAccount.transferFunds(toAccount, amount, description);
    accountDAO.updateAccount(fromAccount);
    accountDAO.updateAccount(toAccount);
  }
}
