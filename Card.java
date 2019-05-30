import javax.swing.ImageIcon;


public class Card
{
    private int value;

    private String suite;

    private ImageIcon img;

    private boolean deact = false;

    public boolean isDeact()
    {
        return deact;
    }

    public void setDeact( boolean deact )
    {
        this.deact = deact;
    }

    public Card( int val, String suite )
    {
        value = val;
        this.suite = suite;
    }

    public Card( String toString )
    {
        String[] split = toString.split( "," );
        value = Integer.parseInt( split[0] );
        suite = split[1];
    }

    public int getValue()
    {
        return value;
    }

    public String getSuite()
    {
        return suite;
    }

    public String toString()
    {
        return value + "," + suite;
    }

    public ImageIcon getImage()
    {
        if ( img == null )
        {
            String imageURI = "/images/" + value + suite + ".png";
            img = new ImageIcon(imageURI);
        }
        return img;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( suite == null ) ? 0 : suite.hashCode() );
        result = prime * result + value;
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        Card other = (Card)obj;
        if ( suite == null )
        {
            if ( other.suite != null )
                return false;
        }
        else if ( !suite.equals( other.suite ) )
            return false;
        if ( value != other.value )
            return false;
        return true;
    }

}