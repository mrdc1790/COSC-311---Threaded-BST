public class DeletedIndex {
    private int numElems;
    private int[] deletedIndexArray;

    public DeletedIndex(int size){
        deletedIndexArray = new int[size];
        numElems = 0;
    }

    public void addIndex (int index){
        deletedIndexArray[numElems] = index;
        numElems++;
    }

    public int getIndex(){
        if (this.isEmpty()) return -1;
        else {
            numElems--;
            return deletedIndexArray[numElems];
        }
    }
    public boolean isEmpty(){ return (numElems == 0); }
}
