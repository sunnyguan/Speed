
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
import java.util.Stack;


// import java.net.*;
public class Player
{
    private ArrayList<Card> hand;

    private Card pile1, pile2;

    private boolean stuck = false;
    // private Socket s;
    
    private boolean autoRefill;
    
    /**
     * @param hand
     *            The player's initial hand. Can have 5 cards at most, so this
     *            parameter should be initialized with a size 5
     * @param deck
     *            The player's initial drawpile. Can have 15 cards at most.
     */
    public Player( boolean mode ) // , Socket s)
    {
        this.autoRefill = mode;
        hand = new ArrayList<Card>();
        pile1 = new Card(0, "NULL");
        pile2 = new Card(0, "NULL");
        
    }

    /*public void addDeck( Stack<Card> deck )
    {
        this.deck = deck;
    }*/

    public void addHand( ArrayList<Card> hand )
    {
        this.hand = hand;
    }

    // TODO: check for off by one errors, may need to return something if empty
    

    // TODO: may need parameter
    public void stuck()
    {
        stuck = true;
    }
    
    public void moveToPile (Card c, int pileNum) {
        if(hand.contains(c)) {
            out.println("MOVETOPILE|" + c.toString() + "|" + pileNum);
        }
    }

    public boolean remove( Card c )
    {
        if ( hand.contains( c ) )
        {
            hand.remove( c );
            if(autoRefill) {
                refill();
            }
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

    public void init() throws UnknownHostException, IOException {
        initialize( "192.168.137.1", 4441 );
    }
    
    public static void main( String[] args ) throws IOException
    {
        Player p = new Player(true);
        p.init();
    }
    
    public void refill() {
        out.println("REFILL");
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
            String fromUser = s.nextLine();
            name = fromUser;
            out.println( "NAME|" + fromUser );
            
            while ( ( fromUser = s.nextLine() ) != null )
            {
                out.println( fromUser );
            }
            s.close();
        }
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
                    System.out.println("Server: " + fromServer);
                    if(fromServer.startsWith( "HANDADD" )) {
                        Card c = new Card(fromServer.split( "\\|" )[1]);
                        hand.add( c );
                        System.out.println("Updated hand: " + hand);
                    } else if (fromServer.startsWith( "SETPILE" )) {
                        Card c = new Card(fromServer.split( "\\|" )[1]);
                        if(fromServer.split( "\\|" )[2].equals( "1" )) {
                            pile1 = c;
                            System.out.println("Pile 1: " + pile1.toString() + "; Pile 2: " + pile2.toString());
                        } else {
                            pile2 = c;
                            System.out.println("Pile 1: " + pile1.toString() + "; Pile 2: " + pile2.toString());
                        }
                    } else if(fromServer.startsWith( "HANDREMOVE" )) {
                        Card c = new Card(fromServer.split( "\\|" )[1]);
                        remove( c );
                        System.out.println("Updated hand: " + hand);
                    } else if(fromServer.startsWith( "PLCHDR" )) {
                        
                        
                    }
                    // sp.processInput( fromServer );
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }
}
