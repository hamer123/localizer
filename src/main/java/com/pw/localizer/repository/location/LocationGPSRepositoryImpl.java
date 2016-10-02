package com.pw.localizer.repository.location;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

import com.pw.localizer.model.entity.LocationGPS;
import com.pw.localizer.repository.location.LocationGPSRepository;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class LocationGPSRepositoryImpl implements LocationGPSRepository{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public LocationGPS create(LocationGPS entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public LocationGPS save(LocationGPS entity) {
		return em.merge(entity);
	}

	@Override
	public void delete(LocationGPS entity) {
		em.remove(entity);
	}

	@Override
	public LocationGPS findById(Long id) {
		return em.find(LocationGPS.class, id);
	}

	@Override
	public List<LocationGPS> findAll() {
		return em.createQuery("SELECT l FROM LocationGPS l", LocationGPS.class)
				 .getResultList();
	}

	@Override
	public List<LocationGPS> findByLoginAndDateOrderByDate(String login, Date from, Date to, int maxResults) {
		return em.createNamedQuery("findByUserLoginAndDateYoungerThanOlderThanOrderByDateDesc", LocationGPS.class)
				.setParameter("login", login)
				 .setParameter("from", from, TemporalType.TIMESTAMP)
				 .setParameter("to", to, TemporalType.TIMESTAMP)
				 .setMaxResults(maxResults)
				 .getResultList();
	}

}
