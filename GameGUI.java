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
import javax.swing.Timer;


public class GameGUI extends JFrame
{

    public static void main( String args[] )
    {
        GameGUI gg = new GameGUI();
        gg.init();
    }

    public void init()
    {
        player = new Player();
        JFrame homePage = new JFrame( "Home Page" );
        homePage.setSize( 600, 550 );
        homePage.getContentPane().setBackground( new Color( 130, 20, 0 ) );

        JButton playButton = new JButton( "PLAY" );
        int mid = homePage.getWidth() / 2 - 182 / 2;
        playButton.setBounds( mid, 248, 182, 29 );
        JButton instructionButton = new JButton( "INSTRUCTIONS" );
        instructionButton.setBounds( mid, 282, 182, 29 );

        String s = "/images/SpeedTitle.png";
        //
        ImageIcon icon = new ImageIcon( GameGUI.class.getResource( s ) );
        JLabel label = new JLabel();
        label.setIcon( icon );
        label.setBounds( homePage.getWidth() / 2 - 306 / 2, 80, 306, 144 );
        homePage.getContentPane().add( label );

        homePage.getContentPane().setLayout( null );
        homePage.getContentPane().add( playButton );
        homePage.getContentPane().add( instructionButton );
        homePage.setResizable( false );
        homePage.setVisible( true );

        // name and IP address GUI
        JFrame setUp = new JFrame( "Set Up" );
        setUp.setSize( 400, 300 );
        setUp.getContentPane().setLayout( null );

        JButton startGame = new JButton( "Let's Play!" );
        startGame.setBounds( 200 - 45, 197, 90, 20 );
        setUp.getContentPane().add( startGame );

        JLabel namelbl = new JLabel( "Name:" );
        namelbl.setBounds( 61, 67, 50, 16 );
        setUp.getContentPane().add( namelbl );
        JLabel IPAddress = new JLabel( "IP Address:" );
        IPAddress.setBounds( 61, 121, 90, 16 );
        setUp.getContentPane().add( IPAddress );

        JTextField nameText = new JTextField( "" );
        JTextField IPText = new JTextField( "" );
        nameText.setBounds( 155, 62, 130, 26 );
        IPText.setBounds( 155, 116, 130, 26 );
        nameText.setEditable( true );
        IPText.setEditable( true );
        setUp.getContentPane().add( nameText );
        setUp.getContentPane().add( IPText );
        nameText.setColumns( 10 );
        IPText.setColumns( 10 );

        JButton ok1 = new JButton( "OK" );
        ok1.setBounds( 322, 62, 50, 29 );
        setUp.getContentPane().add( ok1 );
        JButton ok2 = new JButton( "OK" );
        ok2.setBounds( 322, 116, 50, 29 );
        setUp.getContentPane().add( ok2 );

        // Instructions page GUI
        JFrame instruct = new JFrame( "Instructions" );
        JTextArea text = new JTextArea( "Instructions", 30, 40 );
        JScrollPane sp = new JScrollPane( text );

        JMenuBar mb = new JMenuBar();
        instruct.getContentPane().add( mb );
        JMenu close = new JMenu( "Close" );
        mb.add( close );

        instruct.setLayout( new FlowLayout() );
        instruct.setSize( 500, 600 );
        instruct.getContentPane().add( sp );

        // Speed Game GUI
        game = new JFrame( "Speed Game" );
        game.setSize( 600, 550 );
        pane = game.getLayeredPane();

        pane.setBackground( new Color( 0, 100, 0 ) );
        game.setBackground( new Color( 0, 100, 0 ) );
        game.getContentPane().setBackground( new Color( 0, 100, 0 ) );
        game.setResizable( false );

        JMenuBar menuBar = new JMenuBar();
        game.setJMenuBar( menuBar );
        JMenu gameInstructions = new JMenu( "Instructions" );
        menuBar.add( gameInstructions );

        stuckButton = new JRadioButton( "" );
        stuckButton.setBounds( 515, 166, 42, 23 );
        stuckButton.setBackground( new Color( 0, 100, 0 ) );
        game.getLayeredPane().add( stuckButton );

        JLabel lblStuck = new JLabel( "STUCK" );
        lblStuck.setForeground( new Color( 255, 255, 255 ) );
        lblStuck.setBounds( 505, 148, 61, 16 );
        game.getLayeredPane().add( lblStuck );

        // instruction page
        instructionButton.addMouseListener( new MouseAdapter()
        {
            public void mouseClicked( MouseEvent e )
            {
                instruct.setVisible( true );
            }

        } );

        // play button --> set up GUI
        playButton.addMouseListener( new MouseAdapter()
        {
            public void mouseClicked( MouseEvent e )
            {
                homePage.setVisible( false );
                setUp.setVisible( true );
            }

        } );

        // play button --> set up GUI
        startGame.addMouseListener( new MouseAdapter() // HERE
        {
            public void mouseClicked( MouseEvent e )
            {
                String ip = IPText.getText();
                if ( IPText.getText().charAt( 0 ) != '1' )
                {
                    ip = "192.168.137.1";
                }
                player.init( ip );
                // setName
                player.setName( nameText.getText() );
                System.out.println( IPText.getText() );
                cardsInit( pane );
                setUp.setVisible( false );
                game.setVisible( true );
            }

        } );

        // instructions pop up when playing the game
        gameInstructions.addMouseListener( new MouseAdapter()
        {
            public void mouseClicked( MouseEvent e )
            {
                instruct.setVisible( true );
            }
        } );

        // instructions close
        close.addMouseListener( new MouseAdapter()
        {
            public void mouseClicked( MouseEvent e )
            {
                instruct.setVisible( false );
            }

        } );

        stuckButton.addMouseListener( new MouseAdapter()
        {
            // check if this works
            public void mouseClicked( MouseEvent e )
            {
                player.stuck();
            }
        } );

    }

