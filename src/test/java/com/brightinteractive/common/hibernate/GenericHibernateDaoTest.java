/*
 * Copyright 2012 Bright Interactive, All Rights Reserved.
 */

package com.brightinteractive.common.hibernate;

import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.junit.Before;
import org.junit.Test;

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
        return sessionFactory;
    }

    @Test
    public void testFindByIdCallsSessionLoad()
    {
        @SuppressWarnings("UnusedDeclaration")
        Widget widget = daoToTest.findById(42L);

        verify(mockSession).load(Widget.class, 42L);
    }

    @Test
    public void testFindByAndLockIdCallsSessionLoadWithLockArgument()
    {
        @SuppressWarnings("UnusedDeclaration")
        Widget widget = daoToTest.findByIdAndLock(42L);

        verify(mockSession).load(Widget.class, 42L, LockOptions.UPGRADE);
    }
}
