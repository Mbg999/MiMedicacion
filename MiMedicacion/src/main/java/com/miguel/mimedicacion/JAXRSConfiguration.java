package com.miguel.mimedicacion;

import java.util.HashMap;
import java.util.Map;
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
public class JAXRSConfiguration extends Application {

    /**
     * Register the multipart jersey features
     * https://stackoverflow.com/a/29534950/9764641
     * @return Map&lt;String, Object&gt;
     */
    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("jersey.config.server.provider.classnames", 
                "org.glassfish.jersey.media.multipart.MultiPartFeature");
        return props;
    }
}
