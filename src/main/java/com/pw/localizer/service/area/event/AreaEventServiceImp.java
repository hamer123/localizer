package com.pw.localizer.service.area.event;

import com.pw.localizer.model.entity.AreaEvent;
import com.pw.localizer.model.entity.AreaEventGPS;
import com.pw.localizer.model.entity.AreaEventNetwork;
import com.pw.localizer.model.enums.LocalizationService;
import com.pw.localizer.model.enums.Provider;
import com.pw.localizer.repository.area.event.AreaEventRepository;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Patryk on 2016-11-13.
 */

@Stateless
public class AreaEventServiceImp implements AreaEventService {
    @Inject
    private AreaEventRepository areaEventRepository;

    public void remove(long areaId) {
        areaEventRepository.deleteByArea(areaId);
    }

    public void remove(long areaId, Provider provider) {
        if (provider == Provider.GPS) {
            List<AreaEvent> areaEvents = areaEventRepository.findByAreaAndType(areaId, Arrays.asList(AreaEventGPS.class));
            areaEvents.forEach(e -> areaEventRepository.delete(e));
        } else {
            List<AreaEvent> areaEvents = areaEventRepository.findByAreaAndType(areaId, Arrays.asList(AreaEventNetwork.class));
            areaEvents.forEach(e -> areaEventRepository.delete(e));
        }
    }

    public void remove(long areaId, LocalizationService localizationService) {
        List<AreaEvent> areaEvents = areaEventRepository.findByAreaAndLocalizationService(areaId, localizationService);
        areaEvents.forEach(e -> areaEventRepository.delete(e));
    }

    public AreaEvent create(AreaEvent areaEvent) {
        return areaEventRepository.create(areaEvent);
    }

    public AreaEvent update(AreaEvent areaEvent) {
        return areaEventRepository.save(areaEvent);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<AreaEvent> getByAreaAndDateOlder(long areaId, Date from) {
        return areaEventRepository.findByAreaAndDateOlder(areaId, from);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<AreaEvent> getBySendMailAndAttemptToSendLowerThan(boolean sendMail, int attemptToSend) {
        return areaEventRepository.findBySendMailAndAttemptToSendLowerThan(sendMail, attemptToSend);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<AreaEvent> getByAreaAndOlderAndYounger(long areaId, Date fromDate, Date toDate, int maxResults) {
        return areaEventRepository.findByAreaAndOlderAndYounger(areaId, fromDate, toDate, maxResults);
    }
}
