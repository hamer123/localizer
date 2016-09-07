package com.pw.localizer.model.enums;

public enum ImageTypes {
	JPEG,PNG,JPG,GIF;

	public static ImageTypes convert(String meditaType){
		System.out.println(meditaType);
		return JPEG;
	}
}
