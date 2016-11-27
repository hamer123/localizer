package com.pw.localizer.repository.area.event;

import com.pw.localizer.model.entity.AreaEvent;
import com.pw.localizer.model.enums.LocalizationService;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AreaEventRepositoryImpl implements AreaEventRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public AreaEvent create(AreaEvent entity) {
        entityManager.persist(entity);
        return entity;
    }

    public AreaEvent save(AreaEvent entity) {
        return entityManager.merge(entity);
    }

    public void delete(AreaEvent entity) {
        entityManager.remove(entity);
    }

    public AreaEvent findById(Long id) {
        return entityManager.find(AreaEvent.class, id);
    }

    public List<AreaEvent> findAll() {
        return entityManager.createNamedQuery("AreaEvent.findAll", AreaEvent.class)
                .getResultList();
    }

    public void deleteByArea(long areaId) {
        entityManager.createNamedQuery("AreaEvent.removeByArea")
                .setParameter("areaId", areaId)
                .executeUpdate();
    }

    public List<AreaEvent> findByAreaAndOlderAndYounger(long areaId, Date fromDate, Date toDate, int maxResults) {
        return entityManager.createNamedQuery("AreaEvent.findByAreaAndYoungerAndOlder", AreaEvent.class)
                .setParameter("fromDate", fromDate, TemporalType.TIMESTAMP)
                .setParameter("toDate", toDate, TemporalType.TIMESTAMP)
                .setParameter("areaId", areaId)
                .setMaxResults(maxResults)
                .getResultList();
    }

    public List<AreaEvent> findByAreaAndDateOlder(long areaId, Date from) {
        return entityManager.createNamedQuery("AreaEvent.findByAreaAndDateOlder", AreaEvent.class)
                .setParameter("areaId", areaId)
                .setParameter("fromDate", from, TemporalType.TIMESTAMP)
                .getResultList();
    }

    public List<AreaEvent> findByAreaAndType(long areaId, Collection<Class> types) {
        return entityManager.createNamedQuery("AreaEvent.findByAreaAndType", AreaEvent.class)
                .setParameter("areaId", areaId)
                .setParameter("types", types)
                .getResultList();
    }

    public List<AreaEvent> findByAreaAndLocalizationService(long areaId, LocalizationService localizationService) {
        return entityManager.createNamedQuery("AreaEvent.findByAreaAndLocalizationService", AreaEvent.class)
                .setParameter("areaId", areaId)
                .setParameter("localizationService", localizationService)
                .getResultList();
    }

    public List<AreaEvent> findBySendMailAndAttemptToSendLowerThan(boolean sendMail, int attemptToSend) {
        return entityManager.createNamedQuery("AreaEvent.findBySendMailAndAttemptToSendLowerThan", AreaEvent.class)
                .setParameter("attemptToSend", attemptToSend)
                .setParameter("sendMail", sendMail)
                .getResultList();
    }
}
