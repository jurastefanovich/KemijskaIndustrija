package com.example.stefanovic.kemijskaindustrija.Model;
import com.example.stefanovic.kemijskaindustrija.Authentication.Account;
import java.time.LocalDate;

public class User extends Entitet  {


    private String lastName;
    private LocalDate dateOfBirth;
    private Account account;

    public User(String name, String lastName, LocalDate dateOfBirth, Account account) {
        super(name);
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.account = account;
    }

    public User(Long id, String name, String lastName, LocalDate dateOfBirth, Account account) {
        super(id,name);
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getFullName() {
        return getName() + " " + getLastName();
    }

    public static class UserBuilder{
        private Long id;
        private String name;
        private String lastName;
        private LocalDate dateOfBirth;
        private Account account;

        public UserBuilder setId(Long id){
            this.id = id;
            return this;
        }
        public UserBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder setDatOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public UserBuilder setAccount(Account account) {
            this.account = account;
            return this;
        }

        public User build(){
            return new User(id,name,lastName,dateOfBirth,account);
        }
    }

    @Override
    public String toString() {
        return getId() + " " + getName() + " " + getLastName() + " " + getDateOfBirth() + " " + getAccount();
    }
}