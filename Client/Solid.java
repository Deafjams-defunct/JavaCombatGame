/**
@author: Dylan Foster
@version: 12/14/12
@desciption: Interface, requirements for an object being considered "solid" on the game field.
*/

public interface Solid
{

	public boolean willCollide ( int inX, int inY, int inWidth, int inHeight );

}