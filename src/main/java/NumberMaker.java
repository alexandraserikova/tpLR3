import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class NumberMaker {
    public static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Server Started");
            Socket socket = server.accept();
            System.out.println("Client connected to socket");
            BufferedReader inClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter outClient = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Game start");
            int round = 1;
            boolean game = true;
            int state = 0; // 0- nothing, 1-greater, 2-less, 3- end
            System.out.println("Введите число");
            int number = Integer.parseInt(in.readLine());
            while (game) {
                System.out.println("Раунд " + round);
                JSONObject gameMessage = new JSONObject();
                gameMessage.put("game", game);
                gameMessage.put("round", round);
                gameMessage.put("state", state);
                outClient.write(gameMessage.toString() + "\n");
                outClient.flush();
                System.out.println("Ожидание ответа");
                JSONObject clientMessage = new JSONObject(inClient.readLine());
                int answer = clientMessage.getInt("answer");
                if (number < answer) {
                    state = 1;
                    round++;
                } else if (number > answer) {
                    state = 2;
                    round++;
                } else {
                    game = false;
                    state = 3;
                }
            }
            JSONObject gameMessage = new JSONObject();
            gameMessage.put("game", false);
            gameMessage.put("round", round);
            gameMessage.put("state", state);
            outClient.write(gameMessage.toString() + "\n");
            outClient.flush();
            System.out.println("Игра окончена");
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
