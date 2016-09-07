package com.pw.localizer.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import com.pw.localizer.model.entity.Avatar;
import com.pw.localizer.model.enums.ImageTypes;
import com.pw.localizer.model.enums.Roles;
import com.pw.localizer.service.ImageService;
import com.pw.localizer.service.impl.AvatarService;
import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.map.LatLng;

import com.pw.localizer.model.entity.User;
import com.pw.localizer.model.entity.UserSetting;
import com.pw.localizer.repository.UserRepository;

@Named("createAccount")
@ConversationScoped
public class CreateAccountConversationController implements Serializable{
	private static final long serialVersionUID = -430267591398275516L;
	@Inject
	private AvatarUploadController avatarUploadController;
	@Inject
	private Conversation conversation;
	@Inject 
	private GoogleMapController googleMapController;
	@Inject
	private UserRepository userRepository;
	@Inject
	private ImageService avatarService;
	@Inject
	private Logger logger;

    static final String REDIRECT_TO_FINALIZE = "/account/create/finalize.xhtml?faces-redirect=true";
	static final String REDIRECT_TO_PERSONAL_DATA = "/account/create/personalData.xhtml?faces-redirect=true";
	static final String REDIRECT_TO_DEFAULT_SETTING = "/account/create/defaultSetting.xhtml?faces-redirect=true";
	static final String REDIRECT_TO_SUCCESS = "success.xhtml?faces-redirect=true";
	static final String REDIRECT_TO_NOT_SUCCESS = "notSuccess.xhtml?faces-redirect=true";
	static final String FINALIZE_PAGE = "finalize.xhtml";
	static final String PERSONAL_DATA_PAGE = "personalData.xhtml";
	static final String DEFAULT_SETTING_PAGE = "defaultSetting.xhtml";
	static final String REDIRECT_TO_HOME = "/login.xhtml?faces-redirect=true";
	
	private boolean passPersonalData;
	private boolean passDefaultSetting;
	private User user = new User();
	private String passwordRepeat;
	private double latitude;
	private double longtitude;

	@PostConstruct
	public void postConstruct(){
		googleMapController.setCenter("51.771822, 19.454155");
		googleMapController.setZoom(15);
		latitude = 51.771822;
		longtitude = 19.454155;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////    ACTIONS     //////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String onDefaultSettingStage(){
		passPersonalData = true;
		return REDIRECT_TO_DEFAULT_SETTING;
	}
	
	public String onFinalizeStage(){
		passDefaultSetting = true;
		return REDIRECT_TO_FINALIZE;
	}
	
	public String onPersonalDataStage(){
		return REDIRECT_TO_PERSONAL_DATA;
	}
	
	public void onPointSelectGoogleMap(PointSelectEvent event){
		LatLng latLng = event.getLatLng();
		latitude = latLng.getLat();
		longtitude = latLng.getLng();
	}
	
	public void onStateChangeGoogleMap(StateChangeEvent event){
		googleMapController.onGoogleMapStateChange(event);
	}
	
	public void onSlideEnd(SlideEndEvent event){
		googleMapController.setZoom(event.getValue());
	}

	@Transactional
	public String onCreateAccount(){
		try{
			UserSetting setting = new UserSetting();
			setting.setDefaultLatitude(latitude);
			setting.setDefaultLongtitude(longtitude);
		    setting.setgMapZoom(googleMapController.getZoom());
		    user.setUserSetting(setting);
			user.setRoles(createUserRoles());

			//tworzenie avatara jesli wybrano image
			if(this.getAvatarUploadController().getAvatarContent() != null &&
			   this.getAvatarUploadController().getUploadedFile() != null){
				Avatar avatar = createAvatar();
				this.avatarService.create(avatar, new ByteArrayInputStream(this.getAvatarUploadController().getAvatarContent()));
				user.setAvatar(avatar);
			}
			userRepository.create(user);

			logger.info("Utworzono nowe konto " + user.toString());
			return REDIRECT_TO_SUCCESS;
		} catch(Exception e){
			logger.error(e);
			return REDIRECT_TO_NOT_SUCCESS;
		} finally{
			endConversation();
		}
	}

	private List<Roles> createUserRoles(){
		List<Roles>roles = new ArrayList<>();
		roles.add(Roles.USER);
		return roles;
	}

	private Avatar createAvatar(){
		Avatar avatar = new Avatar();
		avatar.setFormat(ImageTypes.convert(this.getAvatarUploadController().getContentType()));
		avatar.setName(this.getAvatarUploadController().getName());
		avatar.setSize(this.getAvatarUploadController().getSize());
		avatar.setUuid(UUID.randomUUID().toString());
		return avatar;
	}

	public String onBackToHome(){
		endConversation();
		return REDIRECT_TO_HOME;
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////    UTILITIS    //////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void redirectIfStageIsNotPassOrConversationIsTransient(String stage) throws IOException{
		Stage enterStage = Stage.valueOf(stage);
		
		if(enterStage == Stage.DEFAULT_SETTINGS){
			if(conversation.isTransient() || !isPassPersonalData())
				FacesContext.getCurrentInstance().getExternalContext().redirect(PERSONAL_DATA_PAGE);
		} else if(enterStage == Stage.FINALIZE) {
			if(conversation.isTransient() || !isPassDefaultSetting())
				FacesContext.getCurrentInstance().getExternalContext().redirect(PERSONAL_DATA_PAGE);
		}
	}
	
    public void beginConversation(){
		if(!FacesContext.getCurrentInstance().isPostback() && conversation.isTransient())
			conversation.begin();
	}
	
	public void endConversation(){
		conversation.end();
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////     GETTERS   SETTERS    //////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public boolean isPassPersonalData() {
		return passPersonalData;
	}

	public void setPassPersonalData(boolean passPersonalData) {
		this.passPersonalData = passPersonalData;
	}

	public boolean isPassDefaultSetting() {
		return passDefaultSetting;
	}

	public void setPassDefaultSetting(boolean passDefaultSetting) {
		this.passDefaultSetting = passDefaultSetting;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getPasswordRepeat() {
		return passwordRepeat;
	}

	public void setPasswordRepeat(String passwordRepeat) {
		this.passwordRepeat = passwordRepeat;
	}
	
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	
	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	public GoogleMapController getGoogleMapController() {
		return googleMapController;
	}

	public void setGoogleMapController(GoogleMapController googleMapController) {
		this.googleMapController = googleMapController;
	}

	public AvatarUploadController getAvatarUploadController() {
		return avatarUploadController;
	}

	public void setAvatarUploadController(AvatarUploadController avatarUploadController) {
		this.avatarUploadController = avatarUploadController;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////    PRIVATE/PUBLIC CLASS    /////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static enum Stage{
		PERSONALS,DEFAULT_SETTINGS,FINALIZE
	}
}
