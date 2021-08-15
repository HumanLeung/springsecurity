package com.company.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventHandler {

    @EventListener({AuthenticationSuccessEvent.class, InteractiveAuthenticationSuccessEvent.class, ApplicationEvent.class})
    public void processAuthenticationSuccessEvent(ApplicationEvent e){
        System.out.println(e+"this is event");
        Logger logger = LoggerFactory.getLogger(getClass());
//        logger.info((AbstractAuthenticationEvent)e.getAuthentication().toString());
    }
}
