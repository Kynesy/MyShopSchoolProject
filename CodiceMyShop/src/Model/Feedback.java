package Model;

public class Feedback {
    private final String[] values = new String[]{"★","★★","★★★","★★★★","★★★★★"};
    private int index;
    private String message;
    private String username;

    public Feedback(int index, String message) {
        this.index = index;
        this.message = message;
    }

    public Feedback(int index, String message, String username) {
        this.index = index;
        this.message = message;
        this.username = username;
    }

    public int getIndex() {
        return index;
    }

    public String getGraphicalVote(){
        return values[index];
    }

    public String getUsername() {
        return username;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        String string = "Username: "+ username+ "\nVoto: " + values[index] + " \nRecensione: " + message;
        return string;
    }
}
