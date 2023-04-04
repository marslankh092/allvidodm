package com.utech.allinonevideodownloader

import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.utech.allinonevideodownloader.receiver.Receiver
import com.utech.allinonevideodownloader.services.ClipboardMonitor
import com.utech.allinonevideodownloader.tasks.downloadVideo
import com.utech.allinonevideodownloader.utils.Constants
import com.utech.allinonevideodownloader.utils.IOUtils
import com.utech.allinonevideodownloader.utils.LocaleHelper
import com.utech.allinonevideodownloader.utils.iUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), RewardedVideoAdListener {
    val REQUEST_PERMISSION_CODE = 1001
    val REQUEST_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    lateinit var mAdView: AdView
    var fbAdView: com.facebook.ads.AdView? = null


    private lateinit var mRewardedVideoAd: RewardedVideoAd
    private lateinit var mInterstitialAd: InterstitialAd
    private var NotifyID = 1001

    private var csRunning = false

    lateinit var prefEditor: SharedPreferences.Editor
    lateinit var pref: SharedPreferences

    var fbdView: com.facebook.ads.AdView? = null

    private var fbInterstitialAd: com.facebook.ads.InterstitialAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_main)
        MobileAds.initialize(this)
        mAdView = findViewById(R.id.adView)







        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = getString(R.string.AdmobInterstitial)
        mInterstitialAd.loadAd(AdRequest.Builder().build())


        //fb add

        //ads

        //  addFbAd()

        pref = getSharedPreferences(Constants.PREF_CLIP, 0) // 0 - for private mode
        prefEditor = pref.edit()
        csRunning = pref.getBoolean("csRunning", false)

        createNotificationChannel(
            this,
            NotificationManagerCompat.IMPORTANCE_LOW,
            true,
            getString(R.string.app_name),
            getString(R.string.aio_auto)
        )
//TODO
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + packageName)
                )
                startActivityForResult(intent, 1234)
            }
        }
