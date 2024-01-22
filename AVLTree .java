
 /*
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info.
 *
 */

public class AVLTree {

    AVLNode root;
    AVLNode min = null; // The node with the minimum key
    AVLNode max = null; // The node with the maximum key

    public AVLTree() {
        this.root = new AVLNode();
    }

    /**
     * private AVLTree(AVLNode root)
     * <p>
     * Private constructor that gets a node and initiates a tree with the node as the root.
     * pre:root.getParent=null
     * pre: root.isRealNode && root != null
     */
    private AVLTree(AVLNode root) {
        this.root = root;
        this.min = minNode(root);
        this.max = maxNode(root);
    }

    /**
     * public boolean empty()
     * <p>
     * Returns true if and only if the tree is empty.
     */
    public boolean empty() {
        if(this == null)
            return true;
        return !root.isRealNode();
    }

    /**
     * public String search(int k)
     * <p>
     * Returns the info of an item with key k if it exists in the tree.
     * otherwise, returns null.
     */
    public String search(int k) {
        return searchRec(this.root, k);  
    }

    /**
     *  public String searchRec(AVLNode root, int k)
     * <p>
     * Recursive function that is used by search in order to conduct the actual search.
     */
    public String searchRec(AVLNode root, int k) {
        if( root.getKey() == -1)
            return null;
        if (root.getKey() == k)
            return root.getValue();
        if(root.getKey()<k)
            return searchRec((AVLNode) root.getRight(), k);
        return searchRec((AVLNode) root.getLeft(), k);

    }
    /**
     * treePosition(int k, AVLNode x)
     * <p>
     * Look for k in the subtree of x.
     * Returns the last node encountered.
     * Returns virtual node if the tree is empty.
     * Used to find insertion points.
     */
    public AVLNode treePosition(int k, AVLNode x) {
        if (x.getKey() == -1) // If the tree is empty
            return new AVLNode();
        AVLNode temp = x;
        while(temp.getKey() != -1){
            if(k == temp.getKey())
                return temp;
            else if(temp.getKey() < k)
                temp = (AVLNode) temp.getRight();
            else
                temp = (AVLNode) temp.getLeft();
        }
        return (AVLNode)temp.getParent();
    }
    /**
     * public int RotateRight(AVLNode x)
     * <p>
     * Preforms a Right rotation.
     * pre: x is not null & x is not virtual Node
     */
    public void rotateRight(AVLNode x) {
        AVLNode x_parent=(AVLNode)x.getParent();
        boolean is_x_right_child=false;
        if(x_parent != null)
        {
            is_x_right_child=isRightChild(x_parent,x);
        }
        AVLNode y=(AVLNode)x.getLeft();
        AVLNode yRight=(AVLNode)y.getRight();
        y.setRight(x);
        x.setLeft(yRight);
        y.setParent(null);
        if(x_parent != null)
        {
            if(is_x_right_child)
                x_parent.setRight(y);
            else
                x_parent.setLeft(y);
        }
        //Update heights
        updateHeight(x);
        updateHeight(y);
        //Update sizes
        x.setSize(((AVLNode)x.getLeft()).getSize()+((AVLNode)x.getRight()).getSize() + 1);
        y.setSize(((AVLNode)y.getLeft()).getSize()+((AVLNode)y.getRight()).getSize() + 1);
    }
    /**
     * public int RotateLeft(AVLNode y)
     * <p>
     * Preforms Left rotation.
     * pre: y is not null & y is not virtual Node
     */
    public void rotateLeft(AVLNode y) {

        AVLNode y_parent=(AVLNode)y.getParent();
        boolean is_y_right_child=false;
        if(y_parent != null)
        {
            is_y_right_child=isRightChild(y_parent,y);
        }
        AVLNode x=(AVLNode) y.getRight();
        AVLNode xLeft=(AVLNode)x.getLeft();
        x.setLeft(y);
        y.setRight(xLeft);
        x.setParent(null);
        if(y_parent != null)
        {
            if(is_y_right_child)
                y_parent.setRight(x);
            else
                y_parent.setLeft(x);
        }
        //Update heights
        updateHeight(y);
        updateHeight(x);
        //Update sizes
        y.setSize(((AVLNode)y.getLeft()).getSize()+((AVLNode)y.getRight()).getSize() + 1);
        x.setSize(((AVLNode)x.getLeft()).getSize()+((AVLNode)x.getRight()).getSize() + 1);
    }
    /**
     * protected void rebalance(AVLNode x)
     * <p>
     * Rebalancing an unbalanced AVLTree and returns the number of rebalancing operations as learned in  class.
     */
    protected int rebalance(AVLNode x)
    {
        if(getBF(x) == 2)
        {
            int leftBF=getBF((AVLNode) x.getLeft());
            if(leftBF == 1)
            {
                rotateRight(x);
                return 2;
            }
            else if(leftBF == 0)
            {
                rotateRight(x);
                return 3;
            }
            else if(leftBF == -1)
            {
                rotateLeft((AVLNode) x.getLeft());
                rotateRight(x);
                return 5;
            }
        }
        else if(getBF(x) == -2)
        {
            int rightBF = getBF((AVLNode) x.getRight());
            if(rightBF == -1)
            {
                rotateLeft(x);
                return 2;
            }
            else if(rightBF == 0)
            {
                rotateLeft(x);
                return 3;
            }

            else if(rightBF == 1)
            {
                rotateRight((AVLNode) x.getRight());
                rotateLeft(x);
                return 5;
            }
        }
        return 0;
    }
    /**
     * public int insert(int k, String i)
     * <p>
     * Inserts an item with key k and info i to the AVL tree.
     * The tree must remain valid, i.e. keep its invariants.
     * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
     * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
     * Returns -1 if an item with key k already exists in the tree.
     */
    
