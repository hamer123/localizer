package com.pw.localizer.dao.image;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Patryk on 2016-10-03.
 */
public interface ImageDao <UUID, ResourceType> {
    void create(ResourceType resourceType) throws IOException;
    void update(ResourceType resourceType) throws IOException;
    void remove(UUID uuid);
    ResourceType find(UUID uuid) throws FileNotFoundException;
}
