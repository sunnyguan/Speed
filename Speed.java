/*
 * Comment here if there's any changes you want:
 * 
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.*;


public class Speed
{

    private ArrayList<Card> deck;

    private ArrayList<Card> centralPile1 = new ArrayList<Card>( 52 ),
                    centralPile2 = new ArrayList<Card>( 52 );

    private Stack<Card> sideDeck1 = new Stack<Card>(),
                    sideDeck2 = new Stack<Card>();
    
    private ArrayList<Card> hand1, hand2;

    private Stack<Card> deck1, deck2;
    
    // private Player player1, player2;

    private void initDeck() {
        deck = new ArrayList<Card>();
        for(int i = 1; i <= 13; i++) {
            deck.add( new Card(i, "H") );
            deck.add( new Card(i, "C") );
            deck.add( new Card(i, "D") );
            deck.add( new Card(i, "S") );
        }
    }
    
    // TODO: check for errors. error prone!
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
            // TODO: see if its more efficient to declare variable outside loop
            // only if visibility implemented
            Card c1 = deck.get( 4 * i );
            //c1.setVisible1( true );
            Card c2 = deck.get( 4 * i + 1 );
            //c2.setVisible2( true );

            hand1.add( c1 );
            hand2.add( c2 );
            // TODO: will change if we care about order
            sideDeck1.push( deck.get( 4 * i + 2 ) );
            sideDeck2.push( deck.get( 4 * i + 3 ) );
        }

        for ( int i = 0; i < 15; i++ )
        {
            deck1.push( deck.get( 2 * i + 20) );
            deck2.push( deck.get( 2 * i + 21) );
        }
        // player1 = p1;
        // player2 = p2;

        centralPile1.add( deck.get( 50 ) );
        centralPile2.add( deck.get( 51 ) );
        // only if visibility used
        /*
        centralPile1.get( centralPile1.size() - 1 ).setVisible1( true );
        centralPile1.get( centralPile1.size() - 1 ).setVisible2( true );
        centralPile2.get( centralPile2.size() - 1 ).setVisible1( true );
        centralPile2.get( centralPile2.size() - 1 ).setVisible2( true );
        */
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
        // TODO: finish
        // if its one away or if its a king (13) and an ace (1)
        Card c1 = cPile.get( cPile.size() - 1 );
        if ( Math.abs( c1.getValue() - c.getValue() ) == 1
            || Math.abs( c1.getValue() - c.getValue() ) == 12 )
        {
            cPile.add( c );
            return true;
        }

        return false;
    }


    /*public boolean draw( ArrayList<Card> deck, ArrayList<Card> hand )
    {
        return false;
    }


    // May change to actual player
    public void stuck( int player )
    {

    }*/

    //TODO: add stuff for the window pop up
    
    public int stuckCount = 0;
    
    public void removeCard(int id, String card) {
        Card c = new Card(card);
        if(id == 1) {
            hand1.remove( c );
        } else if (id == 2) {
            hand2.remove( c );
        }
    }
    
    public void refill(int id)
    {
        
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

    public boolean flipSideDeck()
    {
        if(!(stuckCount == 2))
        {
            return false;
        }
        if(!sideDeck1.isEmpty())
        {
            centralPile1.add( sideDeck1.pop() );
            centralPile2.add( sideDeck2.pop() );
        }
        else
        {
            //May need it to be more efficient
            Collections.shuffle( centralPile1 );
            Collections.shuffle( centralPile2 );
            sideDeck1.addAll( centralPile1 );
            sideDeck2.addAll( centralPile2 );
            centralPile1.add( sideDeck1.pop() );
            centralPile2.add( sideDeck2.pop() );
        }
        return true;
    }

    //TODO: change if necessary
    public boolean finish( Player p )
    {
        return p.isDone();
    }
    
    public static void main(String[] args) {
        Speed s = new Speed();
        s.startServer();
    }
    
    public static final int PORT = 4441;

    // localhost switch
    public static final boolean localhost = false;
    
    public void startServer()
    {
        String ip;
        if(localhost) {
            ip = "localhost";
        } else {
            ip = getIP();
        }
        if ( ip.equals( "ERR" ) )
        {
            System.out.println( "Could not get IP from command line. " );
            System.exit( -1 );
        }
        System.out.println( "Listening on IP: " + ip + ":" + PORT );

        ServerInputRunner sir = new ServerInputRunner();
        Thread t = new Thread( sir );
        t.start();

        try (ServerSocket serverSocket = new ServerSocket( PORT ))
        {
            SpeedServerThread mst = new SpeedServerThread( serverSocket.accept(), this, 1 );
            mst.start();
            System.out.println("Connected!");

            SpeedServerThread mst2 = new SpeedServerThread( serverSocket.accept(), this, 2 );
            mst2.start();
            
            mst.otherPlayer = mst2;
            mst2.otherPlayer = mst;
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
