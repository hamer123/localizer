package com.pw.localizer.repository.area.event;

import com.pw.localizer.model.entity.AreaEvent;
import com.pw.localizer.model.enums.LocalizationService;
import com.pw.localizer.repository.JpaRepository;

import javax.ejb.Local;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Local
public interface AreaEventRepository extends JpaRepository<AreaEvent, Long> {
    void deleteByArea(long areaId);
    List<AreaEvent> findByAreaAndOlderAndYounger(long areaId, Date fromDate, Date toDate, int maxResults);
    List<AreaEvent> findByAreaAndDateOlder(long areaId, Date from);
    List<AreaEvent> findByAreaAndType(long areaId, Collection<Class> types);
    List<AreaEvent> findByAreaAndLocalizationService(long areaId, LocalizationService localizationService);
    List<AreaEvent> findBySendMailAndAttemptToSendLowerThan(boolean sendMail, int attemptToSend);
}
