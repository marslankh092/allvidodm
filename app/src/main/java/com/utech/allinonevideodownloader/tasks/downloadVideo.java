package com.utech.allinonevideodownloader.tasks;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.utech.allinonevideodownloader.R;
import com.utech.allinonevideodownloader.models.LikeeVideoDownloader;
import com.utech.allinonevideodownloader.models.TikTokNoWaterMarkApi;
import com.utech.allinonevideodownloader.models.TopBuzzDownloader;
import com.utech.allinonevideodownloader.models.TwitterVideoDownloader;
import com.utech.allinonevideodownloader.models.VideoModel;
import com.utech.allinonevideodownloader.models.VimeoVideoDownloader;
import com.utech.allinonevideodownloader.utils.Constants;
import com.utech.allinonevideodownloader.utils.VollySingltonClass;
import com.utech.allinonevideodownloader.utils.iUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.WINDOW_SERVICE;
import static com.utech.allinonevideodownloader.utils.Constants.LikeeApiUrl;
import static com.utech.allinonevideodownloader.utils.Constants.TiktokApi;
import static com.utech.allinonevideodownloader.utils.Constants.TiktokApiNowatermark;
import static com.utech.allinonevideodownloader.utils.iUtils.getFilenameFromURL;

public class downloadVideo {


    public static Context Mcontext;
    public static ProgressDialog pd;
    public static Dialog dialog;
    public static SharedPreferences prefs;
    public static Boolean fromService;
    static String SessionID, Title;
    static int error = 1;
    static LinearLayout mainLayout;
    static Dialog dialogquality;
    static WindowManager windowManager2;
    static WindowManager.LayoutParams params;
    static View mChatHeadView;
    static ImageView img_dialog;
    static ArrayList dataModelArrayList;

    static String myURLIS = "";
    static Dialog dialog_quality_allvids;

    public static void Start(final Context context, String url, Boolean service) {

        Mcontext = context;
        fromService = service;
        Log.i("LOGClipboard111111 clip", "work 2");
//SessionID=title;
//        if (!url.startsWith("http://") && !url.startsWith("https://")) {
//            url = "http://" + url;
//        }
        if (!fromService) {
            pd = new ProgressDialog(context);
            pd.setMessage(Mcontext.getResources().getString(R.string.genarating_download_link));
            pd.setCancelable(false);
            pd.show();
        }
        if (url.contains("tiktok.com")) {


            String myurlis = url;
            final Dialog dialog = new Dialog(Mcontext);


            windowManager2 = (WindowManager) Mcontext.getSystemService(WINDOW_SERVICE);

            int size = 0;

            try {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) Mcontext).getWindowManager()
                        .getDefaultDisplay()
                        .getMetrics(displayMetrics);

                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;

                size = width / 2;

            } catch (Exception e) {
                size = WindowManager.LayoutParams.WRAP_CONTENT;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params = new WindowManager.LayoutParams(
                        size,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                        PixelFormat.TRANSLUCENT);

                params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                params.x = 0;
                params.y = 100;
            } else {
                params = new WindowManager.LayoutParams(
                        size,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                        PixelFormat.TRANSLUCENT);

                params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                params.x = 0;
                params.y = 100;
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            } else {
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
            }
            dialog.getWindow().setAttributes(params);


            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.custom_dialog_tiktok);
            dialog.findViewById(R.id.txt_with).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    getAllData(myurlis, "true");
                    dialog.dismiss();
                }
            });
            dialog.findViewById(R.id.txt_without).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    getAllData(myurlis, "false");
                    dialog.dismiss();
                }
            });
            dialog.findViewById(R.id.txt_mp3).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {


                    getAllData(myurlis, "mp3");
                    dialog.dismiss();
                }
            });
            dialog.show();

//            getAllData(myurlis, "false");
//            new GetTikTokVideoold().execute(url);
//            try {
//                download(url);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            TiktokVideoDownloader downloader = new TiktokVideoDownloader(Mcontext, url);
//            downloader.DownloadVideo();


            //   getAllDataForLikee(url);

        }
//TODO imgur
        if (url.contains("likee")) {


            LikeeVideoDownloader lIkeeVideoDownloader = new LikeeVideoDownloader(Mcontext, url);
            lIkeeVideoDownloader.DownloadVideo();

            //   getAllDataForLikee(url);
        } else if (url.contains("facebook.com")) {

//String[] Furl = url.split("/");
// url = Furl[Furl.length-1];
//iUtils.ShowToast(Mcontext,Furl[Furl.length-1]);
            new GetFacebookVideo().execute(url);
        } else if (url.contains("instagram.com")) {

            new GetInstagramVideo().execute(url);

            // downloadVideo.getAllDataForLikee(url);
        }

