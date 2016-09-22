package com.pw.localizer.repository.user;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.pw.localizer.model.entity.*;
import com.pw.localizer.model.utilitis.UserAdvanceSearch;
import com.pw.localizer.repository.user.UserRepository;

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

	@Override
	public Location findLastGpsLocationByUserId(long id) {
		List<LocationGPS>result = em.createNamedQuery("USER.findLastGpsLocationByUserId", LocationGPS.class)
				.setParameter("id",id)
				.getResultList();
		return result.isEmpty() ? null : result.get(0);
	}

	@Override
	public Location findLastNetworkNaszLocationByUserId(long id) {
		List<LocationNetwork>result = em.createNamedQuery("USER.findLastNetworkNaszLocationByUserId", LocationNetwork.class)
				.setParameter("id",id)
				.getResultList();
		return result.isEmpty() ? null : result.get(0);
	}

	@Override
	public Location findLastNetworkObcyLocationByUserId(long id) {
		List<LocationNetwork>result = em.createNamedQuery("USER.findLastNetworkObcyLocationByUserId", LocationNetwork.class)
				.setParameter("id",id)
				.getResultList();
		return result.isEmpty() ? null : result.get(0);
	}

	@Override
	public List<User> findByLoginLikeAndEmailLikeAndPhoneLike(UserAdvanceSearch userAdvanceSearch) {
		return em.createNamedQuery("USER.findByLoginLikeAndEmailLikeAndPhoneLike",User.class)
				.setParameter("login", userAdvanceSearch.getLogin() == null ? "%" : "%" + userAdvanceSearch.getLogin() + "%")
				.setParameter("email", userAdvanceSearch.getEmail() == null ? "%" : "%" + userAdvanceSearch.getEmail() + "%")
				.setParameter("phone", userAdvanceSearch.getPhone() == null ? "%" : "%" + userAdvanceSearch.getPhone() + "%")
				.getResultList();
	}
}
