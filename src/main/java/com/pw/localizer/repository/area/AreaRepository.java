package com.pw.localizer.repository.area;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import com.pw.localizer.model.entity.Area;
import com.pw.localizer.repository.JpaRepository;

@Local
public interface AreaRepository extends JpaRepository<Area, Long> {
	List<Area> findByProviderId(long id);
	List<Area> findWithEagerFetchPointsAndTargetByProviderId(long id);
	List<Area> findByActive(boolean active);
	List<Long>findIdByProviderIdAndActive(long id, boolean active);
	int updateSetActiveById(boolean active, long id);
}
