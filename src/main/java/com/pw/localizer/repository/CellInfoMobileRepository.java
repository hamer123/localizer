package com.pw.localizer.repository;

import com.pw.localizer.model.entity.CellInfoMobile;

public interface CellInfoMobileRepository extends JpaRepository<CellInfoMobile, Long>{

	CellInfoMobile findByLocationId(Long id);
}
