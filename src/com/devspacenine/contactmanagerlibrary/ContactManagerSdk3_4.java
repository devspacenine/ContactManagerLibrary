package com.devspacenine.contactmanagerlibrary;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts.People;

@SuppressWarnings("deprecation")
public class ContactManagerSdk3_4 extends ContactManager {

	/**
     * Returns a Pick Contact intent using the pre-Eclair "people" URI.
     */
	@Override
	public Intent getContactPickerIntent() {
		return new Intent(Intent.ACTION_PICK, People.CONTENT_URI);
	}

	/**
     * Retrieves the contact information.
     */
	@Override
	public ContactInfo loadContact(ContentResolver contentResolver, Uri contactUri) {
		
		ContactInfo contactInfo = new ContactInfo();
        
		// Load the id, display name, and custom ringtone for the specified person
		Cursor cursor = contentResolver.query(contactUri,
                new String[]{People._ID, People.DISPLAY_NAME, People.CUSTOM_RINGTONE}, null, null, null);
        try {
            if (cursor.moveToFirst()) {
            	contactInfo.setId(cursor.getLong(0));
                contactInfo.setDisplayName(cursor.getString(1));
                contactInfo.setCustomeRingtone(cursor.getString(2));
            }
        } finally {
            cursor.close();
        }

        return contactInfo;
	}

	/**
     * Retrieves a list of contact information for contacts using the supplied ringtone URI.
     */
	@Override
	public ContactInfo[] loadContactsWithRingtone(ContentResolver contentResolver, Uri ringtoneUri) {
		
		ContactInfo[] contactList;
		
		Cursor cursor = contentResolver.query(People.CONTENT_URI,
				new String[]{People._ID, People.DISPLAY_NAME, People.CUSTOM_RINGTONE},
				People.CUSTOM_RINGTONE + "=\'" + ringtoneUri.toString() + "\'", null, null);
		contactList = new ContactInfo[cursor.getCount()];
		
		int counter = 0;
		try {
			while(cursor.moveToNext()) {
				ContactInfo contact = new ContactInfo();
				contact.setId(cursor.getLong(0));
				contact.setDisplayName(cursor.getString(1));
				contact.setCustomeRingtone(cursor.getString(2));
				contactList[counter++] = contact;
			}
		} finally {
			cursor.close();
		}
		
		return contactList;
	}

	/**
     * Checks to see if a ringtone is being used by any contacts.
     */
	@Override
	public boolean isRingtoneUsed(ContentResolver contentResolver, Uri ringtoneUri) {
		
		Cursor cursor = contentResolver.query(People.CONTENT_URI,
				new String[]{People._ID},
				People.CUSTOM_RINGTONE + "=\'" + ringtoneUri.toString() + "\'", null, null);
		
		int count = cursor.getCount();
		cursor.close();
		
		if(count > 0)
			return true;
		return false;
	}

	/**
     * Sets a contacts custom ringtone to the supplied ringtone URI.
     */
	@Override
	public boolean setCustomRingtone(ContentResolver contentResolver, Uri contactUri, Uri ringtoneUri) {
		
		ContentValues values = new ContentValues();
		values.put(People.CUSTOM_RINGTONE, ringtoneUri.toString());
		
		int count = contentResolver.update(contactUri, values, null, null);
		
		if(count > 0)
			return true;
		return false;
	}
	
}