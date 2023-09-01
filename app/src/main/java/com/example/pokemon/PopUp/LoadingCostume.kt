package com.example.pokemon.PopUp

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.example.pokemon.R

class LoadingCostume {
    private lateinit var dialog: CustomDialog

    fun show(context: Context) : Dialog {
        return show(context, null)
    }

    fun show(context: Context, title: CharSequence?) : Dialog {
        val inflater = (context as Activity).layoutInflater
        val view = inflater.inflate(R.layout.loadingcostume, null)
        val clTitle = view.findViewById<TextView>(R.id.cl_title)
        val imgView: ImageView = view.findViewById(R.id.cl_imageView)
        val cardCL: CardView = view.findViewById(R.id.cl_cardview)

        if (title != null) {
            clTitle.text = title
        }

//        cardCL.setBackgroundColor(Color.parseColor("#70000000"))
        cardCL.setBackgroundResource(R.color.transparent)

        Glide.with(context)
            .load(R.drawable.picacu)
            .placeholder(R.drawable.picacu)
            .centerCrop()
            .into(imgView)

        clTitle.setTextColor(Color.BLACK)

        dialog = CustomDialog(context)
        dialog.setContentView(view)
        dialog.show()
        return dialog
    }

    fun dismiss() {
        dialog.dismiss()
    }

    private fun setColorFilter(drawable: Drawable, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION")
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    class  CustomDialog(cntx: Context) : Dialog(cntx, R.style.LoadingCostumeThem) {
        init {
            window?.decorView?.rootView?.setBackgroundResource(R.color.transparent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                window?.decorView?.setOnApplyWindowInsetsListener { view, windowInsets ->
                    windowInsets.consumeSystemWindowInsets()
                }
            }
        }
    }
}