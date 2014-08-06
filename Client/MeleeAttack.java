import java.awt.geom.Arc2D;
import java.awt.geom.Arc2D.Double;

/**
    @author: Dylan Foster
    @version: 12/14/12
    @description: Class representing an attack by a player
*/

public class MeleeAttack extends Action
{

    //Player number, x coordinate, y coordinate, number of frames to be displayed and damage done of this Attack
    private int player, x, y, framesToBeDisplayed, damage;
    
    //Angle this attack is directed
    private double direction;
    
    //Graphical representation of this attack
    private Arc2D.Double arc;
    
    //If this attack has already done damage ( Attacks should only do damage once )
    boolean hasDoneDamage;

    /**
        Constructor
        @param int Player number
        @param double Angle of attack
        @param int x location of attack
        @param int y location of attack
        @param int Damage done by attack
    */
    public MeleeAttack ( int inPlayer, double inTheta, int inX, int inY, int inDamage )
    {

        //Calling Action's constructor
        super( GameConstants.HIGHEST_PRIORITY, inPlayer );

        //Assigning various boring things
        this.x = inX;
        this.y = inY;
        this.direction = inTheta;
        this.framesToBeDisplayed = 5;
        this.damage = inDamage;
        this.hasDoneDamage = false;

        //Creating this attack's visual arc
        this.arc = new Arc2D.Double( 0, 0, 64, 64, ( inTheta * 180 ) / Math.PI - 45, 90, Arc2D.PIE );

    }

    /**
        Inform this attack that it has been displayed for one frame, decriminents number of frames to continue displaying it
    */
    public void displayedForOneFrame() { this.framesToBeDisplayed--; }

    /**
        Checks if this attack intersects with a rectangle at the given coordinates
        @param int X location of object
        @param int Y location of object
        @param int width of object
        @param int height of object
    */
    public boolean intersects ( int inX, int inY, int inWidth, int inHeight )
    {
        
        //Getting difference between location of this attack and the given object
        int deltaX = this.x - inX,
            deltaY = this.y - inY;
        
        //Getting angle between this attack and the given location
        double theta = -Math.atan( deltaY / ( double ) deltaX ) + Math.PI;

        //Adjust angle, because the atan function is nasty.
        if ( theta > Math.PI && deltaX < 0 ) { theta -= Math.PI; }
        
        //If given object falls within the dimension of this attack, and within 45 degrees of the angle of attack on either side,
        //the object and this attack intersect. Return true.
        if ( inX + inWidth - this.x < 64 &&
                inY + inHeight - this.y < 64 &&
                inX + inWidth - this.x > -32 &&
                inY + inHeight - this.y > -32 &&
                theta > this.direction - ( Math.PI / 4 ) &&
                theta < this.direction + ( Math.PI / 4 ) ) { return true; }
            
        //Otherwise, they do not intersect. Return false.
        return false;

    }
    
    /**
        Set this attack as having done damage.
    */
    public void damageDone () { this.hasDoneDamage = true; }
    
    /**
        Gets the x location of this attack
        @return int x location of this attack
    */
    public int getX () { return this.x; }
    
    /**
        Gets the y location of this attack
        @return int y location of this attack
    */
    public int getY () { return this.y; }
    
    /**
        Gets the number of frames this attack has left to display
        @return int number of frames remaining to display
    */
    public int getFramesToBeDisplayed () { return this.framesToBeDisplayed; }
    
    /**
        Gets the damage done by this attack
        @return int damage done by this attack
    */
    public int getDamage () { return this.damage; }
    
    /**
        Gets the direction this attack is directed in
        @return double direction of this attack
    */
    public double getDirection () { return this.direction; }
    
    /**
        Gets the visual arc of this attack
        @return Arc2D.Double Visual representation of this attack
    */
    public Arc2D.Double getArc () { return this.arc; }
    
    /**
        Checks if this attack has previously done damage
        @return boolean if this attack has done damage
    */
    public boolean hasDoneDamage () { return this.hasDoneDamage; }
    
    /**
        Converts this attack into a String
        @return String A String representation of this attack
    */
    public String toString () { return "ATTACK MELEE " + super.getPlayer() + " " + this.x + " " + this.y + " " + this.direction + " " + this.damage; }

}