package com.danielvilha.kotlincamerax

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * Created by danielvilha on 2019-07-08
 */
class PermissionFragment : Fragment() {

    private val rootView by lazy {
        FrameLayout(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return rootView
    }

    companion object {
        private const val CAMERA_REQUEST_CODE = 20
    }
}
