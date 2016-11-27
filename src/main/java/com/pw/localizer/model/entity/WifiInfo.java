package com.pw.localizer.model.entity;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@Getter
@Setter
@NamedQueries(value = {
		@NamedQuery(name="WifiInfo.findByLocationId", query="SELECT l.wifiInfo FROM LocationNetwork l WHERE l.id =:id")
})
public class WifiInfo implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private long id;

	private int frequency;

	private String bssid;

	private int ipAddress;

	private int linkSpeed;

	private String macAddress;

	private int networkId;

	private int rssi;

	private String ssid;
}
