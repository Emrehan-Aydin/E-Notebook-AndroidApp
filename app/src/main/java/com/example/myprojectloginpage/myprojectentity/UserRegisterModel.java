package com.example.myprojectloginpage.myprojectentity;

public class UserRegisterModel {
    public UserRegisterModel(String userName, String userSurname, String userPassword, String userEmail) {
        this.userName = userName;
        this.userSurname = userSurname;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
    }
    public UserRegisterModel(){}

        private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

        private String userSurname;
        private String userPassword;
        private String userEmail;
    }


