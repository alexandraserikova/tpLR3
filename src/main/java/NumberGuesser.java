import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class NumberGuesser {
    public static String ipAddr = "localhost";
    public static int port = 8080;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(ipAddr, port);
            BufferedReader inputUser = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            System.out.println("Client started");
            boolean game = true;
            while (game) {
                try {
                    JSONObject serverMessage = new JSONObject(in.readLine());
                    System.out.println(serverMessage.toString());
                    int state = serverMessage.getInt("state");
                    game = serverMessage.getBoolean("game");
                    switch (state){
                        case 0:
                            System.out.println("Начало игры");
                            break;
                        case 1:
                            System.out.println("Число больше загаданного");
                            break;
                        case 2:
                            System.out.println("Число меньше загаданного");
                            break;
                        case 3:
                            System.out.println("Угдал");
                            continue;
                        default:
                            System.err.println("Ошибка в state");
                    }
                    System.out.println("Введите число");
                    Integer number = Integer.valueOf(inputUser.readLine());
                    JSONObject message = new JSONObject();
                    message.put("answer", number);
                    out.write(message.toString() + "\n");
                    out.flush();
                } catch (IOException e) {
                    try {
                        if (!socket.isClosed()) {
                            socket.close();
                            in.close();
                            out.close();
                        }
                    } catch (IOException ignored) {}
                }

            }
        } catch (IOException e) {
            System.err.println("Socket failed");
        }
    }
}
