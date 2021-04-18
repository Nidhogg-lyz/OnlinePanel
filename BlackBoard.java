package frame;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BlackBoard{
    public static void main ( String[] args )
    {
        final MouseDrawPanel mdp = new MouseDrawPanel ("Use Mouse Draw");
        Panel panel = new Panel ();
        panel.setLayout (new FlowLayout (FlowLayout.LEFT));
        Label startL = new Label ("start: ");
        Label endL = new Label ("end: ");
        final Label nowL = new Label ("now: ");
        final Label startR = new Label ("000,000");
        final Label endR = new Label ("000,000");
        final Label nowN = new Label ("000,000");
        panel.add (startL);
        panel.add (startR);
        panel.add (endL);
        panel.add (endR);
        panel.add (nowL);
        panel.add (nowN);
        mdp.add (panel, "South");
        mdp.addMouseMotionListener (new MouseMotionListener ()
        {
            @Override
            public void mouseMoved ( MouseEvent e )
            {
                if (mdp.left)
                {
                    nowN.setText (e.getX () + " , " + e.getY ());
                }
            }

            @Override
            public void mouseDragged ( MouseEvent e )
            {
                if (mdp.left)
                {
                    mdp.endX = e.getX ();
                    mdp.endY = e.getY ();
                    mdp.paintComponents (mdp.getGraphics ());
                    mdp.startX = mdp.endX;
                    mdp.startY = mdp.endY;
                    endR.setText (mdp.endX + " , " + mdp.endY);
                }
            }
        });
        mdp.addMouseListener (new MouseAdapter ()
        {
            @Override
            public void mousePressed ( MouseEvent e )
            {
                if (e.getButton () == MouseEvent.BUTTON1)
                {
                    mdp.startX = e.getX ();
                    mdp.startY = e.getY ();
                    startR.setText (mdp.startX + " , " + mdp.startY);
                    mdp.left = true;
                }
                else
                {
                    mdp.left = false;
                }
            }

            @Override
            public void mouseReleased ( MouseEvent e )
            {
                if (mdp.left)
                {
                    endR.setText (e.getX () + " , " + e.getY ());
                }
            }
        });
        mdp.setVisible (true);
    }

}


class MouseDrawPanel extends Frame
{
    private static final long serialVersionUID = 1L;
    MouseEvent e = null;
    int startX = -1;
    int startY = -1;
    int endX = -1;
    int endY = -1;
    boolean left = false;

    public MouseDrawPanel ( String title )
    {
        setTitle (title);
        setLayout (new BorderLayout ());
        setResizable (false);
        setSize (500, 400);
        setLocationRelativeTo (null);
        addWindowListener (new WindowAdapter ()
        {
            @Override
            public void windowClosing ( WindowEvent e )
            {
                System.exit (0);
            }
        });
    }

    @Override
    public void paintComponents ( Graphics g )
    {
        float lineWidth = 8.0f;
        ( (Graphics2D) g ).setStroke (new BasicStroke (lineWidth));
        g.setColor (Color.YELLOW);
        g.drawLine (startX, startY, endX, endY);
        g.dispose ();
    }
}