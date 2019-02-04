package com.kinfoitsolutions.ebooks.ui.fragments


import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation

import com.kinfoitsolutions.ebooks.R
import kotlinx.android.synthetic.main.fragment_book_reading.*
import kotlinx.android.synthetic.main.fragment_book_reading.view.*
import kotlinx.android.synthetic.main.fragment_home.*


class BookReadingFragment : Fragment(), View.OnClickListener {


    private lateinit var viewOfLayout: View
    internal var isSelected = false
    internal var textSize = 18
    internal var saveProgress: Int = 0
    private lateinit var  seekBar: SeekBar
    private lateinit var  slideDialog: Dialog


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        viewOfLayout = inflater.inflate(R.layout.fragment_book_reading, container, false)
        setHasOptionsMenu(true)

        viewOfLayout.text.setTextIsSelectable(true)

        viewOfLayout.linear1.setOnClickListener(this)
        viewOfLayout.linear2.setOnClickListener(this)
        viewOfLayout.linear3.setOnClickListener(this)
        viewOfLayout.linear4.setOnClickListener(this)
        viewOfLayout.audio.setOnClickListener(this)



        return viewOfLayout

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_search).setVisible(false)
        super.onPrepareOptionsMenu(menu)

    }



    override fun onClick(v: View) {

        when (v.id) {


            R.id.audio ->{

                Navigation.findNavController(book_container).navigate(R.id.action_bookReadingFragment_to_audioFragment);


            }

            R.id.linear1 ->


                if (!isSelected) {
                    viewOfLayout.linear.setBackgroundColor(Color.parseColor("#fffbe7"))

                    viewOfLayout.image1.setImageResource(R.drawable.ic_eye_orange)
                    viewOfLayout.image2.setImageResource(R.drawable.ic_font_1)
                    viewOfLayout.image3.setImageResource(R.drawable.ic_brightness_1)
                    viewOfLayout.image4.setImageResource(R.drawable.ic_zoom_1)

                    isSelected = true
                } else if (isSelected) {
                    viewOfLayout.linear.setBackgroundColor(Color.parseColor("#ffffff"))

                    viewOfLayout.image1.setImageResource(R.drawable.ic_eye_1)
                    viewOfLayout.image2.setImageResource(R.drawable.ic_font_1)
                    viewOfLayout.image3.setImageResource(R.drawable.ic_brightness_1)
                    viewOfLayout.image4.setImageResource(R.drawable.ic_zoom_1)

                    isSelected = false
                }


            R.id.linear2 -> {
                viewOfLayout.linear.setBackgroundColor(Color.parseColor("#ffffff"))

                viewOfLayout.image1.setImageResource(R.drawable.ic_eye_1)
                viewOfLayout.image2.setImageResource(R.drawable.ic_font_orange)
                viewOfLayout.image3.setImageResource(R.drawable.ic_brightness_1)
                viewOfLayout.image4.setImageResource(R.drawable.ic_zoom_1)


                slideDialog = Dialog(context, R.style.CustomDialogAnimation)
                slideDialog.setContentView(R.layout.zooming_popup1)
                slideDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val layoutParams3 = WindowManager.LayoutParams()
                val layoutParams = WindowManager.LayoutParams()
                slideDialog.getWindow()!!.getAttributes().windowAnimations = R.style.CustomDialogAnimation
                layoutParams.copyFrom(slideDialog.getWindow()!!.getAttributes())


                seekBar = slideDialog.findViewById(R.id.seekBar1) as SeekBar


                seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {


                    override fun onStopTrackingTouch(seekBar: SeekBar) {


                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {

                    }

                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        textSize = textSize + (progress - saveProgress)
                        saveProgress = progress
                        viewOfLayout.text.setTextSize(textSize.toFloat())


                    }
                })


                // int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
                val height = (resources.displayMetrics.heightPixels * 0.18)


                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                layoutParams.height = height.toInt()
                // layoutParams.width = width;
                layoutParams.gravity = Gravity.BOTTOM


                slideDialog.getWindow()!!.setAttributes(layoutParams)
                slideDialog.setCancelable(true)
                slideDialog.setCanceledOnTouchOutside(true)
                slideDialog.show()
            }

            R.id.linear3 -> {

                viewOfLayout.image1.setImageResource(R.drawable.ic_eye_1)
                viewOfLayout.image2.setImageResource(R.drawable.ic_font_1)
                viewOfLayout.image3.setImageResource(R.drawable.ic_brightness_orange)
                viewOfLayout.image4.setImageResource(R.drawable.ic_zoom_1)

                slideDialog = Dialog(context, R.style.CustomDialogAnimation)
                slideDialog.setContentView(R.layout.zooming_popup)
                slideDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

               // setTitle("dev2qa.com - Seekbar Change Screen Brightness Example.")

                // Get display screen brightness value text view object.
                //final TextView screenBrightnessValueTextView = (TextView)findViewById(R.id.change_screen_brightness_value_text_view);

                // Get the seekbar instance.
                seekBar = slideDialog.findViewById(R.id.seekBar1) as SeekBar
                seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

                    @RequiresApi(api = Build.VERSION_CODES.M)
                    override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {

                        val context = context

                        if (Build.VERSION.SDK_INT >= 23) {
                            // if (!Settings.canDrawOverlays(this)) {
                            //
                        } else {
                            // another similar method that supports device have API < 23
                        }

                        val canWriteSettings = Settings.System.canWrite(context)

                        if (canWriteSettings) {

                            // Because max screen brightness value is 255
                            // But max seekbar value is 100, so need to convert.
                            val screenBrightnessValue = i * 255 / 100

                            // Set seekbar adjust screen brightness value in the text view.
                            //screenBrightnessValueTextView.setText(SCREEN_BRIGHTNESS_VALUE_PREFIX + screenBrightnessValue);

                            // Change the screen brightness change mode to manual.
                            Settings.System.putInt(
                                context!!.getContentResolver(),
                                Settings.System.SCREEN_BRIGHTNESS_MODE,
                                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
                            )
                            // Apply the screen brightness value to the system, this will change the value in Settings ---> Display ---> Brightness level.
                            // It will also change the screen brightness for the device.
                            Settings.System.putInt(
                                context!!.getContentResolver(),
                                Settings.System.SCREEN_BRIGHTNESS,
                                screenBrightnessValue
                            )
                        } else {
                            // Show Can modify system settings panel to let user add WRITE_SETTINGS permission for this app.
                            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                            context!!.startActivity(intent)
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar) {

                    }
                })

                //Getting Current screen brightness.
                val currBrightness = Settings.System.getInt(context!!.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0)
                // Set current screen brightness value in the text view.
                // screenBrightnessValueTextView.setText( SCREEN_BRIGHTNESS_VALUE_PREFIX + currBrightness);
                // Set current screen brightness value to seekbar progress.
                seekBar.setProgress(currBrightness)


                val layoutParams1 = WindowManager.LayoutParams()
                slideDialog.getWindow()!!.getAttributes().windowAnimations = R.style.CustomDialogAnimation
                layoutParams1.copyFrom(slideDialog.getWindow()!!.getAttributes())

                val width1 = (resources.displayMetrics.widthPixels * 0.60)
                val height1 = (resources.displayMetrics.heightPixels * 0.18)

                layoutParams1.width = WindowManager.LayoutParams.MATCH_PARENT
                layoutParams1.height = height1.toInt()
                layoutParams1.gravity = Gravity.BOTTOM


                slideDialog.getWindow()!!.setAttributes(layoutParams1)
                slideDialog.setCancelable(true)
                slideDialog.setCanceledOnTouchOutside(true)
                slideDialog.show()
            }

            R.id.linear4 -> {

                viewOfLayout.image1.setImageResource(R.drawable.ic_eye_1)
                viewOfLayout.image2.setImageResource(R.drawable.ic_font_1)
                viewOfLayout.image3.setImageResource(R.drawable.ic_brightness_1)
                viewOfLayout.image4.setImageResource(R.drawable.ic_zoom_orange)


                viewOfLayout.linear.setBackgroundColor(Color.parseColor("#ffffff"))

                slideDialog = Dialog(context, R.style.CustomDialogAnimation)
                slideDialog.setContentView(R.layout.zooming_popup2)
                slideDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val layoutParams2 = WindowManager.LayoutParams()
                slideDialog.getWindow()!!.getAttributes().windowAnimations = R.style.CustomDialogAnimation
                layoutParams2.copyFrom(slideDialog.getWindow()!!.getAttributes())


                //  txtSeekBar.setTextScaleX(textSize);
                seekBar = slideDialog.findViewById(R.id.seekBar1) as SeekBar
                seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    internal var p = 0

                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        // TODO Auto-generated method stub
                        if (p < 15) {
                            p = 15
                            seekBar.progress = p
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {
                        // TODO Auto-generated method stub
                    }

                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        // TODO Auto-generated method stub
                        p = progress
                        text.setTextSize(p.toFloat())
                    }
                })


                val width2 = (resources.displayMetrics.widthPixels * 0.60)
                val height2 = (resources.displayMetrics.heightPixels * 0.18)

                layoutParams2.width = WindowManager.LayoutParams.MATCH_PARENT
                layoutParams2.height = height2.toInt()
                layoutParams2.gravity = Gravity.BOTTOM

                slideDialog.getWindow()!!.setAttributes(layoutParams2)
                slideDialog.setCancelable(true)
                slideDialog.setCanceledOnTouchOutside(true)
                slideDialog.show()

            }
        }

    }

}
