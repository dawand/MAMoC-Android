package uk.ac.standrews.cs.mamoc_client.Execution;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler {

    private final Context myContext;
    private final String LINE_SEPARATOR = "\n";
    private final String sourceDir;
    private final String packageID;

    public ExceptionHandler(Context context, String sourceDir, String packageID) {
        this.myContext = context;
        this.sourceDir = sourceDir;
        this.packageID = packageID;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        Log.e("error", exception.getLocalizedMessage());

        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

        Log.e("com.njlabs.showjava", errorReport.toString());

        Toast.makeText(myContext, "There was an error decompiling this app. Showing incomplete source.", Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(myContext, JavaExplorer.class);
//        intent.putExtra("java_source_dir", sourceDir);
//        intent.putExtra("package_id", packageID);
//        myContext.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

}