    public int insert(int k, String i) {
        
        AVLNode toInsert = new AVLNode(k, i);
        return insert(toInsert);
    }
    private int insert(AVLNode toInsert)
    {
    	boolean wasInserted = false;
        if(empty())
        {
            this.root = toInsert;
            this.min = this.root; // update min field
            this.max = this.root; // update max field
            this.root.setSize(1); // update size
            return 0;
        }
        else
        {
            wasInserted =  treeInsert(toInsert); // checks if node with key k already exist and if not inserts it
            // size gets updated inside treeInsert function.
        }
        if (!wasInserted) // if the key k was already in the tree
            return -1;
        if(toInsert.getKey() > this.max.getKey()) // updates max field if needed
            this.max = toInsert;
        if(toInsert.getKey() < this.min.getKey()) // updates min field if needed
            this.min = toInsert;

        int count_rebalance = 0; // the number of rebalance operations
        AVLNode y= (AVLNode)toInsert.getParent();
        while(y != null)
        {
            int previous_height = y.getHeight();
            int current_height = Math.max(y.getRight().getHeight(), y.getLeft().getHeight())+1;
            if((previous_height - y.getRight().getHeight() == 0 && previous_height - y.getLeft().getHeight() == 1)
              ||(previous_height - y.getLeft().getHeight() ==0 ) && previous_height - y.getRight().getHeight() == 1)
            // count promote operations that doesn't include rotations
            {
                count_rebalance += 1;
            }
            y.setHeight(current_height); //update height
            int curr_count = rebalance(y); //if rebalance was performed --> curr_count>0 
            count_rebalance += curr_count;
            // if(current_height == previous_height)
            if(curr_count > 0) // if rebalance was performed- update the remaining nodes until the root
            {
                updateHeights(y);
                break;
            }
            y = (AVLNode) y.getParent();
        }
        AVLNode find_root = this.root; 
        while(find_root != null) //update the new root 
        {
            this.root = find_root;
            find_root = (AVLNode) find_root.getParent();
        }
        return count_rebalance;
    }
    
