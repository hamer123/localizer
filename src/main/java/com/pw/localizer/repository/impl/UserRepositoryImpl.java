package com.pw.localizer.repository.impl;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.repository.UserRepository;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class UserRepositoryImpl implements UserRepository{
	@PersistenceContext
	private EntityManager em;

	@Override
	public User create(User entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public User save(User entity) {
		return em.merge(entity);
	}

	@Override
	public void delete(User entity) {
		em.remove(entity);
	}

	@Override
	public User findById(Long id) {
		return em.find(User.class, id);
	}

	@Override
	public List<User> findAll() {
		return em.createNamedQuery("USER.findAll", User.class)
				 .getResultList();
	}

	@Override
	public User findByLoginAndPassword(String login, String password) {
		return em.createNamedQuery("USER.findUserFetchRolesByLoginAndPassword", User.class)
		         .setParameter("login", login)
		         .setParameter("password", password)
		         .getSingleResult();
	}

	@Override
	public User findByLogin(String login) {
		return em.createNamedQuery("USER.findByLogin", User.class)
				 .setParameter("login", login)
				 .getSingleResult();
	}

	@Override
	public List<String> findLoginByLoginLike(String loginLike) {
		return em.createNamedQuery("USER.findLoginByLoginLike", String.class)
				 .setParameter("login", loginLike + "%")
				 .getResultList();
	}
//
//	@Override
//	public User findByLoginFetchArea(String login) {
//		User user =  em.createNamedQuery("USER.findByLogin", User.class)
//			       .setParameter("login", login)
//			       .getSingleResult();
//
//		List<Area>areas = user.getAreas();
//		for(Area area : areas)
//			area.getPoints().size();
//
//		return user;
//	}

	@Override
	public User findByEmail(String email) {
		return em.createNamedQuery("USER.findByEmail", User.class)
				 .setParameter("email", email)
				 .getSingleResult();
	}

	@Override
	public boolean isLoginExist(String login) {
		List<User>result = em.createNamedQuery("USER.findByLogin",User.class)
				.setParameter("login", login)
				.getResultList();
		return result.isEmpty() ? false : true;
	}
}
