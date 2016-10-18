package com.pw.localizer.repository.area.event;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.pw.localizer.model.entity.AreaEvent;
import com.pw.localizer.model.entity.AreaEventGPS;
import com.pw.localizer.repository.JpaRepository;

@Local
public interface AreaEventGPSRepository extends JpaRepository<AreaEventGPS, Long> {
	List<AreaEventGPS> findByAreaId(long id);
	List<AreaEventGPS> findByAreaIdAndDate(long id, Date from);
	List<AreaEventGPS> findAllWhereMailSendIsTrue();
	List<AreaEventGPS> findBySendMailAndAttemptToSendLowerThan(boolean sendMail, int attemptToSend);
}
