import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SharedBankState {

    private boolean accessing = false;
    private int threadsWaiting = 0;
    private final double initialClientBalance;
    private final ArrayList<BankAccount> clientsList = new ArrayList<>();
    private String errorMessage = "";

    public SharedBankState(double initialClientBalance) {
        this.initialClientBalance = initialClientBalance;
    }

    public synchronized void acquireLock() throws InterruptedException {
        Thread currentThread = Thread.currentThread();
        System.out.println(getTS() + currentThread.getName() + ": attempting to acquire lock");
        threadsWaiting++;
        while(accessing) {
            System.out.println(getTS() + currentThread.getName() + ": waiting to receive lock - inuse (" + threadsWaiting + " waiting)");
            wait();
        }
        threadsWaiting--;
        accessing = true;
        System.out.println(getTS() + currentThread.getName() + ": lock obtained");
    }

    public synchronized void releaseLock() throws InterruptedException {
        accessing = false;
        notifyAll();
        Thread currentThread = Thread.currentThread();
        System.out.println(getTS() + currentThread.getName() + ": lock released");
    }

    public synchronized String processInputs(String inputLn) {
        String theOutput;
        String[] inputs = inputLn.split(" ");
        String command = inputs[0];
        String accountNo = Thread.currentThread().getName();

        //System.out.println(Arrays.toString(inputs));

        if(command.equalsIgnoreCase("add") && inputs.length == 2 ) {
            if (validateString(inputs[1])) {
                double amount = parseStringToDouble(inputs[1]);
                addMoney(accountNo, amount);
                theOutput = errorMessage + "£" + Math.abs(amount) + " added to " + accountNo;
            } else {
                theOutput = "Invalid amount received.";
            }
        } else if(command.equalsIgnoreCase("subtract") && inputs.length == 2) {
            if (validateString(inputs[1])) {
                double amount = parseStringToDouble(inputs[1]);
                subtractMoney(accountNo, amount);
                theOutput = errorMessage + "£" + Math.abs(amount) + " subtracted from " + accountNo;
            } else {
                theOutput = "Invalid amount received";
            }
        } else if(command.equalsIgnoreCase("transfer") && inputs.length == 4) {
            if (validateAccounts(inputs[1], inputs[2]) && validateString(inputs[3])) {
                double amount = parseStringToDouble(inputs[3]);
                transferMoney(inputs[1], inputs[2], amount);
                theOutput = errorMessage + "£" + Math.abs(amount) + " transferred from " + inputs[1] + " to " + inputs[2];
            } else {
                theOutput = errorMessage + "Invalid amount or accountID received";
            }
        } else if(command.equalsIgnoreCase("exit")) {
            System.out.println(getTS() + "(EXIT) Client Connection Terminated");
            theOutput = "Bye, see you soon! :)";
        } else {
            theOutput = accountNo + " received invalid request - try (add, subtract, transfer, exit)";
        }

        errorMessage = "";
        System.out.println(getTS() + "Server: " + theOutput);
        System.out.println(getTS() + "Bank Accounts:");
        clientsList.forEach(part -> System.out.print(part.getAccountNo() + "(£" + part.getBalance() + "), "));
        System.out.println();
        return theOutput;
    }

    protected String createAccount() {
        BankAccount clientAcc;
        if(clientsList.isEmpty()) {
            clientAcc = new BankAccount("Account1", initialClientBalance);
        } else {
            clientAcc = new BankAccount("Account" + (clientsList.size() + 1), initialClientBalance);
        }
        clientsList.add(clientAcc);
        return clientAcc.getAccountNo();
    }

    protected void addMoney(String accountNo, double moneyToAdd) {
        boolean accountFound = false;
        if(!clientsList.isEmpty()) {
            for (BankAccount current : clientsList) {
                if(current.getAccountNo().equalsIgnoreCase(accountNo)) {
                    double originalBalance = current.getBalance();
                    current.setBalance(current.getBalance() + Math.abs(moneyToAdd));
                    System.out.println(getTS() + "(ADD) " + "Original Balance: £" + originalBalance +
                            ", Updated Balance: £" + current.getBalance());
                    accountFound = true;
                    break;
                }
            }
            if(!accountFound) {
                System.err.println("(ERROR) Account not found in system");
            }
        } else {
            System.err.println("(ERROR) Bank has no accounts");
        }
    }

    protected void subtractMoney(String accountNo, double moneyToRemove) {
        boolean accountFound = false;
        if(!clientsList.isEmpty()) {
            for (BankAccount current : clientsList) {
                if(current.getAccountNo().equalsIgnoreCase(accountNo)) {
                    double originalBalance = current.getBalance();
                    current.setBalance(current.getBalance() - Math.abs(moneyToRemove));
                    System.out.println(getTS() + "(SUBTRACT) " + "Original Balance: £" + originalBalance +
                            ", Updated Balance: £" + current.getBalance());
                    accountFound = true;
                    break;
                }
            }
            if(!accountFound) {
                System.err.println("(ERROR) Account not found in system");
            }
        } else {
            System.err.println("(ERROR) Bank has no accounts");
        }
    }

    protected void transferMoney(String sender, String receiver, double amount) {
        if(!clientsList.isEmpty()) {
            BankAccount senderBankAcc = null, receiverBankAcc = null;

            for (BankAccount current : clientsList) {
                if(current.getAccountNo().equalsIgnoreCase(sender))
                    senderBankAcc = current;
                else if (current.getAccountNo().equalsIgnoreCase(receiver))
                    receiverBankAcc = current;
            }

            if(senderBankAcc != null && receiverBankAcc != null) {
                double originalSenderBal = senderBankAcc.getBalance(), originalReceiverBal = receiverBankAcc.getBalance();
                senderBankAcc.setBalance(senderBankAcc.getBalance() - Math.abs(amount));
                System.out.println(getTS() + "(TRANSFER) " + "Original Balance: £" + originalSenderBal +
                        ",  Updated Sender Balance: £" + senderBankAcc.getBalance());
                receiverBankAcc.setBalance(receiverBankAcc.getBalance() + Math.abs(amount));
                System.out.println(getTS() + "(TRANSFER) " + "Original Balance: £" + originalReceiverBal +
                        ",  Updated Receiver Balance: £" + receiverBankAcc.getBalance());
            }
        } else {
            System.err.println("(ERROR) Bank has no accounts");
        }
    }

    private boolean validateString(String inputString) {
        return !inputString.matches(".*[a-zA-Z]+.*");
    }

    private boolean validateAccounts(String sender, String receiver) {
        boolean senderAccount = false, receiverAccount = false;
        for (BankAccount current : clientsList) {
            if(current.getAccountNo().equalsIgnoreCase(sender))
                senderAccount = true;
            else if (current.getAccountNo().equalsIgnoreCase(receiver))
                receiverAccount = true;
        }
        if (!senderAccount) {
            System.err.println(getTS() + "(ERROR) Sender account not found in system");
            errorMessage += "Sender account not found in system, ";
        }
        if (!receiverAccount) {
            System.err.println(getTS() + "(ERROR) Receiver account not found in system");
            errorMessage += "Receiver account not found in system, ";
        }
        return senderAccount && receiverAccount;
    }

    private double parseStringToDouble(String input) {
        double theOutput = 0;
        try {
            theOutput = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.err.println(getTS() + "(ERROR) Please enter a valid amount");
            errorMessage += "Please enter a valid amount, ";
        }
        return theOutput;
    }

    public String getTS() {
        return ("[" + new SimpleDateFormat("yyyy/MM/dd.HH:mm:ss.SSS").format(new Date()) + "] ");
    }

    protected ArrayList<BankAccount> getClientsList() {
        return clientsList;
    }
}