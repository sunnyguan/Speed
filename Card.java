import java.net.URL;

import javax.swing.ImageIcon;

/*
 * Comment here if there's any changes you want:
 * 
 * 
 */

public class Card 
{
    //Maybe a visibility parameter
    private int value;

    private String suite;
    
    private ImageIcon img;
    
    public boolean deact = false;
    
    /*
    private boolean visible1 = false;
    
    private boolean visible2 = false;
    */

    public Card( int val, String suite)
    {
        value = val;
        this.suite = suite;
    }
    
    public Card(String toString) {
        String[] split = toString.split( "," );
        value = Integer.parseInt(split[0]);
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
    /*
    public boolean getVisible1()
    {
        return visible1;
    }
    
    public void setVisible1(boolean v)
    {
        visible1 = v;
    }
    
    public boolean getVisible2()
    {
        return visible2;
    }
    
    public void setVisible2(boolean v)
    {
        visible2 = v;
    }
    */
    
    public String toString()
    {
        return value + "," + suite;
    }
    
    public ImageIcon getImage()
    {
        if(img == null) {
            String imageURI = "/images/" + value + suite + ".png";
            img = new ImageIcon(Card.class.getResource(imageURI));
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