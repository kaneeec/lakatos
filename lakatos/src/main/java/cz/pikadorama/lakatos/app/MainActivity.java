package cz.pikadorama.lakatos.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;

import cz.pikadorama.lakatos.R;
import cz.pikadorama.lakatos.app.adapter.TextAdapter;

public class MainActivity extends Activity {

    private Ringtones ringtones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new TextAdapter(getApplicationContext(), Sound.getMessages()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                MediaPlayer mp = MediaPlayer.create(MainActivity.this, Sound.values()[position].getSoundId());
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
                mp.start();
            }
        });
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showActionDialog(Sound.values()[position]);
                return true;
            }
        });

        this.ringtones = new Ringtones(this);
    }

    private void showActionDialog(final Sound sound) {
        String[] items = {
                getString(R.string.dialog_action_set_as_ringtone),
                getString(R.string.dialog_action_share)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        showRingtoneTypeDialog(sound);
                        break;
                    case 1:
                        share(sound);
                        break;
                }
            }
        });
        builder.show();
    }

    private void showRingtoneTypeDialog(final Sound sound) {
        final String[] items = {
                getString(R.string.dialog_action_ringtone_ringtone),
                getString(R.string.dialog_action_ringtone_notification),
                getString(R.string.dialog_action_ringtone_alarm)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            final int[] ringtoneTypes = {
                    RingtoneManager.TYPE_RINGTONE,
                    RingtoneManager.TYPE_NOTIFICATION,
                    RingtoneManager.TYPE_ALARM
            };

            @Override
            public void onClick(DialogInterface dialog, int item) {
                setAsRingtone(sound, ringtoneTypes[item]);
            }
        });
        builder.show();
    }

    private void share(Sound sound) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        share.putExtra(Intent.EXTRA_TEXT, sound.getUrl());
        startActivity(Intent.createChooser(share, getString(R.string.share)));
    }

    private void setAsRingtone(Sound sound, int type) {
        try {
            ringtones.setRingtone(sound, type);
            Toast.makeText(MainActivity.this, R.string.ringtone_set, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("Lakatos", e.getMessage(), e);
            Toast.makeText(MainActivity.this, R.string.error_cannot_set_ringtone, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
