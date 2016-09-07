package com.pw.localizer.controller;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import com.pw.localizer.model.google.map.GoogleMapModel;
import com.pw.localizer.model.session.LocalizerSession;
import com.pw.localizer.model.entity.UserSetting;
import com.pw.localizer.model.enums.GoogleMaps;
import com.pw.localizer.serivce.qualifier.UserGoogleMap;

@Named
@Dependent
@UserGoogleMap
public class UserGoogleMapController extends GoogleMapController{
	private static final long serialVersionUID = 2183283451821061250L;
	@Inject
	private LocalizerSession localizerSession;

	@PostConstruct
	public void postConstruct(){
		googleMapModel = new GoogleMapModel();
		UserSetting userSetting = localizerSession.getUser().getUserSetting();
		zoom = userSetting.getgMapZoom();
		center = GoogleMapModel.center(userSetting.getDefaultLatitude(), userSetting.getDefaultLongtitude());
		googleMapType = GoogleMaps.HYBRID;
		streetVisible = true;
	}
	
	public LocalizerSession getLocalizerSession() {
		return localizerSession;
	}

	public void setLocalizerSession(LocalizerSession localizerSession) {
		this.localizerSession = localizerSession;
	}
}
