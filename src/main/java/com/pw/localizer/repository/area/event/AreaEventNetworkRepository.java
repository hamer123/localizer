package com.pw.localizer.repository.area.event;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.pw.localizer.model.entity.AreaEventNetwork;
import com.pw.localizer.repository.JpaRepository;

@Local
public interface AreaEventNetworkRepository extends JpaRepository<AreaEventNetwork, Long> {
	List<AreaEventNetwork> findByAreaId(long id);
	List<AreaEventNetwork> findAllWhereMailSendIsTrue();
	List<AreaEventNetwork> findByAreaIdAndDate(long id, Date from);
	List<AreaEventNetwork> findBySendMailAndAttemptToSendLowerThan(boolean sendMail, int attemptToSend);
}
