package com.pw.localizer.identyfikator;

import com.pw.localizer.model.enums.LocalizerService;
import com.pw.localizer.model.enums.OverlayType;
import com.pw.localizer.model.enums.Provider;

/**
 * Created by wereckip on 30.08.2016.
 */
public class OverlayUUIDRaw {
    private Long id;
    private String login;
    private OverlayType overlayType;
    private Provider provider;
    private LocalizerService localizationService;

    public boolean matches(String uuid){
        String uuidThis = OverlayUUIDConverter.uuid(this);
        return OverlayUUIDUtilitis.matches(uuid,uuidThis);
    }

    public static OverlayUUIDRawBuilder builder(){
        return new OverlayUUIDRawBuilder();
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public OverlayType getOverlayType() {
        return overlayType;
    }

    public Provider getProvider() {
        return provider;
    }

    public LocalizerService getLocalizationService() {
        return localizationService;
    }

    public static class OverlayUUIDRawBuilder{
        OverlayUUIDRaw overlayUUIDRaw = new OverlayUUIDRaw();

        public static OverlayUUIDRawBuilder instance(){
            return new OverlayUUIDRawBuilder();
        }

        public OverlayUUIDRawBuilder id(Long id){
            this.overlayUUIDRaw.id = id;
            return this;
        }

        public OverlayUUIDRawBuilder login(String login){
            this.overlayUUIDRaw.login = login;
            return this;
        }

        public OverlayUUIDRawBuilder overlay(OverlayType overlay){
            this.overlayUUIDRaw.overlayType = overlay;
            return this;
        }

        public OverlayUUIDRawBuilder provider(Provider provider){
            this.overlayUUIDRaw.provider = provider;
            return this;
        }

        public OverlayUUIDRawBuilder localizationService(LocalizerService localizationService){
            this.overlayUUIDRaw.localizationService = localizationService;
            return this;
        }

        public OverlayUUIDRaw build(){
            return this.overlayUUIDRaw;
        }
    }
}
