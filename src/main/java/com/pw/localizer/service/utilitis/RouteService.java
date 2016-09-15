package com.pw.localizer.service.utilitis;

import javax.ejb.Local;

import com.pw.localizer.model.google.component.Route;

@Local
public interface RouteService {

	double calculateLenghtMeters(Route route);
}
