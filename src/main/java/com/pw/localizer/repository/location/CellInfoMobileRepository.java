package com.pw.localizer.repository.location;

import com.pw.localizer.model.entity.CellInfoMobile;
import com.pw.localizer.repository.JpaRepository;

public interface CellInfoMobileRepository extends JpaRepository<CellInfoMobile, Long> {

	CellInfoMobile findByLocationId(Long id);
}
