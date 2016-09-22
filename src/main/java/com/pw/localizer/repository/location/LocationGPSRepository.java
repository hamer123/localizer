package com.pw.localizer.repository.location;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.pw.localizer.model.entity.LocationGPS;
import com.pw.localizer.repository.JpaRepository;

@Local
public interface LocationGPSRepository extends JpaRepository<LocationGPS, Long> {

	List<LocationGPS> findByLoginAndDateOrderByDate(String login, Date younger, Date older, int maxResults);
}
