package com.pw.localizer.repository.location;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.repository.JpaRepository;

@Local
public interface LocationNetworkRepository extends JpaRepository<LocationNetwork, Long> {

	List<LocationNetwork> findByLoginAndDateForServiceNaszOrderByDate(String login, Date younger, Date older, int maxResults);
	List<LocationNetwork> findByLoginAndDateForServiceObcyOrderByDate(String login, Date younger, Date older, int maxResults);
}
