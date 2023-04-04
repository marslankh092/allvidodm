package com.utech.allinonevideodownloader.fragments

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.AdView
import com.facebook.ads.InterstitialAdListener
import com.utech.allinonevideodownloader.R
import com.utech.allinonevideodownloader.adapters.Adapter_VideoFolder
import com.utech.allinonevideodownloader.models.Model_Video
import com.utech.allinonevideodownloader.utils.Constants.DOWNLOAD_DIRECTORY
import com.utech.allinonevideodownloader.utils.Constants.MY_ANDROID_10_IDENTIFIER_OF_FILE
import kotlinx.android.synthetic.main.fragment_gallery.view.*
import java.io.File
import java.util.concurrent.TimeUnit


class galleryMain : Fragment() {
    var obj_adapter: Adapter_VideoFolder? = null
    var al_video = ArrayList<Model_Video>()
    var recyclerView1: RecyclerView? = null
    var recyclerViewLayoutManager: RecyclerView.LayoutManager? = null


    private val KEY_RECYCLER_STATE = "recycler_state"
    var listState: Parcelable? = null


    var fbdView: AdView? = null

    private var fbInterstitialAd: com.facebook.ads.InterstitialAd? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_gallery, container, false)

        recyclerView1 = view.recyclerView



        recyclerViewLayoutManager = GridLayoutManager(context!!, 3)
        recyclerView1!!.layoutManager = recyclerViewLayoutManager


        // addFbAd();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            fn_video_android_10(context!!, requireActivity(), true)

        } else {
            fn_video(context!!, requireActivity(), true)
        }


        return view
    }


    //TODO Call this function to show Facebook ads
    private fun addFbAd() {


        //ads
        fbInterstitialAd = com.facebook.ads.InterstitialAd(
            activity,
            "IMG_16_9_APP_INSTALL#" + resources.getString(R.string.fbAdmobInterstitial)
        )
        // Set listeners for the Interstitial Ad
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
                fbInterstitialAd!!.show()
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


    fun fn_video(cn: Context, activity: FragmentActivity, f: Boolean) {
        al_video = ArrayList<Model_Video>()
        val int_position = 0
        val uri: Uri
        val cursor: Cursor
        val column_index_data: Int
        val column_index_folder_name: Int
        val column_id: Int
        val thum: Int
        val duration: Int

        var absolutePathOfImage: String? = null
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val condition = MediaStore.Video.Media.DATA + " like?"
        val selectionArguments = arrayOf("%$DOWNLOAD_DIRECTORY%")

//        val condition = MediaStore.Video.Media.DATA + " like? "+ Constants.MY_ANDROID_10_IDENTIFIER_OF_FILE
//        val selectionArguments = arrayOf("%${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}%")
//
        val sortOrder = MediaStore.Video.Media.DATE_TAKEN + " DESC"
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Video.Media.DURATION
        )
        cursor = cn.contentResolver
            .query(uri, projection, condition, selectionArguments, "$sortOrder")!!

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        column_index_folder_name =
            cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
        column_id = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        thum = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA)
        duration = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
        var i: Int = 0
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)
            Log.e("Column", absolutePathOfImage)
            Log.e("Folder", cursor.getString(column_index_folder_name))
            Log.e("column_id", cursor.getString(column_id))
            Log.e("thum", cursor.getString(thum))
            Log.e("duration", cursor.getString(duration))

            try {


                val mp: MediaPlayer = MediaPlayer.create(
                    activity, FileProvider.getUriForFile(
                        context!!,
                        context!!.applicationContext.packageName + ".provider",
                        File(absolutePathOfImage)
                    )
                )

                val durationnew: Int = mp.duration


                if (absolutePathOfImage.contains(MY_ANDROID_10_IDENTIFIER_OF_FILE)) {
                    val obj_model = Model_Video()
                    obj_model.isBoolean_selected = false
                    obj_model.str_path = absolutePathOfImage
                    obj_model.str_thumb = cursor.getString(thum)
                    obj_model.duration = durationnew
                    obj_model.id = i

                    al_video.add(obj_model)

                    i = i + 1
                } else {
                    val obj_model = Model_Video()
                    obj_model.isBoolean_selected = false
                    obj_model.str_path = absolutePathOfImage
                    obj_model.str_thumb = cursor.getString(thum)
                    obj_model.duration = durationnew
                    obj_model.id = i

                    al_video.add(obj_model)
                    i = i + 1
                }


            } catch (e: Exception) {
                //iUtils.ShowToast(context, "Gallery Load Error")
            }
        }


        obj_adapter = Adapter_VideoFolder(cn, al_video, activity)

        recyclerView1!!.adapter = null
        recyclerView1!!.adapter = obj_adapter
        obj_adapter!!.notifyDataSetChanged()

