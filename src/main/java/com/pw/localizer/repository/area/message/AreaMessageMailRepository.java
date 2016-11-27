package com.pw.localizer.repository.area.message;

import javax.ejb.Local;

import com.pw.localizer.model.entity.AreaMessageMail;
import com.pw.localizer.repository.JpaRepository;

@Local
public interface AreaMessageMailRepository extends JpaRepository<AreaMessageMail, Long> {
}
