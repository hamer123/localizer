package com.pw.localizer.restful.provider.adapter;

import com.pw.localizer.model.entity.Area;
import org.hibernate.proxy.HibernateProxy;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by Patryk on 2016-09-24.
 */

public class AreaAdapter extends XmlAdapter<Area,Area>{

    @Override
    public Area unmarshal(Area v) throws Exception {
        if(v.getTarget() != null){
            if(v.getTarget() instanceof HibernateProxy){
                v.setTarget(null);
            }
        }
        return v;
    }

    @Override
    public Area marshal(Area v) throws Exception {
        return v;
    }
}
