import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;

public class BankServer {
    public static void main(String[] args) throws IOException {
        ServerSocket BankServerSocket = null;
        boolean listening = true;
        String BankServerName = "WLFB-BankServer";
        int BankServerPortNumber = 4243;
        double initialClientBalance = 1000;

        SharedBankState sharedBankStateObj = new SharedBankState(initialClientBalance);

        FileOutputStream file = new FileOutputStream("ServerLogs.txt", true);
        CustomMultiOutputLogger stdOut = new CustomMultiOutputLogger(System.out, file);
        CustomMultiOutputLogger stdErr = new CustomMultiOutputLogger(System.err, file);
        PrintStream stdOutStream = new PrintStream(stdOut);
        PrintStream stdErrStream = new PrintStream(stdErr);
        System.setOut(stdOutStream);
        System.setErr(stdErrStream);

        try {
            BankServerSocket = new ServerSocket(BankServerPortNumber);
        } catch (IOException e) {
            System.err.println("Could not start " + BankServerName + " on specified port(" + BankServerPortNumber + ")");
            System.exit(-1);
        }

        System.out.println(sharedBankStateObj.getTS() + BankServerName + " started");

        while(listening) {
            new BankServerThread(BankServerSocket.accept(), sharedBankStateObj).start();
        }

        BankServerSocket.close();
    }
}
