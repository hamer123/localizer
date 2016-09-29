package com.pw.localizer.repository.area;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.localizer.model.entity.Area;
import com.pw.localizer.repository.area.AreaRepository;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AreaRepositoryImpl implements AreaRepository{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Area create(Area entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public Area save(Area entity) {
		return em.merge(entity);
	}

	@Override
	public void delete(Area entity) {
		em.remove(entity);
	}

	@Override
	public Area findById(Long id) {
		return em.find(Area.class, id);
	}

	@Override
	public List<Area> findAll() {
		return em.createNamedQuery("Area.findAll", Area.class)
				  .getResultList();
	}

//	@Override
//	public List<Area> findByTargetId(long id){
//		return em.createNamedQuery("Area.findByTargetId", Area.class)
//		         .setParameter("id", id)
//		         .getResultList();
//	}

	@Override
	public List<Area> findWithEagerFetchPointsAndTargetByProviderId(long id) {
		List<Area>polygonModels = em.createNamedQuery("Area.findWithEagerFetchPointsAndTargetByProviderId", Area.class)
				 .setHint("javax.persistence.fetchgraph", em.getEntityGraph("Area.fetchAll"))
				 .setParameter("id", id)
				 .getResultList();	
//
//		em.getEntityGraph("Area.fetchAll")
//		for(Area polygonModel : polygonModels)
//			polygonModel.getPoints().size();
		
		return polygonModels;
	}

	@Override
	public int updateSetActiveById(boolean active, long id) {
		return em.createNamedQuery("Area.updateByIdSetActive")
				 .setParameter("areaId", id)
				 .setParameter("active", active)
		         .executeUpdate();
	}

	@Override
	public List<Area> findByProviderId(long id) {
		return em.createNamedQuery("Area.findByProviderId", Area.class)
				  .setParameter("id", id)
				  .getResultList();
	}

	@Override
	public List<Area> findByActive(boolean active) {
		return em.createNamedQuery("Area.findByAktywny", Area.class)
				 .setParameter("aktywny", active)
				 .getResultList();
	}

	@Override
	public List<Long> findIdByProviderIdAndActive(long id, boolean active) {
		return em.createNamedQuery("Area.findIdByProviderIdAndAktywny", Long.class)
				 .setParameter("id", id)
				 .setParameter("active", active)
				 .getResultList();
	}

//	@Override
//	public void removeById(long id) {
//		Area area = em.find(Area.class, id);
//		em.remove(area);
//	}

}