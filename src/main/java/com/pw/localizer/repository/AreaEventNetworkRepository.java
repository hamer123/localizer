package com.pw.localizer.repository;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.pw.localizer.model.entity.AreaEventNetwork;

@Local
public interface AreaEventNetworkRepository extends JpaRepository<AreaEventNetwork, Long>{
	public List<AreaEventNetwork> findByAreaId(long id);
	public List<AreaEventNetwork> findAllWhereMailSendIsTrue();
	public List<AreaEventNetwork> findByAreaIdAndDate(long id, Date from);
}
