package frame;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class Server {
    static String target=new String("Rabbit");
    static String title="Server";
    static public void setTarget(){
        target= JOptionPane.showInputDialog("请输入要画的目标:","Rabbit");
        if(target==null){
            System.err.println("没有获取到所画目标");
            System.exit(-1);
        }
    }
    static class ServerThread extends Thread{
        int port;
        Socket client;//实现多客户端时，此处可改为client数组或向量，用于记录连入的客户端
        Vector<Socket> clientlist =new Vector<Socket>();

        public ServerThread(int port){
            this.port=port;
        }
        @Override
        public void run() {
            try {
                //while(true){
                    try{
                        ServerSocket server=new ServerSocket(port);//为实现多客户端与服务器相连，这里可以使用while循环反复监听并创建新的
                        client=server.accept();//用户线程，将线程与当前服务器io相连，并设定一定加入时间后退出循环，即不再接受新的连接请求
                        //clientlist.add(server.accept());
                    }catch (IOException e){
                        System.err.println("Server Failed");
                    }
                    //if(time is up!) break;//设置加入时间限制
                //}
                /*for(Socket cle:clientlist){
                    //set io,即将所有客户端的输入流与服务器端的输出流相连，此处仅考虑实现你画我猜功能时的多客户端实现，因此服务器端的输入仅仅只有各个客户端传入的答案字符串
                }*/
                DataOutputStream out=new DataOutputStream(client.getOutputStream());
                DataInputStream in =new DataInputStream(client.getInputStream());
                mycanvas canvas=new mycanvas(port,title);
                canvas.setio(in,out);
                Thread CanvasThread=new Thread(canvas);
                CanvasThread.start();
                if(port==1234) {
                    setTarget();
                    File output=new File("output.txt");
                    BufferedWriter o=new BufferedWriter(new FileWriter(output));
                    String answer = in.readUTF();
                    if (answer.equals(target)) {
                        o.write(target);
                        o.flush();
                        System.exit(10);
                    }
                }
            }catch (IOException e){
                System.err.println(e);
                System.exit(-1);
            }
        }
    }

    public static void main(String []args){
        try{
            new ServerThread(1234).start();//分别侦听两个端口
            new ServerThread(5678).start();
        }catch (Exception e){
            System.err.println("Server failed");
            System.exit(1);
        }
    }
}

/*
class ClientSocket extends Socket implements Runnable{
    InputStreamReader i;
    ObjectOutputStream o;
    char []answer;
    String ANSWER;
    mycanvas canvas;
    public ClientSocket(ServerSocket s,Socket client,mycanvas canvas,String str){
        try{
            i=new InputStreamReader(client.getInputStream());
            o=new ObjectOutputStream(client.getOutputStream());
            this.canvas=canvas;
            ANSWER=str;
        }catch (IOException e){

        }
    }
    public String getAnswer(){
        return new String(answer);
    }
    public boolean isRight(){
        return (new String(answer))==ANSWER;
    }
    @Override
    public void run() {
        try {
            while(true){
            o.writeObject(canvas);
            i.read(answer);
            }

        }catch (IOException e){
            System.err.println("ClientDisconnected!");
        }
    }
}
*/