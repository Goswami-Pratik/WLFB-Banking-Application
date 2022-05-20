import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BankServerThread extends Thread{

    private final Socket bankSocket;
    private final SharedBankState sharedBankStateObj;

    public BankServerThread(Socket bankSocket, SharedBankState sharedBankStateObj) {
        this.bankSocket = bankSocket;
        this.sharedBankStateObj = sharedBankStateObj;
    }

    public void run() {
        try{
            String clientUniqueAccountID = sharedBankStateObj.createAccount();
            PrintWriter out = new PrintWriter(bankSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(bankSocket.getInputStream()));
            String inputLn, outputLn;
            out.println(clientUniqueAccountID);
            System.out.println(sharedBankStateObj.getTS() + "Initialised Client-ID: " + clientUniqueAccountID);
            Thread current = Thread.currentThread();
            current.setName(clientUniqueAccountID);

            while((inputLn = in.readLine()) != null) {
                try {
                    sharedBankStateObj.acquireLock();
                    outputLn = sharedBankStateObj.processInputs(inputLn);
                    out.println(outputLn);
                    if(inputLn.equalsIgnoreCase("exit")) {
                        sharedBankStateObj.releaseLock();
                        break;
                    }
                    sharedBankStateObj.releaseLock();
                } catch (InterruptedException e) {
                    System.err.println("Failed to get lock when reading: " + e);
                }
            }

            out.close();
            in.close();
            bankSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
