package com.kinfoitsolutions.ebooks.ui.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.Toast
import com.kinfoitsolutions.ebooks.R
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_profile.view.*
import android.R.attr.data
import android.app.Activity.RESULT_OK
import android.provider.MediaStore
import android.graphics.Bitmap


class ProfileFragment : Fragment() {


    private lateinit var viewOfLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewOfLayout =  inflater.inflate(R.layout.fragment_profile, container, false)
        setHasOptionsMenu(true)


        viewOfLayout.iv_camera.setOnClickListener {
            // for fragment (DO NOT use `getActivity()`)
            CropImage.activity()
                .start(context!!, this);

        }

        return viewOfLayout

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === RESULT_OK) {
                val resultUri = result.uri

                val bitmap = MediaStore.Images.Media.getBitmap(context!!.getContentResolver(), resultUri)

                viewOfLayout.profilePic.setImageBitmap(bitmap)


            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_search).setVisible(false)
        menu.findItem(R.id.action_edit).setVisible(true)
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_edit -> {
                Toast.makeText(context,"Submitted",Toast.LENGTH_SHORT).show()
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }


}
