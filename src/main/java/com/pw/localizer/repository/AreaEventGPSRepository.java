package com.pw.localizer.repository;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.pw.localizer.model.entity.AreaEventGPS;

@Local
public interface AreaEventGPSRepository extends JpaRepository<AreaEventGPS, Long>{
	public List<AreaEventGPS> findByAreaId(long id);
	public List<AreaEventGPS> findByAreaIdAndDate(long id, Date from);
	public List<AreaEventGPS> findAllWhereMailSendIsTrue();
}
