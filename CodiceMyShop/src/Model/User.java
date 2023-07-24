package Model;


public class User {
    private String name;
    private String surname;
    private String username;
    private String password;
    private String email;
    private int isDisabled; //0 non è disabilitato, 1 lo è


    public User() {
    }

    public int isDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(int isDisabled) {
        this.isDisabled = isDisabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "-Username: "+ getUsername() +"  -Nome: "+ getName() +"  -Email: "+ getEmail()  ;
    }
}
