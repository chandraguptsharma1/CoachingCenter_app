package com.cashpor.coachingcenter_app.helper;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.cashpor.coachingcenter_app.model.AppVersionData;

import java.io.File;

public class AppUpdateHelper {

    public static boolean isUpdateAvailable(int currentVersionCode, int latestVersionCode) {
        return latestVersionCode > currentVersionCode;
    }

    public static void showUpdateDialog(Context context, AppVersionData data) {
        boolean isForceUpdate = data.getForceUpdate() == 1;

        String notes = data.getReleaseNotes();
        if (notes == null || notes.trim().isEmpty()) {
            notes = "Bug fixes and improvements";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("New Update Available");
        builder.setMessage(
                "A new version of Coaching Center App is available.\n\n" +
                        "Latest Version: " + data.getLatestVersion() + "\n" +
                        "Version Code: " + data.getVersionCode() + "\n\n" +
                        "What's New:\n" + notes
        );

        builder.setPositiveButton("Download & Install", (dialog, which) -> {
            downloadAndInstallApk(context, data.getApkUrl(), data.getLatestVersion());
        });

        if (!isForceUpdate) {
            builder.setNegativeButton("Later", (dialog, which) -> dialog.dismiss());
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(!isForceUpdate);
        alertDialog.setCanceledOnTouchOutside(!isForceUpdate);
        alertDialog.show();
    }

    private static void downloadAndInstallApk(Context context, String apkUrl, String versionName) {
        try {
            String fileName = "coaching_center_v" + versionName + ".apk";

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
            request.setTitle("Downloading Update");
            request.setDescription("Downloading latest app version...");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setAllowedOverMetered(true);
            request.setAllowedOverRoaming(true);
            request.setMimeType("application/vnd.android.package-archive");
            request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName);

            DownloadManager downloadManager =
                    (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            long downloadId = downloadManager.enqueue(request);

            Toast.makeText(context, "Download started...", Toast.LENGTH_SHORT).show();

            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context ctx, Intent intent) {
                    long completedDownloadId =
                            intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                    if (completedDownloadId == downloadId) {
                        try {
                            DownloadManager.Query query = new DownloadManager.Query();
                            query.setFilterById(downloadId);
                            Cursor cursor = downloadManager.query(query);

                            if (cursor != null && cursor.moveToFirst()) {
                                int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                                int localUriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);

                                int status = cursor.getInt(statusIndex);

                                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                    String localUri = cursor.getString(localUriIndex);

                                    if (localUri != null) {
                                        Uri fileUri = Uri.parse(localUri);
                                        installApk(context, fileUri, fileName);
                                    } else {
                                        Toast.makeText(context, "Downloaded file not found", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show();
                                }
                            }

                            if (cursor != null) cursor.close();
                        } catch (Exception e) {
                            Toast.makeText(context, "Install failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        try {
                            context.unregisterReceiver(this);
                        } catch (Exception ignored) {
                        }
                    }
                }
            };

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.registerReceiver(receiver,
                        new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                        Context.RECEIVER_NOT_EXPORTED);
            } else {
                context.registerReceiver(receiver,
                        new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            }

        } catch (Exception e) {
            Toast.makeText(context, "Download error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private static void installApk(Context context, Uri downloadedUri, String fileName) {
        try {
            File apkFile;

            if ("file".equalsIgnoreCase(downloadedUri.getScheme())) {
                apkFile = new File(downloadedUri.getPath());
            } else {
                apkFile = new File(
                        context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                        fileName
                );
            }

            Uri contentUri = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".provider",
                    apkFile
            );

            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            installIntent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            context.startActivity(installIntent);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No installer found", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Unable to install APK: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}