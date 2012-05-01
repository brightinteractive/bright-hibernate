package com.brightinteractive.common.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.SessionFactoryUtils;

import org.hibernate.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;

import com.brightinteractive.common.dao.GenericDao;

/**
 * <p>Implements the generic CRUD data access operations using Hibernate APIs.</p>
 *
 * <p>To write a DAO, subclass and parametrize this class with your persistent class. Of course, assuming that you have
 * a traditional 1:1 approach for Entity:DAO design.</p>
 *
 * @author Christian Bauer
 * @author Francis Devereux
 */
public abstract class GenericHibernateDao<T, ID extends Serializable>
    implements GenericDao<T, ID>
{
    private Class<T> persistentClass;

    @Resource
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public GenericHibernateDao()
    {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
            .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected Session getSession()
    {
        return SessionFactoryUtils.getSession(sessionFactory, true);
    }

    public SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    public Class<T> getPersistentClass()
    {
        return persistentClass;
    }

    @SuppressWarnings("unchecked")
    public T findById(ID id)
    {
        return (T) getSession().load(getPersistentClass(), id);
    }

	@SuppressWarnings("unchecked")
    public boolean exists(ID id)
    {
        return getSession().get(getPersistentClass(), id) != null;
    }

    @SuppressWarnings("unchecked")
    public T findByIdAndLock(ID id)
    {
        return (T) getSession().load(getPersistentClass(), id, LockOptions.UPGRADE);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll()
    {
        return findByCriteria();
    }

    @SuppressWarnings("unchecked")
    public List<T> findByExample(T exampleInstance, String... excludeProperty)
    {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        Example example = Example.create(exampleInstance);
        for (String exclude : excludeProperty)
        {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }

    @SuppressWarnings("unchecked")
    public T makePersistent(T entity)
    {
        getSession().saveOrUpdate(entity);
        return entity;
    }

    public void makeTransient(T entity)
    {
        getSession().delete(entity);
    }

    public void flush()
    {
        getSession().flush();
    }

    public void clear()
    {
        getSession().clear();
    }

    /**
     * Use this inside subclasses as a convenience method.
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(Criterion... criterion)
    {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        for (Criterion c : criterion)
        {
            crit.add(c);
        }
        return crit.list();
    }
}