    /**
     * protected boolean treeInsert(AVLNode x)
     * <p>
     * Inserts the node to the tree.
     * Returns true if the node was inserted, else returns false(it was existed already);
     * <p>
     */
    protected boolean treeInsert( AVLNode toInsert) {
    	int k=toInsert.getKey();
        AVLNode y = treePosition(k,this.root);
        if(y.getKey() == k) // if the key already exists 
        {
            return false;
        }
        else if (y.getKey() < k)
            y.setRight(toInsert);
        else if ((y.getKey() > k))
            y.setLeft(toInsert);

        updateSizes(toInsert); //update size fields
        return true;
    }


    /**
     * public int delete(int k)
     * <p>
     * Deletes an item with key k from the binary tree, if it is there.
     * The tree must remain valid, i.e. keep its invariants.
     * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
     * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
     * Returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) {
        AVLNode rebalance_start;
        AVLNode to_delete=treePosition(k,this.root);
        if(!to_delete.isRealNode())
            return -1;
        if(to_delete.getKey() != k) // the node doesnt exist
            return -1;
        if(size()==1) // deleting the last node
        {
            this.root= new AVLNode();
            this.max=null; // update max field
            this.min=null; // update min field
            return 0;
        }

        boolean need_to_change_min = false; // true= this.min need to be updated
        boolean need_to_change_max = false;  // true= this.max need to be updated
        if(to_delete.getKey() == this.max.getKey()) //checks if we delete max node
            need_to_change_max = true;
        if(to_delete.getKey() == this.min.getKey()) // checks if we delete min node
            need_to_change_min = true;


        if(to_delete.getRight().getKey() == -1 && to_delete.getLeft().getKey() == -1) // deleting a leaf
        {
            rebalance_start = (AVLNode) to_delete.getParent();

            if(rebalance_start.getRight() == to_delete) // if we delete a right son
                rebalance_start.setRight(new AVLNode());
            else // deleting a left son
                rebalance_start.setLeft(new AVLNode());
            to_delete.setParent(null);
        }
        else if(to_delete.getRight().getKey() == -1 || to_delete.getLeft().getKey() == -1) // deleting an unary node.
        {
            AVLNode child_of_to_delete;
            if(to_delete.getRight().isRealNode()) //check if the node we delete has right\left son and save a pointer to him
                child_of_to_delete=(AVLNode)to_delete.getRight();
            else
                child_of_to_delete=(AVLNode)to_delete.getLeft();
          //disconnect the node we deleting and replace it with his son:
            child_of_to_delete.setParent(null); 
            to_delete.setRight(new AVLNode());
            to_delete.setLeft(new AVLNode());
            if(to_delete.getParent() != null)
            {
                if(isRightChild((AVLNode)to_delete.getParent(), to_delete))
                    to_delete.getParent().setRight(child_of_to_delete);
                else
                    to_delete.getParent().setLeft(child_of_to_delete);
                to_delete.setParent(null);
            }

            rebalance_start=child_of_to_delete;
        }
        else // deleting node with 2 son's
        {
            AVLNode succ = successor(to_delete);
            rebalance_start = (AVLNode) succ.getParent();
            if(rebalance_start == to_delete) //the father of the successor is the node we delete
                rebalance_start = succ;
            AVLNode father_succ = (AVLNode) succ.getParent();
            //disconnect the successor:
            if(isRightChild(father_succ,succ)) // successor is right son
                father_succ.setRight(succ.getRight());
            else // successor is right son
                father_succ.setLeft(succ.getRight());
            succ.setParent(null);
            succ.setRight(new AVLNode());
            succ.setLeft(new AVLNode());
            //connect the successor :    
            AVLNode pointer_father = (AVLNode) to_delete.getParent();
            if(pointer_father!=null)
            {
                if(isRightChild(pointer_father,to_delete)) //deleting right child
                    pointer_father.setRight(succ);
                else
                    pointer_father.setLeft(succ);
            }
            succ.setLeft(to_delete.getLeft());
            succ.setRight(to_delete.getRight());
            // disconnect to_delete:
            to_delete.setParent(null);
            to_delete.setRight(new AVLNode());
            to_delete.setLeft(new AVLNode());

            if(pointer_father == null)//deleting the root
            {
                succ.setParent(null);
            }
        }
        updateSizes(rebalance_start); // update sizes
        int count_rebalance = 0;
        int previous_height;
        int current_height;
        AVLNode find_root=rebalance_start;
        while(rebalance_start != null) // going up and rablancing/ updating height if needed
        {
        	find_root=rebalance_start; // update the root 
            previous_height = rebalance_start.getHeight();
            current_height =Math.max(rebalance_start.getRight().getHeight(), rebalance_start.getLeft().getHeight()) + 1;
            if(previous_height-rebalance_start.getRight().getHeight() == 2 &&
                    previous_height - rebalance_start.getLeft().getHeight() == 2) // demote without rotation
                count_rebalance++;
            rebalance_start.setHeight(current_height); //update height
            count_rebalance += rebalance(rebalance_start);
            rebalance_start = (AVLNode) rebalance_start.getParent();
        }
        this.root = find_root; // update root

        if(need_to_change_max) // update max field if needed
        {
            this.max = maxNode(this.root);
        }
        if(need_to_change_min) // update min field if needed
        {
            this.min = minNode(this.root);
        }

        return count_rebalance;
    }

    /**
     * public String min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty.
     */
    public String min() {
        if (!this.root.isRealNode())
            return null;
        return this.min.value;
    }
    /**
     * protected AVLNode minNode(AVLNode x)
     * <p>
     * Returns the min node in the x's sub-tree;
     * <p>
     * precondition: x is not virtual and not null.
     * postcondition: none
     */
    protected AVLNode minNode(AVLNode x) {
        if(x.getLeft().getKey() == -1)
            return x;
        else
            return minNode((AVLNode) x.getLeft());
    }
    /**
     * protected AVLNode maxNode(AVLNode x)
     * <p>
     * Returns the max node in the x's sub-tree;
     * <p>
     * precondition: x is not virtual and not null.
     * postcondition: none
     */

