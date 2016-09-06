package com.pw.localizer.controller;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import com.pw.localizer.model.google.map.GoogleMapModel;
import com.pw.localizer.model.session.LokalizatorSession;
import com.pw.localizer.model.entity.UserSetting;
import com.pw.localizer.model.enums.GoogleMaps;
import com.pw.localizer.serivce.qualifier.UserGoogleMap;

@Named
@Dependent
@UserGoogleMap
public class UserGoogleMapController extends GoogleMapController{
	private static final long serialVersionUID = 2183283451821061250L;
	@Inject
	private LokalizatorSession lokalizatorSession;

	@PostConstruct
	public void postConstruct(){
		googleMapModel = new GoogleMapModel();
		UserSetting userSetting = lokalizatorSession.getUser().getUserSetting();
		zoom = userSetting.getgMapZoom();
		center = GoogleMapModel.center(userSetting.getDefaultLatitude(), userSetting.getDefaultLongtitude());
		googleMapType = GoogleMaps.HYBRID;
		streetVisible = true;
	}
	
	public LokalizatorSession getLokalizatorSession() {
		return lokalizatorSession;
	}

	public void setLokalizatorSession(LokalizatorSession lokalizatorSession) {
		this.lokalizatorSession = lokalizatorSession;
	}
}
