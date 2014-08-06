import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;

/**
@author: Dylan Foster
@version: 12/14/12
@desciption: Represents a solid graphical object on the game field.
*/

public class Pillar extends JComponent implements Solid
{
 
    public Pillar ()
    {
        
        //Adds pillar graphic to this component.
        JLabel image = new JLabel( new ImageIcon( "pillar.png" ) );
        image.setBounds( 0, 0, 100, 100 );
        this.add( image );
        
    }
    
    /**
        Checks if an object with the given dimensions will collide with this one.
        @param int x
        @param int y
        @param int width
        @param int height
        @return boolean collision occured
    */
    public boolean willCollide ( int inX, int inY, int inWidth, int inHeight )
    {
        
        //Creates ellipse representing this pillar.
		Ellipse2D.Double pillar = new Ellipse2D.Double( this.getX(), this.getY(), 100, 100 );

        //Returns if this pillar collides with the given object.
		return pillar.intersects( inX, inY, inWidth, inHeight );
        
    }
    
}