//TODO
        btnDownload.setOnClickListener { view ->


//TODO Facebook comment the finction call code below

            if (Constants.show_facebookads) {
                addFbAd()
            }

            val url = etURL.text.toString()
            DownloadVideo(url)


        }


        chkAutoDownload.setOnClickListener { view ->

            val checked = chkAutoDownload?.isChecked
            if (!checked!!) {
                chkAutoDownload?.isChecked = false
                stopClipboardMonitor()
            } else {
                showAdDialog()

            }

        }


        changelanguage_btn.setOnClickListener {view ->

            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.dialog_change_language)

            val l_english = dialog.findViewById(R.id.l_english) as TextView
            l_english.setOnClickListener {

                LocaleHelper.setLocale(application, "en")
                window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL


                recreate()
                dialog.dismiss()
            }

            val l_arabic = dialog.findViewById(R.id.l_arabic) as TextView
            l_arabic.setOnClickListener {
                LocaleHelper.setLocale(application, "ar")
                window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
//                    editor.putString("language", "urdu")
//                    editor.apply()


                recreate()
                dialog.dismiss()

            }
            val l_urdu = dialog.findViewById(R.id.l_urdu) as TextView
            l_urdu.setOnClickListener {
                LocaleHelper.setLocale(application, "ur")
                window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
//                    editor.putString("language", "urdu")
//                    editor.apply()


                recreate()
                dialog.dismiss()
            }


            val l_turkey = dialog.findViewById(R.id.l_turkey) as TextView
            l_turkey.setOnClickListener {
                LocaleHelper.setLocale(application, "tr")
                window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
//                    editor.putString("language", "urdu")
//                    editor.apply()


                recreate()
                dialog.dismiss()
            }


            val l_hindi = dialog.findViewById(R.id.l_hindi) as TextView
            l_hindi.setOnClickListener {
                LocaleHelper.setLocale(application, "hi")
                window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
//                    editor.putString("language", "urdu")
//                    editor.apply()


                recreate()
                dialog.dismiss()
            }




            dialog.show()

        }


        if (csRunning) {
            chkAutoDownload.isChecked = true
            startClipboardMonitor()
        } else {
            chkAutoDownload.isChecked = false
            stopClipboardMonitor()
        }



        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this)
        mRewardedVideoAd.rewardedVideoAdListener = this
        loadRewardedVideoAd()

        val random = Random()
        var num = random.nextInt(2)

        Log.e("myhdasbdhf ", num.toString())


        // Get intent, action and MIME type
        val intent = intent
        val action = intent.action
        val type = intent.type

        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                handleSendText(intent) // Handle text being sent
            }
        }


            val adRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)




        if (!isNeedGrantPermission()) {

        }




        bottomnav.setOnNavigationItemSelectedListener {
            when (it.itemId) {


                R.id.action_gallery -> {


                    val intent = Intent(this, GalleryActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.action_statussaver -> {
                    val intent = Intent(this, LoadFragment::class.java)
                    intent.putExtra("name","status")
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.action_extras -> {
                    val intent = Intent(this, LoadFragment::class.java)
                    intent.putExtra("name","extra")
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.action_openwhatsapp -> {

// val intent = Intent(this, MainActivityLivewallpaper::class.java)
//startActivity(intent)
                    val launchIntent = packageManager.getLaunchIntentForPackage("com.whatsapp")
                    if (launchIntent != null) {

                        startActivity(launchIntent)
                        finish()
                    } else {

                        iUtils.ShowToast(
                            this,
                            this.resources.getString(R.string.appnotinstalled)
                        )
                    }


                    true
                }
                else -> false
            }
        }


    }

    override fun onResume() {
        super.onResume()
        bottomnav.selectedItemId = 0
    }

    fun handleSendText(intent: Intent) {
        var url = intent.getStringExtra(Intent.EXTRA_TEXT)

        downloadVideo.Start(this, url, false)

//        val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
//            override fun handleMessage(message: Message) {
//                iUtils.ShowToast(this@MainActivity, "Working On Download")
//            }
//        }
    }




    private fun loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(
            getString(R.string.AdmobRewardID),
            AdRequest.Builder().build()
        )
    }


    fun addFbAd() {

        //ads
        fbInterstitialAd = com.facebook.ads.InterstitialAd(
            this,
            "IMG_16_9_APP_INSTALL#" + resources.getString(R.string.fbAdmobInterstitial)
        )
        // Set listeners for the Interstitial Ad
        fbInterstitialAd!!.setAdListener(object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad) {
                // Interstitial ad displayed callback
                Log.e(ContentValues.TAG, "Interstitial ad displayed.")
            }

            override fun onInterstitialDismissed(ad: Ad) {
                // Interstitial dismissed callback
                Log.e(ContentValues.TAG, "Interstitial ad dismissed.")
            }

            override fun onError(ad: Ad, adError: AdError) {
                // Ad error callback
                Log.e(
                    ContentValues.TAG,
                    "Interstitial ad failed to load: " + adError.errorMessage
                )
            }

            override fun onAdLoaded(ad: Ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(ContentValues.TAG, "Interstitial ad is loaded and ready to be displayed!")
                // Show the ad
                if (fbInterstitialAd!!.isAdLoaded) {
                    fbInterstitialAd!!.show()
                } else {
                    fbInterstitialAd!!.loadAd()
                }
            }

            override fun onAdClicked(ad: Ad) {
                // Ad clicked callback
                Log.d(ContentValues.TAG, "Interstitial ad clicked!")
            }

            override fun onLoggingImpression(ad: Ad) {
                // Ad impression logged callback
                Log.d(ContentValues.TAG, "Interstitial ad impression logged!")
            }
        })

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        fbInterstitialAd!!.loadAd()

    }

    fun createNotificationChannel(
        context: Context,
        importance: Int,
        showBadge: Boolean,
        name: String,
        description: String
    ) {
        // 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 2
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            // 3
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.e("loged112211", "Notificaion Channel Created!")
        }
    }


    private fun isNeedGrantPermission(): Boolean {
        try {
            if (IOUtils.hasMarsallow()) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        REQUEST_PERMISSION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this@MainActivity,
                            REQUEST_PERMISSION
                        )
                    ) {
                        val msg =
                            String.format(
                                getString(R.string.format_request_permision),
                                getString(R.string.app_name)
                            )

                        val localBuilder = AlertDialog.Builder(this@MainActivity)
                        localBuilder.setTitle(getString(R.string.permission_title))
                        localBuilder
                            .setMessage(msg).setNeutralButton(
                                getString(R.string.grant_option)
                            ) { paramAnonymousDialogInterface, paramAnonymousInt ->
                                ActivityCompat.requestPermissions(
                                    this@MainActivity,
                                    arrayOf(REQUEST_PERMISSION),
                                    REQUEST_PERMISSION_CODE
                                )
                            }
                            .setNegativeButton(
                                getString(R.string.cancel_option)
                            ) { paramAnonymousDialogInterface, paramAnonymousInt ->
                                paramAnonymousDialogInterface.dismiss()
                                finish()
                            }
                        localBuilder.show()

                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(REQUEST_PERMISSION),
                            REQUEST_PERMISSION_CODE
                        )
                    }
                    return true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        try {
            if (requestCode == REQUEST_PERMISSION_CODE) {
                if (grantResults != null && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //
                } else {
                    iUtils.ShowToast(this@MainActivity, getString(R.string.info_permission_denied))

                    finish()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            iUtils.ShowToast(this@MainActivity, getString(R.string.info_permission_denied))
            finish()
        }

    }




    override fun onBackPressed() {


        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog_ad_exit)
        val yesBtn = dialog.findViewById(R.id.btn_exitdialog_yes) as Button
        val noBtn = dialog.findViewById(R.id.btn_exitdialog_no) as Button

        val adviewnew = dialog.findViewById(R.id.adView_dia) as AdView
        val adRequest = AdRequest.Builder().build()
        adviewnew.loadAd(adRequest)



        yesBtn.setOnClickListener {
            System.exit(0)
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }



    fun startClipboardMonitor() {
        prefEditor.putBoolean("csRunning", true)
        prefEditor.commit()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val service = startForegroundService(
                Intent(
                   this,
                    ClipboardMonitor::class.java
                ).setAction(Constants.STARTFOREGROUND_ACTION)
            )
        } else {
            val service = startService(
                Intent(
                   this,
                    ClipboardMonitor::class.java
                )
            )
        }

    }

    fun stopClipboardMonitor() {
        prefEditor.putBoolean("csRunning", false)
        prefEditor.commit()

        val service = stopService(
            Intent(
                this,
                ClipboardMonitor::class.java
            ).setAction(Constants.STOPFOREGROUND_ACTION)
        )


    }



    fun DownloadVideo(url: String) {


        //if (iUtils.checkURL(url)) {
        if (url.equals("") && iUtils.checkURL(url)) {
            iUtils.ShowToast(this, resources?.getString(R.string.enter_valid))


        } else {

            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.")
            }
            Log.d("mylogissssss", "The interstitial wasn't loaded yet.")
            downloadVideo.Start(this, url, false)

        }
    }


    private fun showAdDialog() {


        val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(this)


        dialogBuilder.setMessage(getString(R.string.doyouseead))

            .setCancelable(false)

            .setPositiveButton(
                getString(R.string.watchad),
                DialogInterface.OnClickListener { dialog, id ->


                    if (mRewardedVideoAd.isLoaded) {
                        mRewardedVideoAd.show()
                    } else {

                        iUtils.ShowToast(
                            this,
                            resources?.getString(R.string.videonotavaliabl)
                        )

                        chkAutoDownload?.isChecked = true
                        val checked = chkAutoDownload?.isChecked

                        if (checked!!) {
                            Log.e("loged", "testing checked!")
                            startClipboardMonitor()
                        } else {
                            Log.e("loged", "testing unchecked!")


                            stopClipboardMonitor()
                            // setNofication(false);
                        }
                    }


                })

            .setNegativeButton(
                getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()

                    val checked = chkAutoDownload?.isChecked
                    if (checked!!) {
                        chkAutoDownload?.isChecked = false
                    }

                })


        val alert = dialogBuilder.create()
        alert.setTitle(getString(R.string.enabAuto))
        alert.show()

    }


    //reward video methods

    override fun onRewarded(reward: RewardItem) {

        //  iUtils.ShowToast(context, context!!.resources.getString(R.string.don_start))
        chkAutoDownload?.isChecked = true

        startClipboardMonitor()


    }

    override fun onRewardedVideoAdLeftApplication() {
        //  Toast.makeText(this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show()
    }

    override fun onRewardedVideoAdClosed() {
        //  Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show()
        iUtils.ShowToast(this@MainActivity, getString(R.string.completad))
//
        val checked = chkAutoDownload?.isChecked
        if (checked!!) {
            chkAutoDownload?.isChecked = false
        }
    }

    override fun onRewardedVideoAdFailedToLoad(errorCode: Int) {

//        iUtils.ShowToast(context, getString(R.string.videofailedload))
//
//        val checked = view?.chkAutoDownload?.isChecked
//
//        if (checked!!) {
//            Log.e("loged", "testing checked!")
//            startClipboardMonitor()
//        } else {
//            Log.e("loged", "testing unchecked!")
//
//
//            stopClipboardMonitor()
//            // setNofication(false);
//        }

    }

    override fun onRewardedVideoAdLoaded() {
        //   Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show()
    }

    override fun onRewardedVideoAdOpened() {
        //   Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show()
    }

    override fun onRewardedVideoStarted() {
        //  Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show()
    }

    override fun onRewardedVideoCompleted() {
        chkAutoDownload?.isChecked = true

        startClipboardMonitor()
    }




    override fun attachBaseContext(newBase: Context?) {
        var newBase = newBase
        newBase = LocaleHelper.onAttach(newBase)
        super.attachBaseContext(newBase)
    }
}