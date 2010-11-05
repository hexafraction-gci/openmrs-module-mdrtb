package org.openmrs.module.mdrtb.web.taglib.patientchart;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.mdrtb.MdrtbConstants;
import org.openmrs.module.mdrtb.regimen.Regimen;
import org.openmrs.module.mdrtb.regimen.RegimenComponent;


public class DrugCellTag extends TagSupport {

    private static final long serialVersionUID = 1L;

	private final Log log = LogFactory.getLog(getClass());
	
	private Concept drug;
	
	private List<Regimen> regimens;
	
	private String style;
	
	private String showTooltip;
	
	  public int doStartTag() {
		 
	    	String ret = null;
	    	
	    	String colorString = "";
	    	
	    	StringBuffer titleString = new StringBuffer();
	    	
	    	if(regimens != null) {
	    		for(Regimen regimen : regimens) {
	    			RegimenComponent component = regimen.getRegimenComponentByDrugConcept(drug);
	    			if(component != null) {
	    				updateTitle(titleString, component);
	    				colorString = MdrtbConstants.PATIENT_CHART_REGIMEN_CELL_COLOR;
	    			}
	    		}
	    	}
	    	
	    	if(StringUtils.isNotBlank(colorString)) {
	    		ret = "<td class=\"chartCell\"" + ("true".equalsIgnoreCase(this.showTooltip) ? " title=\"" + titleString + "\"" : "") + " style=\"background-color:" + colorString + "\"/>";
	    	}
	    	else {
	    		ret = "<td class=\"chartCell\"/>";
	    	}
	    		
	    	try {
				JspWriter w = pageContext.getOut();
				w.println(ret);
			}
			catch (IOException ex) {
				log.error("Error while starting drug cell tag", ex);
			}
	     
	    	return SKIP_BODY;
	    }
	
	public int doEndTag() {
	    	this.drug = null;
	    	this.regimens = null;
	    	return EVAL_PAGE;
	 }
	
	/**
	 * Utility methods
	 */

	/**
	 * Adds the details of the specified regimen component (i.e. drug order) to the
	 * title (which can be rendered in a tooltip)
	 */
	private static void updateTitle(StringBuffer titleString, RegimenComponent component) {
		
		// if the component is null or a drug order is missing, do nothing--protect against null pointer crashing everything
		if(component == null || component.getDrugOrder() == null || component.getDrug() == null) {
			return;
		}
		
		SimpleDateFormat df = MdrtbConstants.DATE_FORMAT_DISPLAY;
		
		titleString.append("<nobr>");
		titleString.append(component.getDrug().getName());
		titleString.append(" ");
		titleString.append(component.getDrugOrder().getDose());
		titleString.append(" ");
		titleString.append(component.getDrugOrder().getUnits());
		titleString.append("</nobr><br/><nobr>");
		titleString.append(component.getDrugOrder().getFrequency());
		titleString.append("</nobr><br/><nobr>");
		titleString.append(df.format(component.getStartDate()));
		titleString.append("</nobr>");
		
		if(component.getStopDate() != null) {
			titleString.append("<br/><nobr>");
			titleString.append(df.format(component.getStopDate()));
			titleString.append("</nobr>");
		}
		
		if(component.getStopReason() != null) {
			titleString.append("<br/><nobr>");
			titleString.append(component.getStopReason().getBestName(Context.getLocale()));
			titleString.append("</nobr>");
		}
		
		titleString.append("<br/>");
	}

	
	
	public void setDrug(Concept drug) {
	    this.drug = drug;
    }

	public Concept getDrug() {
	    return drug;
    }

	public void setRegimens(List<Regimen> regimens) {
	    this.regimens = regimens;
    }

	public List<Regimen> getRegimens() {
	    return regimens;
    }

	public void setStyle(String style) {
	    this.style = style;
    }

	public String getStyle() {
	    return style;
    }

	public void setShowMouseover(String showTooltip) {
	    this.showTooltip = showTooltip;
    }

	public String getShowMouseover() {
	    return showTooltip;
    }
}
