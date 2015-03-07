package com.florianmski.coderwall.data.models;

import java.util.List;

public class User
{
    public String username;
    public String name;
    public String team;
    public String location;
    public String company;
    public String title;
    public String about;
    public String thumbnail;
    public Integer endorsements;
    public List<Badge> badges;
    public String[] specialities;
    public String[] accomplishments;
    public Account accounts;
}
