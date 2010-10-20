package org.openmrs.module.mdrtb.web.controller.form;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.api.context.Context;
import org.openmrs.module.mdrtb.form.SimpleIntakeForm;
import org.openmrs.module.mdrtb.program.MdrtbPatientProgram;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/module/mdrtb/form/intake.form")
public class SimpleIntakeFormController extends AbstractFormController {
	
	@ModelAttribute("intake")
	public SimpleIntakeForm getIntakeForm(@RequestParam(required = true, value = "encounterId") Integer encounterId,
	                                      @RequestParam(required = true, value = "patientId") Integer patientId,
	                                      @RequestParam(required = false, value = "patientProgramId") Integer patientProgramId) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		// if no form is specified, create a new one
		if (encounterId == -1) {
			SimpleIntakeForm form = new SimpleIntakeForm(Context.getPatientService().getPatient(patientId));
		
			// prepopulate the form with information that has been specified
			if (patientProgramId != null) {
				MdrtbPatientProgram program = new MdrtbPatientProgram(Context.getProgramWorkflowService().getPatientProgram(patientProgramId));
				form.setEncounterDatetime(program.getDateEnrolled());
				form.setLocation(program.getLocation());
			}
				
			return form;
		}
		else {
			return new SimpleIntakeForm(Context.getEncounterService().getEncounter(encounterId));
		}
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showIntakeForm() {
		ModelMap map = new ModelMap();
		return new ModelAndView("/module/mdrtb/form/intake", map);	
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView processIntakeForm (@ModelAttribute("intake") SimpleIntakeForm intake, BindingResult errors, 
	                                       @RequestParam(required = true, value = "patientId") Integer patientId,
	                                       @RequestParam(required = true, value = "patientProgramId") Integer patientProgramId,
	                                       @RequestParam(required = true, value = "returnUrl") String returnUrl,
	                                       SessionStatus status, HttpServletRequest request, ModelMap map) {

		// TODO: validate
		
		// save the actual update
		Context.getEncounterService().saveEncounter(intake.getEncounter());

		// clears the command object from the session
		status.setComplete();
		map.clear();

		// TODO: add a redirect handle here to handle proper redirect?
		return new ModelAndView(new RedirectView(returnUrl + "&patientId=" + patientId));
	}
}