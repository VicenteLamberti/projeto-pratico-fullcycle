package com.fullcycle.vicente.domain.exceptions;

import com.fullcycle.vicente.domain.AggregateRoot;
import com.fullcycle.vicente.domain.Identifier;
import com.fullcycle.vicente.domain.validation.Error;
import com.fullcycle.vicente.domain.validation.handler.Notification;

import java.util.Collections;
import java.util.List;

public class NotificationException extends DomainException{
    public NotificationException(final String aMessage, final Notification notification) {
        super(aMessage, notification.getErrors());
    }

}
