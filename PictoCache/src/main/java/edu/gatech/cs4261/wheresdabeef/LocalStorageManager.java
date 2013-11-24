package edu.gatech.cs4261.wheresdabeef;

import android.content.Context;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by jonathan on 10/31/13.
 */
public class LocalStorageManager {
    private static final String subcriptionFile = "subscriptions";

    public static void saveSubscriptions(Context context, ArrayList<String> subscriptions) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(context.openFileOutput(subcriptionFile, Context.MODE_PRIVATE));
            objectOutputStream.writeObject(subscriptions);
            objectOutputStream.close();
        }
        catch (Exception e) {
            Toast.makeText(context, "An error occurred", 5).show();
        }
    }

    public static ArrayList<String> loadSubscriptions(Context context) {
        ArrayList<String> subscriptions = null;


        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(context.openFileInput(subcriptionFile));
            subscriptions = (ArrayList) objectInputStream.readObject();
            objectInputStream.close();
        }
        catch (FileNotFoundException e) {
            saveSubscriptions(context, new ArrayList<String>());
        }
        catch (Exception e) {
            Toast.makeText(context, "An error occurred", 5).show();
        }
        if (subscriptions == null) {
            return new ArrayList<String>();
        }
        else {
            return subscriptions;
        }
    }
}
