package com.example.myprojectloginpage.myprojectentity;

public class User {
    private String userName;
    private String userEmail;
    public User(){}
    public User(String userName, String userEmail, String userSurname, String uId, String userPassword) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userSurname = userSurname;
        this.uId = uId;
        this.userPassword = userPassword;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    private String userSurname;

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    private String uId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    private String userPassword;

}
