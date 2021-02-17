package OnlineChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static ServerSocket serverSocket;
    public static final int USER_COUNT = 3;
    private static List<UserHandler> users;
    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(9999);
        ExecutorService executor = Executors.newFixedThreadPool(USER_COUNT);
        users = new ArrayList<>();
        Server server = new Server();
        System.out.println("Server is listing to port 9999");
        while (true) {
            try {
                Socket client = serverSocket.accept();
                UserHandler userHandler = new UserHandler(client, server);
                if (users.size() == USER_COUNT) {
                    userHandler.message("true");
                    client.close();
                    continue;
                } else
                    userHandler.message("false");
                System.out.println("New client" + client);
                users.add(userHandler);
                executor.submit(userHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Object obj;

    static {
        obj = new Object();
    }

    public void messageForAll(String sender, String content) {
        for (int i = 0; i < users.size(); i++) {
            UserHandler user = users.get(i);
            user.message("[" + sender + "]" + ":" + content);
        }
    }

    public void delete(UserHandler userDelete) {
        for (int i = 0; i < users.size(); i++) {
            UserHandler user = users.get(i);
            if (user.equals(userDelete)) {
                users.remove(i);
                break;
            }
        }
    }

    public String getUsers() {
        StringBuilder stringBuilder = new StringBuilder();
        int cnt = 0;
        stringBuilder.append("\n");
        for (int i = 0; i < users.size(); i++, cnt++) {
            UserHandler user = users.get(i);
            stringBuilder.append("["+ user.getUserName() + "] ");
            cnt %= 3;
            if (cnt == 0 || i == users.size() - 1) stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
