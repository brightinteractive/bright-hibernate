/*
 * Copyright 2012 Bright Interactive, All Rights Reserved.
 */

package com.brightinteractive.common.hibernate;

import java.sql.Connection;

import org.hibernate.Interceptor;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests for GenericHibernateDao
 *
 * @author Bright Interactive
 */
public class GenericHibernateDaoTest
{
    private Session mockSession;
    private WidgetDao daoToTest;

    @Before
    public void setUp()
    {
        mockSession = createMockSession();

        SessionFactory mockSessionFactory = createMockSessionFactory(mockSession);

        daoToTest = new WidgetDao();
        daoToTest.setSessionFactory(mockSessionFactory);
    }

    private static Session createMockSession()
    {
        return mock(Session.class);
    }

    private static SessionFactory createMockSessionFactory(Session session)
    {
        SessionFactory sessionFactory = mock(SessionFactory.class);
        when(sessionFactory.openSession()).thenReturn(session);
        when(sessionFactory.openSession(any(Interceptor.class))).thenReturn(session);
        when(sessionFactory.openSession(any(Connection.class))).thenReturn(session);
        when(sessionFactory.openSession(any(Connection.class), any(Interceptor.class))).thenReturn(session);
        return sessionFactory;
    }

    @Test
    public void testFindByIdCallsSessionLoad()
    {
        @SuppressWarnings("UnusedDeclaration")
        Widget widget = daoToTest.findById(42L, false);

        verify(mockSession).load(Widget.class, 42L);
    }

    @Test
    public void testFindByAndLockIdCallsSessionLoadWithLockArgument()
    {
        @SuppressWarnings("UnusedDeclaration")
        Widget widget = daoToTest.findById(42L, true);

        verify(mockSession).load(Widget.class, 42L, LockMode.UPGRADE);
    }
}
