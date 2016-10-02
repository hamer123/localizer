package com.pw.localizer.model.entity;
import javax.persistence.*;
import java.io.Serializable;

@NamedQueries(value = {
		@NamedQuery(name = "findByUserLoginAndDateYoungerThanOlderThanOrderByDateDesc", 
				    query = "SELECT l FROM LocationGPS l WHERE l.user.login =:login AND l.date > :from AND l.date < :to ORDER BY l.date DESC")
})
@Entity
public class LocationGPS extends Location implements Serializable{}
