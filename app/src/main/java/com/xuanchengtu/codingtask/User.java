//Xuancheng Tu's Skillion coding task

package com.xuanchengtu.codingtask;

/*
    A simple class that stores a user's address information. Only used in class User.
 */
class Address{
    public String street;
    public String suite;
    public String city;
    public String zipcode;
    public String lat;
    public String lng;
};

/*
    A simple class that stores data of a user.
    Data of all users is in a json file fetched from a URL at runtime
*/
public class User {
    public String name;
    public String companyName;
    public String email;
    public Address address;
}
