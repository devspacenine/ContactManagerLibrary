package com.devspacenine.contactmanagerlibrary;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

/**
 * This abstract class defines SDK-independent API for communication with
 * Contacts Provider. The actual implementation used by the application depends
 * on the level of API available on the device. If the API level is Cupcake or
 * Donut, we want to use the {@link ContactAccessorSdk3_4} class. If it is
 * Eclair or higher, we want to use {@link ContactAccessorSdk5}.
 */
public abstract class ContactManager {

	/**
     * Static singleton instance of {@link ContactAccessor} holding the
     * SDK-specific implementation of the class.
     */
	private static ContactManager mInstance;
	
	public static ContactManager getInstance() {
		
		if(mInstance == null) {
			String className;
			
			/*
             * Check the version of the SDK we are running on. Choose an
             * implementation class designed for that version of the SDK.
             *
             * Unfortunately we have to use strings to represent the class
             * names. If we used the conventional ContactAccessorSdk5.class.getName()
             * syntax, we would get a ClassNotFoundException at runtime on pre-Eclair SDKs.
             * Using the above syntax would force Dalvik to load the class and try to
             * resolve references to all other classes it uses. Since the pre-Eclair
             * does not have those classes, the loading of ContactAccessorSdk5 would fail.
             */
			int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
			if(sdkVersion < Build.VERSION_CODES.ECLAIR)
				className = "com.devspacenine.contactmanagerlibrary.ContactManagerSdk3_4";
			else
				className = "com.devspacenine.contactmanagerlibrary.ContactManagerSdk5";
			
			/*
             * Find the required class by name and instantiate it.
             */
			try {
				Class<? extends ContactManager> clazz =
					Class.forName(className).asSubclass(ContactManager.class);
				mInstance = clazz.newInstance();
			}catch(Exception e) {
				throw new IllegalStateException(e);
			}
		}
		return mInstance;
	}
	
	/**
     * Returns the {@link Intent#ACTION_PICK} intent configured for the right authority: legacy
     * or current.
     */
	public abstract Intent getContactPickerIntent();
	
	/**
     * Loads contact data for the supplied URI. The actual queries will differ for different APIs
     * used, but the result is the same: the {@link #mDisplayName} and {@link #mCustomRingtone}
     * fields are populated with correct data.
     */
    public abstract ContactInfo loadContact(ContentResolver contentResolver, Uri contactUri);
    
    /**
     * Loads contacts with the supplied custom ringtone URI. The actual queries will differ for different APIs
     * used, but the result is the same: the {@link #mDisplayName} and {@link #mCustomRingtone}
     * fields are populated with correct data.
     */
    public abstract ContactInfo[] loadContactsWithRingtone(ContentResolver contentResolver, Uri ringtoneUri);
    
    /**
     * Checks to see if any contacts are using the supplied ringtone URI as a custom ringtone.
     * The actual queries will differ for different APIs used, but the result is the same: a boolean.
     */
    public abstract boolean isRingtoneUsed(ContentResolver contentResolver, Uri ringtoneUri);
    
    /**
     * Updates a contact's custom ringtone with the supplied ringtone URI. The actual queries will
     * differ for different APIs used, but the result is the same: a boolean.
     */
    public abstract boolean setCustomRingtone(ContentResolver contentResolver, Uri contactUri, Uri ringtoneUri);
}
