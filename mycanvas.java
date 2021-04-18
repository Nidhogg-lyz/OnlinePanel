package frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.security.PublicKey;

public class mycanvas extends JPanel implements Runnable, Serializable {
    DataOutputStream out;
    DataInputStream in;
    int port;
    MouseEvent e = null;
    Color color=Color.RED;
    private String target;
    int startX = -1;
    int startY = -1;
    int endX = -1;
    int endY = -1;
    private boolean presssed=false;
    public mycanvas(int port){
        super();
        this.port=port;
        setSize(600,600);
        mycanvas c1=this;
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
        JFrame frame=new JFrame("MyCanvas");
        //可以实现用户友好的颜色选择界面，即以菜单形式提供常见的几种颜色供用户选择
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

        JPanel colorbox=new JPanel();colorbox.setLayout(new GridLayout(1,5));
        JLabel colorlabel=new JLabel("选择颜色");JTextField r=new JTextField("255",3);JTextField g=new JTextField("0",3);JTextField b=new JTextField("0",3);
        JButton button=new JButton("确定");
        colorbox.add(colorlabel);colorbox.add(r);colorbox.add(g);colorbox.add(b);colorbox.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try{
                    int R=Integer.parseInt(r.getText()),G=Integer.parseInt(g.getText()),B=Integer.parseInt(b.getText());
                    if(R>=0&&R<=255&&G>=0&&G<=255&&B>=0&&B<=255){
                    setColor(new Color(R,G,B));
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"RGB应为0到255之间的整数");
                    }
                }
                catch (Exception e){
                    JOptionPane.showMessageDialog(null, "RGB不能无输入");
                }
            }
        });
        frame.add(colorbox,BorderLayout.NORTH);

        frame.add(c1);frame.setResizable(false);
        frame.setSize(600,600);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }

    private void setColor(Color color){
        this.color=color;
    }
    private void write(){
        try{
            out.writeInt(color.getRed());
            out.writeInt(color.getGreen());
            out.writeInt(color.getBlue());
            out.writeInt(startX);
            out.writeInt(startY);
            out.writeInt(endX);
            out.writeInt(endY);
        }catch (IOException e1){
            System.err.println("Disconnected!");
            System.exit(-1);
        }
    }

    public void setio(DataInputStream i,DataOutputStream o){
        in=i;out=o;
    }
    @Override
    protected void paintComponent(Graphics g) {
        Float linewidth=3.0f;
        ((Graphics2D)g).setStroke(new BasicStroke(linewidth));
        g.setColor(color);
        g.drawLine(startX,startY,endX,endY);
        g.dispose();
    }
    public void display(){
        JFrame f=new JFrame();
        f.add(this);
        f.setSize(600,600);
        f.setVisible(true);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(1);
            }
        });
        TextField text=new TextField(1);
        text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    target=text.getText();
                }
            }
        });
        f.add(text,BorderLayout.NORTH);
    }
    public String getText(){
        if(target==null) {
            return null;
        }
        return new String(target);
    }

    @Override
    public void run() {
        if(port==5678){
            while(true){
                try{
                    //setColor(new Color(in.readInt(),in.readInt(),in.readInt()));//这里本意是接受用户传来的颜色输入并将用户定义颜色后所画在本机复刻
                    startX=in.readInt();
                    startY=in.readInt();
                    endX=in.readInt();
                    endY=in.readInt();
                    paint(this.getGraphics());
                }catch (IOException e){
                    System.err.println("Disconnected!");
                    System.exit(-1);
                }
            }
        }
    }
}
