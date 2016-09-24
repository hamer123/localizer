package com.pw.localizer.jsf.utilitis;

import java.util.regex.Pattern;

import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.LocationNetwork;
import com.pw.localizer.model.enums.LocalizerService;
import com.pw.localizer.model.enums.OverlayType;
import com.pw.localizer.model.enums.Provider;

public class OverlayIdentyfikator {
	private long id;
	private String login;
	private Provider providerType;
	private LocalizerService localizerService;
	private OverlayType overlay;

	public OverlayIdentyfikator(Location location){
		this.id = location.getId();
		this.login = location.getUser().getLogin();
		this.providerType = location.getProviderType();
		if(location.getProviderType() == Provider.NETWORK)
			this.localizerService = getLocalizationServicesFromLocation(location);
	}
	
	public OverlayIdentyfikator(Location location, OverlayType overlay){
		this.id = location.getId();
		this.login = location.getUser().getLogin();
		this.providerType = location.getProviderType();
		this.overlay = overlay;
		if(location.getProviderType() == Provider.NETWORK)
			this.localizerService = getLocalizationServicesFromLocation(location);
	}
	
	private OverlayIdentyfikator(OverlayType overlay, Provider providerType, LocalizerService localizerService, String login, long id){
		this.overlay = overlay;
		this.id = id;
		this.login = login;
		this.providerType = providerType;
		this.localizerService = localizerService;
	}

	private LocalizerService getLocalizationServicesFromLocation(Location location){
		LocationNetwork locationNetwork = (LocationNetwork)location;
		return locationNetwork.getLocalizerService();
	}
	
	public Pattern createPattern(){
		StringBuilder regex = new StringBuilder();
		
		regex.append(getOverlaySection())
		     .append("_")
		     .append(getProviderTypeSection())
		     .append("_")
		     .append(getLocalizationServicesSection())
		     .append("_")
		     .append(getLoginSection())
		     .append("_")
		     .append(getIdSection());
		
//		System.out.println(regex.toString());
		
		return Pattern.compile(regex.toString());
	}
	
	public String createIdentyfikator(){
		StringBuilder identyfikator = new StringBuilder();
		
		identyfikator.append(overlay)
		  .append("_")
		  .append(providerType)
		  .append("_")
		  .append(localizerService)
		  .append("_")
		  .append(login)
		  .append("_")
		  .append(id);
		
//		System.out.println(identyfikator.toString());
		
		return identyfikator.toString();
	}
	
	private String getIdSection(){
		if(id == 0)
			return ".+";
		else
			return String.valueOf(id);
	}
	
	private String getOverlaySection(){
		if(overlay == null)
			return ".+";
		
		return overlay.toString();
	}
	
	private String getLocalizationServicesSection(){
		if(localizerService == null)
			return ".+";
		else
			return localizerService.toString();
	}
	
	private String getProviderTypeSection(){
		if(providerType == null)
			return ".+";
		else
			return providerType.toString();
	}
	
	private String getLoginSection(){
		if(login == null)
			return ".+";
		else
			return login;
	}

	public long getId() {
		return id;
	}

	public String getLogin() {
		return login;
	}


	public Provider getProviderType() {
		return providerType;
	}

	public LocalizerService getLocalizerService() {
		return localizerService;
	}

	public OverlayType getOverlay() {
		return overlay;
	}



	public static class OverlayIdentyfikatorBuilder{
		private long id;
		private String login;
		private LocalizerService localizerService;
		private Provider providerType;
		private OverlayType overlay;
		
		public OverlayIdentyfikatorBuilder id(long id){
			this.id = id;
			return this;
		}
		
		public OverlayIdentyfikatorBuilder login(String login){
			this.login = login;
			return this;
		}
		
		public OverlayIdentyfikatorBuilder providerType(Provider providerType){
			this.providerType = providerType;
			return this;
		}
		
		public OverlayIdentyfikatorBuilder localzationServiceType(LocalizerService localizerService){
			this.localizerService = localizerService;
			return this;
		}
		
		public OverlayIdentyfikatorBuilder overlayType(OverlayType overlay){
			this.overlay = overlay;
			return this;
		}
		
		public OverlayIdentyfikator build(){
			return new OverlayIdentyfikator(
					overlay,
					providerType,
                    localizerService,
					login,
					id
					);
		}
	}
}
