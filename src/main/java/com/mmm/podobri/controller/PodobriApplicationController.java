package com.mmm.podobri.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mmm.podobri.bo.EventViewWrapper;
import com.mmm.podobri.bo.EventsFilter;
import com.mmm.podobri.bo.TeamInfo;
import com.mmm.podobri.controller.databinding.ActivityEditor;
import com.mmm.podobri.dao.DaoUtils;
import com.mmm.podobri.model.Activity;
import com.mmm.podobri.model.City;
import com.mmm.podobri.model.Country;
import com.mmm.podobri.model.Education;
import com.mmm.podobri.model.Event;
import com.mmm.podobri.model.EventCostType;
import com.mmm.podobri.model.Opportunity;
import com.mmm.podobri.model.OpportunityCategory;
import com.mmm.podobri.model.OrganizationsType;
import com.mmm.podobri.service.EventService;
import com.mmm.podobri.service.UserService;


@Controller
public class PodobriApplicationController
{
    public List<OpportunityCategory> categoriesList;
    public List<Opportunity> opportunitiesList;
    public List<Activity> activitiesList;
    public List<EventCostType> costTypesList;
    public List<Education> educations;
    public List<Country> countries;
    public List<City> cities;
    public List<OrganizationsType> organizationTypes;

    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;


    @Autowired
    public void init(PlatformTransactionManager transactionManager)
    {
        new TransactionTemplate(transactionManager).execute(new TransactionCallback<Object>()
        {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus)
            {
                if (eventService != null)
                {
                    final DaoUtils daoUtils = eventService.getDaoUtils();
                    categoriesList = new ArrayList<OpportunityCategory>(daoUtils.getAllOpportunityCategories());
                    opportunitiesList = new ArrayList<Opportunity>(daoUtils.getAllOpportunities());
                    activitiesList = new ArrayList<Activity>(daoUtils.getAllActivities());
                    costTypesList = new ArrayList<EventCostType>(daoUtils.getAllEventCostTypes());
                    educations = new ArrayList<Education>(daoUtils.getAllEducations());
                    countries = new ArrayList<Country>(daoUtils.getAllCountries());
                    cities = new ArrayList<City>(daoUtils.getAllCities());
                    organizationTypes = new ArrayList<OrganizationsType>(daoUtils.getAllOrganizationTypes());
                }
                return null;
            }
        });
    }


    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        binder.registerCustomEditor(List.class, "activities", new ActivityEditor(List.class, userService.getDaoUtils()));
    }


    @RequestMapping(value = {"", "/"})
    public ModelAndView podobri()
    {
        final ModelAndView model = new ModelAndView("podobri");
        model.addObject("closestEventsGroup", buildClosesEventSection());
        EventsFilter filter = new EventsFilter();
        model.addObject("eventsFilter", filter);
        model.addObject("teaminfos", buildTeamInfoList());
        return loadSelects(model);
    }


    @RequestMapping("/about")
    public ModelAndView viewAbout()
    {
        final ModelAndView model = new ModelAndView("about");
        model.addObject("teaminfos", buildTeamInfoList());
        return model;
    }


    private List<List<EventViewWrapper>> buildClosesEventSection()
    {
        List<List<EventViewWrapper>> closestEventsGroup = new ArrayList<List<EventViewWrapper>>();
        int groupSize = 6;
        List<Event> allEvents = eventService.findAll();
        List<EventViewWrapper> allEventsWrapper = EventViewWrapper.buildEventViewWrapperList(allEvents);
        int i = 0;
        while (i + groupSize < allEventsWrapper.size())
        {
            closestEventsGroup.add(allEventsWrapper.subList(i, i + groupSize));
            i += groupSize;
        }
        closestEventsGroup.add(allEventsWrapper.subList(i, allEventsWrapper.size()));
        return closestEventsGroup;
    }


    private List<TeamInfo> buildTeamInfoList()
    {
        List<TeamInfo> ourTeam = new ArrayList<TeamInfo>();
        ourTeam.add(new TeamInfo("Mariyan",
                                 "Valchev",
                                 "Developer",
                                 "+359 894 379 ***",
                                 "m.valchev@gmail.com",
                                 "Mariyan gruadate his engeneering degree in TU-Sofia in 2014. He is master of Computer science. He works as Java Developer at ****** AD.He has over 2.5 years professional experience as developer."));
        ourTeam.add(new TeamInfo("Stefan",
                                 "Valchev",
                                 "Developer",
                                 "+359 888 123 456",
                                 "teko999@gmail.com",
                                 "Stefan is electrician engeneer.He complete his degree in TU-Sofia in 2014. He also like programming and he has more than 5 years non-professional experience. Now he works as PHP developer in PITO OOD."));
        ourTeam.add(new TeamInfo("Dimiter",
                                 "Penchev",
                                 "Developer",
                                 "+359 777 456 123",
                                 "d.penchev@gmail.com",
                                 "Dimiter is electrician engeneer.He complete his degree in TU-Sofia in 2014. He works as software developer from more than 2 years. His prefered program language is Java with smaller such. Now he works for Rila Solution."));
        return ourTeam;
    }


    private ModelAndView loadSelects(ModelAndView model)
    {
        model.addObject("categoriesList", categoriesList);
        model.addObject("opportunitiesList", opportunitiesList);
        model.addObject("activitiesList", activitiesList);
        model.addObject("costTypesList", costTypesList);
        model.addObject("educations", educations);
        model.addObject("countries", countries);
        model.addObject("cities", cities);
        model.addObject("organizationTypes", organizationTypes);
        return model;
    }
}
