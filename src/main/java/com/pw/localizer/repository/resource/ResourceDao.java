package com.pw.localizer.repository.resource;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Patryk on 2016-10-03.
 */
public interface ResourceDao<UUID, ResourceType> {
    void create(ResourceType resourceType, String uuid) throws IOException;
    void update(ResourceType resourceType, String uuid) throws IOException;
    void remove(UUID uuid);
    ResourceType find(UUID uuid) throws FileNotFoundException;
    byte[] content(UUID uuid);
}
