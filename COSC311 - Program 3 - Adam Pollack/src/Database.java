import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Database {
    private DatabaseRecord[] myDB;
    private BST_OrderedIndex firstNameIndex, lastNameIndex, IDIndex;
    private DeletedIndex deletedIndex;
    public int numElems;

    //assignment of all the structures of other classes (IndexLinkedList[firstNameIndex, lastNameIndex, IDIndex], DatabaseRecord)
    private final Scanner scnr;

    public Database() {
        this(new Scanner(System.in));
    }

    public Database(Scanner scnr) {
        this.scnr = scnr;
        myDB = new DatabaseRecord[200];
        deletedIndex = new DeletedIndex(200);
        firstNameIndex = new BST_OrderedIndex();
        lastNameIndex = new BST_OrderedIndex();
        IDIndex = new BST_OrderedIndex();
        numElems = 0;

        //try-catch for reading in the dataset, uses a temporary array to hold them
        try {
            File myObj = new File("dataset.txt");
            Scanner file = new Scanner(myObj);
            while (file.hasNextLine()) {
                String[] data = file.nextLine().split(" ");
                this.insert(data[2], data[1], data[0]);
            }
            file.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }
    }

    //used to traverse the Database

    public DatabaseRecord[] getMyDB() { return myDB; }
    public BST_OrderedIndex getFirstNameIndex() { return firstNameIndex; }
    public BST_OrderedIndex getLastNameIndex() { return lastNameIndex; }
    public BST_OrderedIndex getIDIndex() { return IDIndex; }
    public DeletedIndex getDeletedIndex(){ return deletedIndex; }
    public DatabaseRecord getDatabaseRecord(int indexToReturn){ return myDB[indexToReturn]; }
    public int getNumElems() { return numElems; }

    //used to delete an entry
    public int deleteRecord(String idDeleted){
        int indexOfDeletedRecord;
        try {
            //indexOfDeletedRecord = this.find(idDeleted)
            indexOfDeletedRecord = this.find(idDeleted);
            deletedIndex.addIndex(indexOfDeletedRecord);
            DatabaseRecord databaseRecordToDelete = myDB[indexOfDeletedRecord];
            IDIndex.deleteRecord(databaseRecordToDelete.getID());
            firstNameIndex.deleteRecord(databaseRecordToDelete.getFirstName());
            lastNameIndex.deleteRecord(databaseRecordToDelete.getLastName());
            numElems--;
        } catch (Exception e) {
            indexOfDeletedRecord = -1;
        }
        System.out.println(indexOfDeletedRecord);
        return indexOfDeletedRecord;
    }

    //used to search for an entry (using find) and returns true if found
    public boolean search(String IDtoBeFound){
        int YoN = find(IDtoBeFound);
        if (YoN == -1) return false;
        else return true;
    }

    //used by search to compare values to what is being searched
    public int find(String IDtoBeFound){
        if (IDIndex.isEmpty()) return -1;
        String id = formatID(IDtoBeFound);
        if (id == null) return -1;
        BST_IndexRecord target = IDIndex.find(id);
        if (target == null) return -1;
        else return target.getWhere();
    }

    //used to insert in a correct order into their respective LinkedLists
    public void insert (String IDtoBeEntered, String firstName, String lastName){
        firstName = firstName.trim();
        lastName = lastName.trim();
        IDtoBeEntered = formatID(IDtoBeEntered);
        if (IDtoBeEntered == null) {
            System.out.println("Invalid ID. Must be numeric.");
            return;
        }
        if (search(IDtoBeEntered)) {
            System.out.println("The ID " + IDtoBeEntered + " is already in use.");
            return;
        }
        DatabaseRecord record = new DatabaseRecord(IDtoBeEntered, firstName, lastName);
        int indexToInsertAt;
        if (deletedIndex.isEmpty()) indexToInsertAt = numElems;
        else indexToInsertAt = deletedIndex.getIndex();
        myDB[indexToInsertAt] = record;
        firstNameIndex.addRecord(new BST_IndexRecord(firstName, indexToInsertAt));
        lastNameIndex.addRecord(new BST_IndexRecord(lastName, indexToInsertAt));
        IDIndex.addRecord(new BST_IndexRecord(IDtoBeEntered, indexToInsertAt));
        numElems++;
    }

    //These are all used by the driver to print in the correct order
    public void ListByIDAscending() { listIt(3, 1); }

    public void ListByFirstAscending() { listIt(1, 1); }

    public void ListByLastAscending() { listIt(2, 1); }

    public void ListByIDDescending() { listIt(3, 2); }

    public void ListByFirstDescending() { listIt(1, 2); }

    public void ListByLastDescending() { listIt(2, 2); }

    //method used by the ordering, determines which scenario to prioritize (i.e. ID vs First Name)
    public void listIt(int field, int order){
        BST_OrderedIndex indexToPrint = null;
        switch (field){
            case 1:
                indexToPrint = firstNameIndex;
                break;
            case 2:
                indexToPrint = lastNameIndex;
                break;
            case 3:
                indexToPrint = IDIndex;
                break;
            default:
                System.err.println("Invalid field input for listIt");
                break;
        }
        if (indexToPrint.isEmpty()){
            System.out.println("Database is empty.\n");
        } else if (order == 1){
            indexToPrint.printIncreasing(myDB);
        } else if (order == 2){
            indexToPrint.printDecreasing(myDB);
        } else {
            System.err.println("Invalid order input for listIt");
        }
    }

    //method used in driver by user to add an entry
    public void addIt(){
        System.out.print("Enter a unique ID number to add: ");
        if (!scnr.hasNextLine()) return;
        String id = formatID(scnr.nextLine());
        if (id == null) {
            System.out.println("Invalid ID. Must be numeric.");
            return;
        }
        if (find(id) != -1) {
            System.out.println("The ID " + id + " is already in use.");
            return;
        }
        System.out.print("Enter first name: ");
        if (!scnr.hasNextLine()) return;
        String firstName = scnr.nextLine();
        System.out.print("Enter last name: ");
        if (!scnr.hasNextLine()) return;
        insert(id, firstName, scnr.nextLine());
    }

    public void deleteIt(){
        String deleteID;
        System.out.print("Please enter the ID to be deleted: ");
        if (!scnr.hasNextLine()) return;
        deleteID = scnr.nextLine().trim();
        //messy
        int x = deleteRecord(deleteID);
        if (x !=-1) System.out.println("Record "+deleteID+ " has been deleted.\n");
        else System.out.println("The ID you entered does not exist. Please try again.");
    }

    //method used in driver to find a record
    public void findIt(){
        String findID;
        System.out.print("Please enter the ID to be found: ");
        if (!scnr.hasNextLine()) return;
        findID = scnr.nextLine().trim();
        int indexOfRecord = find(findID);
        if (indexOfRecord == -1){
            System.out.println("The ID you entered does not exist.");
        } else {
            System.out.println(getDatabaseRecord(indexOfRecord).toString());
        }
    }

    private static String formatID(String value) {
        try {
            return String.format(java.util.Locale.ROOT, "%05d", Integer.parseInt(value.trim()));
        } catch (NumberFormatException error) {
            return null;
        }
    }
}
