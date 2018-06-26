package dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public abstract class GenericDAO<T, PK extends Serializable> implements IGenericDAO<T, PK> {
	
	protected Class<T> entityClass;
	protected EntityManager entityManager;
	
	public GenericDAO(EntityManager em) {
		this();
		this.entityManager = em;
	}
	
	@SuppressWarnings("unchecked")
	public GenericDAO() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
	}
	
	public EntityManager getEntityManager() {
		return this.entityManager;
	}
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public T insert(T t) {
		this.entityManager.persist(t);
		return t;
	}

	@Override
	public T find(PK id) {
		return this.entityManager.find(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		Query q = entityManager.createQuery("select object(o) from " + entityClass.getSimpleName() + " as o");
		return q.getResultList();
	}

	@Override
	public T update(T t) {
		return this.entityManager.merge(t);
	}
	
	@Override
	public void refresh(T t) {
		this.entityManager.refresh(t);
	}

	@Override
	public void delete(T t) {
		t = this.entityManager.merge(t);
		this.entityManager.remove(t);
	}

	@Override
	public void beginTransaction() {
		this.entityManager.getTransaction().begin();
	}

	@Override
	public void commit() {
		this.entityManager.flush();
		this.entityManager.getTransaction().commit();
	}

	@Override
	public void rollback() {
		this.entityManager.getTransaction().rollback();
	}
}