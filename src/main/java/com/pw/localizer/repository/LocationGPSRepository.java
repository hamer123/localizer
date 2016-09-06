package com.pw.localizer.repository;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.pw.localizer.model.entity.LocationGPS;

@Local
public interface LocationGPSRepository extends JpaRepository<LocationGPS, Long>{

	List<LocationGPS> findByLoginAndDateOrderByDate(String login, Date younger, Date older, int maxResults);
}
