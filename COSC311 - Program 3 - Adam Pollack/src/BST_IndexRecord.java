public class BST_IndexRecord implements Comparable<BST_IndexRecord> {
    public BST_IndexRecord rightChild, leftChild;
    public boolean isRightAThread, isLeftAThread;
    private String key;
    private int where;

    //Parameterized constructor for the Node class
    public BST_IndexRecord(String key, int where){
        rightChild = this;
        leftChild = this;
        isRightAThread = true;
        isLeftAThread = true;
        this.key = key.toLowerCase();
        this.where = where;
    }

    //method to return key to compare in later methods
    public String getKey(){
        return key;
    }

    //method to return the placement within the LinkedList Index for that particular field
    public int getWhere(){
        return where;
    }

    //method to return the next node placement
    public BST_IndexRecord getRightChild(){
        return rightChild;
    }

    //method to return the previous node placement
    public BST_IndexRecord getLeftChild(){
        return leftChild;
    }

    //method to alter the next node value
    public void setRightChildAsThread(BST_IndexRecord record){
        rightChild = record;
        isRightAThread = true;
    }

    //method to alter the previous node value
    public void setLeftChildAsThread(BST_IndexRecord record){
        leftChild = record;
        isLeftAThread = true;
    }

    public void setRightChild(BST_IndexRecord record) {
        rightChild = record;
        isRightAThread = false;
    }

    public void setLeftChild(BST_IndexRecord record) {
        leftChild = record;
        isLeftAThread = false;
    }

    //checks whether the next is at end
    public boolean isRightAThread(){
        return isRightAThread;
    }

    //checks whether the previous is at the beginning
    public boolean isLeftAThread(){
        return isLeftAThread;
    }

    public boolean isTheTail(){
        return (rightChild.equals(this) && isRightAThread);
    }

    public boolean isTheHead(){
        return (leftChild.equals(this) && isLeftAThread);
    }

    public boolean hasLeftChild(){
        return (!isLeftAThread);
    }

    public boolean hasRightChild(){
        return (!isRightAThread);
    }

    public boolean hasTwoChildren(){
        return (hasLeftChild() && hasRightChild());
    }

    public boolean hasNoChildren(){
        return (!hasRightChild() && !hasLeftChild());
    }

    //compares values to place correctly
    public int compareTo(BST_IndexRecord record){
        return this.key.compareTo(record.getKey());
    }

    //method to return a sentence containing the required data (key/where)
    public String toString(){
        return key + " " + where;
    }
}
