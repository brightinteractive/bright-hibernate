/*
 * Copyright 2012 Bright Interactive, All Rights Reserved.
 */

package com.brightinteractive.common.hibernate;

/**
 * Example of a class whose instances are persisted by Hibernate.
 *
 * @author Bright Interactive
 */
public class Widget
{
    private Long id;
    private String name;

    public Widget()
    {
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
