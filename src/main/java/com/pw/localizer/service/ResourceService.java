package com.pw.localizer.service;

import javax.ejb.Local;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by wereckip on 23.08.2016.
 *
 * @param <UUID> identytyfikator zasobu
 * @param <ResourceType> typ w jakim zwracany jest zasob
 */

@Local
public interface ResourceService<UUID, ResourceType> {
    void create(UUID uuid, ResourceType resourceType) throws IOException;
    void update(UUID uuid, ResourceType resourceType) throws IOException;
    void remove(UUID uuid);
    ResourceType find(UUID uuid) throws FileNotFoundException;
}
