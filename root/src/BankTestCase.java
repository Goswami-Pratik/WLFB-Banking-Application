import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BankTestCase {

    SharedBankState sharedObj;
    double initialAccountValue = 1000.0;
    Random rand = new Random();

    @BeforeEach
    void setUp() {
        sharedObj = new SharedBankState(initialAccountValue);
        sharedObj.createAccount();
        sharedObj.createAccount();
    }

    @Test
    @DisplayName("Add money to client")
    void addMoney() {
        rand.setSeed(42);
        for (int i = 0; i < 100; i++) {
            double randomValue = rand.nextDouble()*100;
            BankAccount client1 = sharedObj.getClientsList().get(0);
            double expectedBal = client1.getBalance()+randomValue;
            sharedObj.addMoney(client1.getAccountNo(), randomValue);
            client1 = sharedObj.getClientsList().get(0);
            assertEquals(expectedBal, client1.getBalance());
        }
    }

    @Test
    @DisplayName("Subtract money from client")
    void subtractMoney() {
        rand.setSeed(42);
        for (int i = 0; i < 100; i++) {
            double randomValue = rand.nextDouble()*100;
            BankAccount client1 = sharedObj.getClientsList().get(0);
            double expectedBal = client1.getBalance()-randomValue;
            sharedObj.subtractMoney(client1.getAccountNo(), randomValue);
            client1 = sharedObj.getClientsList().get(0);
            assertEquals(expectedBal, client1.getBalance());
        }
    }

    @Test
    @DisplayName("Transfer money between 2 clients")
    void transferMoney() {
        rand.setSeed(42);
        for (int i = 0; i < 100; i++) {
            double randomValue = rand.nextDouble()*100;
            BankAccount client1 = sharedObj.getClientsList().get(0);
            BankAccount client2 = sharedObj.getClientsList().get(1);
            double senderExpectedBal = client1.getBalance()-randomValue;
            double receiverExpectedBal = client2.getBalance()+randomValue;
            sharedObj.transferMoney(client1.getAccountNo(), client2.getAccountNo(), randomValue);
            client1 = sharedObj.getClientsList().get(0);
            client2 = sharedObj.getClientsList().get(1);
            assertTrue((client1.getBalance() == senderExpectedBal) && (client2.getBalance() == receiverExpectedBal));
        }
    }

    @Test
    @DisplayName("Create bank account")
    void createBankAcc() {
        SharedBankState testObj = new SharedBankState(initialAccountValue);
        for (int i = 1; i <= 100; i++) {
            testObj.createAccount();
            String expectedClientAccNo = "Account" + i;
            BankAccount createdClient = testObj.getClientsList().get(i-1);
            assertTrue((createdClient.getAccountNo().equals(expectedClientAccNo)) &&
                    (createdClient.getBalance() == initialAccountValue) && (testObj.getClientsList().size() == i));
        }
    }


}