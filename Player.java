
/*
 * Comment here if there's any changes you want:
 * 
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;


public class Player
{
    private ArrayList<Card> hand;

    private Card pile1, pile2;

    private boolean stuck = false;

    private ImageIcon imgs[] = new ImageIcon[52];
    
    public Card getPile1()
    {
        return pile1;
    }

    public Card getPile2()
    {
        return pile2;
    }

    public ArrayList<Card> getHand()
    {
        return hand;
    }

    public void setHand( ArrayList<Card> hand )
    {
        this.hand = hand;
    }

    private String ip;
    
    /**
     * @param hand
     *            The player's initial hand. Can have 5 cards at most, so this
     *            parameter should be initialized with a size 5
     * @param deck
     *            The player's initial drawpile. Can have 15 cards at most.
     */
    public Player( String ip ) // , Socket s)
    {
        // initImgs();
        hand = new ArrayList<Card>();
        pile1 = new Card( 0, "NULL" );
        pile2 = new Card( 0, "NULL" );
    }
    
    /*private void initImgs()
    {
        for ( int i = 1; i <= 13; i++ )
        {
            String h = "/images/" + i + "H.png";
            String c = "/images/" + i + "C.png";
            String d = "/images/" + i + "D.png";
            String s = "/images/" + i + "S.png";
            imgs[( i - 1 ) * 4] = new ImageIcon( Speed.class.getResource( h ) );
            imgs[( i - 1 ) * 4 + 1] = new ImageIcon( Speed.class.getResource( c ) );
            imgs[( i - 1 ) * 4 + 2] = new ImageIcon( Speed.class.getResource( d ) );
            imgs[( i - 1 ) * 4 + 3] = new ImageIcon( Speed.class.getResource( s ) );

        }
    }*/

    public void addHand( ArrayList<Card> hand )
    {
        this.hand = hand;
    }
    
    public void stuck()
    {
        if(!stuck) {
            stuck = true;
            out.println("STUCK");
        }
    }

    public void moveToPile( Card c, int pileNum )
    {
        if ( hand.contains( c ) )
        {
            out.println( "MOVETOPILE|" + c.toString() + "|" + pileNum );
        }
    }

    int refillIndex = 0;
    
    public boolean remove( Card c )
    {
        if ( hand.contains( c ) )
        {
            refillIndex = hand.indexOf( c );
            // hand.remove( c );
            refill();
            return true;
        }
        return false;
    }

    public boolean isStuck()
    {
        return stuck;
    }

    public boolean isDone()
    {
        // TODO check with server
        return hand.isEmpty(); // && deck.isEmpty();
    }

    // networking

    String name = "";

    String oppName = "";

    Socket kkSocket;

    PrintWriter out;

    BufferedReader in;

    public void initialize( String ip, int port ) throws UnknownHostException, IOException
    {
        String hostName = ip;
        int portNumber = port;

        kkSocket = new Socket( hostName, portNumber );
        out = new PrintWriter( kkSocket.getOutputStream(), true );
        in = new BufferedReader( new InputStreamReader( kkSocket.getInputStream() ) );

        startThreads();
    }

    public void startThreads()
    {
        Thread userRunner = new Thread( new UserInputRunner() );
        Thread serverRunner = new Thread( new ServerOutputRunner() );
        userRunner.start();
        serverRunner.start();
    }

    public void init(String ip) 
    {
        try
        {
            initialize( ip, 4441 );
        }
        catch ( UnknownHostException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*public static void main( String[] args ) throws IOException
    {
        Player p = new Player();
        p.init();
    }*/

    public void refill()
    {
        out.println( "REFILL" );
    }

    class UserInputRunner implements Runnable
    {
        // sends message depending on the user state
        @Override
        public void run()
        {
            // USED AS TEMPORARY REPLACEMENT FOR GUI INPUTS
            // SHOULD BE REPLACED BY INPUT LISTENERS WITH GUI !!
            Scanner s = new Scanner( System.in );
            String fromUser;// = s.nextLine();
            // name = fromUser;
            // out.println( "NAME|" + fromUser );

            while ( ( fromUser = s.nextLine() ) != null )
            {
                out.println( fromUser );
            }
            s.close();
        }
    }
    
    public void setName(String name) {
        this.name = name;
        out.println("NAME|" + name);
    }

    class ServerOutputRunner implements Runnable
    {
        // receive message from other player
        @Override
        public void run()
        {
            String fromServer = "";
            try
            {
                while ( ( fromServer = in.readLine() ) != null )
                {
                    System.out.println( "Server: " + fromServer );
                    if ( fromServer.startsWith( "HANDADD" ) )
                    {
                        Card c = new Card( fromServer.split( "\\|" )[1] );
                        if(hand.size() == 5) {
                        	hand.remove(refillIndex);
                        }
                        hand.add( refillIndex, c );
                        System.out.println( "Updated hand: " + hand );
                    }
                    else if (fromServer.startsWith("DEACTCARD")) {
                    	hand.get(refillIndex).deact = true;
                    }
                    else if ( fromServer.startsWith( "SETPILE" ) )
                    {
                        if(stuck) {
                            stuck = false;
                            System.out.println("UNSTUCK");
                        }
                        Card c = new Card( fromServer.split( "\\|" )[1] );
                        if ( fromServer.split( "\\|" )[2].equals( "1" ) )
                        {
                            pile1 = c;
                            System.out.println( "Pile 1: " + pile1.toString() + "; Pile 2: " + pile2.toString() );
                        }
                        else
                        {
                            pile2 = c;
                            System.out.println( "Pile 1: " + pile1.toString() + "; Pile 2: " + pile2.toString() );
                        }
                    }
                    else if ( fromServer.startsWith( "HANDREMOVE" ) )
                    {
                        Card c = new Card( fromServer.split( "\\|" )[1] );
                        remove( c );
                        System.out.println( "Updated hand: " + hand );
                    }
                    else if ( fromServer.startsWith( "YOUWIN" ) )
                    {
                        state = 2;
                        break;
                    }
                    else if ( fromServer.startsWith( "YOULOSE" ) )
                    {
                        state = 0;
                        break;
                    }
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }
    
    public int state = 1; // 0 = lose, 1 = running, 2 = win
    
    public int stuckCount = 0;
}
