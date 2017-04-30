package com.example.harshith.chatsockets;

import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.security.AllPermission;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by harshith on 4/30/17.
 */

public class Database {
    private static String you;
    private static ArrayList<Person> allPeople;
    private static ArrayList<Person> friends;
    private static ArrayList<Person> onlineUsers;
    private static Map<String, Person> people;

    public static boolean contains(ArrayList<Person> users, String username ) {
        if(users != null && username != null) {
            for(Person person: users) {
                if(person.getUsername().equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void initialize(ArrayList<Person> allPeople, ArrayList<Person> friends, ArrayList<Person> onlineUsers) {
        Database.people = new HashMap<String, Person>();
        Database.friends = new ArrayList<Person>();
        Database.onlineUsers = new ArrayList<Person>();
        for (int i = 0; i != allPeople.size(); i++) {
            people.put(allPeople.get(i).getUsername(), allPeople.get(i));
            if(allPeople.get(i).getFriendIndication() == 0) {
                friends.add(allPeople.get(i));
            }
            if(contains(onlineUsers, allPeople.get(i).getUsername())) {
                onlineUsers.add(allPeople.get(i));
            }
        }
        Database.allPeople = allPeople;
    }

    public static String getYou() {
        return you;
    }

    public static void setYou(String you) {
        Database.you = you;
    }

    public static ArrayList<Person> getAllPeople() {
        return allPeople;
    }

    public static void setAllPeople(ArrayList<Person> allPeople) {
        Database.allPeople = allPeople;
    }

    public static void setFriends(ArrayList<Person> friends) {
        Database.friends = friends;
    }

    public static void setOnlineUsers(ArrayList<Person> onlineUsers) {
        Database.onlineUsers = onlineUsers;
    }

    public static synchronized ArrayList<Person> getPeople() {
        return allPeople;
    }

    public static synchronized void setPeople(ArrayList<Person> allpeople) {
        Database.allPeople = allpeople;
    }

    public static synchronized String getUsername() {
        return you;
    }

    public static synchronized void setUsername(String you) {
        Database.you = you;
    }

    public static synchronized void addMessage(ArrayList<String> message) {
        if(message.get(0).equals("message")) {
            if(people.containsKey(message.get(1))) {
                people.get(message.get(1)).addMessage(message.get(2), false);
            }
        }
    }

    public static synchronized void addMyMessage(String username, String message) {
        if(username != null && people != null && message != null) {
            if(people.containsKey(username)) {
                people.get(username).addMessage(message, true);
            }
        }
    }

    public static synchronized void setOnline(ArrayList<String> online) {
        if(online != null) {
            if (online.get(0).equals("online")) {
                if(people.containsKey(online.get(1))) {
                    people.get(online.get(1)).setOnline(true);
                    onlineUsers.add(people.get(online.get(1)));
                }
            }
        }
    }
    public static synchronized void setOffline(ArrayList<String> offline) {
        if(offline != null) {
            if (offline.get(0).equals("online")) {
                if(people.containsKey(offline.get(1))) {
                    people.get(offline.get(1)).setOnline(false);
                    onlineUsers.remove(people.get(offline.get(1)));
                }
            }
        }
    }
    public static synchronized void addFriend(ArrayList<String> friend) {
        if(friend != null) {
            if(friend.get(0).equals("friend")) {
                if(people != null) {
                    if(people.containsKey(friend.get(1))) {
                        people.get(friend.get(1)).setFriendIndication(0);
                    }
                }
            }
        }
    }
    public static synchronized void removeFriend(ArrayList<String> friend) {
        if(friend != null) {
            if(friend.get(0).equals("friend")) {
                if(people != null) {
                    if(people.containsKey(friend.get(1))) {
                        people.get(friend.get(1)).setFriendIndication(0);
                    }
                }
            }
        }
    }
    public static synchronized void getOnlineUsers() {

    }
    public static synchronized void getFriends() {

    }
    public static synchronized void getAllUsers() {

    }
    public static synchronized void addPerson(ArrayList<String> person) {

    }

}
