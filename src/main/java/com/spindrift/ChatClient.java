package com.spindrift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * ClassName: ChatClient
 * Package: com.spindrift
 * Description:
 *
 * @Author 闫其武
 * @Create 2024/9/18 14:56
 * @Version 1.0
 */
public class ChatClient {
    public static void main(String[] args)  {

        try(Socket socket = new Socket("127.0.0.1", 8888)){

            Send send = new Send(socket);
            Receive receive = new Receive(socket);

            send.start();
            receive.start();

            send.join();
        }catch (Exception e){

        }

    }
}

class Send extends Thread {

    private Socket socket;

    public Send(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (OutputStream outputStream = socket.getOutputStream()) {

            PrintStream ps = new PrintStream(outputStream);
            Scanner input = new Scanner(System.in);
            //从键盘不断的输入自己的话，给服务器发送，由服务器给其他人转发
            while (true) {
                String str = input.nextLine();
                if ("exit".equals(str)) {
                    break;
                }
                ps.println(str);
            }
            input.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class Receive extends Thread{
    private Socket socket;

    public Receive(Socket socket) {
        this.socket = socket;
    }
    public void run(){
        try(InputStream inputStream = socket.getInputStream();
            Scanner input = new Scanner(inputStream)) {

            while(input.hasNextLine()){
                String line = input.nextLine();
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}