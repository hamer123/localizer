package com.pw.localizer.repository;

import javax.ejb.Local;

import com.pw.localizer.model.entity.AreaMessageMail;

@Local
public interface AreaMessageMailRepository extends JpaRepository<AreaMessageMail, Long> {

	int updateAcceptById(long id, boolean accept);
}