//        else if (url.contains("dailymotion.com")) {
//
//
//            final Dialog dialog_quality_daily = new Dialog(Mcontext);
//            dialog_quality_daily.setContentView(R.layout.dialog_quality_dailymotion);
//
//
//            final RadioGroup radioGroup = dialog_quality_daily.findViewById(R.id.radiogroup_dailymotion);
//            Button btn_Download = dialog_quality_daily.findViewById(R.id.btn_downloadDailymotion);
//
//            final String finalUrl = url;
//            btn_Download.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
////                    if (!fromService) {
////                        pd = new ProgressDialog(context);
////                        pd.setMessage(DOWNLOADING_MSG);
////                        pd.setCancelable(false);
////                        pd.show();
////                    }
//                    dialog_quality_daily.dismiss();
//                    RadioButton btn = null;
//                    String quality = "";
//                    if (radioGroup.getCheckedRadioButtonId() != -1) {
//                        btn = dialog_quality_daily.findViewById(radioGroup.getCheckedRadioButtonId());
//                        quality = btn.getText().toString();
//                    }
//
//
//                    DailyMotionDownloader downloader = new DailyMotionDownloader(Mcontext, quality, finalUrl, 12);
//                    downloader.DownloadVideo();
//                }
//            });
//            dialog_quality_daily.show();
//        }

        else if (url.contains("topbuzz.com")) {

            TopBuzzDownloader downloader = new TopBuzzDownloader(Mcontext, url, 12);
            downloader.DownloadVideo();
//            final Dialog dialog_quality_daily = new Dialog(context);
//            dialog_quality_daily.setContentView(R.layout.dialog_quality_dailymotion);
//
//
//            final RadioGroup radioGroup = dialog_quality_daily.findViewById(R.id.radiogroup_dailymotion);
//            Button btn_Download = dialog_quality_daily.findViewById(R.id.btn_downloadDailymotion);
//
//            final String finalUrl = url;
//            btn_Download.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    RadioButton btn = null;
//                    String quality = "";
//                    if (radioGroup.getCheckedRadioButtonId() != -1) {
//                        btn = dialog_quality_daily.findViewById(radioGroup.getCheckedRadioButtonId());
//                        quality = btn.getText().toString();
//                    }
//
//
//
//                }
//            });
        } else if (url.contains("vimeo.com")) {
            VimeoVideoDownloader downloader = new VimeoVideoDownloader(Mcontext, url);
            downloader.DownloadVideo();

        } else if (url.contains("twitter.com")) {
            TwitterVideoDownloader downloader = new TwitterVideoDownloader(Mcontext, url);
            downloader.DownloadVideo();

        }


        //new


        //working
        else if (url.contains("gag.com")) {
            getAllDataForLikee(url, false);

        } else if (url.contains("buzzfeed.com")) {
            getAllDataForLikee(url, false);

        }

        //TODO Add quality list
        else if (url.contains("flickr.com")) {
            getAllDataForLikee(url, true);

        } else if (url.contains("vk.com")) {

            url = url.replace("//m.", "//");

            getAllDataForLikee(url, true);

        } else if (url.contains("dailymotion.com") || url.contains("dai.ly")) {
            getAllDataForLikee(url, true);

        } else if (url.contains("espn.com")) {
            getAllDataForLikee(url, true);

        } else if (url.contains("mashable.com")) {
            getAllDataForLikee(url, true);

        } else if (url.contains("ted.com")) {
            getAllDataForLikee(url, true);

        } else if (url.contains("twitch.com")) {
            getAllDataForLikee(url, true);

        } else if (url.contains("imdb.com")) {
            getAllDataForLikee(url, false);

        } else if (url.contains("imgur.com")) {
            url = url.replace("//m.", "//");
            getAllDataForLikee(url, false);

        } else if (url.contains("tumblr.com")) {
            getAllDataForLikee(url, false);

        } else if (url.contains("igtv.com")) {
            getAllDataForLikee(url, false);

        }


//TODO youtube from here
        else if (url.contains("youtube.com") || url.contains("youtu.be")) {
            //  String youtubeLink = "https://www.youtube.com/watch?v=668nUCeBHyY";
            if (Constants.showyoutube) {
                Log.i("LOGClipboard111111 clip", "work 3");
                getYoutubeDownloadUrl(url);
            } else {
                if (!fromService) {
                    pd.dismiss();

                    Handler mHandler = new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(Message message) {
                            iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));

                        }
                    };
                }
            }

        }

//TODO Till Here
        else {
            if (!fromService) {
                pd.dismiss();

                Handler mHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));

                    }
                };
            }
        }

