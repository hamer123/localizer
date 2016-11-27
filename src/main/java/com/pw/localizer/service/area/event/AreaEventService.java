package com.pw.localizer.service.area.event;

import com.pw.localizer.model.entity.AreaEvent;
import com.pw.localizer.model.enums.LocalizationService;
import com.pw.localizer.model.enums.Provider;

import javax.ejb.Local;
import java.util.Date;
import java.util.List;

@Local
public interface AreaEventService {

    /**
     * Remove all for provided area
     * @param areaId
     */
    void remove(long areaId);

    /**
     * Remove all for provided area event type ( GPS, NETWORK )
     * @param areaId
     * @param provider
     */
    void remove(long areaId, Provider provider);

    /**
     * Remove all for provided area event type ( where we can provide NETWORK type such as NASZ or OBCY )
     * @param areaId
     * @param localizationService
     */
    void remove(long areaId, LocalizationService localizationService);

    /**
     * Persist new AreaEvent instance
     * @param areaEvent
     * @return
     */
    AreaEvent create(AreaEvent areaEvent);

    /**
     * Update already exist component
     * @param areaEvent
     * @return
     */
    AreaEvent update(AreaEvent areaEvent);

    /**
     * Get by Area id and from date
     * @param areaId
     * @param from
     * @return
     */
    List<AreaEvent> getByAreaAndDateOlder(long areaId, Date from);

    /**
     * get records that was not sent and attempts to send were not more than
     * @param sendMail
     * @param attemptToSend
     * @return
     */
    List<AreaEvent> getBySendMailAndAttemptToSendLowerThan(boolean sendMail, int attemptToSend);

    /**
     * get by area and date between
     * @param areaId
     * @param fromDate
     * @param toDate
     * @param maxResults
     * @return
     */
    List<AreaEvent> getByAreaAndOlderAndYounger(long areaId, Date fromDate, Date toDate, int maxResults);
}
