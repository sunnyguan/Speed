
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;


public class Speed
{

    private ArrayList<Card> deck;

    private ArrayList<Card> centralPile1 = new ArrayList<Card>( 52 ), centralPile2 = new ArrayList<Card>( 52 );

    private Stack<Card> sideDeck1 = new Stack<Card>(), sideDeck2 = new Stack<Card>();

    private ArrayList<Card> hand1, hand2;

    private Stack<Card> deck1, deck2;

    private void initDeck()
    {
        deck = new ArrayList<Card>();
        for ( int i = 1; i <= 13; i++ )
        {
            deck.add( new Card( i, "H" ) );
            deck.add( new Card( i, "C" ) );
            deck.add( new Card( i, "D" ) );
            deck.add( new Card( i, "S" ) );
        }
    }

    public Speed()
    {
        initDeck();
        Collections.shuffle( deck );
        hand1 = new ArrayList<Card>( 5 );
        hand2 = new ArrayList<Card>( 5 );
        deck1 = new Stack<Card>();
        deck2 = new Stack<Card>();

        for ( int i = 0; i < 5; i++ )
        {
            Card c1 = deck.get( 4 * i );
            Card c2 = deck.get( 4 * i + 1 );

            hand1.add( c1 );
            hand2.add( c2 );

            sideDeck1.push( deck.get( 4 * i + 2 ) );
            sideDeck2.push( deck.get( 4 * i + 3 ) );

        }

        for ( int i = 0; i < 15; i++ )
        {
            deck1.push( deck.get( 2 * i + 20 ) );
            deck2.push( deck.get( 2 * i + 21 ) );
        }

        centralPile1.add( deck.get( 50 ) );
        centralPile2.add( deck.get( 51 ) );
    }

    /**
     * Tries to set the card on the given pile. Returns true if the action is
     * successfully completed, false otherwise
     * 
     * @param p
     *            Player card is from
     * @param c
     *            Card to be set
     * @param pile
     *            must be either 1 or 2. 1 corresponds to the left pile
     * @return boolean true if the card can be set, false otherwise
     */
    public boolean set( int id, Card c, int pile )
    {
        ArrayList<Card> cPile = centralPile2;
        if ( pile == 1 )
        {
            cPile = centralPile1;
        }
        Card c1 = cPile.get( cPile.size() - 1 );
        if ( Math.abs( c1.getValue() - c.getValue() ) == 1 || Math.abs( c1.getValue() - c.getValue() ) == 12 )
        {
            cPile.add( c );
            return true;
        }

        return false;
    }

    public int stuckCount = 0;

    public void removeCard( int id, String card )
    {
        Card c = new Card( card );
        if ( id == 1 )
        {
            hand1.remove( c );
        }
        else if ( id == 2 )
        {
            hand2.remove( c );
        }
    }

    public Stack<Card> getDeck1()
    {
        return deck1;
    }

    public void setDeck1( Stack<Card> deck1 )
    {
        this.deck1 = deck1;
    }

    public Stack<Card> getDeck2()
    {
        return deck2;
    }

    public void setDeck2( Stack<Card> deck2 )
    {
        this.deck2 = deck2;
    }

    public void addStuck()
    {
        stuckCount++;
    }

    public boolean flipSideDeck()
    {
        if ( !sideDeck1.isEmpty() )
        {
            centralPile1.add( sideDeck1.pop() );
            centralPile2.add( sideDeck2.pop() );
        }
        else
        {
            // May need it to be more efficient
            Collections.shuffle( centralPile1 );
            Collections.shuffle( centralPile2 );
            sideDeck1.addAll( centralPile1 );
            sideDeck2.addAll( centralPile2 );
            centralPile1.add( sideDeck1.pop() );
            centralPile2.add( sideDeck2.pop() );
        }
        stuckCount = 0;
        return true;
    }

    public Card getCentralPile1Card()
    {
        return centralPile1.get( centralPile1.size() - 1 );
    }

    public Card getCentralPile2Card()
    {
        return centralPile2.get( centralPile2.size() - 1 );
    }

    public static void main( String[] args )
    {
        Speed s = new Speed();
        s.startServer();
    }
    
    public static final int PORT = 4441;

    private static final boolean TESTING = false;

    public void startServer()
    {
        String ip = "192.168.1.131";// manually enter ip here ! 
        if ( ip.equals( "ERR" ) )
        {
            System.out.println( "Could not get IP from command line. " );
            System.exit( -1 );
        }
        System.out.println( "Listening on IP: " + ip + ":" + PORT );

        if ( TESTING )
        {
            ServerInputRunner sir = new ServerInputRunner();
            Thread t = new Thread( sir );
            t.start();
        }

        try (ServerSocket serverSocket = new ServerSocket( PORT ))
        {
            SpeedServerThread mst = new SpeedServerThread( serverSocket.accept(), this, 1 );
            mst.start();
            System.out.println( "Connected! Waiting for name..." );

            SpeedServerThread mst2 = new SpeedServerThread( serverSocket.accept(), this, 2 );
            mst2.start();
            System.out.println( "Connected! Waiting for name..." );

            mst.setOtherPlayer( mst2 );
            mst2.setOtherPlayer( mst );
        }
        catch ( IOException e )
        {
            System.err.println( "Could not listen on port " + PORT );
            System.exit( -1 );
        }
    }

    class ServerInputRunner implements Runnable
    {

        @Override
        public void run()
        {
            Scanner s = new Scanner( System.in );
            String fromUser = s.nextLine();
            
            while ( !fromUser.equals( null ) )
            {
                if ( fromUser.toLowerCase().equals( "quit" ) || fromUser.toLowerCase().equals( "exit" ) )
                {
                    break;
                }
                else
                {
                    System.out.println( "Type \"quit\" or \"exit\" to stop the server." );
                }
                fromUser = s.nextLine();
            }
            s.close();
        }
    }

    public ArrayList<Card> getCentralPile1()
    {
        return centralPile1;
    }

    public void setCentralPile1( ArrayList<Card> centralPile1 )
    {
        this.centralPile1 = centralPile1;
    }

    public ArrayList<Card> getCentralPile2()
    {
        return centralPile2;
    }

    public void setCentralPile2( ArrayList<Card> centralPile2 )
    {
        this.centralPile2 = centralPile2;
    }

    public Stack<Card> getSideDeck1()
    {
        return sideDeck1;
    }

    public void setSideDeck1( Stack<Card> sideDeck1 )
    {
        this.sideDeck1 = sideDeck1;
    }

    public Stack<Card> getSideDeck2()
    {
        return sideDeck2;
    }

    public void setSideDeck2( Stack<Card> sideDeck2 )
    {
        this.sideDeck2 = sideDeck2;
    }

    public ArrayList<Card> getHand1()
    {
        return hand1;
    }

    public void setHand1( ArrayList<Card> hand1 )
    {
        this.hand1 = hand1;
    }

    public ArrayList<Card> getHand2()
    {
        return hand2;
    }

    public void setHand2( ArrayList<Card> hand2 )
    {
        this.hand2 = hand2;
    }

    public static String getIP()
    {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command( "cmd.exe", "/c", "ipconfig" );

        try
        {

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader( new InputStreamReader( process.getInputStream() ) );

            String line;
            while ( ( line = reader.readLine() ) != null )
            {
                if ( line.contains( "IPv4" ) )
                {
                    return line.split( ": " )[1];
                }
            }

        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        return "ERR";
    }
}
