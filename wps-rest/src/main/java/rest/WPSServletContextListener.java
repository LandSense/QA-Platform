package rest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.n52.wps.commons.WPSConfig;

/**
 * Servlet context listener for initialising the WPS application 
 */
@WebListener
public class WPSServletContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		WPSConfig.getInstance(arg0.getServletContext().getRealPath("/WEB-INF/classes/wps_config.xml"));
	}

}
