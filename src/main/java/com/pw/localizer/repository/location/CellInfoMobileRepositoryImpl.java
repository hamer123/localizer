package com.pw.localizer.repository.location;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.localizer.model.entity.CellInfoMobile;
import com.pw.localizer.repository.location.CellInfoMobileRepository;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CellInfoMobileRepositoryImpl implements CellInfoMobileRepository{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public CellInfoMobile create(CellInfoMobile entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CellInfoMobile save(CellInfoMobile entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(CellInfoMobile entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CellInfoMobile findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CellInfoMobile> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CellInfoMobile findByLocationId(Long id) {
		return em.createQuery("SELECT l.cellInfoMobile FROM LocationNetwork l WHERE l.id =:id", CellInfoMobile.class)
		  .setParameter("id", id)
		  .getSingleResult();
	}

}
