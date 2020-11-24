public class DatabaseRecord {
    private String firstName;
    private String lastName;
    private String ID;

    //Parameterized constructor for the DatabaseRecord
    public DatabaseRecord(String ID, String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
        this.ID = ID;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getID(){
        return ID;
    }
    //method used to return a sentence containing the required data
    public String toString(){
        return ID + " " + firstName + " " + lastName;
    }
}
