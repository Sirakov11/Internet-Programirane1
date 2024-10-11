import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Свързани сте със сървъра.");

            String request;
            while (true) {
                System.out.println("Изберете команда: ADD, LIST, AVERAGE или EXIT:");
                request = console.readLine();

                if ("EXIT".equalsIgnoreCase(request)) {
                    break;
                }

                out.println(request);
                String response = in.readLine();
                System.out.println("Отговор от сървъра: " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
