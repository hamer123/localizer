package com.pw.localizer.repository;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import com.pw.localizer.model.entity.AreaPoint;

@Local
public interface AreaPointRepository extends JpaRepository<AreaPoint, Long>{
	List<AreaPoint> findByAreaId(long id);
	Map<Integer, AreaPoint> findByAreaIdOrderByNumberMapToNumber(long id);
}
