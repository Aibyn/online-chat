package OnlineChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        try {
            Socket socket = new Socket("localhost", 9999);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            // Greetings
            System.out.println("Welcome to chat");
            System.out.println("To exit the chat type \"bye\"");
            System.out.println("Please enter your username: ");
            String username = scanner.nextLine();
            writer.println(username);
            writer.flush();
            String isFull = reader.readLine();
            if (isFull.equals("true")) {
                System.out.println("Sorry chat is full. You can enter next time");
                reader.close();
                writer.close();
                System.exit(0);
            }
            Thread t1 = new Thread(() -> {
                String serverData = null;
                while (true) {
                    try {
                        serverData = reader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(serverData);
                }
            });
            t1.setDaemon(true);
            t1.start();
            String data = null;
            do {
                data = scanner.nextLine();
                writer.println(data);
                writer.flush();
            } while (!"bye".equals(data.toLowerCase()));
            socket.close();
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
