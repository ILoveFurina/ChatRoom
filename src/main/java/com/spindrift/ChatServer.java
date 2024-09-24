package com.spindrift;

import lombok.AllArgsConstructor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * ClassName: Server
 * Package: com.spindrift
 * Description:
 *
 * @Author 闫其武
 * @Create 2024/9/18 15:15
 * @Version 1.0
 */
public class ChatServer {

    static ArrayList<Socket> online = new ArrayList<Socket>();
    public static void main(String[] args){
        try(ServerSocket socketServer = new ServerSocket(8888)) {

            while(true){
                Socket socket = socketServer.accept();
                online.add(socket);
                Message.broadCast("欢迎"+socket.getInetAddress().getHostAddress()+"加入聊天室");
                new Message(socket,socket.getInetAddress().getHostAddress()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

@AllArgsConstructor
class Message extends Thread{
    private Socket socket;

    private String ip;
    public void run(){
        try(BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
            String str;
            while((str = br.readLine())!=null){
                //给其他在线客户端转发
                broadCast(ip+" 说:"+str);
            }
            broadCast(ip+"已下线");
        }catch(IOException e){
            e.printStackTrace();

        }finally {
            ChatServer.online.remove(socket);
        }
    }

     static void broadCast(String s) throws IOException {
        for(Socket socket: ChatServer.online ){
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os,true);
            pw.println(s);
        }
    }

}