//iUtils.ShowToast(Mcontext,url);
//iUtils.ShowToast(Mcontext,SessionID);


        prefs = Mcontext.getSharedPreferences("AppConfig", MODE_PRIVATE);
    }

    //TODO youtube comment them from here

    private static void getYoutubeDownloadUrl(String youtubeLink) {

        new YouTubeExtractor(Mcontext) {

            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                //    mainProgressBar.setVisibility(View.GONE);

                if (ytFiles != null) {

                    if (!fromService) {
                        pd.dismiss();
                    }


                    windowManager2 = (WindowManager) Mcontext.getSystemService(WINDOW_SERVICE);
                    LayoutInflater layoutInflater = (LayoutInflater) Mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    mChatHeadView = layoutInflater.inflate(R.layout.dialog_quality_ytd, null);

                    mainLayout = mChatHeadView.findViewById(R.id.linlayout_dialog);

                    img_dialog = mChatHeadView.findViewById(R.id.img_dialog);


                    dialogquality = new Dialog(Mcontext);
                    dialogquality.setContentView(R.layout.dialog_quality_ytd);
                    mainLayout = dialogquality.findViewById(R.id.linlayout_dialog);
                    img_dialog = dialogquality.findViewById(R.id.img_dialog);


                    int size = 0;

                    try {
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        ((Activity) Mcontext).getWindowManager()
                                .getDefaultDisplay()
                                .getMetrics(displayMetrics);

                        int height = displayMetrics.heightPixels;
                        int width = displayMetrics.widthPixels;

                        size = width / 2;

                    } catch (Exception e) {
                        size = WindowManager.LayoutParams.WRAP_CONTENT;
                    }


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        params = new WindowManager.LayoutParams(
                                size,
                                WindowManager.LayoutParams.WRAP_CONTENT,
                                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                                PixelFormat.TRANSLUCENT);

                        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                        params.x = 0;
                        params.y = 100;
                    } else {
                        params = new WindowManager.LayoutParams(
                                size,
                                WindowManager.LayoutParams.WRAP_CONTENT,
                                WindowManager.LayoutParams.TYPE_PHONE,
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                                PixelFormat.TRANSLUCENT);

                        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                        params.x = 0;
                        params.y = 100;
                    }


                    // mainLayout.setLayoutParams(params);


                    for (int i = 0, itag; i < ytFiles.size(); i++) {
                        itag = ytFiles.keyAt(i);
                        // ytFile represents one file with its url and meta data
                        YtFile ytFile = ytFiles.get(itag);

                        // Just add videos in a decent format => height -1 = audio
                        if (ytFile.getFormat().getHeight() == -1 || ytFile.getFormat().getHeight() >= 360) {
                            addButtonToMainLayouttest(vMeta.getTitle(), ytFile);
                        }
                    }

                    img_dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogquality.dismiss();
                        }
                    });

                    dialogquality.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                    dialogquality.getWindow().setAttributes(params);
                    //  dialogquality.getWindow().setColorMode(Mcontext.getResources().getColor(R.color.colorAccent));

                    dialogquality.show();

