package com.pw.localizer.repository.location;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;


import javax.persistence.PersistenceContext;

import com.pw.localizer.model.entity.WifiInfo;
import com.pw.localizer.repository.location.WifiInfoRepository;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class WifiInfoRepositoryImpl implements WifiInfoRepository {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public WifiInfo create(WifiInfo entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WifiInfo save(WifiInfo entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(WifiInfo entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WifiInfo findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WifiInfo> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WifiInfo findByLocationId(Long id) {
		List<WifiInfo>result =  em.createNamedQuery("WifiInfo.findByLocationId", WifiInfo.class)
				 .setParameter("id", id)
				 .getResultList();
		if(!result.isEmpty()){
			return result.get(0);
		}
		
		return null;
	}
}
