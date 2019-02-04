package com.kinfoitsolutions.ebooks.ui.fragments


import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.kinfoitsolutions.ebooks.R
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_profile.view.*
import id.zelory.compressor.Compressor
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.ContextWrapper
import android.provider.MediaStore
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.drivingschool.android.AppConstants
import com.kinfoitsolutions.ebooks.ui.BaseFragment
import com.kinfoitsolutions.ebooks.ui.Utils
import com.kinfoitsolutions.ebooks.ui.responsemodel.Getprofile.GetProfileResponse
import com.kinfoitsolutions.ebooks.ui.responsemodel.UpdateProfile.UpdateProfileResponse
import com.kinfoitsolutions.ebooks.ui.restclient.RestClient
import com.orhanobut.hawk.Hawk
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_dashboard.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.RequestBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import java.io.*


class ProfileFragment : BaseFragment() {

    private lateinit var viewOfLayout: View
    private lateinit var user_name: String
    private lateinit var email: String
    private lateinit var phone: String
    private lateinit var imageUri: Uri

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View {

        viewOfLayout = inflater.inflate(R.layout.fragment_profile, parent, false)

        setHasOptionsMenu(true)

        viewOfLayout.edName.isEnabled = false
        viewOfLayout.edEmail.isEnabled = false
        viewOfLayout.edPhone.isEnabled = false

        viewOfLayout.btnUpdateProfile.visibility = View.GONE

        viewOfLayout.iv_camera.isEnabled = false

        if (isNetworkConnected(context!!)){
            getuserprofile()
        }
        else {

            showSnackBarFrag("Check your internet connection",activity!!.main_container)
        }


        viewOfLayout.iv_camera.setOnClickListener {
            // for fragment (DO NOT use `getActivity()`)
            CropImage.activity().start(context!!, this);

        }

        viewOfLayout.btnUpdateProfile.setOnClickListener {

            if (isNetworkConnected(context!!)){
                user_name = viewOfLayout.edName.text.toString().trim()
                email = viewOfLayout.edEmail.text.toString().trim()
                phone = viewOfLayout.edPhone.text.toString().trim()

                when {

                    user_name == "" -> {
                        viewOfLayout.edName.setError("Enter your Name")

                    }
                    email == "" -> {
                        viewOfLayout.edEmail.setError("Enter your email")

                    }

                    phone == "" -> {
                        viewOfLayout.edPhone.setError("Enter your phone")

                    }
                    else -> {

                        val myDialog = Utils.showProgressDialog(context, "Updating......")


                        val restClient = RestClient.getClient()

                        val token = RequestBody.create(
                            MediaType.parse
                                ("multipart/form-data"), Hawk.get(AppConstants.TOKEN, "")
                        )

                        val fullName = RequestBody.create(
                            MediaType.parse
                                ("multipart/form-data"), user_name
                        )
                        val email_id = RequestBody.create(
                            MediaType.parse
                                ("multipart/form-data"), email
                        )
                        val phone = RequestBody.create(
                            MediaType.parse
                                ("multipart/form-data"), phone
                        )


                        //pass it like this

                        try {
                            val bitmap = MediaStore.Images.Media.getBitmap(context!!.getContentResolver(), imageUri)

                            val file = File(saveToInternalStorage(bitmap)+"/profile.jpg")

                            val compressedImageFile = Compressor(context).compressToFile(file)

                            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile)

                            // MultipartBody.Part is used to send also the actual file name
                            val imageFile = MultipartBody.Part.createFormData("image", compressedImageFile.getName(), requestFile)

                            restClient.updateProfile(token,fullName, email_id, phone, imageFile)
                                .enqueue(object : Callback<UpdateProfileResponse> {

                                    override fun onResponse(call: Call<UpdateProfileResponse>, response: Response<UpdateProfileResponse>) {

                                        if (response.isSuccessful) {

                                            if (response.body()!!.code.equals(100)) {

                                                viewOfLayout.edName.isEnabled = false
                                                viewOfLayout.edEmail.isEnabled = false
                                                viewOfLayout.edPhone.isEnabled = false
                                                viewOfLayout.iv_camera.isEnabled = false

                                                viewOfLayout.btnUpdateProfile.visibility = View.GONE


                                                Toast.makeText(context, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                                                myDialog.dismiss()

                                            }
                                            else{
                                                Toast.makeText(context, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                                                myDialog.dismiss()

                                            }

                                        } else if (response.code() == 401) {
                                            // Handle unauthorized
                                            Toast.makeText(context, "Unauthorized", Toast.LENGTH_SHORT).show()
                                            myDialog.dismiss()

                                        } else if (response.code() == 501) {
                                            // Handle unauthorized
                                            Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show()
                                            myDialog.dismiss()

                                        } else {
                                            //response is failed
                                            Toast.makeText(context, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                                            myDialog.dismiss()


                                        }

                                    }

                                    override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                                        Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show()
                                        myDialog.dismiss()

                                    }


                                })


                        }catch (e:Exception){
                            myDialog.dismiss()
                            showSnackBarFrag("Please Select another image",activity!!.main_container)
                        }



                    }

                }
            }

            else{
                showSnackBarFrag("Check your internet connection",activity!!.main_container)

            }

        }

        return viewOfLayout

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === RESULT_OK) {

                imageUri = result.uri

                val bitmap = MediaStore.Images.Media.getBitmap(context!!.getContentResolver(), imageUri)

                saveToInternalStorage(bitmap)

                viewOfLayout.profilePic.setImageBitmap(bitmap)


            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    private fun saveToInternalStorage(bitmapImage: Bitmap): String {
        val cw = ContextWrapper(context)
        // path to /data/data/yourapp/app_data/imageDir
        val directory = cw.getDir("Images", Context.MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, "profile.jpg")
        ///data/user/0/com.kinfoitsolutions.ebooks/app_Images/profile.jpg

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return directory.absolutePath
        ///data/user/0/com.kinfoitsolutions.ebooks/app_Images
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_search).setVisible(false)
        menu.findItem(R.id.action_edit).setVisible(true)
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_edit -> {
              //  Toast.makeText(context, "Submitted", Toast.LENGTH_SHORT).show()


                viewOfLayout.edName.isEnabled = true
                viewOfLayout.edPhone.isEnabled = true
                viewOfLayout.iv_camera.isEnabled = true

                viewOfLayout.btnUpdateProfile.visibility = View.VISIBLE


                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    fun getuserprofile() {

        val myDialog = Utils.showProgressDialog(activity, "Progressing......")


        val restClient = RestClient.getClient()

        restClient.getprofile(Hawk.get(AppConstants.TOKEN)).enqueue(object : Callback<GetProfileResponse> {


            override fun onResponse(call: Call<GetProfileResponse>, response: Response<GetProfileResponse>) {

                if (response.isSuccessful) {

                    if (response.body()!!.code.equals(100)) {
                        myDialog.dismiss()
                        if (response.body()!!.user.name != null) {
                            viewOfLayout.edName!!.setText(response.body()!!.user.name);
                        }
                        if (response.body()!!.user.email != null) {
                            viewOfLayout.edEmail.setText(response.body()!!.user.email);
                        }
                        if (response.body()!!.user.phone != null) {
                            viewOfLayout.edPhone.setText(response.body()!!.user.phone.toString());
                        }
                        if (response.body()!!.user.userProfile.profileImage != null) {


                            var imageUrl: String? = ""
                            try {
                                imageUrl = response.body()!!.user.userProfile.profileImage
                                Log.e("profilepic", "" + response.body()!!.user.userProfile.profileImage)

                                Picasso.get().load(imageUrl).fit()
                                    .placeholder(R.drawable.ic_avatar)
                                    .error(R.drawable.ic_avatar)
                                    .into(viewOfLayout.profilePic)

                            } catch (e: Exception) {
                            }


                        }
                    } else if (response.code() == 401) {
                        // Handle unauthorized
                        Toast.makeText(context, "Unauthorized", Toast.LENGTH_SHORT).show()

                    } else if (response.code() == 500) {
                        // Handle unauthorized
                        Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show()

                    } else {
                        //code 101 invalid credentials
                        myDialog.dismiss()
                        Toast.makeText(context, "", Toast.LENGTH_SHORT).show()


                    }


                } else {

                    //response is failed
                    myDialog.dismiss()


                }

            }

            override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show()
                myDialog.dismiss()

            }


        })

    }

}
