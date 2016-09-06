package com.pw.localizer.repository;

import javax.ejb.Local;

import com.pw.localizer.model.entity.WifiInfo;

@Local
public interface WifiInfoRepository extends JpaRepository<WifiInfo, Long> {

	WifiInfo findByLocationId(Long id);
}
