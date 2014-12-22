package com.tzemanovic.muccoursework;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Tomas Zemanovic on 21/12/2014.
 */
public class AboutDialog extends DialogFragment {

    private static final String MESSAGE = "Dota 2 Brah\n\nKeep yourself up-to-date with the world of Dota 2.\n\nDeveloped by Tomas Zemanovic";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder aboutDialog = new AlertDialog.Builder(getActivity());
        aboutDialog.setMessage(MESSAGE).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // close
            }
        });
        aboutDialog.setTitle("About");
        aboutDialog.setIcon(R.drawable.ic_action_about);
        return aboutDialog.create();
    }
}
