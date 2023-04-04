package com.utech.allinonevideodownloader

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.utech.allinonevideodownloader.fragments.galleryMain
import com.utech.allinonevideodownloader.fragments.statusSaverGallery
import com.utech.allinonevideodownloader.utils.LocaleHelper
import com.utech.allinonevideodownloader.utils.iUtils
import java.util.*

class GalleryActivity : AppCompatActivity() {

    private var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_gallery)




        setlayout()

    }


    fun setlayout() {
        viewPager = findViewById<ViewPager>(R.id.viewpagergallery)
        setupViewPager(viewPager!!)

        tabLayout = findViewById<TabLayout>(R.id.tabsgallery)
        tabLayout!!.setupWithViewPager(viewPager)
        setupTabIcons()
       // supportActionBar?.hide()

    }

    fun setupTabIcons() {

        tabLayout?.getTabAt(0)?.setIcon(R.drawable.ic_gallery_color_24dp)
        tabLayout?.getTabAt(1)?.setIcon(R.drawable.statuspic)


    }


    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(galleryMain(), getString(R.string.gallery_fragment_statussaver))
        adapter.addFragment(statusSaverGallery(), getString(R.string.StatusSaver_gallery))

        viewPager.adapter = adapter
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) :
        FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {

            return mFragmentList[position]

//            viewPager!!.currentItem;
//            return when(position){
//
//                0-> download();
//                1->gallery();
//                else -> gallery();
//            }
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }


//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.action_privacy -> {

//                val intent = Intent(this, MainActivityStatusSaver::class.java)
//                startActivity(intent)
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.privacy))
                    .setMessage(R.string.privacy_message)
                    .setPositiveButton(
                        android.R.string.yes
                    ) { dialog, which -> dialog.dismiss() }
                    .setIcon(R.drawable.ic_info_black_24dp)
                    .show()



                true
            }


            R.id.action_rate -> {

//                val intent = Intent(this, MainActivityStatusSaver::class.java)
//                startActivity(intent)

                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.RateAppTitle))
                    .setMessage(getString(R.string.RateApp))
                    .setCancelable(false)
                    .setPositiveButton(
                        getString(R.string.rate_dialog),
                        DialogInterface.OnClickListener { dialog, whichButton ->
                            val appPackageName = packageName
                            try {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=$appPackageName")
                                    )
                                )
                            } catch (anfe: android.content.ActivityNotFoundException) {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                                    )
                                )
                            }
                        })
                    .setNegativeButton(getString(R.string.later_btn), null).show()

                true
            }


            R.id.ic_whatapp -> {

//                val intent = Intent(this, MainActivityLivewallpaper::class.java)
//                startActivity(intent)
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


            R.id.action_language -> {

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

                true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun attachBaseContext(newBase: Context?) {
        var newBase = newBase
        newBase = LocaleHelper.onAttach(newBase)
        super.attachBaseContext(newBase)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@GalleryActivity, MainActivity::class.java))
        finish()
    }
}