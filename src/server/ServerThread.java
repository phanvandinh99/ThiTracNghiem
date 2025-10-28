/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            System.out.println("Xử lý client...");
            // Xử lý câu hỏi, chấm điểm ở đây
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
