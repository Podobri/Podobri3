package com.mmm.podobri.model;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


/**
 * The persistent class for the countries database table.
 */
@Entity
@Table(name = "countries")
@NamedQuery(name = "Country.findAll", query = "SELECT c FROM Country c")
public class Country
    implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private byte id;

    @Column(nullable = false, length = 70)
    private String country;

    // bi-directional many-to-one association to City
    @OneToMany(mappedBy = "country")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<City> cities;

    // bi-directional many-to-one association to Event
    @OneToMany(mappedBy = "country")
    private List<Event> events;

    // bi-directional many-to-one association to UserInfo
    @OneToMany(mappedBy = "country")
    private List<UserInfo> userInfos;


    public Country()
    {}


    public byte getId()
    {
        return this.id;
    }


    public void setId(byte id)
    {
        this.id = id;
    }


    public String getCountry()
    {
        return this.country;
    }


    public void setCountry(String country)
    {
        this.country = country;
    }


    public List<City> getCities()
    {
        return this.cities;
    }


    public void setCities(List<City> cities)
    {
        this.cities = cities;
    }


    public City addCity(City city)
    {
        getCities().add(city);
        city.setCountry(this);

        return city;
    }


    public City removeCity(City city)
    {
        getCities().remove(city);
        city.setCountry(null);

        return city;
    }


    public List<Event> getEvents()
    {
        return this.events;
    }


    public void setEvents(List<Event> events)
    {
        this.events = events;
    }


    public Event addEvent(Event event)
    {
        getEvents().add(event);
        event.setCountry(this);

        return event;
    }


    public Event removeEvent(Event event)
    {
        getEvents().remove(event);
        event.setCountry(null);

        return event;
    }


    public List<UserInfo> getUserInfos()
    {
        return this.userInfos;
    }


    public void setUserInfos(List<UserInfo> userInfos)
    {
        this.userInfos = userInfos;
    }


    public UserInfo addUserInfo(UserInfo userInfo)
    {
        getUserInfos().add(userInfo);
        userInfo.setCountry(this);

        return userInfo;
    }


    public UserInfo removeUserInfo(UserInfo userInfo)
    {
        getUserInfos().remove(userInfo);
        userInfo.setCountry(null);

        return userInfo;
    }
}
