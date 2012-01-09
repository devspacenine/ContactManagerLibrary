package com.devspacenine.contactmanagerlibrary;

/**
 * A model object containing contact data.
 */
public class ContactInfo {

	private long mId;
    private String mDisplayName;
    private String mCustomRingtone;

    public void setId(long id) {
    	this.mId = id;
    }
    
    public long getId() {
    	return mId;
    }
    
    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }
    
    public void setCustomeRingtone(String customRingtone) {
    	this.mCustomRingtone = customRingtone;
    }
    
    public String getCustomRingtone() {
    	return mCustomRingtone;
    }
}
