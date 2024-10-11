import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server is started and it listens at port 8080...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void addStudent(String name, int grade) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(name + "," + grade);
            writer.newLine();
        }
    }

    private static synchronized List<String> getAllGrades() throws IOException {
        List<String> grades = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                grades.add(line);
            }
        }
        return grades;
    }


    private static synchronized double calculateAverageGrade() throws IOException {
        List<String> grades = getAllGrades();
        int sum = 0, count = 0;
        for (String grade : grades) {
            String[] parts = grade.split(",");
            sum += Integer.parseInt(parts[1]);
            count++;
        }
        return count == 0 ? 0 : (double) sum / count;
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                String request;
                while ((request = in.readLine()) != null) {
                    String[] parts = request.split(" ");
                    String command = parts[0];

                    switch (command) {
                        case "ADD":
                            String name = parts[1];
                            int grade = Integer.parseInt(parts[2]);
                            addStudent(name, grade);
                            out.println("The student is added successfully.");
                            break;
                        case "LIST":
                            List<String> grades = getAllGrades();
                            out.println("Grades: " + String.join(", ", grades));
                            break;
                        case "AVERAGE":
                            double average = calculateAverageGrade();
                            out.println("Avg grade: " + average);
                            break;
                        default:
                            out.println("Invalid command.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
