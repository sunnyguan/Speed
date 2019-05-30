import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** @see https://stackoverflow.com/a/5312702/230513 */
public class CardPanel extends JPanel {

    private static final int W = 640;
    private static final int H = 480;
    private Point textPt = new Point(0, 0);
    private Point oriPt = new Point(0, 0);
    private Point mousePt;
    
    public boolean collide1 = false;
    public boolean collide2 = false;
    
    public boolean deact = false;
    
    public ImageIcon icon;
    public Card card;
    private boolean released;

    public CardPanel(Card c, int index) {
        icon = c.getImage();
        this.card = c;
        int x = GameGUI.myStartx + GameGUI.playerdx * index;
        int y = GameGUI.myStarty;
        textPt = new Point(x,y);
        oriPt = new Point(x,y);
        this.setFont(new Font("Serif", Font.ITALIC + Font.BOLD, 32));
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                mousePt = e.getLocationOnScreen();
                released = false;
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                released = true;
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if(!released && !deact) {
                    int dx = e.getXOnScreen() - mousePt.x;
                    int dy = e.getYOnScreen() - mousePt.y;
                    setBounds(textPt.x + dx, textPt.y + dy, 60, 92);
                    textPt.x = textPt.x + dx;
                    textPt.y = textPt.y + dy;
                    mousePt = e.getLocationOnScreen();
                    
                    if(collide(textPt.x, textPt.y, GameGUI.centralX + GameGUI.centraldx, GameGUI.centralY)) {
                        collide1 = true;
                        setBounds(oriPt.x, oriPt.y, 60, 92);
                        textPt.x = oriPt.x;
                        textPt.y = oriPt.y;
                        released = true;
                    } else if(collide(textPt.x, textPt.y, GameGUI.centralX + 2 * GameGUI.centraldx, GameGUI.centralY)) {
                        collide2 = true;
                        setBounds(oriPt.x, oriPt.y, 60, 92);
                        textPt.x = oriPt.x;
                        textPt.y = oriPt.y;
                        released = true;
                    }
                    repaint();
                }
                
                // System.out.println(e.getX() + ", " + e.getY());
                //setBounds(e.getX() - 30, e.getY() - 46, 60, 92);
                
            }
        });
        setBackground( new Color(0, 100, 0) );
    }
    
    public boolean collide(int x1, int y1, int x2, int y2) {
        boolean x = Math.abs( x1 - x2 ) <= 20;
        boolean y = Math.abs( y1 - y2 ) <= 20;
        return x && y;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(W, H);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(!deact) {
            icon.paintIcon( this, g, 0, 0 );
        }
    }

    /*public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame f = new JFrame();
                f.add(new CardPanel());
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
    }*/
}