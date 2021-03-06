/*
 * EventsController.java
 *
 * created at 21.01.2015 �. by Mariyan
 */
package com.mmm.podobri.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.util.AutoPopulatingList;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.mmm.podobri.bo.EditParticipants;
import com.mmm.podobri.bo.EventViewWrapper;
import com.mmm.podobri.bo.EventsFilter;
import com.mmm.podobri.bo.Filter;
import com.mmm.podobri.bo.MailTemplate;
import com.mmm.podobri.controller.databinding.ActivityEditor;
import com.mmm.podobri.dao.DaoUtils;
import com.mmm.podobri.model.Activity;
import com.mmm.podobri.model.City;
import com.mmm.podobri.model.Country;
import com.mmm.podobri.model.Event;
import com.mmm.podobri.model.EventCostType;
import com.mmm.podobri.model.EventsParticipant;
import com.mmm.podobri.model.EventsProgram;
import com.mmm.podobri.model.Lector;
import com.mmm.podobri.model.Opportunity;
import com.mmm.podobri.model.OpportunityCategory;
import com.mmm.podobri.model.OrganizationsForm;
import com.mmm.podobri.model.Sponsor;
import com.mmm.podobri.service.EventService;
import com.mmm.podobri.util.FileUploadUtil;


@Controller
@RequestMapping("/events")
public class EventsController
{
    @Autowired
    private EventService eventService;

