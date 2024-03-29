package com.losacabaos.skillsharing.student;

import lombok.Data;

@Data
public class Student implements Comparable<Student> {
    private String name;
    private String surname;
    private String email;
    private String password;
    private String confirmPassword;
    private String oldPassword;
    private String username;
    private int balanceHours = 0;
    private boolean blocked = false;
    private String street;
    private int number;
    private int pc;
    private String locality;
    private boolean skp = false;
    private String degree;

    @Override
    public int compareTo(Student otherStudent) {
        return this.username.compareTo(otherStudent.getUsername());
    }
}