    protected AVLNode maxNode(AVLNode x)
    {
        if(!x.getRight().isRealNode())
            return x;
        return maxNode((AVLNode)x.getRight());
    }
    /**
     * public String max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty.
     */
    public String max() {
        if (!this.root.isRealNode())
            return null;
        return this.max.getValue();
    }
    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        int[] keysArr = new int[this.size()];
        keysToArrayRec(keysArr,new int[1],this.root);
        return keysArr;
    }

    public void keysToArrayRec(int [] keysArr, int[] pointer, AVLNode x) {
        if(x.isRealNode()){
            keysToArrayRec(keysArr, pointer,(AVLNode)x.getLeft());
            keysArr[pointer[0]] = x.getKey();
            pointer[0]++;
            keysToArrayRec(keysArr,pointer, (AVLNode)x.getRight());
        }
    }
    /**
     * public String[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() {
        String[] infoArr = new String[this.size()];
        infoToArrayRec(infoArr,new int[1],this.root);
        return infoArr;
    }
    public void infoToArrayRec(String [] infoArr, int[] pointer, AVLNode x) {
        if(x.isRealNode()){
            infoToArrayRec(infoArr, pointer,(AVLNode)x.getLeft());
            infoArr[pointer[0]] = x.getValue();
            pointer[0]++;
            infoToArrayRec(infoArr,pointer, (AVLNode)x.getRight());
        }
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     */
    public int size() {
        return this.root.getSize();
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     */
    public IAVLNode getRoot() {
        if(empty())
            return null;
        return this.root;
    }

    /**
     * public AVLTree[] split(int x)
     * <p>
     * splits the tree into 2 trees according to the key x.
     * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
     * <p>
     * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
     * postcondition: none
     */
    public AVLTree[] split(int x) {
        AVLNode x_node = treePosition(x,(AVLNode) this.getRoot());
        AVLTree bigger_then_x = new AVLTree();
        AVLTree smaller_then_x = new AVLTree();
        if(x_node.getRight().isRealNode())
            bigger_then_x = new AVLTree((AVLNode) x_node.getRight()); //min,max is updated in the constructor
        if(x_node.getLeft().isRealNode())
            smaller_then_x = new AVLTree((AVLNode) x_node.getLeft());
        if(smaller_then_x.getRoot() != null)
            smaller_then_x.getRoot().setParent(null);//disconnect the sub-trees from x
        if(bigger_then_x.getRoot() != null)
            bigger_then_x.getRoot().setParent(null);
        x_node.setRight(new AVLNode());
        x_node.setLeft(new AVLNode());
        AVLTree[] result= new AVLTree[2];
        if(x_node.getParent() == null) //x is root
        {
            result[0] = smaller_then_x; result[1] = bigger_then_x;
            return result;
        }
        AVLNode curr_pointer = (AVLNode) x_node.getParent();
        boolean curr_came_from_right = isRightChild(curr_pointer,x_node);
        if(curr_came_from_right)
            curr_pointer.setRight(new AVLNode());
        else
            curr_pointer.setLeft(new AVLNode());
        x_node.setParent(null);
        boolean next_came_from_right=false;
        while(curr_pointer != null) // go up in order to the needed join operations
        {
            AVLNode next_pointer=(AVLNode) curr_pointer.getParent();
            if(next_pointer != null)
                next_came_from_right = isRightChild(next_pointer,curr_pointer);
            AVLNode curr_right = (AVLNode) curr_pointer.getRight();
            AVLNode curr_left = (AVLNode) curr_pointer.getLeft();
            curr_pointer.setRight(new AVLNode());
            curr_pointer.setLeft(new AVLNode());
            curr_right.setParent(null);
            curr_left.setParent(null);
            curr_pointer.setParent(null);
            if(next_pointer != null && next_came_from_right )
                next_pointer.setRight(new AVLNode());
            else if(next_pointer != null)
                next_pointer.setLeft(new AVLNode());
            curr_pointer.setSize(1); // update size after disconnecting
            curr_pointer.setHeight(0);
            AVLTree curr_left_tree = new AVLTree();
            AVLTree curr_right_tree = new AVLTree();
            if(curr_came_from_right)
            {
                if(curr_left.isRealNode())
                    curr_left_tree = new AVLTree(curr_left);
                smaller_then_x.join(curr_pointer,curr_left_tree); //min,max,sizes fields updates inside join
            }
            else
            {
                if(curr_right.isRealNode())
                    curr_right_tree = new AVLTree(curr_right);
                bigger_then_x.join(curr_pointer,curr_right_tree);//min,max,sizes fields updates inside join
            }

            curr_pointer = next_pointer;
            curr_came_from_right = next_came_from_right;
        }
        result[0] = smaller_then_x;
        result[1] = bigger_then_x;
        return result;
    }
    /**
     *protected boolean isRightChild(AVLNode parent, AVLNode child)
     * @pre parent!=null
     *
     * @return parent.getRight() == child
     */
    protected boolean isRightChild(AVLNode parent, AVLNode child)
    {
        return parent.getRight() == child;
    }
    /**
     * public int join(IAVLNode x, AVLTree t)
     * <p>
     * joins t and x with the tree.
     * Returns the complexity of the operation (|tree.height - t.height| + 1).
     * <p>
     * precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (height = -1).
     * postcondition: none
     */
    public int join(IAVLNode x, AVLTree t) {
        int result;
        if(!x.isRealNode())
            return 0;
        if(x.getParent() != null) // if x has parent- disconnect them
        {
            if(isRightChild((AVLNode)x.getParent(),(AVLNode)x))
                x.getParent().setRight(new AVLNode());
            else
                x.getParent().setLeft(new AVLNode());
            x.setParent(null);
        }
        // disconnect x from his sub trees
        x.getRight().setParent(null);
        x.getLeft().setParent(null);
        x.setRight(new AVLNode());
        x.setLeft(new AVLNode());
        x.setHeight(0);
        x.setSize(1);
        
        if (t.empty() && !this.empty()) //t is empty and this isn't
        {
            result=root.getHeight() + 2;
            insert((AVLNode)x); //max,min,sizes field updates inside insert
            return result;
        }
        else if (this.empty() && !t.empty()) //this is empty and t isn't
        {
            result = t.getRoot().getHeight() + 2;
            t.insert((AVLNode)x); //t's max,min,sizes fields updates inside insert
            this.root = (AVLNode) t.getRoot();
            this.max = t.max; // update max field
            this.min = t.min; // update min field

            return result;
        }
        else if(this.empty() && t.empty()) // both trees empty
        {
            this.root = (AVLNode) x;
            this.max = this.root ; // update max field
            this.min = this.root ; // update min field
            return 1;
        }
        AVLTree bigger_than_x;
        AVLTree smaller_than_x;
        if (this.getRoot().getKey() > t.getRoot().getKey())
        {
            bigger_than_x = this;
            smaller_than_x = t;
        }
        else
        {
            bigger_than_x = t;
            smaller_than_x = this;
        }
        this.max=bigger_than_x.max; // update min field
        this.min=smaller_than_x.min; // update max field

        result = Math.abs(smaller_than_x.getRoot().getHeight() - bigger_than_x.getRoot().getHeight()) + 1;
        boolean bigger_is_higher = false;
        if(smaller_than_x.getRoot().getHeight() < bigger_than_x.getRoot().getHeight())
            bigger_is_higher=true;

        AVLNode b; //finds the node that will be x's right/left child.
        if(bigger_is_higher)
            b = (AVLNode) bigger_than_x.getRoot();
        else
            b = (AVLNode) smaller_than_x.getRoot();
        while(b.getHeight()>Integer.min(smaller_than_x.getRoot().getHeight(),bigger_than_x.getRoot().getHeight()))
        {
            if(bigger_is_higher)
                b = (AVLNode) b.getLeft();
            else
                b = (AVLNode) b.getRight();
        }
        AVLNode c = (AVLNode) b.getParent();
        
        if(bigger_than_x.getRoot().getHeight() == smaller_than_x.getRoot().getHeight()) // both trees the same height
        {
            x.setRight(bigger_than_x.getRoot());
            x.setLeft(smaller_than_x.getRoot());
        }
        else if(bigger_is_higher) // the tree with the bigger keys is higher
        {
            c.setLeft(x);
            x.setRight(b);
            x.setLeft(smaller_than_x.getRoot());
        }
        else
        {
            c.setRight(x);
            x.setLeft(b);
            x.setRight(bigger_than_x.getRoot());
        }
        updateHeights((AVLNode)x); // updates all the heights from x to the root
        updateSizes((AVLNode)x); // updates all the sizes from x to the root
        while(x != null) //rebalancing
        {
            updateHeight((AVLNode)x);
            rebalance((AVLNode) x);
            x = (AVLNode)x.getParent();
        }
        AVLNode find_root = this.root;
        while(find_root != null) //update root
        {
            this.root = find_root;
            find_root = (AVLNode)find_root.getParent();
        }
        return result;

    }

    /**
     * protected AVLNode successor(AVLNode root)
     * <p>
     * Returns the nodes successor;
     * <p>
     * precondition: x != null && x.isReal
     * postcondition: if the node has no successor(he's the max) --> return null
     */
    protected AVLNode successor(AVLNode x) {

        if(x.getRight().getKey() != -1)
            return minNode((AVLNode) x.getRight());
        AVLNode y = (AVLNode) x.getParent();
        while(y != null && isRightChild(y,x)){
            x = y;
            y = (AVLNode) x.getParent();
        }
        if(y == null) // x is max node in the tree
            return null;

        return y;

    }
    /**
     *  public void updateSizes(AVLNode x)
     * <p>
     * Updates sizes of the tree nodes.
     * pre: x is not a virtual node
     */
    protected void updateSizes(AVLNode x) {
        x.setSize(((AVLNode)x.getRight()).getSize()+((AVLNode)x.getLeft()).getSize()+1);
        AVLNode y = (AVLNode) x.getParent();
        while( y != null)
        {
            y.setSize(((AVLNode)y.getRight()).getSize()+((AVLNode)y.getLeft()).getSize()+1);
            y = (AVLNode) y.getParent();
        }

    }
    /**
     *protected void updateHeights(AVLNode x)
     * <p>
     * Updates heights of the tree nodes.
     * pre: x is not a virtual node.
     */
    protected void updateHeights(AVLNode x)
    {
        x.setHeight(Math.max(x.getLeft().getHeight(),x.getRight().getHeight())+1);
        AVLNode y = (AVLNode) x.getParent();
        while(y != null )
        {
            y.setHeight(Math.max(y.getLeft().getHeight(),y.getRight().getHeight())+1);
            y = (AVLNode)y.getParent();
        }

    }
    /**
     *protected void updateHeight(AVLNode x)
     * <p>
     * Updates height of the tree node.
     */
    protected void updateHeight(AVLNode x)
    {
        x.setHeight(Math.max(x.getLeft().getHeight(),x.getRight().getHeight()) + 1);
    }

    /**
     * protected int getBF(AVLNode x)
     * <p>
     * Returns the node's balance factor.
     */
    protected int getBF(AVLNode x)
    {
        return x.getLeft().getHeight()-x.getRight().getHeight();
    }


    /**
     * public interface IAVLNode
     * ! Do not delete or modify this - otherwise all tests will fail !
     */
    public interface IAVLNode {
        public int getKey(); // Returns node's key (for virtual node return -1).

        public String getValue(); // Returns node's value [info], for virtual node returns null.

        public void setLeft(IAVLNode node); // Sets left child.

        public IAVLNode getLeft(); // Returns left child, if there is no left child returns null.

        public void setRight(IAVLNode node); // Sets right child.

        public IAVLNode getRight(); // Returns right child, if there is no right child return null.

        public void setParent(IAVLNode node); // Sets parent.

        public IAVLNode getParent(); // Returns the parent, if there is no parent return null.

        public boolean isRealNode(); // Returns True if this is a non-virtual AVL node.

        public void setHeight(int height); // Sets the height of the node.

        public int getHeight(); // Returns the height of the node (-1 for virtual nodes).

        public int getSize(); // Returns the size of the node (0 for virtual nodes).
        
        public void setSize(int size); // Sets the size of the node.

    }

    /**
     * public class AVLNode
     * <p>
     * If you wish to implement classes other than AVLTree
     * (for example AVLNode), do it in this file, not in another file.
     * <p>
     * This class can and MUST be modified (It must implement IAVLNode).
     */
    public static class AVLNode implements IAVLNode {
        String value;
        int key, height, size;
        AVLNode left, parent, right;

        public AVLNode() {
            this.key = -1;
            this.value = null;
            this.right = null;
            this.left = null;
            this.parent = null;
            this.height = -1;
            this.size = 0;
        }

        public AVLNode(int key, String value) {
            assert key >= 0;
            this.key = key;
            this.value = value;
            this.setRight(new AVLNode());
            this.setLeft(new AVLNode());
            this.parent = null;
            this.height = 0;
            this.size = 1;
        }

        public int getKey() {
            return this.key;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void setLeft(IAVLNode node) {
            this.left = (AVLNode) node;
            node.setParent(this);
        }

        public IAVLNode getLeft() {
            return this.left;
        }

        public void setRight(IAVLNode node) {
            this.right = (AVLNode) node;
            node.setParent(this);
        }

        public IAVLNode getRight() {
            return this.right;
        }

        public void setParent(IAVLNode node) {
            this.parent = (AVLNode) node;
        }

        public IAVLNode getParent() {
            return this.parent;
        }


        public void setHeight(int height) {
            this.height = height;
        }

        public int getHeight() {
            return this.height;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getSize() {
            return this.size;
        }

        public boolean isRealNode() {
            return this.key != -1;
        }

    }
    }
