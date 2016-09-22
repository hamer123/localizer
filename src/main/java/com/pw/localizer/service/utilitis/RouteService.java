package com.pw.localizer.service.utilitis;

import org.primefaces.model.map.Polyline;

public interface RouteService {

	double calculateLengthMeters(Polyline polyline);
}
