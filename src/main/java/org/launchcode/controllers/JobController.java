package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, @RequestParam int id) {

        // TODO #1 - get the Job with the given ID and pass it into the view
        Job somejob = jobData.findById(id);
        ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(somejob);
        model.addAttribute("jobs", jobs);
        model.addAttribute("id", id);

        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @ModelAttribute @Valid JobForm jobForm, Errors errors) {

        // TODO #6 - Validate the JobForm model, and if valid, create a
        // new Job and add it to the jobData data store. Then
        // redirect to the job detail view for the new Job.
        if (errors.hasErrors()) {
            model.addAttribute("name", "Name may not be empty");
            return "new-job";

        }

        String name = jobForm.getName();
        int employerId = jobForm.getEmployerId();
        int skillId = jobForm.getSkillId();
        int locationId = jobForm.getLocationId();
        int positionTypeId = jobForm.getPositionTypeId();

        Employer employer = jobData.getEmployers().findById(employerId);; // need to get the employer info based on the employer id
        CoreCompetency coreCompetency = jobData.getCoreCompetencies().findById(skillId); // need to get the skill info based on the skillid
        Location location = jobData.getLocations().findById(locationId); // need to get the location info based on the locationid
        PositionType positionType = jobData.getPositionTypes().findById(positionTypeId); //need to get the positionType based on the positionTypeId

        Job newJob = new Job(name, employer, location, positionType, coreCompetency);
        jobData.add(newJob);
        int nextJobId = newJob.getId();

        ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(newJob);
        model.addAttribute(nextJobId);
        model.addAttribute(jobs);

        return "redirect:/job?id=" + nextJobId;

    }

}
