package OnlineChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class UserHandler extends Thread {
    private Socket socket;
    private Server server;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;

    public UserHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            username = reader.readLine();
            System.out.println(Thread.currentThread() + " " + username);
            message("This users are online:" + server.getUsers());
            server.messageForAll("Server", "New user entered the chat - " + username);
            String data = null;
            do {
                data = reader.readLine();
                server.messageForAll(username, data);
            } while (!"bye".equals(data.toLowerCase()));
            server.messageForAll("Server", "User left the chat - " + username);
            socket.close();
            reader.close();
            writer.close();
            server.delete(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void message(String message) {
        writer.println(message);
        writer.flush();
    }

    public String getUserName() {
        return username;
    }
}
