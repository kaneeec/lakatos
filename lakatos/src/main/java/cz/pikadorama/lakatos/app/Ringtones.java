package cz.pikadorama.lakatos.app;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.google.common.io.ByteStreams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.pikadorama.lakatos.R;

public class Ringtones {

    private final Context context;

    public Ringtones(Context context) {
        this.context = context;
    }

    public void setRingtone(Sound sound, int type) throws IOException {
        File ringtoneFile = getRingtoneFile(sound);

        copyRawFileToRingtoneDir(sound, ringtoneFile);

        ContentValues values = createRingtoneConfiguration(sound, ringtoneFile);
        Uri ringtoneContentUri = MediaStore.Audio.Media.getContentUriForPath(ringtoneFile.getAbsolutePath());
        Uri ringtoneInstanceUri = context.getContentResolver().insert(ringtoneContentUri, values);

        // If we set the same ringtone again (null returned), we cannot just insert a new copy and use it,
        // we have to find id of the original one
        if (ringtoneInstanceUri == null) {
            ringtoneInstanceUri = findOriginalInstanceUri(ringtoneContentUri, ringtoneFile);
        }

        RingtoneManager.setActualDefaultRingtoneUri(context, type, ringtoneInstanceUri);
    }

    private Uri findOriginalInstanceUri(Uri ringtoneContentUri, File ringtoneFile) {
        try (Cursor c = context.getContentResolver().query(ringtoneContentUri, null, null, null, null)) {
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndex(MediaStore.MediaColumns._ID));
                String ringtoneFilePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                if (ringtoneFile.getAbsolutePath().equals(ringtoneFilePath)) {
                    return Uri.parse(ringtoneContentUri.toString() + "/" + id);
                }
            }
        }
        throw new IllegalStateException("Unable to find original instance URI for ringtone. " + ringtoneFile.getAbsolutePath());
    }

    private void copyRawFileToRingtoneDir(Sound sound, File ringtoneFile) throws IOException {
        Uri rawResourceUri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/" + sound.getSoundName());

        if (!ringtoneFile.exists()) {
            ringtoneFile.getParentFile().mkdirs();
            ringtoneFile.createNewFile();
        }

        AssetFileDescriptor rawSoundFile = context.getContentResolver().openAssetFileDescriptor(rawResourceUri, "r");
        try (FileInputStream fis = rawSoundFile.createInputStream();
             FileOutputStream fos = new FileOutputStream(ringtoneFile)) {
            ByteStreams.copy(fis, fos);
        }
    }

    private ContentValues createRingtoneConfiguration(Sound sound, File ringtoneFile) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, ringtoneFile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, context.getString(R.string.ringtone_prefix) + sound.getMessage());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/ogg");
        values.put(MediaStore.MediaColumns.SIZE, ringtoneFile.length());
        values.put(MediaStore.Audio.Media.ARTIST, R.string.app_name);
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        values.put(MediaStore.Audio.Media.IS_ALARM, true);
        values.put(MediaStore.Audio.Media.IS_MUSIC, true);
        return values;
    }

    private File getRingtoneFile(Sound sound) {
        return new File(Environment.getExternalStorageDirectory() + "/" + context.getPackageName() + "/ringtones", sound.getSoundFileName());
    }

}
