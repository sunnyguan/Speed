import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class CardPanel extends JPanel
{

    private Point textPt = new Point( 0, 0 );

    private Point oriPt = new Point( 0, 0 );

    private Point mousePt;

    private boolean collide1 = false;

    private boolean collide2 = false;

    private boolean deact = false;

    private ImageIcon icon;

    private Card card;

    private boolean released;

    public boolean isCollide1()
    {
        return collide1;
    }

    public void setCollide1( boolean collide1 )
    {
        this.collide1 = collide1;
    }

    public boolean isCollide2()
    {
        return collide2;
    }

    public void setCollide2( boolean collide2 )
    {
        this.collide2 = collide2;
    }

    public boolean isDeact()
    {
        return deact;
    }

    public void setDeact( boolean deact )
    {
        this.deact = deact;
    }

    public ImageIcon getIcon()
    {
        return icon;
    }

    public void setIcon( ImageIcon icon )
    {
        this.icon = icon;
    }

    public Card getCard()
    {
        return card;
    }

    public void setCard( Card card )
    {
        this.card = card;
    }

    public boolean isReleased()
    {
        return released;
    }

    public void setReleased( boolean released )
    {
        this.released = released;
    }

    public CardPanel( Card c, int index )
    {
        icon = c.getImage();
        this.card = c;
        int x = GameGUI.myStartx + GameGUI.playerdx * index;
        int y = GameGUI.myStarty;
        textPt = new Point( x, y );
        oriPt = new Point( x, y );
        this.setFont( new Font( "Serif", Font.ITALIC + Font.BOLD, 32 ) );
        this.addMouseListener( new MouseAdapter()
        {

            @Override
            public void mousePressed( MouseEvent e )
            {
                mousePt = e.getLocationOnScreen();
                released = false;
                repaint();
            }

            @Override
            public void mouseReleased( MouseEvent e )
            {
                released = true;
            }
        } );
        this.addMouseMotionListener( new MouseMotionAdapter()
        {

            @Override
            public void mouseDragged( MouseEvent e )
            {
                if ( !released && !deact )
                {
                    int dx = e.getXOnScreen() - mousePt.x;
                    int dy = e.getYOnScreen() - mousePt.y;
                    setBounds( textPt.x + dx, textPt.y + dy, 60, 92 );
                    textPt.x = textPt.x + dx;
                    textPt.y = textPt.y + dy;
                    mousePt = e.getLocationOnScreen();

                    if ( collide( textPt.x, textPt.y, GameGUI.centralX + GameGUI.centraldx, GameGUI.centralY ) )
                    {
                        collide1 = true;
                        setBounds( oriPt.x, oriPt.y, 60, 92 );
                        textPt.x = oriPt.x;
                        textPt.y = oriPt.y;
                        released = true;
                    }
                    else if ( collide( textPt.x, textPt.y, GameGUI.centralX + 2 * GameGUI.centraldx, GameGUI.centralY ) )
                    {
                        collide2 = true;
                        setBounds( oriPt.x, oriPt.y, 60, 92 );
                        textPt.x = oriPt.x;
                        textPt.y = oriPt.y;
                        released = true;
                    }
                    repaint();
                }
            }
        } );
        setBackground( new Color( 0, 100, 0 ) );
    }

    private boolean collide( int x1, int y1, int x2, int y2 )
    {
        boolean x = Math.abs( x1 - x2 ) <= 20;
        boolean y = Math.abs( y1 - y2 ) <= 20;
        return x && y;
    }

    @Override
    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );
        if ( !deact )
        {
            icon.paintIcon( this, g, 0, 0 );
        }
    }

    /*
     * public static void main(String[] args) { EventQueue.invokeLater(new
     * Runnable() { // testing
     * 
     * @Override public void run() { JFrame f = new JFrame(); f.add(new
     * CardPanel()); f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); f.pack();
     * f.setLocationRelativeTo(null); f.setVisible(true); } }); }
     */
}