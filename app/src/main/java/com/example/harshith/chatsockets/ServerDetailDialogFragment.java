package com.example.harshith.chatsockets;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by harshith on 4/29/17.
 */

public class ServerDetailDialogFragment extends DialogFragment {

    String ip_addr;
    int port;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.server_detail_dialog, null);

        final EditText ip_address = (EditText) dialogView.findViewById(R.id.ip);
        final EditText portno = (EditText) dialogView.findViewById(R.id.portno);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView)
            .setPositiveButton("Connect", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(! ip_addr.equals("") && ip_addr.equals("")) {
                        ip_addr = String.valueOf(ip_address.getText());
                        port = Integer.parseInt(String.valueOf(portno.getText()));
                    }
                }
            })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Create the ServerDetailsDialog object and return it
        return builder.create();
    }
}
