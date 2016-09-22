package com.pw.localizer.repository.location;

import javax.ejb.Local;

import com.pw.localizer.model.entity.WifiInfo;
import com.pw.localizer.repository.JpaRepository;

@Local
public interface WifiInfoRepository extends JpaRepository<WifiInfo, Long> {

	WifiInfo findByLocationId(Long id);
}
