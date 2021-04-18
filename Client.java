package frame;

import javafx.scene.control.ComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class Client {
    static String answer;
    static String host_ip="127.0.0.1";
    static String title="User";
    static class panel extends JPanel implements Runnable{
        static DataInputStream i;
        static DataOutputStream o;
        private boolean presssed=false;
        Color color=Color.RED;
        int port;
        int startX = -1;
        int startY = -1;
        int endX = -1;
        int endY = -1;
        boolean flag=false;
        panel(int port){
            super();
            this.port=port;
            setSize(600,600);
            JFrame frame=new JFrame(title);
            frame.setSize(600,600);
            if(port==1234){
                JLabel tiptext=new JLabel("我的答案");
                JPanel answerpanel=new JPanel();
                TextField text=new TextField(1);
                text.setSize(300,20);
                text.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e){
                        if((char)e.getKeyChar()==KeyEvent.VK_ENTER){
                            answer=new String(text.getText());
                            flag=true;
                            text.setText("\u0000");
                            try{
                                o.writeUTF(answer);
                            }catch (IOException e1){
                                System.exit(-1);
                            }
                        }
                    }
                });
                answerpanel.setLayout(new GridLayout(1,2));
                answerpanel.add(tiptext);answerpanel.add(text);
                frame.add(answerpanel,BorderLayout.NORTH);
                text.setVisible(true);
            }
            else{//这里本意是想实现用户友好的颜色选择界面，即以菜单形式提供常见的几种颜色供用户选择，使得用户也能在画板上选择本地画笔颜色
                /*JPanel colorbox=new JPanel();colorbox.setLayout(new GridLayout(1,2));
                JLabel colorlabel=new JLabel("选择颜色");
                JComboBox<String> colorlist=new JComboBox<String>();
                colorlist.addItem("RED");colorlist.addItem("YELLOW");colorlist.addItem("BLACK");
                colorlist.addItem("BLUE");colorlist.addItem("GREEN");colorlist.addItem("GRAY");
                colorlist.addItem("PINK");colorlist.addItem("ORANGE");colorlist.addItem("CYAN");
                colorlist.setVisible(true);
                frame.add(colorlist,BorderLayout.NORTH);
                colorlist.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent itemEvent) {
                        String colorname=(String)itemEvent.getItem();
                        Color color=null;
                        switch(colorname){
                            case "RED":
                                color=Color.RED;break;
                            case "YELLOW":
                                color=Color.YELLOW;break;
                            case "Black":
                                color=Color.BLACK;break;
                            case "BLUE":
                                color=Color.BLUE;break;
                            case "GREEN":
                                color=Color.GREEN;break;
                            case "GRAY":
                                color=Color.GRAY;break;
                            case "PINK":
                                color=Color.PINK;break;
                            case "ORANGE":
                                color=Color.ORANGE;break;
                            case "CYAN":
                                color=Color.CYAN;break;
                        }
                        setColor(color);
                    }
                });
                colorbox.add(colorlabel);colorbox.add(colorlist);
                frame.add(colorbox,BorderLayout.NORTH);*/
                /*JPanel colorbox=new JPanel();colorbox.setLayout(new GridLayout(1,5));
                JLabel colorlabel=new JLabel("选择颜色");JTextField r=new JTextField("255",3);JTextField g=new JTextField("0",3);JTextField b=new JTextField("0",3);
                JButton button=new JButton("确定");
                colorbox.add(colorlabel);colorbox.add(r);colorbox.add(g);colorbox.add(b);colorbox.add(button);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        try{
                        int R=Integer.parseInt(r.getText()),G=Integer.parseInt(g.getText()),B=Integer.parseInt(b.getText());
                        setColor(new Color(R,G,B));
                        }
                        catch (Exception e){
                            JOptionPane.showMessageDialog(null, "RGB不能无输入");
                        }
                    }
                });
                frame.add(colorbox,BorderLayout.NORTH);*/
            }
            frame.add(this);
            frame.setVisible(true);frame.setResizable(false);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
        }

        public void setio(DataInputStream i,DataOutputStream o){
            this.i=i;this.o=o;
        }

        private void setColor(Color color){

            this.color=color;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Float linewidth=3.0f;
            ((Graphics2D)g).setStroke(new BasicStroke(linewidth));
            g.setColor(color);
            g.drawLine(startX,startY,endX,endY);
            g.dispose();
        }

        private void write(){
            try{
                /*o.writeInt(color.getRed());
                o.writeInt(color.getGreen());
                o.writeInt(color.getBlue());*/
                o.writeInt(startX);
                o.writeInt(startY);
                o.writeInt(endX);
                o.writeInt(endY);
            }catch (IOException e1){
                System.err.println("Disconnected!");
                System.exit(-1);
            }
        }
        @Override
        public void run(){
            /*JFrame frame=new JFrame();
            frame.setSize(600,800);
            TextField text=new TextField(1);
            panel client=new panel();
            client.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    try{
                        nextPoint=new Point(i.readInt(),i.readInt());
                        System.out.println(nextPoint.x+nextPoint.y);
                        client.draw();
                    }catch (Exception e1){
                    }
                }
            });
            text.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e){
                    if((char)e.getKeyChar()==KeyEvent.VK_ENTER){
                        answer=new String(text.getText());
                        flag=true;
                        text.setText("");
                        try{
                            o.writeUTF(answer);
                        }catch (IOException e1){
                        }
                    }
                }
            });
            frame.add(text,BorderLayout.NORTH);
            frame.add(client);
            setVisible(true);
            text.setVisible(true);
            frame.setVisible(true);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });*/
            if(port==5678){
                panel c1=this;
                c1.addMouseMotionListener(new MouseMotionListener() {
                    @Override
                    public void mouseDragged(MouseEvent mouseEvent) {
                        c1.endX=mouseEvent.getX();
                        c1.endY=mouseEvent.getY();
                        c1.paint(c1.getGraphics());
                        c1.write();
                        c1.startX=c1.endX;
                        c1.startY=c1.endY;
                    }

                    @Override
                    public void mouseMoved(MouseEvent mouseEvent) {
                        c1.endX=mouseEvent.getX();
                        c1.endY=mouseEvent.getY();
                        if(c1.presssed==true) {
                            c1.paint(c1.getGraphics());
                            c1.write();
                        }
                        c1.startX=c1.endX;
                        c1.startY=c1.endY;
                    }
                });
                c1.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if(e.getButton()==MouseEvent.BUTTON1){
                            c1.startX=e.getX();
                            c1.startY=e.getY();
                            c1.presssed=true;
                        }
                        else
                            c1.presssed=false;
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        c1.presssed=false;
                    }
                });

            }

            while(true){
                try{
                    setColor(new Color(i.readInt(),i.readInt(),i.readInt()));
                    startX=i.readInt();
                    startY=i.readInt();
                    endX=i.readInt();
                    endY=i.readInt();
                    paint(this.getGraphics());
                }catch (IOException e){
                    System.err.println("Disconnected!");
                    System.exit(-1);
                }
            }
        }
    }
    public static void main(String []args) throws Exception{
        Object[] obj ={ "你画我猜","画板" };
        int port;
        String s = (String) JOptionPane.showInputDialog(null,"请选择功能:\n", "功能", JOptionPane.PLAIN_MESSAGE, new ImageIcon("icon.png"), obj, "你画我猜");
        if(s=="你画我猜")
            port=1234;
        else {
            port = 5678;
        }
        Socket client=new Socket(host_ip,port);//这里host是服务器地址
        DataInputStream in=new DataInputStream(client.getInputStream());
        DataOutputStream out=new DataOutputStream(client.getOutputStream());
        panel clientpanel=new panel(port);clientpanel.setio(in,out);
        new Thread(clientpanel).start();
    }
}