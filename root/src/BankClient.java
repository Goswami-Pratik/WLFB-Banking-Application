import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class BankClient {
    public static void main(String[] args) throws IOException {
        Socket BankClientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        int BankSocketNumber = 4243;
        String BankServerHost = "localhost";
        String BankClientID;

        try{
            BankClientSocket = new Socket(BankServerHost, BankSocketNumber);
            out = new PrintWriter(BankClientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(BankClientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + BankServerHost);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: "+ BankSocketNumber);
            System.exit(1);
        }

        BankClientID = in.readLine().split(" ")[0];
        System.out.println("Initialised " + BankClientID + " client and IO connections");
        System.out.println("Commands: \n-> add amount \n-> subtract amount \n-> transfer senderID receiverID amount \n-> exit");
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(System.in));
        String fromServer, fromUser;

        do {
            fromUser = stdInput.readLine();
            if (fromUser != null) {
                System.out.println("Sending: " + fromUser);
                out.println(fromUser);
            }
            fromServer = in.readLine();
            System.out.println("Received: " + fromServer);
        } while (!fromServer.equalsIgnoreCase("Bye, see you soon! :)"));

        out.close();
        in.close();
        stdInput.close();
        BankClientSocket.close();

    }
}
