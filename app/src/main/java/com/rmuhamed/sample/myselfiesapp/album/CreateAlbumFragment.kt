package com.rmuhamed.sample.myselfiesapp.album

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.rmuhamed.sample.myselfiesapp.ACCESS_TOKEN
import com.rmuhamed.sample.myselfiesapp.R
import com.rmuhamed.sample.myselfiesapp.camera.CameraActivity
import com.rmuhamed.sample.myselfiesapp.getViewModel
import com.rmuhamed.sample.myselfiesapp.launchActivity
import com.rmuhamed.sample.myselfiesapp.repository.RepositoryFactory
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.create_album_dialog.*


class CreateAlbumFragment : BottomSheetDialogFragment() {

    private lateinit var accessToken: String
    private lateinit var viewModel: CreateAlbumViewModel

    companion object {
        fun newInstance(accessToken: String) =
            CreateAlbumFragment().apply {
                arguments = Bundle().also {
                    it.putString(ACCESS_TOKEN, accessToken)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        accessToken = arguments?.getString(ACCESS_TOKEN) ?: ""

        viewModel =
            getViewModel(CreateAlbumViewModel::class.java, activity!!.application, RepositoryFactory.Type.ALBUM)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_album_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        create_album_button.setOnClickListener {
            viewModel.createAlbum(
                create_album_title_input_textField.editText?.text.toString(),
                create_album_description_input_textField.editText?.text.toString()
            )
        }

        viewModel.albumNotCreatedLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                Snackbar.make(gallery_pictures, it, BaseTransientBottomBar.LENGTH_SHORT).show()
                progress.visibility = View.GONE
            }
        })

        viewModel.albumCreationLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                takeFirstPicture()
            }
        })
    }

    private fun takeFirstPicture() {
        launchActivity {
            Intent().apply {
                setClass(this@CreateAlbumFragment.context!!, CameraActivity::class.java)
            }
        }
    }
}