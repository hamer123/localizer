package com.pw.localizer.identyfikator;

import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.enums.Overlays;

import java.util.regex.Pattern;

/**
 * Created by wereckip on 09.09.2016.
 */
public class OverlayUUIDRawBuilderArea implements OverlayUUIDBuilder{
    private Area area;
    private OverlayUUIDRaw uuidRaw;

    public OverlayUUIDRawBuilderArea(Area area){
        this.area = area;
        buildUUIDRaw();
    }

    private void buildUUIDRaw(){
        this.uuidRaw = OverlayUUIDRaw.OverlayUUIDRawBuilder.insatnce()
                .overlay(Overlays.POLYGON)
                .login(area.getProvider().getLogin())
                .id(area.getId())
                .build();
    }

    @Override
    public String regex() {
        return OverlayUUIDConverter.regex(this.uuidRaw);
    }

    @Override
    public String uuid() {
        return OverlayUUIDConverter.uuid(this.uuidRaw);
    }

    @Override
    public Pattern pattern() {
        return Pattern.compile(OverlayUUIDConverter.regex(this.uuidRaw));
    }

    @Override
    public OverlayUUIDRaw uuidRaw() {
        return this.uuidRaw;
    }
}
