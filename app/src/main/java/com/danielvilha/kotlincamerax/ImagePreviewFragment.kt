package com.danielvilha.kotlincamerax

import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_image_preview.*
import java.io.*
import java.util.*

/**
 * Created by danielvilha on 2019-15-08
 */
class ImagePreviewFragment : Fragment() {

    private var imageFilePath: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageFilePath = arguments?.getString(KEY_IMAGE_FILE_PATH)
        if (imageFilePath.isNullOrBlank()) {
            Navigation.findNavController(requireActivity(), R.id.mainContent).popBackStack()
        } else {
            image_view.setImageURI(Uri.parse(imageFilePath))
        }
    }

    override fun onStart() {
        super.onStart()

        save.setOnClickListener {
            saveDialog()
        }

        unsave.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.mainContent)
                .navigate(R.id.homeFragment)
        }
    }

    private fun saveDialog() {
        val dialogBuilder = AlertDialog.Builder(context!!)

        dialogBuilder.setMessage(R.string.message)
            .setCancelable(false)
            .setPositiveButton(R.string.proceed) { _, _ ->
                val drawable = image_view.drawable
                val bitmap = drawable.toBitmap()
                saveImage(bitmap)
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.setTitle(R.string.title_message)
        alert.show()
    }

    private fun saveImage(bitmap: Bitmap) {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val directory = File((Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        Log.d(TAG, directory.toString())

        if (!directory.parentFile.exists()) {
            directory.parentFile.mkdir()
        }

        if (!directory.exists()) {
            directory.mkdirs()
        }

        try {
            Log.d(TAG, directory.toString())
            val file = File(directory, ((Calendar.getInstance().timeInMillis).toString() + ".jpg"))
            file.createNewFile()
            val fileOutput = FileOutputStream(file)
            fileOutput.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(context, arrayOf(file.path), arrayOf("image/jpeg"), null)
            fileOutput.close()

            Log.d(TAG, "File Saved::--->" + file.absolutePath)

            Navigation.findNavController(requireActivity(), R.id.mainContent)
                .navigate(R.id.homeFragment)
        }
        catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val TAG = "ImagePreviewFragment"
        private const val KEY_IMAGE_FILE_PATH = "key_image_file_path"
        private const val IMAGE_DIRECTORY = "/kotlincamerax"

        fun arguments(absolutePath: String): Bundle {
            return Bundle().apply {
                putString(KEY_IMAGE_FILE_PATH, absolutePath)
            }
        }
    }
}
