package org.motechproject.couchmrs.viewer.controller;

import org.motechproject.couchmrs.viewer.domain.Patient;
import org.motechproject.couchmrs.viewer.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Properties;

@Controller
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final String couchBaseUrl;

    @Autowired
    public PatientController(PatientService patientService, @Qualifier("couchdbProperties")Properties couchProperties) {
        this.patientService = patientService;
        this.couchBaseUrl = String.format("http://%s:%s", couchProperties.getProperty("host"), couchProperties.getProperty("port"));
    }

    @RequestMapping(value="/{id}", method= RequestMethod.GET)
    public ModelAndView getPatient(@PathVariable String id) {
        Patient patient = findPatientById(id);
        return new ModelAndView("/patient/view").addObject("patient", patient).addObject("couchBaseUrl", couchBaseUrl);
    }

    private Patient findPatientById(String id) {
        Patient patient = patientService.findByMotechId(id);
        if(patient == null) {
            patient = patientService.findByPatientId(id);
        }
        return patient;
    }

    @RequestMapping(value="/raw/{id}", method= RequestMethod.GET)
    @ResponseBody
    public Patient getRaw(@PathVariable String id) {
        return findPatientById(id);
    }
}