    public final List<OpportunityCategory> categoriesList = new ArrayList<OpportunityCategory>();
    public final List<Opportunity> opportunitiesList = new ArrayList<Opportunity>();
    public final List<Activity> activitiesList = new ArrayList<Activity>();
    public final List<Country> countriesList = new ArrayList<Country>();
    public final List<City> citiesList = new ArrayList<City>();
    public final List<EventCostType> costTypesList = new ArrayList<EventCostType>();


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
                    categoriesList.addAll(daoUtils.getAllOpportunityCategories());
                    opportunitiesList.addAll(daoUtils.getAllOpportunities());
                    activitiesList.addAll(daoUtils.getAllActivities());
                    countriesList.addAll(daoUtils.getAllCountries());
                    citiesList.addAll(daoUtils.getAllCities());
                    costTypesList.addAll(daoUtils.getAllEventCostTypes());
                }
                return null;
            }
        });
    }


    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
        binder.registerCustomEditor(List.class, "activities", new ActivityEditor(List.class, eventService.getDaoUtils()));
        // binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }


    @RequestMapping
    public ModelAndView eventsHome()
    {
        final ModelAndView model = new ModelAndView("events/events");
        Filter filter = new EventsFilter();
        model.addObject("eventsFilter", filter);
        model.addObject("actualEvents", EventViewWrapper.buildEventViewWrapperList(eventService.findAll()));
        return loadSelects(model);
    }


    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ModelAndView searchEvents(@Valid @ModelAttribute("eventsFilter") EventsFilter searchCriteria,
                                     BindingResult result,
                                     ModelMap model)
    {
        if (result.hasErrors())
        {
            // return model;
        }
        final ModelAndView m = new ModelAndView("events/events");
        List<Event> filteredEvents = eventService.search(searchCriteria);
        model.addAttribute("actualEvents", EventViewWrapper.buildEventViewWrapperList(filteredEvents));
        String filteredEventsMessage = "Found " + filteredEvents.size() + " results";
        model.addAttribute("filteredEventsMessage", filteredEventsMessage);
        m.addAllObjects(model);
        return loadSelects(m);
    }


    @RequestMapping(value = "/search/main", method = RequestMethod.POST)
    public ModelAndView searchEventsMain(@Valid @ModelAttribute("eventsFilter") EventsFilter searchCriteria, BindingResult result)
    {
        if (result.hasErrors())
        {
            // return model;
        }
        final ModelAndView m = new ModelAndView("events/events");
        List<Event> filteredEvents = eventService.search(searchCriteria);
        m.addObject("actualEvents", EventViewWrapper.buildEventViewWrapperList(filteredEvents));
        String filteredEventsMessage = "Found " + filteredEvents.size() + " results";
        m.addObject("filteredEventsMessage", filteredEventsMessage);
        EventsFilter filter = new EventsFilter();
        m.addObject("eventsFilter", filter);
        return loadSelects(m);
    }


    @RequestMapping(value = "/viewEvent/{id}", method = RequestMethod.GET)
    public ModelAndView viewEvent(@PathVariable Integer id)
    {
        final ModelAndView model = new ModelAndView("events/eventsView");
        final Event event = eventService.findOne(id);
        EventViewWrapper eventWrapper = new EventViewWrapper(event);
        model.addObject("event", eventWrapper);
        return model;
    }


    @RequestMapping(value = "/deleteEvent/{id}", method = RequestMethod.GET)
    public String deleteEvent(@PathVariable Integer id)
    {
        // TODO remove comment
        // eventService.deleteById(id);
        return "redirect:/events/myEvents";
    }


    @RequestMapping(value = "/editEvent/{id}", method = RequestMethod.GET)
    public ModelAndView editEvent(@PathVariable Integer id)
    {
        final ModelAndView model = new ModelAndView("events/eventsEdit");
        Event event = eventService.findOne(id);
        // initEventAutoPopulation(event); ???
        model.addObject("event", event);
        return loadSelects(model);
    }


    @RequestMapping(value = "/updateEvent", method = RequestMethod.POST)
    public ModelAndView updateEvent(@ModelAttribute("event") Event event, BindingResult result, ModelAndView model)
    {
        if (result.hasErrors())
        {
            return model;
        }
        eventService.update(event);
        return new ModelAndView("redirect:/events");
    }


    @RequestMapping(value = "/createEvent", method = RequestMethod.GET)
    public ModelAndView createEvent()
    {
        final ModelAndView model = new ModelAndView("events/eventsAdd");
        Event event = new Event();
        initEventAutoPopulation(event);
        model.addObject("event", event);
        List<OrganizationsForm> forms = eventService.getAvailableForms();
        model.addObject("forms", forms);
        return loadSelects(model);
    }


    @RequestMapping(value = "/createEventSubmit", method = RequestMethod.POST)
    public ModelAndView createEventSubmit(@ModelAttribute("event") Event event, BindingResult result, ModelAndView model)
    {
        if (result.hasErrors())
        {
            return model;
        }
        eventService.createNewEvent(event);
        return new ModelAndView("redirect:/events");
    }


    @RequestMapping(value = "/fileUpload", method = RequestMethod.GET)
    public String fileUpload()
    {
        return "events/fileUpload";
    }


    @RequestMapping(value = "/fileUploadSubmit", method = RequestMethod.POST)
    public String fileUpload(@RequestParam("file") CommonsMultipartFile file)
    {
        String processImages = FileUploadUtil.processImages(file, true, true);
        System.out.println("Image saved:" + processImages);
        return "events/fileUpload";
    }


    @RequestMapping(value = "/createInitiative", method = RequestMethod.GET)
    public ModelAndView createInitiative()
    {
        final ModelAndView model = new ModelAndView("events/eventsInit");
        model.addObject("event", new Event());
        return loadSelects(model);
    }


    @RequestMapping(value = "/apply/{id}", method = RequestMethod.GET)
    public ModelAndView apply(@PathVariable Integer id)
    {
        final Event event = eventService.findOne(id);
        boolean apply = eventService.apply(event, null);
        final ModelAndView model = new ModelAndView("events/eventsView");
        EventViewWrapper eventWrapper = new EventViewWrapper(event);
        model.addObject("event", eventWrapper);
        String applyResult;
        if (apply)
        {
            applyResult = "You succesfull apply for the event";
        }
        else
        {
            applyResult = "You are already applied for this event";
        }
        model.addObject("applyResult", applyResult);
        return model;
    }


    @RequestMapping(value = "/fillForm/{id}", method = RequestMethod.GET)
    public ModelAndView applyWithForm(@PathVariable Integer id)
    {
        final ModelAndView model = new ModelAndView("forms/viewFormUser");
        final Event event = eventService.findOne(id);
        OrganizationsForm form = event.getForm();
        model.addObject("formContent", form.getForm());
        model.addObject("eventId", id);
        return model;
    }


    @RequestMapping(value = "/applyForm", method = RequestMethod.POST)
    public ModelAndView applyForm(@RequestParam("content") String content, @RequestParam("eventId") String eventId)
    {
        final Event event = eventService.findOne(Integer.parseInt(eventId));
        boolean apply = eventService.apply(event, content);
        final ModelAndView model = new ModelAndView("events/eventsView");
        EventViewWrapper eventWrapper = new EventViewWrapper(event);
        model.addObject("event", eventWrapper);
        String applyResult;
        if (apply)
        {
            applyResult = "You succesfull apply for the event";
        }
        else
        {
            applyResult = "You are already applied for this event";
        }
        model.addObject("applyResult", applyResult);
        return model;
    }


    @RequestMapping(value = "/myEvents", method = RequestMethod.GET)
    public ModelAndView myEvents()
    {
        final ModelAndView model = new ModelAndView("events/myEvents");
        List<Event> myEvents = eventService.getMyEvents();
        List<EventsParticipant> editParticipantsList = new AutoPopulatingList<EventsParticipant>(EventsParticipant.class);
        EditParticipants editParticipants = new EditParticipants();
        editParticipants.setParticipantsList(editParticipantsList);
        model.addObject("editParticipants", editParticipants);
        MailTemplate mailTemplate = new MailTemplate();
        model.addObject("mailTemplate", mailTemplate);
        model.addObject("events", EventViewWrapper.buildEventViewWrapperList(myEvents));
        return model;
    }


    @RequestMapping(value = "/updateParticipants", method = RequestMethod.POST)
    public String updateParticipants(@ModelAttribute("editParticipants") EditParticipants editParticipants,
                                     BindingResult result,
                                     ModelAndView model)
    {
        if (result.hasErrors())
        {

        }
        eventService.updateParticipants(editParticipants);
        return "redirect:/events/myEvents";
    }


    @RequestMapping(value = "/myEvents/sendMailToParticipants/{id}", method = RequestMethod.POST)
    public String sendMailToParticipants(@PathVariable Integer id, @ModelAttribute("mailTemplate") MailTemplate template)
    {
        eventService.sendMailToAllParticipants(id, template);
        return "redirect:/events/myEvents";
    }


    @RequestMapping(value = "/myEvents/sendMailToParticipants/{id}/{userId}", method = RequestMethod.POST)
    public String sendMailToParticipant(@PathVariable Integer id,
                                        @PathVariable Integer userId,
                                        @ModelAttribute("mailTemplate") MailTemplate template)
    {

        eventService.sendMailToParticipant(id, userId, template);
        return "redirect:/events/myEvents";
    }


    @RequestMapping(value = "/generatePDF/{id}", method = RequestMethod.GET)
    public ModelAndView generatePDF(@PathVariable Integer id)
    {
        ModelAndView model = new ModelAndView("pdfView");
        Event event = eventService.findOne(id);
        model.addObject("event", event);
        return model;
    }


    @RequestMapping(value = "/generateExcel/{id}", method = RequestMethod.GET)
    public ModelAndView generateExcel(@PathVariable Integer id)
    {
        ModelAndView model = new ModelAndView("excelView");
        Event event = eventService.findOne(id);
        model.addObject("event", event);
        return model;
    }


    private ModelAndView loadSelects(ModelAndView model)
    {
        model.addObject("categoriesList", categoriesList);
        model.addObject("opportunitiesList", opportunitiesList);
        model.addObject("activitiesList", activitiesList);
        model.addObject("countriesList", countriesList);
        model.addObject("citiesList", citiesList);
        model.addObject("costTypesList", costTypesList);
        return model;
    }


    private Event initEventAutoPopulation(Event event)
    {
        List<EventsProgram> eventsPrograms = new AutoPopulatingList<EventsProgram>(EventsProgram.class);
        event.setEventsPrograms(eventsPrograms);
        List<Sponsor> sponsors = new AutoPopulatingList<Sponsor>(Sponsor.class);
        event.setSponsors(sponsors);
        List<Lector> lectors = new AutoPopulatingList<Lector>(Lector.class);
        event.setLectors(lectors);
        return event;
    }
}
