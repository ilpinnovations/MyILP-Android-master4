package com.ilp.ilpschedule.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.ilp.ilpschedule.R;
 
public class AlertDialogManager {
    /**
     * Function to display simple Alert Dialog
     * @param context - application context
     * @param title - alert dialog title
     * @param message - alert message
     * @param status - success/failure (used to set icon)
     *               - pass null if you don't want icon
     * */
    public void showAlertDialog(Context context, String title, String message,
            Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
 
        // Setting Dialog Title
        alertDialog.setTitle(title);
 
        // Setting Dialog Message
        alertDialog.setMessage(message);
 
        if(status != null)
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
 
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
    }
    
    public void showAlertDialogWithResp(final Context context,String title, String message,
            Boolean status, final String action) {
    	
    	 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
    	 
    	 alertDialogBuilder.setTitle(title);
  
    	 alertDialogBuilder.setMessage(message);
         
    	 alertDialogBuilder.setCancelable(false);
         
    	 
    	 alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int id) {
                     Intent intent = new Intent(action);
                     context.startActivity(intent);                  
             }
         });
    	 
    	 alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int id) {
                 dialog.cancel();
             }
         });
  
         if(status != null)
        	 alertDialogBuilder.setIcon((status) ? R.drawable.success : R.drawable.fail);
  
    	
  
    	 alertDialogBuilder.show();
    	
    }
    
}