package com.pw.localizer.repository.avatar;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.localizer.model.entity.Avatar;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AvatarRepositoryImpl implements AvatarRepository{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Avatar create(Avatar entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public Avatar save(Avatar entity) {
		return em.merge(entity);
	}

	@Override
	public void delete(Avatar entity) {
		em.remove(entity);
	}

	@Override
	public Avatar findById(Long id) {
		return em.find(Avatar.class, id);
	}

	@Override
	public List<Avatar> findAll() {
		return em.createQuery("SELECT a FROM Avatar", Avatar.class)
				 .getResultList();
	}

}
