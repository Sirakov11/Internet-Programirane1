import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected with the server.");

            String request;
            while (true) {
                System.out.println("Choose command: ADD, LIST, AVERAGE или EXIT:");
                request = console.readLine();

                if ("EXIT".equalsIgnoreCase(request)) {
                    break;
                }

                out.println(request);
                String response = in.readLine();
                System.out.println("Respond from server: " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
