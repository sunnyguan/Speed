import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;


public class GameGUI extends JFrame
{
    public String playerName;
    public String IPAddress;
    public boolean ipEntered;
    public static void main(String args[])
    {
        GameGUI gg = new GameGUI();
        gg.init();
    }
    
    public void init() {
        player = new Player("192.168.137.1");
        player.init( "192.168.137.1" );
        JFrame homePage = new JFrame("Home Page");
        homePage.setSize( 600, 550 );
        homePage.getContentPane().setBackground( new Color(130, 20, 0) );
        
        JButton playButton = new JButton("PLAY");
        int mid = homePage.getWidth() / 2 - 182 /2;
        playButton.setBounds(mid, 248, 182, 29);
        JButton instructionButton = new JButton("INSTRUCTIONS");
        instructionButton.setBounds(mid, 282, 182, 29);
               
        String s = "/images/SpeedTitle.png";
        //
        ImageIcon icon = new ImageIcon(GameGUI.class.getResource(s));
        JLabel label = new JLabel();
        label.setIcon(icon);
        label.setBounds(homePage.getWidth() / 2 - 306/2, 80, 306, 144);
        homePage.getContentPane().add( label );
        
        homePage.getContentPane().setLayout(null);
        homePage.getContentPane().add(playButton);
        homePage.getContentPane().add(instructionButton);
        homePage.setResizable( false );
        homePage.setVisible( true );
        
        // name and IP address GUI
        JFrame setUp = new JFrame("Set Up");
        setUp.setSize(400, 300);
        setUp.getContentPane().setLayout(null);
        
        JButton startGame = new JButton("Let's Play!");
        startGame.setBounds( 200-45, 197, 90, 20 );
        setUp.getContentPane().add( startGame );
        
        JLabel namelbl = new JLabel("Name:");
        namelbl.setBounds(61, 67, 50, 16);
        setUp.getContentPane().add(namelbl);
        JLabel IPAddress = new JLabel("IP Address:");
        IPAddress.setBounds(61, 121, 90, 16);
        setUp.getContentPane().add(IPAddress);
        

        JTextField nameText = new JTextField("");
        JTextField IPText = new JTextField("");
        nameText.setBounds(155, 62, 130, 26);
        IPText.setBounds(155, 116, 130, 26);
        nameText.setEditable( true );
        IPText.setEditable( true );
        setUp.getContentPane().add(nameText);
        setUp.getContentPane().add(IPText);
        nameText.setColumns(10);
        IPText.setColumns(10);
        
        JButton ok1 = new JButton("OK");
        ok1.setBounds(322, 62, 50, 29);
        setUp.getContentPane().add(ok1);
        JButton ok2 = new JButton("OK");
        ok2.setBounds(322, 116, 50, 29);
        setUp.getContentPane().add(ok2);
        
        ok1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                setPlayerName(nameText.getText());
            }
        });
        
        ok2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                setIPAddress( IPText.getText());
            }
        });
       
        // Instructions page GUI
        JFrame instruct = new JFrame("Instructions");
        JTextArea text = new JTextArea("Instructions", 30, 40);
        JScrollPane sp = new JScrollPane(text);
        
        JMenuBar mb = new JMenuBar();
        instruct.getContentPane().add(mb);
        JMenu close = new JMenu("Close");
        mb.add( close );
        
        instruct.setLayout(new FlowLayout());
        instruct.setSize(500, 600);
        instruct.getContentPane().add(sp);
        
        //Stuck GUI
        JFrame stuckFrame = new JFrame("Stuck");
        stuckFrame.setSize( 388, 223 );
        stuckFrame.getContentPane().setLayout(null);
        JButton yes = new JButton("Yes");
        JButton no = new JButton("No");
        yes.setBounds( 220, 143, 108, 29 );
        no.setBounds( 58, 143, 108, 29 );
        stuckFrame.getContentPane().add( yes );
        stuckFrame.getContentPane().add( no );
        JTextField stuckQuestion = new JTextField();
        stuckQuestion.setText("Would you like to flip the side deck?");
        stuckQuestion.setHorizontalAlignment(SwingConstants.CENTER);
        stuckQuestion.setBounds(58, 61, 275, 26);
        stuckFrame.getContentPane().add(stuckQuestion);
        stuckQuestion.setColumns(10);
        
        
        // Speed Game GUI
        game = new JFrame("Speed Game");
        game.setSize( 600, 550 );
        pane = game.getLayeredPane();
        
        game.getContentPane().setBackground( new Color(0, 100, 0) );
        //game.getContentPane().setLayout(null);
        game.setResizable(false);
        
        JMenuBar menuBar = new JMenuBar();
        game.setJMenuBar(menuBar);
        JMenu gameInstructions = new JMenu("Instructions");
        menuBar.add(gameInstructions);

        
        stuckButton = new JRadioButton("");
        stuckButton.setBounds(512, 166, 42, 23);
        game.getContentPane().add(stuckButton);
        
        JLabel lblStuck = new JLabel("STUCK");
        lblStuck.setBounds(505, 148, 61, 16);
        game.getContentPane().add(lblStuck);
        
        // instruction page
        instructionButton.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked (MouseEvent e )
            {                
                instruct.setVisible(true);
            }
            
        });
        
        // play button --> set up GUI
        playButton.addMouseListener( new MouseAdapter()
        {
            public void mouseClicked (MouseEvent e )
            {
                homePage.setVisible( false );
                setUp.setVisible( true );
            }

        });
        
        // play button --> set up GUI
        startGame.addMouseListener( new MouseAdapter() //HERE
        {
            public void mouseClicked (MouseEvent e )
            {
                setIPAddress( IPText.getText());
                // setName
                player.setName(nameText.getText());
                ipEntered = true;
                cardsInit(pane);
                setUp.setVisible( false );
                game.setVisible( true );
            }

        });
        
        // instructions pop up when playing the game
        gameInstructions.addMouseListener( new MouseAdapter()
        {
            public void mouseClicked (MouseEvent e )
            {
                instruct.setVisible( true );
            }
        });
        
     // instructions close
        close.addMouseListener( new MouseAdapter()
        {
            public void mouseClicked (MouseEvent e )
            {
                instruct.setVisible( false );
            }

        });
        
        stuckButton.addMouseListener( new MouseAdapter() 
        {
            // check if this works
            public void mouseClicked (MouseEvent e )
            {
                if (stuckButton.isEnabled())
                {
                    player.stuck();
                }
            }
        });
        
        // When both players enable the STUCK button and a window pops up asking
        // the player if they want to flip the side deck and the player clicks
        // the "yes" button
        yes.addMouseListener( new MouseAdapter() 
        {
            // check if this works
            public void mouseClicked (MouseEvent e )
            {
                //player.agreeStuck();
                //stuckButton.setEnabled( false );
                //stuckFrame.setVisible( false );
            }
        });
        
        // When both players enable the STUCK button and a window pops up asking
        // the player if they want to flip the side deck and the player clicks
        // the "no" button
        no.addMouseListener( new MouseAdapter() 
        {
            // check if this works
            public void mouseClicked (MouseEvent e )
            {
                player.stuck();
                stuckFrame.setVisible( false );
                stuckButton.setEnabled( false );
            }
        });
            
    }
    
    JRadioButton stuckButton;
    
    Player player;
    
    // other (or upper cards) ~ 1
    // my (other my cards) ~ 2
    
    public static int playerdx = 75;
    public static int otherStartx= 130;
    public static int myStartx = 80;
    public static int otherStarty = 49;
    public static int myStarty = 370;
    
    public static int centraldx = 118;
    public static int centralX = 70;
    public static int centralY =( myStarty + (otherStarty + 92)) / 2 - 92/2;
    
    public static int[] hand1X = {otherStartx, otherStartx + playerdx, 
        otherStartx + 2 * playerdx, otherStartx + 3 * playerdx, otherStartx + 4 * playerdx};
    
    public static int[] hand2X = {myStartx, myStartx + playerdx, 
        myStartx + 2 * playerdx, myStartx + 3 * playerdx, myStartx + 4 * playerdx};
    
    
    private void cardsInit(JLayeredPane pane) {
        myCards = new ArrayList<CardPanel>();
        ArrayList<JLabel> theirCards = new ArrayList<JLabel>();
        ArrayList<Card> h1 = player.getHand();
        ImageIcon back = new ImageIcon(GameGUI.class.getResource("/images/back.png"));
        for(int i = 0; i < 5; i++) {
            CardPanel cp = new CardPanel(h1.get(i), i);
            cp.setBounds(myStartx + playerdx * i, myStarty, 60, 92);
            myCards.add( cp );
            cp.setOpaque( true );
            
            JLabel jp = new JLabel("");
            jp.setBounds(otherStartx + playerdx * i, otherStarty, 60, 92);
            jp.setIcon( back );
            theirCards.add( jp );
            
            pane.add(cp, new Integer(2));
            pane.add(jp, new Integer(1));
        }
        
        JLabel central1 = new JLabel("");
        JLabel central2 = new JLabel("");
        JLabel side1 = new JLabel("");
        JLabel side2 = new JLabel("");
        JLabel deck1 = new JLabel("");
        JLabel deck2 = new JLabel("");
        
        side1.setBounds(centralX, centralY, 65, 92);
        central1.setBounds(centralX + centraldx, centralY, 60, 92);
        central2.setBounds(centralX + 2 * centraldx, centralY, 60, 92);
        side2.setBounds(centralX + 3 * centraldx, centralY, 65, 92);
        deck1.setBounds(484, 331, 65, 92);
        deck2.setBounds(41, 85, 65, 92);
        
        central1.setIcon( player.getPile1().getImage());
        central2.setIcon( player.getPile2().getImage());
        
        side1.setIcon(back);
        side2.setIcon(back);
        deck1.setIcon(back);
        deck2.setIcon(back);

        pane.add(central1, new Integer(1));
        pane.add(central2, new Integer(1));
        pane.add(side1, new Integer(1));
        pane.add(side2, new Integer(1));
        pane.add(deck1, new Integer(1));
        pane.add(deck2, new Integer(1));
        
        Timer timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i = 0; i < 5; i++) {
                    if(player.getHand().size() <= i) {
                        myCards.get( i ).deact = true;
                        continue;
                    }
                    Card c = player.getHand().get( i );
                    myCards.get( i ).card = c;
                    myCards.get( i ).icon = c.getImage();
                    // check collision
                    if(myCards.get( i ).collide1) {
                        player.out.println("MOVETOPILE|" + myCards.get( i ).card.toString() + "|1");
                        myCards.get( i ).collide1 = false;
                    } else if(myCards.get( i ).collide2) {
                        player.out.println("MOVETOPILE|" + myCards.get( i ).card.toString() + "|2");
                        myCards.get( i ).collide2 = false;
                    }
                    if(!player.isStuck()) {
                        stuckButton.setSelected( false );
                    }
                }
                central1.setIcon( player.getPile1().getImage());
                central2.setIcon( player.getPile2().getImage());
                
                pane.repaint();
            }
        });
        
        timer.start();
    }
    
    ArrayList<CardPanel> myCards = new ArrayList<CardPanel>();
    JLabel central1 = new JLabel("");
    JLabel central2 = new JLabel("");
    JFrame game;
    /*
     * 
     * This sets up all the cards in the beginning of the game and shows the
     * images for each the card
     * @param game - the JFrame holding the game
     * @param player - the instance of the speed game
     
    private void cardsInit_o(JFrame game, Player player)
    {
        ArrayList<JLabel> theirCards = new ArrayList<JLabel>();
        ArrayList<Card> h1 = player.getHand();
        ImageIcon back = new ImageIcon(GameGUI.class.getResource("/images/back.png"));
        for(int i = 0; i < 5; i++) {
            CardPanel cp = new CardPanel(h1.get(i).getImage(), i);
            cp.setBounds(myStartx + playerdx * i, myStarty, 60, 92);
            myCards.add( cp );
            cp.setOpaque( true );
            
            JLabel jp = new JLabel("");
            jp.setBounds(otherStartx + playerdx * i, otherStarty, 60, 92);
            jp.setIcon( back );
            theirCards.add( jp );
            
            game.getContentPane().add(cp);
            game.getContentPane().add(jp);
        }
        
        JLabel side1 = new JLabel("");
        JLabel side2 = new JLabel("");
        JLabel deck1 = new JLabel("");
        JLabel deck2 = new JLabel("");
        
        side1.setBounds(centralX, centralY, 65, 92);
        central1.setBounds(centralX + centraldx, centralY, 60, 92);
        central2.setBounds(centralX + 2 * centraldx, centralY, 60, 92);
        side2.setBounds(centralX + 3 * centraldx, centralY, 65, 92);
        deck1.setBounds(484, 331, 65, 92);
        deck2.setBounds(41, 85, 65, 92);
        
        central1.setIcon( player.getPile1().getImage());
        central2.setIcon( player.getPile2().getImage());
        
        side1.setIcon(back);
        side2.setIcon(back);
        deck1.setIcon(back);
        deck2.setIcon(back);

        game.add(central1);
        game.getContentPane().add(central2);
        game.getContentPane().add(side1);
        game.getContentPane().add(side2);
        game.getContentPane().add(deck1);
        game.getContentPane().add(deck2);
        
    }*/
    
    JLayeredPane pane;
    
    /*@Override
    public void paint(Graphics g) {
        super.paint( g );
        ArrayList<Card> h1 = player.getHand();
        for(int i = 0; i < 5; i++) {
            Card c = h1.get( i );
            ImageIcon cp = c.getImage();
            cp.paintIcon( this, g, myStartx + playerdx * i, myStarty );
        }
        System.out.println("painted!!!!!!!!");
    }*/
    
    public void setPlayerName(String s)
    {
        playerName = s;
    }
    
    public void setIPAddress(String s)
    {
        IPAddress = s;
        System.out.println(IPAddress);
    }
    
    // could be buggy
    public void updateCentPilesPic(JFrame game, Speed speed)
    {
        JLabel c1 = (JLabel)game.getComponentAt( centralX + centraldx, centralY );
        c1.setIcon(speed.getCentralPile1Card().getImage());
        JLabel c2 = (JLabel)game.getComponentAt( centralX + 2 * centraldx, centralY);
        c2.setIcon( speed.getCentralPile2Card().getImage() );
    }
    
    public void updateSidePilesPic(JFrame game, Speed speed)
    {
        JLabel s1 = (JLabel)game.getComponentAt( centralX, centralY );
        JLabel s2 = (JLabel)game.getComponentAt( centralX + 3 * centraldx, centralY);
        if (speed.getSideDeck1().isEmpty())
        {
            s1.setIcon(null);
            s2.setIcon(null);
        }   
    }
    
    // updates the deck pic of the other (top) player
    public void updateDeck1Pic(JFrame game, Speed speed)
    {
        JLabel d1 = (JLabel)game.getComponentAt(41, 85);
        if (speed.getDeck1().isEmpty())
        {
            d1.setIcon(null);
        }   
    }
    
    // updates the deck pic of my (bottom) player
    public void updateDeck2Pic(JFrame game, Speed speed)
    {
        JLabel d2 = (JLabel)game.getComponentAt(484, 331);
        if (speed.getDeck2().isEmpty())
        {
            d2.setIcon(null);
        }   
    }
    
    /**
     * 
     * Updates the image of a specific card in the upper hand
     * @param game - the JFrame holding the game
     * @param speed - the instance of the speed game
     * @param n - card number in hand (goes 1-5)
     */
    public void updateHand1Card(JFrame game, Speed speed, int n)
    {
        JLabel h1 = (JLabel)game.getComponentAt(hand1X[n - 1], myStarty);
        h1.setIcon( speed.getHand1().get(n - 1).getImage());
    }
    
    /**
     * 
     * Updates the image of a specific card in the upper hand
     * @param game - the JFrame holding the game
     * @param speed - the instance of the speed game
     * @param n - card number in hand (goes 1-5)
     */
    public void updateHand2Card(JFrame game, Speed speed, int n)
    {
        JLabel h2 = (JLabel)game.getComponentAt(hand2X[n - 1], myStarty);
        h2.setIcon( speed.getHand2().get(n - 1).getImage());
    }
    
    public void win()
    {
        JFrame winPage = new JFrame("YOU WON!");
        winPage.setSize( 600, 400 );
        winPage.getContentPane().setBackground( new Color(130, 20, 0) );
        winPage.getContentPane().setLayout(null);
        winPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel winLabel = new JLabel("");
        winLabel.setBounds(300-434/2, 100, 434, 126);
        winLabel.setIcon( new ImageIcon (GameGUI.class.getResource("/images/Win.png")) );
        winPage.getContentPane().add(winLabel);
    }
    
    public void lose()
    {
        JFrame losePage = new JFrame("YOU LOST :(");
        losePage.setSize( 600, 400 );
        losePage.getContentPane().setBackground( new Color(130, 20, 0) );
        losePage.getContentPane().setLayout(null);
        losePage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel loseLabel = new JLabel("");
        loseLabel.setBounds(300-520/2, 100, 520, 126);
        loseLabel.setIcon( new ImageIcon (GameGUI.class.getResource("/images/Lost.png")) );
        losePage.getContentPane().add(loseLabel);
    }
}
