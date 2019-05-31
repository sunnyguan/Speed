
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Stack;


public class SpeedServerThread extends Thread
{
    private int id = -1;

    private Socket socket = null;

    private SpeedServerThread otherPlayer = null;

    private Speed game;

    private String name;

    public SpeedServerThread( Socket socket, Speed game, int id )
    {
        super( "KKMultiServerThread" );
        this.id = id;
        this.socket = socket;
        this.game = game;
    }

    public String getPlayerName()
    {
        return name;
    }

    public SpeedServerThread getOtherPlayer()
    {
        return otherPlayer;
    }

    public void setOtherPlayer( SpeedServerThread otherPlayer )
    {
        this.otherPlayer = otherPlayer;
    }

    public PrintWriter getOut()
    {
        return out;
    }

    public void setOut( PrintWriter out )
    {
        this.out = out;
    }

    private PrintWriter out;

    public void run()
    {
        try
        {
            out = new PrintWriter( socket.getOutputStream(), true );

            BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
            String inputLine = in.readLine();
            name = inputLine.substring( 5 );
            System.out.println( name + " joined the server." );

            if ( id == 1 )
            {
                for ( Card c : game.getHand1() )
                {
                    out.println( "HANDADD|" + c.toString() );
                }
            }
            else
            {
                for ( Card c : game.getHand2() )
                {
                    out.println( "HANDADD|" + c.toString() );
                }
            }
            out.println( "SETPILE|" + game.getCentralPile1().get( 0 ) + "|1" );
            out.println( "SETPILE|" + game.getCentralPile2().get( 0 ) + "|2" );
            while ( ( inputLine = in.readLine() ) != null )
            {
                if ( inputLine.equals( "STUCK" ) )
                {
                    game.stuckCount++;
                    if ( game.stuckCount > 2 )
                        game.stuckCount %= 2;
                    if ( game.stuckCount == 2 )
                    {
                        game.flipSideDeck();
                        String msg1 = "SETPILE|" + game.getCentralPile1Card().toString() + "|1";
                        String msg2 = "SETPILE|" + game.getCentralPile2Card().toString() + "|2";
                        out.println( msg1 );
                        out.println( msg2 );
                        otherPlayer.out.println( msg1 );
                        otherPlayer.out.println( msg2 );
                        game.stuckCount = 0;
                        System.out.println( "Both players stuck." );
                    }
                }
                else if ( inputLine.equals( "UNSTUCK" ) )
                {
                    game.stuckCount--;
                }
                else if ( inputLine.startsWith( "HANDREMOVE" ) )
                {
                    game.removeCard( id, inputLine.split( "\\|" )[1] );
                    ArrayList<Card> hand = getHand( id );
                    Stack<Card> deck = getDeck( id );
                    if ( hand.isEmpty() && deck.isEmpty() )
                    {
                        gameOverAction();
                        break;
                    }
                }
                else if ( inputLine.startsWith( "MOVETOPILE" ) )
                {
                    Card c = new Card( inputLine.split( "\\|" )[1] );
                    int pileNum = Integer.parseInt( inputLine.split( "\\|" )[2] );
                    boolean canSet = game.set( id, c, pileNum );
                    if ( canSet )
                    {
                        game.removeCard( id, inputLine.split( "\\|" )[1] );
                        out.println( "HANDREMOVE|" + c );
                        String msg = "SETPILE|" + inputLine.split( "\\|" )[1] + "|" + inputLine.split( "\\|" )[2];
                        out.println( msg );
                        otherPlayer.out.println( msg );

                        ArrayList<Card> hand = getHand( id );
                        Stack<Card> deck = getDeck( id );
                        if ( hand.isEmpty() && deck.isEmpty() )
                        {
                            gameOverAction();
                            break;
                        }
                    }
                }
                else if ( inputLine.startsWith( "REFILL" ) )
                {
                    ArrayList<Card> hand = getHand( id );
                    Stack<Card> deck = getDeck( id );

                    while ( hand.size() < 5 )
                    {
                        if ( !deck.isEmpty() )
                        {
                            Card c = deck.pop();
                            hand.add( c );
                            out.println( "HANDADD|" + c.toString() );
                        }
                        else
                        {
                            out.println( "DEACTCARD" );
                            otherPlayer.out.println( "DECKEMPTY2" );
                            break;
                        }
                    }
                }
                else if ( inputLine.startsWith( "FGAMEOVER" ) )
                { // TODO testing only!
                    gameOverAction();
                    // break;
                }

                System.out.println( inputLine );
                System.out.println( "Left in deck: " + game.getDeck1().size() + ", " + game.getDeck2().size() );
            }
            socket.close();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

    }

    public ArrayList<Card> getHand( int id )
    {
        return id == 1 ? game.getHand1() : game.getHand2();
    }

    public Stack<Card> getDeck( int id )
    {
        return id == 1 ? game.getDeck1() : game.getDeck2();
    }

    public void gameOverAction()
    {
        out.println( "YOUWIN" );
        otherPlayer.out.println( "YOULOSE" );
        System.out.println( "Game over! " + name + " wins! " + otherPlayer.getPlayerName() + " loses!" );
    }

    public boolean remove( int id, Card c )
    {
        ArrayList<Card> hand = id == 1 ? game.getHand1() : game.getHand2();
        return hand.remove( c );
    }
}
