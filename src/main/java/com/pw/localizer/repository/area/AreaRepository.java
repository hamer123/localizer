package com.pw.localizer.repository.area;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;

import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.entity.AreaPoint;
import com.pw.localizer.repository.JpaRepository;

@Local
public interface AreaRepository extends JpaRepository<Area, Long> {
	List<Area> findByProviderId(long id);
	List<Area> findByProviderIdEagerFetchPoints(long id);
	List<Area> findByActive(boolean active);
	List<Long>findIdByProviderIdAndActive(long id, boolean active);
	List<AreaPoint> findAreaPointsByAreaId(long id);
	int updateSetActiveById(boolean active, long id);
}
