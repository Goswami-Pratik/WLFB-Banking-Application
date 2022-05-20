public class BankAccount {
    private final String accountNo;
    private double balance;

    public BankAccount(String accountNo, double myBalance) {
        this.accountNo = accountNo;
        this.balance = myBalance;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
