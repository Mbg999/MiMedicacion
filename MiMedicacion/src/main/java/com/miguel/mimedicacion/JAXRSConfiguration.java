package com.miguel.mimedicacion;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configures JAX-RS for the application.
 * 
 * api will be the base url
 * 
 * @author Miguel
 */
@ApplicationPath("api")
public class JAXRSConfiguration extends Application {}
