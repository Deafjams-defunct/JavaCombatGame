import java.util.ArrayList;
import java.util.Iterator;

/**
    @author: Dylan Foster
    @version: 12/14/12
    @description: Modfified minheap code from book. Changed to max heap, all elements stored are Actions, added remove( Action ) and contains( Action ). M
*/
public class MaxHeap
{
    
    private ArrayList<Action> elements;
    
    public MaxHeap()
    {
    
        elements = new ArrayList<Action>();
        elements.add( null );
    
    }

    /**
        Adds a new element to this heap.
        @param newElement the element to add
    */
    public void add ( Action newElement )
    {
        
        // Add a new leaf
        elements.add( null );
        int index = elements.size() - 1;

        // Demote parents that are larger than the new element
        while ( index > 1 && getParent( index ).compareTo( newElement ) < 0 )
        {
            
            elements.set( index, getParent( index ) );
            index = getParentIndex( index );
            
        }

        // Store the new element into the vacant slot
        elements.set( index, newElement );
        
    }

    /**
    Gets the minimum element stored in this heap.
    @return the minimum element
    */
    public Action peek()
    {

        if ( elements.size() > 1 )
        {

            return elements.get(1);

        }

        return null;

    }

    /**
    Removes the minimum element from this heap.
    @return the minimum element
    */
    public Action remove()
    {

        if ( elements.size() > 1 )
        {

            Action maximum = elements.get(1);

            // Remove last element
            int lastIndex = elements.size() - 1;
            Action last = elements.remove(lastIndex);

            if ( lastIndex > 1 )
            {
                elements.set(1, last);
                fixHeap();
            }

            return maximum;

        }

        return null;

    }

    /**
        Removes the given element from the heap.
        @param Action Action to remove
    */
    public void remove ( Action inAction )
    {
            
        if ( elements.size() > 1 )
        {
        
            //Using Iterator to avoid concurrent access issues
            Iterator i = this.elements.iterator();

            //Iterates through elements in ArrayList
            while ( i.hasNext() )
            {
            
                Object element = i.next();
                
                //Checks type of given and element in ArrayList Action. If they are equal, remove given element from heap.
                if ( ( inAction instanceof Move && element instanceof Move && ( ( Move ) inAction ).equals( ( Move ) element ) ) ||
                    ( inAction instanceof HealthChange && element instanceof HealthChange && ( ( HealthChange ) inAction ).equals( ( HealthChange ) element ) ) )
                {
                    
                    //Adjusts "last element" in heap
                    int lastIndex = elements.size() - 1;
                    
                    //Removes element from heap.
                    i.remove();

                }

            }

        }
        
        //Fixes the heap.
        if ( this.elements.size() > 2 ) { this.fixHeap(); }

    }

    /**
        Checks if this heap contains an Action
        @param Action Action to check against heap
        @return boolean Heap contains given Action
    */
    public boolean contains ( Action inAction )
    {
        
        //Cycling through all elements in the heap.
        for ( Action e : this.elements )
        {

            //If given and array's Action are Moves.
            if ( inAction instanceof Move && e instanceof Move )
            {
                
                //Check if movements are equal
                if ( ( ( Move ) inAction ).equals( ( Move ) e ) ) { return true; }


            }
            //If given and arrat's Actions are HealthChanges
            else if ( inAction instanceof HealthChange && e instanceof HealthChange )
            {
            
                //Check if health changes are equal
                if ( ( ( HealthChange ) inAction ).equals( ( HealthChange ) e ) ) { return true; }
            
            }

        }
        
        return false;

    }

    /**
    Turns the tree back into a heap, provided only the root
    node violates the heap condition.
    */
    private void fixHeap()
    {
        
        Action root = elements.get( 1 );

        int lastIndex = elements.size() - 1;
        // Promote children of removed root while they are larger than last

        int index = 1;
        boolean more = true;
        while (more )
        {
            int childIndex = getLeftChildIndex(index);
            if ( childIndex <= lastIndex )
            {
                // Get smaller child

                // Get left child first
                Action child = getLeftChild( index );

                // Use right child instead if it is smaller
                if ( getRightChildIndex( index ) <= lastIndex && getRightChild( index ).compareTo( child ) > 0 )
                {
                    
                    childIndex = getRightChildIndex( index) ;
                    child = getRightChild( index );
                    
                }

                // Check if larger child is smaller than root
                if ( child.compareTo( root ) > 0 )
                {
                    
                    // Promote child
                    elements.set( index, child );
                    index = childIndex;
                    
                }
                else
                {
                
                    // Root is smaller than both children
                    more = false;
                
                }
            
            }
            else
            {
            
                // No children
                more = false;
            
            }
        
        }

        // Store root element in vacant slot
        elements.set( index, root );
    }

    /**
        Returns the number of elements in this heap.
        @return int number of elementsin this heap.
    */
    public int size()
    {
        
        return elements.size() - 1;
        
    }

    /**
    Returns the index of the left child.
    @param index the index of a node in this heap
    @return the index of the left child of the given node
    */
    private static int getLeftChildIndex( int index )
    {
        
        return 2 * index;
        
    }

    /**
    Returns the index of the right child.
    @param index the index of a node in this heap
    @return the index of the right child of the given node
    */
    private static int getRightChildIndex( int index )
    {
        
        return 2 * index + 1;
        
    }

    /**
    Returns the index of the parent.
    @param index the index of a node in this heap
    @return the index of the parent of the given node
    */
    private static int getParentIndex( int index )
    {
        
        return index / 2;
        
    }

    /**
    Returns the value of the left child.
    @param index the index of a node in this heap
    @return the value of the left child of the given node
    */
    private Action getLeftChild( int index )
    {
        return elements.get( 2 * index );
    }

    /**
    Returns the value of the right child.
    @param index the index of a node in this heap
    @return the value of the right child of the given node
    */
    private Action getRightChild( int index )
    {
        return elements.get( 2 * index + 1 );
    }

    /**
    Returns the value of the parent.
    @param index the index of a node in this heap
    @return the value of the parent of the given node
    */
    private Action getParent( int index )
    {
        
        return elements.get( index / 2 );
        
    }

}
