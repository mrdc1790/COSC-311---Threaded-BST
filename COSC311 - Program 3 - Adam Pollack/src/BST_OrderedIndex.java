public class BST_OrderedIndex {
    private int numElems;
    private BST_IndexRecord head, tail, rover;

    //sets up the LinkedList with a front and back
    public BST_OrderedIndex(){
        head = null;
        tail = null;
        rover = null;
        numElems = 0;
    }

    //used by the Node to get the front value
    public BST_IndexRecord getRover(){
        return rover;
    }

    //used to determine whether the LinkedList is empty
    public boolean isEmpty(){
        return (numElems == 0);
    }

    //used to add the record in their respective placement
    public void addRecord(BST_IndexRecord record) {
        if (this.isEmpty()) {
            head = record;
            tail = record;
            rover = record;
        } else {
            BST_IndexRecord currentRecord = rover;
            boolean recordToAdd = false;
            while (!recordToAdd) {
                if (currentRecord.compareTo(record) < 0) {
                    if (currentRecord.isRightAThread()) {
                        if (currentRecord.isTheTail()) {
                            tail = record;
                            currentRecord.setRightChild(record);
                            record.setRightChildAsThread(currentRecord.getRightChild());
                            record.setLeftChildAsThread(currentRecord);
                            recordToAdd = true;
                        } else {
                            record.setRightChildAsThread(currentRecord.getRightChild());
                            currentRecord.setRightChild(record);
                            record.setLeftChildAsThread(currentRecord);
                            recordToAdd = true;

                        }
                    } else {
                        currentRecord = currentRecord.getRightChild();
                    }
                } else {
                    if (currentRecord.isLeftAThread()) {
                        if (currentRecord.isTheHead()) {
                            head = record;
                            currentRecord.setLeftChild(record);
                            record.setLeftChildAsThread(record);
                            record.setRightChildAsThread(currentRecord);
                            recordToAdd = true;
                        } else {
                            record.setLeftChildAsThread(currentRecord.getLeftChild());
                            currentRecord.setLeftChild(record);
                            record.setRightChildAsThread(currentRecord);
                            recordToAdd = true;
                        }
                    } else {
                        currentRecord = currentRecord.getLeftChild();
                    }
                }
            }
        }
        numElems++;
    }

    public void deleteRecord_CaseA(String dataToDelete) {
        BST_IndexRecord record = find(dataToDelete);
        BST_IndexRecord parentNode = findPrevious(dataToDelete);
        if (parentNode.getLeftChild().getKey().toUpperCase().equals(dataToDelete.toUpperCase())) {
            parentNode.isLeftAThread = true;
            parentNode.leftChild = null;
        } else if (!record.getRightChild().isLeftAThread() && record.equals(rover.getRightChild().getLeftChild())) {
            parentNode = record.getRightChild();
            parentNode.setLeftChildAsThread(parentNode);
        } else {
            System.err.println("Error: Delete() in BST_OrderedIndex for hasNoChildren");
        }
        if (record.isTheHead()) head = parentNode;
        if (record.isTheTail()) tail = parentNode;
    }

    public void deleteRecord_CaseB(String dataToDelete) {
        BST_IndexRecord record = find(dataToDelete);
        BST_IndexRecord parentNode = findPrevious(dataToDelete);
    }

    // used to delete an entry from the LinkedList
    public void deleteRecord(String dataToDelete) {
        if (numElems == 0) ;
        else if (numElems == 1) {
            if (rover.getKey().equals(dataToDelete)) {
                head = null;
                tail = null;
                rover = null;
            }
        } else {
            BST_IndexRecord rightTraversalNode = null;
            BST_IndexRecord record = find(dataToDelete);
            if (record == null) return;
            if (record.hasNoChildren()) {
                deleteRecord_CaseA(dataToDelete);
            } else if ((record.hasRightChild() || record.hasLeftChild()) && !record.hasTwoChildren()) {
                deleteRecord_CaseB(dataToDelete);
            }
                /*
                else if (record.hasTwoChildren()) { ///CODE TO FIX
                rightTraversalNode = record.getRightChild();
                BST_IndexRecord next = rightTraversalNode;
                BST_IndexRecord leftTraversalNode = record.getLeftChild();

                while (!rightTraversalNode.isRightAThread()) rightTraversalNode = rightTraversalNode.getRightChild();
                while (!leftTraversalNode.isLeftAThread()) leftTraversalNode = leftTraversalNode.getLeftChild();
                while (!next.isLeftAThread()) next = next.getLeftChild();
                boolean parentNodeCheck = false;
                while (!parentNodeCheck) {
                    if (leftTraversalNode.isLeftAThread() && !leftTraversalNode.getLeftChild().isRightAThread() && record.equals(leftTraversalNode.getLeftChild().getRightChild())) {
                        //record is rightChild of parentNode
                        parentNode = leftTraversalNode.getLeftChild();
                        parentNode.setRightChild(next);
                        next.setLeftChild(record.getLeftChild()); //A
                        //if statement on if children have children; check threads, repoint threads
                        record.getLeftChild().setRightChildAsThread(next);//B
                        parentNodeCheck = true;
                    } else if (rightTraversalNode.isRightAThread() && !rightTraversalNode.getRightChild().isLeftAThread() && record.equals(rightTraversalNode.getRightChild().getLeftChild())) {
                        //record is leftChild of parentNode
                        parentNode = rightTraversalNode.getRightChild();
                        parentNode.setLeftChild(next);
                        next.setLeftChild(record.getLeftChild());//A
                        //if statement on if children have children; check threads, repoint threads
                        if (record.getLeftChild().hasRightChild()) {
                            BST_IndexRecord leftNodeToThread = record.getLeftChild().getRightChild();
                            while (!leftNodeToThread.isRightAThread())
                                leftNodeToThread = leftNodeToThread.getRightChild();
                            leftNodeToThread.setRightChildAsThread(next);
                        }
                        if (record.getRightChild().hasLeftChild()) {
                            BST_IndexRecord rightNodeToThread = record.getLeftChild().getRightChild();
                            while (!rightNodeToThread.isLeftAThread())
                                rightNodeToThread = rightNodeToThread.getLeftChild();
                            rightNodeToThread.setLeftChildAsThread(next);
                        }
                        record.getLeftChild().setRightChildAsThread(next);//B
                        parentNodeCheck = true;
                    } else {
                        System.err.println("OrderedIndex delete() hasTwoChildren case");
                        parentNodeCheck = true;
                    }
                }
            } else if (record.hasLeftChild()) {
                rightTraversalNode = record.getLeftChild();
                boolean parentNodeCheck = false;
                while (!parentNodeCheck) {
                    if (rightTraversalNode.isLeftAThread() && !rightTraversalNode.getLeftChild().isRightAThread() && record.equals(rightTraversalNode.getLeftChild().getRightChild())) {
                        //record is rightChild of parentNode
                        parentNode = rightTraversalNode.getLeftChild();
                        parentNode.setRightChild(record.getLeftChild());
                        parentNodeCheck = true;
                    } else if (rightTraversalNode.isRightAThread() && record.equals(rightTraversalNode.getRightChild().getRightChild().getLeftChild())) {
                        //record is leftChild of parentNode
                        parentNode = record.getRightChild();
                        parentNode.setLeftChild(record.getLeftChild());
                        parentNodeCheck = true;
                    } else {
                        rightTraversalNode = rightTraversalNode.getLeftChild();
                    }
                }
                if (record.isTheTail()) {
                    tail = record.getLeftChild();
                    record.getLeftChild().setRightChildAsThread(record.getLeftChild());
                } else record.getLeftChild().setRightChildAsThread(record.getRightChild());
            } else if (record.hasRightChild()) {
                rightTraversalNode = record.getRightChild();
                boolean parentNodeCheck = false;
                while (!parentNodeCheck) {
                    if (rightTraversalNode.isRightAThread() && !rightTraversalNode.getRightChild().isLeftAThread() && record.equals(rightTraversalNode.getRightChild().getLeftChild())) {
                        //record is leftChild of parentNode
                        parentNode = rightTraversalNode.getRightChild();
                        parentNode.setLeftChild(record.getRightChild());
                        parentNodeCheck = true;
                    } else if (rightTraversalNode.isLeftAThread() && !record.getLeftChild().isRightAThread() && record.equals(record.getLeftChild().getRightChild())) {
                        parentNode = record.getLeftChild();
                        parentNode.setRightChild(record.getRightChild());
                        parentNodeCheck = true;
                    } else rightTraversalNode = rightTraversalNode.getRightChild();
                }
                if (record.isTheHead()) {
                    head = record.getRightChild();
                    rightTraversalNode.setLeftChildAsThread(rightTraversalNode);
                } else rightTraversalNode.setLeftChildAsThread(parentNode);
            }
        }*/
            }
            numElems--;
        }

    public static BST_IndexRecord next(BST_IndexRecord ptr){
        if (ptr.isRightAThread() == true) return ptr.rightChild;
        else {
            ptr = ptr.rightChild;
            while (ptr.isLeftAThread() == false) ptr = ptr.leftChild;
        }
        return ptr;
    }

    public BST_IndexRecord find(String dataToFind){
        BST_IndexRecord record = null;
        if (this.isEmpty());
        else{
            BST_IndexRecord currentRecord = rover;
            boolean recordIsFound = false;
            while (!recordIsFound){
                if (currentRecord.getKey().toUpperCase().equals(dataToFind.toUpperCase())){
                    record = currentRecord;
                    recordIsFound = true;
                } else if (currentRecord.getKey().toUpperCase().compareTo(dataToFind.toUpperCase()) < 0){
                    if (currentRecord.isRightAThread()) break;
                    currentRecord = currentRecord.getRightChild();
                } else {
                    if (currentRecord.isLeftAThread()) break;
                    currentRecord = currentRecord.getLeftChild();
                }
            }
        }
        return record;
    }

    public BST_IndexRecord findPrevious(String dataToFind) {
        BST_IndexRecord ptr = null;
        if (this.isEmpty()) ;
        else {
            BST_IndexRecord currentRecord = rover;
            boolean recordIsFound = false;
            while (!recordIsFound) {
                if (currentRecord.getKey().toUpperCase().compareTo(dataToFind.toUpperCase()) < 0) {
                    currentRecord = currentRecord.getRightChild();
                    if ((currentRecord.getLeftChild().isRightAThread() || currentRecord.getLeftChild().isLeftAThread()) && currentRecord.getLeftChild().getKey().toUpperCase().equals(dataToFind.toUpperCase())) {
                        ptr = currentRecord;
                        recordIsFound = true;
                    } else if (currentRecord.getRightChild().isRightAThread() || currentRecord.getRightChild().isLeftAThread()){
                        currentRecord = currentRecord.getRightChild();
                        if ((currentRecord.getLeftChild().getKey().toUpperCase().equals(dataToFind.toUpperCase()) || currentRecord.getRightChild().getKey().toUpperCase().equals((dataToFind.toUpperCase())))){
                            ptr = currentRecord;
                            recordIsFound = true;
                        }
                    }
                } else {
                    if (currentRecord.getKey().compareTo(dataToFind) < 0) {
                        currentRecord = currentRecord.getRightChild();
                        if (currentRecord.getLeftChild().isLeftAThread() && currentRecord.getLeftChild().getKey().toUpperCase().equals(dataToFind.toUpperCase())) {
                            ptr = currentRecord;
                            break;
                        }
                    }
                    currentRecord = currentRecord.getLeftChild();
                    if (currentRecord.getLeftChild().isLeftAThread() && currentRecord.getLeftChild().getKey().toUpperCase().equals(dataToFind.toUpperCase())) {
                        ptr = currentRecord;
                        break;
                    }
                }
            }
        }
        return ptr;
    }

    public boolean search (String dataToSearchFor){
        if (find(dataToSearchFor) == null) return false;
        else return true;
    }

    //used in Database class to dynamically print in increasing value that is specified
    public void printIncreasing(DatabaseRecord[] myDB){
        if (this.isEmpty()){
            System.out.println("Database is empty.");
        } else {
            BST_IndexRecord printedRecord = head;
            BST_IndexRecord traversalRecord;
            while (!printedRecord.isTheTail()){
                System.out.println(myDB[printedRecord.getWhere()]);
                if (printedRecord.isRightAThread()) printedRecord = printedRecord.getRightChild();
                else {
                    traversalRecord = printedRecord.getRightChild();
                    while (!traversalRecord.isLeftAThread()) traversalRecord = traversalRecord.getLeftChild();
                    printedRecord = traversalRecord;
                }
            }
            System.out.println(myDB[printedRecord.getWhere()].toString());
        }
    }

    //used in Database class to dynamically print in decreasing value that is specified
    public void printDecreasing(DatabaseRecord[] myDB){
        if (this.isEmpty()){
            System.out.println("Database is empty.");
        } else {
            BST_IndexRecord printedRecord = tail;
            BST_IndexRecord traversalRecord;
            while (!printedRecord.isTheHead()){
                System.out.println(myDB[printedRecord.getWhere()]);
                if (printedRecord.isLeftAThread()) printedRecord = printedRecord.getLeftChild();
                else {
                    traversalRecord = printedRecord.getLeftChild();
                    while (!traversalRecord.isRightAThread()) traversalRecord = traversalRecord.getRightChild();
                    printedRecord = traversalRecord;
                }
            }
            System.out.println(myDB[printedRecord.getWhere()].toString());
        }
    }
}
