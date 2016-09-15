package com.pw.localizer.service.utilitis;

import javax.ejb.Local;

import com.pw.localizer.model.google.component.Route;

public interface RouteService {

	double calculateLenghtMeters(Route route);
}