//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//
//                        int size = 0;
//
//                        try {
//                            DisplayMetrics displayMetrics = new DisplayMetrics();
//                            ((Activity) Mcontext).getWindowManager()
//                                    .getDefaultDisplay()
//                                    .getMetrics(displayMetrics);
//
//                            int height = displayMetrics.heightPixels;
//                            int width = displayMetrics.widthPixels;
//
//                            size = width / 2;
//
//                        } catch (Exception e) {
//                            size = WindowManager.LayoutParams.WRAP_CONTENT;
//                        }
//
//                        params = new WindowManager.LayoutParams(
//                                size,
//                                WindowManager.LayoutParams.WRAP_CONTENT,
//                                WindowManager.LayoutParams.TYPE_PHONE,
//                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//                                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                                PixelFormat.TRANSLUCENT);
//
//                        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
//                        params.x = 0;
//                        params.y = 100;
//                        windowManager2 = (WindowManager) Mcontext.getSystemService(WINDOW_SERVICE);
//
//
//                        for (int i = 0, itag; i < ytFiles.size(); i++) {
//                            itag = ytFiles.keyAt(i);
//                            // ytFile represents one file with its url and meta data
//                            YtFile ytFile = ytFiles.get(itag);
//
//                            // Just add videos in a decent format => height -1 = audio
//                            if (ytFile.getFormat().getHeight() == -1 || ytFile.getFormat().getHeight() >= 360) {
//                                addButtonToMainLayout(vMeta.getTitle(), ytFile);
//                            }
//                        }
//
//
//                        img_dialog.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                if (windowManager2 != null) {
//                                    try {
//                                        windowManager2.removeView(mChatHeadView);
//                                    } catch (Exception e) {
//                                        Log.i("LOGClipboard111111", "error is " + e.getMessage());
//                                        //  windowManager2.addView(mChatHeadView, params);
//                                    }
//                                }
//                            }
//                        });
//
//
//                        if (mainLayout.isShown()) {
//                            windowManager2.updateViewLayout(mainLayout, params);
//
//                        } else {
//
//                            // add overlay
//                            windowManager2.addView(mChatHeadView, params);
//                        }
//
//
//                    }
//                    else {
//
//
//
//                        int size = 0;
//
//                        try {
//                            DisplayMetrics displayMetrics = new DisplayMetrics();
//                            ((Activity) Mcontext).getWindowManager()
//                                    .getDefaultDisplay()
//                                    .getMetrics(displayMetrics);
//
//                            int height = displayMetrics.heightPixels;
//                            int width = displayMetrics.widthPixels;
//
//                            size = width / 2;
//
//                        } catch (Exception e) {
//                            size = WindowManager.LayoutParams.WRAP_CONTENT;
//                        }
//                        params = new WindowManager.LayoutParams(
//                                size,
//                                WindowManager.LayoutParams.WRAP_CONTENT,
//                                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//                                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                                PixelFormat.TRANSLUCENT);
//
//
//                        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
//                        params.x = 0;
//                        params.y = 100;
//                        windowManager2 = (WindowManager) Mcontext.getSystemService(WINDOW_SERVICE);
//
//
//                        for (int i = 0, itag; i < ytFiles.size(); i++) {
//                            itag = ytFiles.keyAt(i);
//                            // ytFile represents one file with its url and meta data
//                            YtFile ytFile = ytFiles.get(itag);
//
//                            // Just add videos in a decent format => height -1 = audio
//                            if (ytFile.getFormat().getHeight() == -1 || ytFile.getFormat().getHeight() >= 360) {
//                                addButtonToMainLayout(vMeta.getTitle(), ytFile);
//                            }
//                        }
//
//
//                        img_dialog.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                if (windowManager2 != null) {
//                                    try {
//                                        windowManager2.removeView(mChatHeadView);
//                                    } catch (Exception e) {
//                                        Log.i("LOGClipboard111111", "error is " + e.getMessage());
//                                        //     windowManager2.addView(mChatHeadView, params);
//                                    }
//                                }
//                            }
//                        });
//
//
//                        if (mainLayout.isShown()) {
//                            windowManager2.updateViewLayout(mainLayout, params);
//
//                        } else {
//
//                            // add overlay
//                            windowManager2.addView(mChatHeadView, params);
//                        }
//
//
//                    }
//


                    // int itag = 22;
                    // Here you can get download url
//                    for (int i = 0, itag; i < ytFiles.size(); i++) {
//                        itag = ytFiles.keyAt(i);
//                        // ytFile represents one file with its url and meta data
//                        YtFile ytFile = ytFiles.get(itag);
//
//                        // Just add videos in a decent format => height -1 = audio
//                        if (ytFile.getFormat().getHeight() == -1 || ytFile.getFormat().getHeight() >= 360) {
//                            addButtonToMainLayout(vMeta.getTitle(), ytFile);
//                        }
//                    }

                    // dialogquality.show();
                } else {
                    if (!fromService) {
                        pd.dismiss();
                    }

                    Handler mHandler = new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(Message message) {
                            iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));

                        }
                    };

                }


            }
        }.extract(youtubeLink, true, false);
    }

    private static void addButtonToMainLayout(final String videoTitle, final YtFile ytfile) {


        // Display some buttons and let the user choose the format
        String btnText = (ytfile.getFormat().getHeight() == -1) ? "Audio " +
                ytfile.getFormat().getAudioBitrate() + " kbit/s" :
                ytfile.getFormat().getHeight() + "p";
        btnText += (ytfile.getFormat().isDashContainer()) ? " dash" : "";
        Button btn = new Button(Mcontext);

        btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        btn.setText(btnText);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (windowManager2 != null) {
                    try {
                        windowManager2.removeView(mChatHeadView);
                    } catch (Exception e) {
                        Log.i("LOGClipboard111111", "error is " + e.getMessage());

                    }
                }

                String filename;
                if (videoTitle.length() > 55) {
                    filename = videoTitle.substring(0, 55) + "." + ytfile.getFormat().getExt();
                } else {
                    filename = videoTitle + "." + ytfile.getFormat().getExt();
                }
                filename = filename.replaceAll("[\\\\><\"|*?%:#/]", "");

//                downloadFromUrl(ytfile.getUrl(), videoTitle, filename);
//
//
//                String downloadUrl = ytFiles.get(itag).getUrl();


                new downloadFile().Downloading(Mcontext, ytfile.getUrl(), filename, ".mp4");


                dialogquality.dismiss();
            }
        });
        mainLayout.addView(btn);
    }

    private static void addButtonToMainLayouttest(final String videoTitle, final YtFile ytfile) {


        // Display some buttons and let the user choose the format
        String btnText = (ytfile.getFormat().getHeight() == -1) ? "MP3 " +
                ytfile.getFormat().getAudioBitrate() + " kbit/s" :
                ytfile.getFormat().getHeight() + "p";
        btnText += (ytfile.getFormat().isDashContainer()) ? " No Audio" : "";
        Button btn = new Button(Mcontext);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 8, 8, 8);
        btn.setLayoutParams(params);

        // btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btn.setBackground(Mcontext.getResources().getDrawable(R.drawable.btn_bg_download_screen));
        btn.setTextColor(Color.WHITE);

        btn.setText(btnText);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (windowManager2 != null) {
                    try {
                        windowManager2.removeView(mChatHeadView);
                    } catch (Exception e) {
                        Log.i("LOGClipboard111111", "error is " + e.getMessage());

                    }
                }

                String filename;
                if (videoTitle.length() > 55) {
                    filename = videoTitle.substring(0, 55);
                } else {
                    filename = videoTitle;
                }
                filename = filename.replaceAll("[\\\\><\"|*?%:#/]", "");

