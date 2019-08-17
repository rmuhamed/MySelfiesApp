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
import com.rmuhamed.sample.myselfiesapp.*
import com.rmuhamed.sample.myselfiesapp.api.RetrofitController
import com.rmuhamed.sample.myselfiesapp.camera.CameraActivity
import com.rmuhamed.sample.myselfiesapp.repository.CreateAlbumRepository
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
            getViewModel { CreateAlbumViewModel(CreateAlbumRepository(RetrofitController.imgurAPI, accessToken)) }
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
                takeFirstPicture(it, accessToken)
            }
        })
    }

    private fun takeFirstPicture(albumId: String, accessToken: String) {
        launchActivity {
            Intent().apply {
                setClass(this@CreateAlbumFragment.context!!, CameraActivity::class.java)
                putExtra(ALBUM_ID, albumId)
                putExtra(ACCESS_TOKEN, accessToken)
            }
        }
    }
}