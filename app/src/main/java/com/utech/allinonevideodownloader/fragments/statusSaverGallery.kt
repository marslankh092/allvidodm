package com.utech.allinonevideodownloader.fragments

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.utech.allinonevideodownloader.R
import com.utech.allinonevideodownloader.adapters.GalleryAdapter
import com.utech.allinonevideodownloader.models.GalleryModel
import com.utech.allinonevideodownloader.utils.Constants
import kotlinx.android.synthetic.main.fragment_status_saver_gallery.view.*
import java.io.File
import java.util.*


class statusSaverGallery : Fragment() {
    var recycler_view: RecyclerView? = null
    var recyclerViewAdapter: GalleryAdapter? = null
    protected lateinit var files: Array<File>


    var listState: Parcelable? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_status_saver_gallery, container, false)


        recycler_view = view.recycler_view


        setUpRecyclerView()


        return view
    }


    private fun setUpRecyclerView() {
        recycler_view?.setHasFixedSize(true)
        recycler_view?.layoutManager = GridLayoutManager(activity, 2)

        //  request.setDestinationUri(new File(mBaseFolderPath).);
        recyclerViewAdapter =
            GalleryAdapter(
                activity,
                getData()
            )


        recycler_view?.adapter = recyclerViewAdapter

        recyclerViewAdapter!!.notifyDataSetChanged()
    }


    private fun getData(): java.util.ArrayList<GalleryModel>? {
        val filesList = java.util.ArrayList<GalleryModel>()
        var f: GalleryModel
        val targetPath = Environment.getExternalStorageDirectory()
            .absolutePath + Constants.SAVE_FOLDER_NAME

        Log.e("mygsdhdfsdhsjf", targetPath + "")

        val targetDirector = File(targetPath)
        if (targetDirector.listFiles() != null) {
            files = targetDirector.listFiles()
        }
//        if (files == null) {
////            noImageText.setVisibility(View.INVISIBLE);
//            Toast.makeText(activity, "hello", Toast.LENGTH_SHORT).show()
//        }
        try {
            Arrays.sort<File>(files) { o1, o2 ->
                if ((o1 as File).lastModified() > (o2 as File).lastModified()) {
                    -1
                } else if (o1.lastModified() < o2.lastModified()) {
                    +1
                } else {
                    0
                }
            }
            for (i in files.indices) {
                val file: File = files.get(i)
                f = GalleryModel()
                f.name = getString(R.string.savedstt) + (i + 1)
                f.filename = file.name
                f.uri = Uri.fromFile(file)
                f.path = files.get(i).absolutePath
                filesList.add(f)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return filesList
    }


}