//                downloadFromUrl(ytfile.getUrl(), videoTitle, filename);
//
//
//                String downloadUrl = ytFiles.get(itag).getUrl();


                if (ytfile.getFormat().getExt().equals("m4a")) {
                    new downloadFile().Downloading(Mcontext, ytfile.getUrl(), filename, ".mp3");
                } else {
                    new downloadFile().Downloading(Mcontext, ytfile.getUrl(), filename, "." + ytfile.getFormat().getExt());

                }

                dialogquality.dismiss();
            }
        });
        mainLayout.addView(btn);
    }


    //TODO youtube comment till here

    public static void download(String url12) {
        String readLine;
        URL url = null;
        try {
            url = new URL(url12);


            Log.d("ThumbnailURL11111_1 ", url12);


//        URLConnection openConnection = url.openConnection();
//        openConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            //       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openConnection.getInputStream()));


            URL url1 = new URL(url12);
            URLConnection connection = url1.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));


            while ((readLine = bufferedReader.readLine()) != null) {
                //  readLine = bufferedReader.readLine();
                Log.d("ThumbnailURL11111_2  ", readLine);


                readLine = readLine.substring(readLine.indexOf("VideoObject"));
                String substring = readLine.substring(readLine.indexOf("thumbnailUrl") + 16);
                substring = substring.substring(0, substring.indexOf("\""));

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("ThumbnailURL: ");
                stringBuilder.append(substring);

                Log.d("ThumbnailURL", substring);
                readLine = readLine.substring(readLine.indexOf("contentUrl") + 13);
                readLine = readLine.substring(0, readLine.indexOf("?"));
                stringBuilder = new StringBuilder();
                stringBuilder.append("ContentURL: ");
                stringBuilder.append(readLine);

                Log.d("ContentURL1111 thumb  ", substring);
                Log.d("ContentURL1111", stringBuilder.toString());


                if (readLine == null) {
                    break;
                }
            }
            bufferedReader.close();

        } catch (Exception e) {
//            Log.d("ContentURL1111 errrr", e.getMessage());
            e.printStackTrace();
        }
        // new downloadFile().Downloading(Mcontext, URL, Title, ".mp4");
        //   new DownloadFileFromURL().execute(new String[]{readLine});
    }

    private static class GetTikTokVideoold extends AsyncTask<String, Void, Document> {
        Document doc;
        Handler mHandler;

        @Override
        protected Document doInBackground(String... urls) {
            try {
                doc = Jsoup.connect(urls[0]).get();

                Log.d("ThumbnailURL11111_1 ", doc.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Error");
            }
            return doc;
        }

        protected void onPostExecute(Document result) {
// pd.dismiss();
// Log.d("GetResult", );
            try {
                String URL = result.select("link[rel=\"canonical\"]").last().attr("href");

                if (!URL.equals("") && URL.contains("video/")) {
                    URL = URL.split("video/")[1];
                    Title = result.title();
// iUtils.ShowToast(Mcontext,URL);
                    Log.d("ThumbnailURL11111_1 ", URL);

                    new DownloadTikTokVideo().execute(URL);
                } else {
                    if (!fromService) {

                        pd.dismiss();
                    }
                    System.out.println("mydataerror is0 ");

                    mHandler = new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(Message message) {
                            iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));

                        }
                    };
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
                if (!fromService) {
                    System.out.println("mydataerror is " + e.getMessage());

                    pd.dismiss();
                }
                mHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));

                    }
                };
            }


        }
    }

    private static class GetFacebookVideo extends AsyncTask<String, Void, Document> {
        Document doc;
        Handler mHandler;

        @Override
        protected Document doInBackground(String... urls) {
            try {

//doc = Jsoup.connect(FacebookApi).data("v",urls[0]).get();
                doc = Jsoup.connect(urls[0]).get();
            } catch (Exception e) {
                e.printStackTrace();

                mHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));

                    }
                };
                Log.d(TAG, "doInBackground: Error");

            }
            return doc;

        }

        protected void onPostExecute(Document result) {
            if (!fromService) {

                pd.dismiss();
            }
// Log.d("GetResult", );
            try {
                String URL = result.select("meta[property=\"og:video\"]").last().attr("content");
                Title = result.title();
// iUtils.ShowToast(Mcontext,URL);
                new downloadFile().Downloading(Mcontext, URL, Title, ".mp4");
            } catch (NullPointerException e) {
                e.printStackTrace();

                mHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));

                    }
                };
            }
