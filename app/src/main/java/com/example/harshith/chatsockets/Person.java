package com.example.harshith.chatsockets;

import java.util.ArrayList;

/**
 * Created by harshith on 4/28/17.
 */

public class Person {
    static ArrayList<Person> getPersonArray(ArrayList<String> list) {
        if(list.get(0).equals("users") || list.get(0).equals("olusers")) {
            ArrayList<Person> arrayList = new ArrayList<Person>(list.size()/2);
            for (int i = 1; i != list.size(); i++) {
                if(i%2 == 1) {
                    arrayList.add(new Person(list.get(i + 1), list.get(i), list.get(i)));
                }
            }
            return  arrayList;
        }
        else {
            System.out.println(list);
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(String lastOnline) {
        this.lastOnline = lastOnline;
    }

    private String name, username, lastOnline;

    public Person() {

    }
    public Person(String name, String username, String lastOnline) {
        this.name = name;
        this.username = username;
        this.lastOnline = lastOnline;
    }

}
