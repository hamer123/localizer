package com.pw.localizer.repository.area.message;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.localizer.model.entity.AreaMessageMail;
import com.pw.localizer.repository.area.message.AreaMessageMailRepository;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AreaMessageMailRepositoryImpl implements AreaMessageMailRepository{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public AreaMessageMail create(AreaMessageMail entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public AreaMessageMail save(AreaMessageMail entity) {
		return em.merge(entity);
	}

	@Override
	public void delete(AreaMessageMail entity) {
		em.remove(entity);
	}

	@Override
	public AreaMessageMail findById(Long id) {
		return em.find(AreaMessageMail.class, id);
	}

	@Override
	public List<AreaMessageMail> findAll() {
		return em.createQuery("SELECT amm FROM AreaMessageMail  amm", AreaMessageMail.class)
				.getResultList();
	}
}