// new DownloadTikTokVideo().execute(URL);

        }
    }

    public static void getAllDataForLikee(String urlp, boolean hasQualityOption) {

        System.out.println("resccccccccURL " + urlp);
        //  url = "https://vm.tiktok.com/KEpK7n/";


        System.out.println("resccccccccmyurl is " + LikeeApiUrl + urlp);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, LikeeApiUrl + urlp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("rescccccccc " + response + "     myurl is " + LikeeApiUrl + urlp);

                ArrayList<VideoModel> videoModelArrayList = new ArrayList<>();

                try {
                    JSONObject jSONObject = new JSONObject(response);

                    String videotitleis = jSONObject.getString("title");

                    JSONArray str = jSONObject.getJSONArray("links");


                    for (int i = 0; i < str.length(); i++) {
                        VideoModel videoModel = new VideoModel();
                        JSONObject jSONObject2 = str.getJSONObject(i);
                        videoModel.setTitle(videotitleis);
                        videoModel.setUrl(jSONObject2.getString("url"));
                        videoModel.setType(jSONObject2.getString("type"));
                        videoModel.setSize(jSONObject2.getString("size"));
                        videoModel.setQuality(jSONObject2.getString("quality"));

                        videoModelArrayList.add(videoModel);

                    }

                    if (hasQualityOption) {

                        dialog_quality_allvids = new Dialog(Mcontext);


                        if (!fromService) {
                            pd.dismiss();
                        }


                        windowManager2 = (WindowManager) Mcontext.getSystemService(WINDOW_SERVICE);
                        LayoutInflater layoutInflater = (LayoutInflater) Mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        mChatHeadView = layoutInflater.inflate(R.layout.dialog_quality_ytd, null);


                        dialog_quality_allvids.setContentView(mChatHeadView);


                        mainLayout = mChatHeadView.findViewById(R.id.linlayout_dialog);

                        img_dialog = mChatHeadView.findViewById(R.id.img_dialog);

                        mainLayout = dialog_quality_allvids.findViewById(R.id.linlayout_dialog);
                        img_dialog = dialog_quality_allvids.findViewById(R.id.img_dialog);


                        int size = 0;

                        try {
                            DisplayMetrics displayMetrics = new DisplayMetrics();
                            ((Activity) Mcontext).getWindowManager()
                                    .getDefaultDisplay()
                                    .getMetrics(displayMetrics);

                            int height = displayMetrics.heightPixels;
                            int width = displayMetrics.widthPixels;

                            size = width / 2;

                        } catch (Exception e) {
                            size = WindowManager.LayoutParams.WRAP_CONTENT;
                        }


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            params = new WindowManager.LayoutParams(
                                    size,
                                    WindowManager.LayoutParams.WRAP_CONTENT,
                                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                                    PixelFormat.TRANSLUCENT);

                            params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                            params.x = 0;
                            params.y = 100;
                        } else {
                            params = new WindowManager.LayoutParams(
                                    size,
                                    WindowManager.LayoutParams.WRAP_CONTENT,
                                    WindowManager.LayoutParams.TYPE_PHONE,
                                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                                    PixelFormat.TRANSLUCENT);

                            params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                            params.x = 0;
                            params.y = 100;
                        }


                        // mainLayout.setLayoutParams(params);


                        for (int i = 0; i < videoModelArrayList.size(); i++) {


                            addButtonToMainLayouttest_allvideo(videoModelArrayList.get(i).getQuality(), videoModelArrayList.get(i).getUrl(), videoModelArrayList.get(i).getTitle());

                        }

                        img_dialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog_quality_allvids.dismiss();
                            }
                        });

                        dialog_quality_allvids.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                        dialog_quality_allvids.getWindow().setAttributes(params);
                        //  dialogquality.getWindow().setColorMode(Mcontext.getResources().getColor(R.color.colorAccent));

                        dialog_quality_allvids.show();


                        dialog_quality_allvids.show();
                    } else {


                        new downloadFile().Downloading(Mcontext, videoModelArrayList.get(0).getUrl(), getFilenameFromURL(videoModelArrayList.get(0).getUrl()), ".mp4");
                    }
                    if (!fromService) {
                        pd.dismiss();


                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.starting_download));


                    }


                } catch (Exception str2) {
                    str2.printStackTrace();
                    // Toast.makeText(Mcontext, "Invalid URL", 0).show();

                    if (!fromService) {
                        pd.dismiss();
                        iUtils.ShowToast(Mcontext, response + ":   " + Mcontext.getResources().getString(R.string.invalid_url));
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("i ah error " + error.getMessage());

                if (!fromService) {
                    pd.dismiss();

                    Handler mHandler = new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(Message message) {
                            iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));

                        }
                    };
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<String, String>();

                //  params.put("url", urlp);


                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VollySingltonClass.getmInstance(Mcontext).addToRequsetque(stringRequest);


    }

    private static class DownloadTikTokVideo extends AsyncTask<String, Void, Document> {
        Document doc;
        private Handler mHandler;

        @Override
        protected Document doInBackground(String... urls) {
            try {
                Map<String, String> Headers = new HashMap<String, String>();
                Headers.put("Cookie", "1");
                Headers.put("User-Agent", "1");
                Headers.put("Accept", "application/json");
                Headers.put("Host", "api2.musical.ly");
                Headers.put("Connection", "keep-alive");
                doc = Jsoup.connect(TiktokApi).data("aweme_id", urls[0]).ignoreContentType(true).headers(Headers).get();
                System.out.println("mydataerror is doc i  " + doc);

            } catch (Exception e) {
                //  System.out.println("mydataerror is "+e.getMessage());
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Error");
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                System.out.println("mydataerror is " + e.getMessage());

            }
            return doc;

        }

        protected void onPostExecute(Document result) {
            System.out.println("mydataerror is result " + result);

            if (!fromService) {

                pd.dismiss();
            }
            String URL = result.body().toString().replace("<body>", "").replace("</body>", "");
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(URL);
                String URLs = jsonObject.getJSONObject("aweme_detail").getJSONObject("video").getJSONObject("play_addr").getJSONArray("url_list").getString(0);

                new downloadFile().Downloading(Mcontext, URLs, Title, ".mp4");
// iUtils.ShowToast(Mcontext,URLs);

            } catch (JSONException e) {
                System.out.println("mydataerror is " + e.getMessage());

                Log.d("Error", e.toString());
                mHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));

                    }
                };
            }


        }
    }


    private static class GetTikTokVideo extends AsyncTask<String, Void, Document> {
        Document doc;

        private GetTikTokVideo() {
        }

        protected Document doInBackground(String... strArr) {
            try {
                this.doc = Jsoup.connect(strArr[0]).get();
            } catch (Exception se) {
                se.printStackTrace();
                Log.d("ContentValues", "doInBackground: Error");
            }
            return this.doc;
        }

        protected void onPostExecute(Document document) {
            String str = "Something went wrong!";
            downloadVideo.pd.dismiss();
            try {
                String html = document.select("script[id=\"videoObject\"]").last().html();
//                String html2 = document.select("script[id=\"_NEXT_DATA_\"]").last().html();
                if (html.equals("")) {
                    if (fromService != null) {
                        downloadVideo.pd.dismiss();
                    }
                    iUtils.ShowToast(downloadVideo.Mcontext, str);
                    return;
                }
                JSONObject jSONObject = new JSONObject(html);
                //   JSONObject jSONObject2 = new JSONObject(html2);
                downloadVideo.Title = document.title();

                Log.d("ContentValues1 ", jSONObject.getString("contentUrl"));
                //   Log.d("ContentValues2 ", jSONObject2.getString("contentUrl"));
//                 if (true) {
//                     downloadFile.Downloading(downloadVideo.Mcontext, jSONObject2.getString("contentUrl"), downloadVideo.Title, ".mp4");
//
//                 }else {
//                     downloadFile.Downloading(downloadVideo.Mcontext, jSONObject.getString("contentUrl"), downloadVideo.Title, ".mp4");
//                 }


            } catch (Exception e) {
                e.printStackTrace();
                if (fromService != null) {
                    downloadVideo.pd.dismiss();
                }
                iUtils.ShowToast(downloadVideo.Mcontext, str);
            }
        }
    }


    public static void getAllData(String url, String watermark) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, TiktokApiNowatermark, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("rescccccccc " + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.getString("status").equals("success")) {

                        Gson gson = new Gson();
                        TikTokNoWaterMarkApi tikTokNoWaterMarkApidata = gson.fromJson(jsonObject.toString(), TikTokNoWaterMarkApi.class);

                        System.out.println("resccccccccdataFull_Vide " + tikTokNoWaterMarkApidata.video_full_title);
                        System.out.println("resccccccccdataORG " + tikTokNoWaterMarkApidata.ogvideourl);

                        if (watermark.equals("true")) {
                            new downloadFile().Downloading(Mcontext, tikTokNoWaterMarkApidata.ogvideourl, tikTokNoWaterMarkApidata.video_full_title + "_" + tikTokNoWaterMarkApidata.username, ".mp4");

                            if (!fromService) {
                                pd.dismiss();

                                Handler mHandler = new Handler(Looper.getMainLooper()) {
                                    @Override
                                    public void handleMessage(Message message) {
                                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.starting_download));

                                    }
                                };
                            }

                        } else if (watermark.equals("false") && tikTokNoWaterMarkApidata.watermark_removed.equals("yes")) {


                            new downloadFile().Downloading(Mcontext, tikTokNoWaterMarkApidata.videourl, tikTokNoWaterMarkApidata.video_full_title + "_" + tikTokNoWaterMarkApidata.username, ".mp4");
                            //   new DownloadFileFromURL(Mcontext,tikTokNoWaterMarkApidata.name).execute(tikTokNoWaterMarkApidata.videourl);


                            if (!fromService) {
                                pd.dismiss();

                                Handler mHandler = new Handler(Looper.getMainLooper()) {
                                    @Override
                                    public void handleMessage(Message message) {
                                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.starting_download));

                                    }
                                };
                            }

                        } else if (watermark.equals("mp3")) {


                            new downloadFile().Downloading(Mcontext, tikTokNoWaterMarkApidata.musicplayurl, tikTokNoWaterMarkApidata.video_full_title + "_" + tikTokNoWaterMarkApidata.username, ".mp3");


                            if (!fromService) {
                                pd.dismiss();

                                Handler mHandler = new Handler(Looper.getMainLooper()) {
                                    @Override
                                    public void handleMessage(Message message) {
                                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.starting_download));

                                    }
                                };
                            }


                        }


                    } else {
                        if (!fromService) {
                            pd.dismiss();

                            Handler mHandler = new Handler(Looper.getMainLooper()) {
                                @Override
                                public void handleMessage(Message message) {
                                    iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));

                                }
                            };
                        }
                    }


                } catch (Exception e) {
                    if (!fromService) {
                        pd.dismiss();

                        Handler mHandler = new Handler(Looper.getMainLooper()) {
                            @Override
                            public void handleMessage(Message message) {
                                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));

                            }
                        };
                    }
                    System.out.println("i ah error " + e.getMessage());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("i ah error " + error.getMessage());

                if (!fromService) {
                    pd.dismiss();

                    Handler mHandler = new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(Message message) {
                            iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));

                        }
                    };
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<String, String>();
                params.put("tikurl", url);

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VollySingltonClass.getmInstance(Mcontext).addToRequsetque(stringRequest);


    }

    private static void addButtonToMainLayouttest_allvideo(final String videoTitle, String ytfile, String video_title) {


        // Display some buttons and let the user choose the format
        String btnText = videoTitle;
        Button btn = new Button(Mcontext);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 8, 8, 8);
        btn.setLayoutParams(params);

        // btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btn.setBackground(Mcontext.getResources().getDrawable(R.drawable.btn_bg_download_screen));
        btn.setTextColor(Color.WHITE);

        btn.setText(btnText);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (windowManager2 != null) {
                    try {
                        windowManager2.removeView(mChatHeadView);
                    } catch (Exception e) {
                        Log.i("LOGClipboard111111", "error is " + e.getMessage());

                    }
                }


//                downloadFromUrl(ytfile.getUrl(), videoTitle, filename);
//
//
//                String downloadUrl = ytFiles.get(itag).getUrl();


                new downloadFile().Downloading(Mcontext, ytfile, video_title + "_" + videoTitle, ".mp4");


                dialog_quality_allvids.dismiss();
            }
        });
        mainLayout.addView(btn);
    }

    private static class GetInstagramVideo extends AsyncTask<String, Void, Document> {
        Document doc;
        private Handler mHandler;

        @Override
        protected Document doInBackground(String... urls) {
            try {
                doc = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Error");
            }
            return doc;

        }

        protected void onPostExecute(Document result) {
            if (!fromService) {

                pd.dismiss();
            }
// Log.d("GetResult", );
            try {
                String URL = result.select("meta[property=\"og:video\"]").last().attr("content");
                Title = result.title();
//iUtils.ShowToast(Mcontext, URL);

                new downloadFile().Downloading(Mcontext, URL, Title + ".mp4", ".mp4");
            } catch (NullPointerException e) {
                e.printStackTrace();
                mHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));

                    }
                };
            }
        }
    }


}
