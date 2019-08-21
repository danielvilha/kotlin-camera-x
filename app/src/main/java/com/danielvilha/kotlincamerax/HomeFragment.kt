package com.danielvilha.kotlincamerax

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.row_image.view.*
import java.io.File

/**
 * Created by danielvilha on 2019-15-08
 */
class HomeFragment : Fragment() {

    private var adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()

        val gpath = Environment.getExternalStorageDirectory().absolutePath
        val spath = IMAGE_DIRECTORY
        val fullpath = File(gpath + File.separator + spath)
        getImages(fullpath)

        recycler.adapter = adapter

        fab.setOnClickListener {
            displayCameraFragment()
        }
    }

    private fun displayCameraFragment() {
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.permissionFragment, true).build()
        Navigation.findNavController(requireActivity(), R.id.mainContent)
            .navigate(R.id.cameraFragment, null, navOptions)
    }

    private fun getImages(root: File) {
        val files = root.listFiles()

        for (f in files) {
            adapter.add(ImageAdapter(f))
            Log.d(TAG, "Image file: $f")
        }
        // if (files != null && files.isNotEmpty()) {
            
        // }
    }

    companion object {
        private const val TAG = "HomeFragment"
        private const val IMAGE_DIRECTORY = "/kotlincamerax"
    }
}


class ImageAdapter(private val item: File): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
//        viewHolder.itemView.image.setImageURI(items[position].parentFile.toUri())

        Picasso.get().load(item.parentFile.toUri()).into(viewHolder.itemView.image_view)
    }

    override fun getLayout(): Int {
        return R.layout.row_image
    }
}