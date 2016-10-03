package com.pw.localizer.dao.image;

import com.pw.localizer.model.entity.Avatar;

import javax.annotation.Resource;
import javax.transaction.TransactionSynchronizationRegistry;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Patryk on 2016-10-03.
 */


public class AvatarDao implements ImageDao<String, InputStream> {
    @Resource
    TransactionSynchronizationRegistry txReg;

    @Override
    public void create(InputStream inputStream) throws IOException {

    }

    @Override
    public void update(InputStream inputStream) throws IOException {

    }

    @Override
    public void remove(String uuid) {

    }

    @Override
    public InputStream find(String uuid) throws FileNotFoundException {
        return null;
    }
}