//
//        //recyclerView1!!.setLayoutManager(null);
//        recyclerView1!!.getRecycledViewPool().clear();
//        recyclerView1!!.swapAdapter(obj_adapter, false);
//       // recyclerView1!!.setLayoutManager(layoutManager);
//        obj_adapter!!.notifyDataSetChanged();


    }


    fun fn_video_android_10(cn: Context, activity: FragmentActivity, f: Boolean) {
        al_video = ArrayList<Model_Video>()
        val int_position = 0
        val uri: Uri
        val cursor: Cursor
        val column_index_data: Int
        val column_index_folder_name: Int
        val column_id: Int
        val thum: Int
        val duration: Int

        var absolutePathOfImage: String? = null
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val condition = MediaStore.Video.Media.DATA + " like?"
        val selectionArguments =
            arrayOf("%${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}%")

//        val condition = MediaStore.Video.Media.DATA + " like? "+ Constants.MY_ANDROID_10_IDENTIFIER_OF_FILE
//        val selectionArguments = arrayOf("%${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}%")
//
        val sortOrder = MediaStore.Video.Media.DATE_TAKEN + " DESC"
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Video.Media.DURATION
        )
        cursor = cn.contentResolver
            .query(uri, projection, condition, selectionArguments, "$sortOrder")!!

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        column_index_folder_name =
            cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
        column_id = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        thum = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA)
        duration = cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION)
        var i: Int = 0
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)
            Log.e("Column", absolutePathOfImage)
            Log.e("Folder", cursor.getString(column_index_folder_name))
            Log.e("column_id", cursor.getString(column_id))
            Log.e("thum", cursor.getString(thum))


            try {

                val mp: MediaPlayer = MediaPlayer.create(
                    activity, FileProvider.getUriForFile(
                        context!!,
                        context!!.applicationContext.packageName + ".provider",
                        File(absolutePathOfImage)
                    )
                )

                val durationnew: Int = mp.duration
                mp.release()


                Log.e("duration", cursor.getString(duration) + "")
                Log.e("durationnew", durationnew.toString())


                val minutes = TimeUnit.MILLISECONDS.toMinutes(durationnew.toLong())

            // long seconds = (milliseconds / 1000);
            val seconds = TimeUnit.MILLISECONDS.toSeconds(durationnew.toLong())

            if (absolutePathOfImage.contains(MY_ANDROID_10_IDENTIFIER_OF_FILE)) {
                val obj_model = Model_Video()
                obj_model.isBoolean_selected = false
                obj_model.str_path = absolutePathOfImage
                obj_model.str_thumb = cursor.getString(thum)
                obj_model.duration = durationnew
                obj_model.id = i

                al_video.add(obj_model)

                i = i + 1
            }
            } catch (e: Exception) {

            }

        }


        obj_adapter = Adapter_VideoFolder(cn, al_video, activity)

        recyclerView1!!.adapter = null
        recyclerView1!!.adapter = obj_adapter
        obj_adapter!!.notifyDataSetChanged()

//
//        //recyclerView1!!.setLayoutManager(null);
//        recyclerView1!!.getRecycledViewPool().clear();
//        recyclerView1!!.swapAdapter(obj_adapter, false);
//       // recyclerView1!!.setLayoutManager(layoutManager);
//        obj_adapter!!.notifyDataSetChanged();


    }


//    override fun setMenuVisibility(visible: Boolean) {
//        super.setMenuVisibility(visible)
//        if (visible) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                fn_video_android_10(context!!, requireActivity(), true)
//
//            } else {
//             //   fn_video(context!!, requireActivity(), true)
//            }
//        }
//    }

    override fun onResume() {
        super.onResume()
        Log.e("resume", "12412535")

    }

}

