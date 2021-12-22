package com.mobiledev.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import androidx.annotation.RequiresPermission
import java.io.File


@RequiresPermission(Manifest.permission.READ_CONTACTS)
fun Context.isContactExists(
    phoneNumber: String
): Boolean {
    val lookupUri = Uri.withAppendedPath(
        ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
        Uri.encode(phoneNumber)
    )
    val projection = arrayOf(
        ContactsContract.PhoneLookup._ID,
        ContactsContract.PhoneLookup.NUMBER,
        ContactsContract.PhoneLookup.DISPLAY_NAME
    )
    contentResolver.query(lookupUri, projection, null, null, null).use {
        return (it?.moveToFirst() == true)
    }
}


@SuppressLint("Range")
@RequiresPermission(Manifest.permission.READ_CONTACTS)
@JvmOverloads
fun Context.retrieveAllContacts(
    searchPattern: String = "",
    retrieveAvatar: Boolean = true,
    limit: Int = 40,
    offset: Int = 0
): List<ContactData> {
    val result: MutableList<ContactData> = mutableListOf()
    contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        CONTACT_PROJECTION,
        if (searchPattern.isNotBlank()) "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} LIKE ?" else null,
        if (searchPattern.isNotBlank()) arrayOf("%$searchPattern%") else null,
        if (limit > 0 && offset > -1) "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} ASC LIMIT $limit OFFSET $offset"
        else ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC"
    )?.use {
        if (it.moveToFirst()) {
            do {
                val contactId = it.getLong(it.getColumnIndex(CONTACT_PROJECTION[0]))
                val name = it.getString(it.getColumnIndex(CONTACT_PROJECTION[1])) ?: ""
                val contactNumber = it.getString(it.getColumnIndex(CONTACT_PROJECTION[2])) ?: ""
                val avatar = it.getString(it.getColumnIndex(CONTACT_PROJECTION[3])) ?: null

                val email = retrieveEmail(contactId)
                result.add(ContactData(contactId, name, contactNumber, avatar, email))
            } while (it.moveToNext())
        }
    }
    return result
}

@SuppressLint("Range")
@RequiresPermission(Manifest.permission.READ_CONTACTS)
@JvmOverloads
fun Context.retrieveAllContactsByPhoneURI(
    searchPattern: String = "",
    retrieveAvatar: Boolean = true,
    limit: Int = -1,
    offset: Int = -1
): List<ContactData> {
    val result: MutableList<ContactData> = mutableListOf()
    contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        CONTACT_PROJECTION,
        if (searchPattern.isNotBlank()) "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?" else null,
        if (searchPattern.isNotBlank()) arrayOf("%$searchPattern%") else null,
        if (limit > 0 && offset > -1) "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC LIMIT $limit OFFSET $offset"
        else ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
    )?.use {
        if (it.moveToFirst()) {
            do {
                val contactId = it.getLong(it.getColumnIndex(CONTACT_PROJECTION[0]))
                val name = it.getString(it.getColumnIndex(CONTACT_PROJECTION[1])) ?: ""
                val contactNumber = it.getString(it.getColumnIndex(CONTACT_PROJECTION[2])) ?: ""
                val avatar = it.getString(it.getColumnIndex(CONTACT_PROJECTION[3])) ?: null


                avatar?.let {
                    val file = File(avatar)
                }
                val email = retrieveEmail(contactId)
                result.add(ContactData(contactId, name, contactNumber, avatar, email))
            } while (it.moveToNext())
        }
    }
    return result
}


@SuppressLint("Range")
@RequiresPermission(Manifest.permission.READ_CONTACTS)
@JvmOverloads
fun Context.searchContactByName(
    searchPattern: String = "",
    retrieveAvatar: Boolean = true,
    limit: Int = -1,
    offset: Int = -1
): List<ContactData> {
    val result: MutableList<ContactData> = mutableListOf()
    contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        CONTACT_PROJECTION,
        if (searchPattern.isNotBlank()) "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?" else null,
        if (searchPattern.isNotBlank()) arrayOf("%$searchPattern%") else null,
        if (limit > 0 && offset > -1) "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC LIMIT $limit OFFSET $offset"
        else ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
    )?.use {
        if (it.moveToFirst()) {
            do {
                val contactId = it.getLong(it.getColumnIndex(CONTACT_PROJECTION[0]))
                val name = it.getString(it.getColumnIndex(CONTACT_PROJECTION[1])) ?: ""
                val contactNumber = it.getString(it.getColumnIndex(CONTACT_PROJECTION[2])) ?: ""
                val avatar = it.getString(it.getColumnIndex(CONTACT_PROJECTION[3])) ?: null
//
//                val avatar = if (retrieveAvatar) retrieveAvatar(contactId) else null
                val email = retrieveEmail(contactId)
                result.add(ContactData(contactId, name, contactNumber, null, email))
            } while (it.moveToNext())
        }
    }
    return result
}

private fun Context.retrievePhoneNumber(contactId: Long): List<String> {
    val result: MutableList<String> = mutableListOf()
    contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} =?",
        arrayOf(contactId.toString()),
        null
    )?.use {
        if (it.moveToFirst()) {
            do {
//                result.add(it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))
            } while (it.moveToNext())
        }
    }
    return result
}

private fun Context.retrieveAvatar(contactId: Long): Uri? {
    return contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        "${ContactsContract.Data.CONTACT_ID} =? AND ${ContactsContract.Data.MIMETYPE} = '${ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE}'",
        arrayOf(contactId.toString()),
        null
    )?.use {
        if (it.moveToFirst()) {
            val contactUri = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI,
                contactId
            )
            Uri.withAppendedPath(
                contactUri,
                ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
            )
        } else null
    }
}


@SuppressLint("Range")
private fun Context.retrieveEmail(contactId: Long): String? {

    return contentResolver.query(
        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
        ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?",
        arrayOf(contactId.toString()), null
    )?.use {
        if (it.moveToFirst()) {
            it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
        } else null
    }
}

private val CONTACT_PROJECTION = arrayOf(
    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
    ContactsContract.CommonDataKinds.Phone.NUMBER,
    ContactsContract.CommonDataKinds.Phone.PHOTO_URI
)

data class ContactData(
    val contactId: Long,
    val name: String,
    val phone_no: String,
    val pic: String?,
    val email: String?
)