    private JRadioButton stuckButton;

    private Player player;

    public static int playerdx = 75;

    public static int otherStartx = 130;

    public static int myStartx = 80;

    public static int otherStarty = 49;

    public static int myStarty = 370;

    public static int centraldx = 118;

    public static int centralX = 70;

    public static int centralY = ( myStarty + ( otherStarty + 92 ) ) / 2 - 92 / 2;

    private static int[] hand1X = { otherStartx, otherStartx + playerdx, otherStartx + 2 * playerdx,
        otherStartx + 3 * playerdx, otherStartx + 4 * playerdx };

    private static int[] hand2X = { myStartx, myStartx + playerdx, myStartx + 2 * playerdx, myStartx + 3 * playerdx,
        myStartx + 4 * playerdx };

    private JLabel deck1;

    private JLabel deck2;

    private void cardsInit( JLayeredPane pane )
    {
        myCards = new ArrayList<CardPanel>();
        ArrayList<JLabel> theirCards = new ArrayList<JLabel>();
        ArrayList<Card> h1 = player.getHand();
        ImageIcon back = new ImageIcon( GameGUI.class.getResource( "/images/back.png" ) );
        for ( int i = 0; i < h1.size(); i++ )
        {
            CardPanel cp = new CardPanel( h1.get( i ), i );
            cp.setBounds( myStartx + playerdx * i, myStarty, 60, 92 );
            myCards.add( cp );
            cp.setOpaque( true );

            JLabel jp = new JLabel( "" );
            jp.setBounds( otherStartx + playerdx * i, otherStarty, 87, 92 );
            jp.setIcon( back );
            theirCards.add( jp );

            pane.add( cp, new Integer( 2 ) );
            pane.add( jp, new Integer( 1 ) );
        }

        JLabel central1 = new JLabel( "" );
        JLabel central2 = new JLabel( "" );
        JLabel side1 = new JLabel( "" );
        JLabel side2 = new JLabel( "" );
        deck1 = new JLabel( "" );
        deck2 = new JLabel( "" );

        side1.setBounds( centralX, centralY, 65, 92 );
        central1.setBounds( centralX + centraldx, centralY, 60, 92 );
        central2.setBounds( centralX + 2 * centraldx, centralY, 60, 92 );
        side2.setBounds( centralX + 3 * centraldx, centralY, 65, 92 );
        deck1.setBounds( 484, 331, 65, 92 );
        deck2.setBounds( 41, 85, 65, 92 );

        central1.setIcon( player.getPile1().getImage() );
        central2.setIcon( player.getPile2().getImage() );

        side1.setIcon( back );
        side2.setIcon( back );
        deck1.setIcon( back );
        deck2.setIcon( back );

        pane.add( central1, new Integer( 1 ) );
        pane.add( central2, new Integer( 1 ) );
        pane.add( side1, new Integer( 1 ) );
        pane.add( side2, new Integer( 1 ) );
        pane.add( deck1, new Integer( 1 ) );
        pane.add( deck2, new Integer( 1 ) );

        Timer timer = new Timer( 40, new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                if ( player.state == 2 )
                {
                    win();
                    ( (Timer)e.getSource() ).stop();
                }
                else if ( player.state == 0 )
                {
                    lose();
                    ( (Timer)e.getSource() ).stop();
                }
                else
                {
                    if ( player.isDeck1Empty() )
                    {
                        game.getLayeredPane().remove( deck1 );
                        game.getLayeredPane().revalidate();
                        game.revalidate();
                    }
                    if ( player.isDeck2Empty() )
                    {
                        game.getLayeredPane().remove( deck2 );
                        game.getLayeredPane().revalidate();
                        game.revalidate();
                    }
                    for ( int i = 0; i < 5; i++ )
                    {
                        if ( player.getHand().get( i ).isDeact() )
                        {
                            myCards.get( i ).setDeact( true );
                            // System.out.println(i + " deactivated.");
                            continue;
                        }
                        Card c = player.getHand().get( i );
                        myCards.get( i ).setCard( c );
                        myCards.get( i ).setIcon( c.getImage() );
                        // check collision
                        if ( myCards.get( i ).isCollide1() )
                        {
                            player.out.println( "MOVETOPILE|" + myCards.get( i ).getCard().toString() + "|1" );
                            myCards.get( i ).setCollide1( false );
                        }
                        else if ( myCards.get( i ).isCollide2() )
                        {
                            player.out.println( "MOVETOPILE|" + myCards.get( i ).getCard().toString() + "|2" );
                            myCards.get( i ).setCollide2( false );
                        }
                        if ( !player.isStuck() )
                        {
                            stuckButton.setSelected( false );
                        }
                    }
                    central1.setIcon( player.getPile1().getImage() );
                    central2.setIcon( player.getPile2().getImage() );

                    pane.repaint();
                }
            }
        } );

        timer.start();
    }

    private ArrayList<CardPanel> myCards = new ArrayList<CardPanel>();

    private JLabel central1 = new JLabel( "" );

    private JLabel central2 = new JLabel( "" );

    private JFrame game;

    private JLayeredPane pane;

    public void win()
    {
        JFrame winPage = new JFrame( "YOU WON!" );
        winPage.setSize( 600, 400 );
        winPage.getContentPane().setBackground( new Color( 130, 20, 0 ) );
        winPage.getContentPane().setLayout( null );
        winPage.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        JLabel winLabel = new JLabel( "" );
        winLabel.setBounds( 300 - 434 / 2, 100, 434, 126 );
        winLabel.setIcon( new ImageIcon( GameGUI.class.getResource( "/images/Win.png" ) ) );
        winPage.getContentPane().add( winLabel );

        winPage.setVisible( true );
        game.setVisible( false );
    }

    public void lose()
    {
        JFrame losePage = new JFrame( "YOU LOST :(" );
        losePage.setSize( 600, 400 );
        losePage.getContentPane().setBackground( new Color( 130, 20, 0 ) );
        losePage.getContentPane().setLayout( null );
        losePage.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        JLabel loseLabel = new JLabel( "" );
        loseLabel.setBounds( 300 - 520 / 2, 100, 520, 126 );
        loseLabel.setIcon( new ImageIcon( GameGUI.class.getResource( "/images/Lost.png" ) ) );
        losePage.getContentPane().add( loseLabel );

        losePage.setVisible( true );
        game.setVisible( false );
    }
}
