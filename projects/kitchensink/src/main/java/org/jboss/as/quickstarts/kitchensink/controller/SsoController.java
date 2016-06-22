package org.jboss.as.quickstarts.kitchensink.controller;

import java.io.Serializable;
import java.security.Principal;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.IDToken;

@Named
@SessionScoped
public class SsoController implements Serializable {

	private static final long serialVersionUID = -1369259560018805262L;

	@Inject
    private FacesContext facesContext;

    @Inject
    private Logger logger;	
    
    public SsoController() {
	}

    public void login() {
        try {
        	HttpServletRequest req =  (HttpServletRequest)facesContext.getExternalContext().getRequest();
        	HttpServletResponse rsp =  (HttpServletResponse)facesContext.getExternalContext().getResponse();
        	
        	req.authenticate(rsp);
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Loggin error");
            facesContext.addMessage(null, m);
        }
    }    
    
    //Invalidate the session and send a redirect to index.html
    public void logout() {
    	try{
	        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
	    	HttpServletRequest req =  (HttpServletRequest)facesContext.getExternalContext().getRequest();
	    	req.logout();
	        session.invalidate();
	        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
	        response.sendRedirect("index.html");
	        facesContext.responseComplete();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Loggin error");
            facesContext.addMessage(null, m);
        }
    }
    
    public IDToken getUserAuthToken(){
    	HttpServletRequest req =  (HttpServletRequest)facesContext.getExternalContext().getRequest();
    	final Principal userPrincipal;
//    	String realUserName = "annonymous user";
    	IDToken token = null;
    	
    	if (req.getRemoteUser() != null){
    		userPrincipal = req.getUserPrincipal();
//    		realUserName = userPrincipal.getName();
    		
    		if (userPrincipal instanceof KeycloakPrincipal) {

    		    @SuppressWarnings("unchecked")
				KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) userPrincipal;
    		    token = kp.getKeycloakSecurityContext().getIdToken();
//    		    realUserName = token.getPreferredUsername();
    		}    		
    	}
    	
    	return token;
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Login failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }    
    
}
