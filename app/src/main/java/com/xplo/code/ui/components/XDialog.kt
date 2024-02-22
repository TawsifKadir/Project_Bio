package com.xplo.code.ui.components

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.button.MaterialButton
import com.xplo.code.R

/**
 * Copyright 2020 (C) bongo
 *
 *
 * Created  : 4/12/20
 * Updated  :
 * Author   : Nasif Ahmed
 * Desc     :
 * Comment  :
 */
class XDialog(builder: Builder) : DialogFragment() {

    companion object {
        private const val TAG = "XDialog"
    }

    private val fragmentManager: FragmentManager

    private val layoutId: Int
    private val thumbId: Int
    private val thumbTint: Int
    private val title: String?
    private val message: String?
    private val posButtonText: String?
    private val negButtonText: String?
    private val neuButtonText: String?
    private val isCancelable: Boolean

    private val listener: DialogListener?
    private var layoutRoot: LinearLayout? = null
    private var ivDialogThumb: ImageView? = null
    private var tvDialogTitle: TextView? = null
    private var tvDialogMessage: TextView? = null
    private var btDialogPositiveButton: MaterialButton? = null
    private var btDialogNegativeButton: MaterialButton? = null
    private var btDialogNeutralButton: MaterialButton? = null

    init {
        //this.activity = builder.activity;
        fragmentManager = builder.fragmentManager
        layoutId = builder.layoutId
        thumbId = builder.thumbId
        thumbTint = builder.thumbTint
        title = builder.title
        message = builder.message
        posButtonText = builder.posButtonText
        negButtonText = builder.negButtonText
        neuButtonText = builder.neuButtonText
        isCancelable = builder.isCancelable
        listener = builder.listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(layoutId, container, false)
        rootView.minimumHeight = dpToPixel(150)
        rootView.minimumWidth = dpToPixel(200)
        return rootView
    }

    private fun dpToPixel(dp: Int): Int {
        val scale = requireContext().resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        layoutRoot = view.findViewById(R.id.layoutRoot)
        ivDialogThumb = view.findViewById(R.id.ivDialogThumb)
        tvDialogTitle = view.findViewById(R.id.tvDialogTitle)
        tvDialogMessage = view.findViewById(R.id.tvDialogMessage)
        btDialogPositiveButton = view.findViewById(R.id.btDialogPositiveButton)
        btDialogNegativeButton = view.findViewById(R.id.btDialogNegativeButton)
        btDialogNeutralButton = view.findViewById(R.id.btDialogNeutralButton)
        setView()
    }

    private fun setView() {
        setCancelable(isCancelable)

        if (thumbId == -1 && ivDialogThumb != null) ivDialogThumb?.visibility = View.GONE
        if (title == null) tvDialogTitle?.visibility = View.GONE
        if (message == null) tvDialogMessage?.visibility = View.GONE
        if (posButtonText == null) btDialogPositiveButton?.visibility = View.GONE
        if (negButtonText == null) btDialogNegativeButton?.visibility = View.GONE
        if (neuButtonText == null) btDialogNeutralButton?.visibility = View.GONE
        if (thumbId != -1 && ivDialogThumb != null) ivDialogThumb?.setImageResource(thumbId)
        if (thumbTint != -1 && ivDialogThumb != null) {
            ImageViewCompat.setImageTintList(ivDialogThumb!!, ColorStateList.valueOf(thumbTint))
        }
        if (title != null) tvDialogTitle?.text = title
        if (message != null) tvDialogMessage?.text = message
        if (posButtonText != null) btDialogPositiveButton?.text = posButtonText
        if (negButtonText != null) btDialogNegativeButton?.text = negButtonText
        if (neuButtonText != null) btDialogNeutralButton?.text = neuButtonText

        //tvDialogMessage?.movementMethod = ScrollingMovementMethod()

        btDialogPositiveButton?.setOnClickListener {
            listener?.onClickPositiveButton()
            dismiss()
        }
        btDialogNegativeButton?.setOnClickListener {
            listener?.onClickNegativeButton()
            dismiss()
        }
        btDialogNeutralButton?.setOnClickListener {
            listener?.onClickNeutralButton()
            dismiss()
        }
    }

    fun show() {
        if (!fragmentManager.isDestroyed) {
            super.show(fragmentManager, TAG)
        }
    }

    class Builder(val fragmentManager: FragmentManager) {

        var layoutId = R.layout.custom_dialog_pr_nl
        var thumbId = -1
        var thumbTint = -1
        var title: String? = null
        var message: String? = null
        var posButtonText: String? = null
        var negButtonText: String? = null
        var neuButtonText: String? = null
        var isCancelable = true
        var listener: DialogListener? = null

        fun setLayoutId(layoutId: Int): Builder {
            this.layoutId = layoutId
            return this
        }

        fun setThumbId(thumbId: Int): Builder {
            this.thumbId = thumbId
            return this
        }

        /**
         * To tint thumb icon
         *
         * @param thumbTint must be a "color int". If you have a color resource like R.color.blue, you need to load the color int first:
         */
        fun setThumbTint(thumbTint: Int): Builder {
            this.thumbTint = thumbTint
            return this
        }

        fun setTitle(txt: String?): Builder {
            title = txt
            return this
        }

        fun setMessage(txt: String?): Builder {
            message = txt
            return this
        }

        fun setPosButtonText(txt: String?): Builder {
            posButtonText = txt
            return this
        }

        fun setNegButtonText(txt: String?): Builder {
            negButtonText = txt
            return this
        }

        fun setNeuButtonText(txt: String?): Builder {
            neuButtonText = txt
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            isCancelable = cancelable
            return this
        }

        fun setListener(listener: DialogListener?): Builder {
            this.listener = listener
            return this
        }

        fun build(): XDialog {
            return XDialog(this)
        }
    }

    interface DialogListener {
        fun onClickPositiveButton()
        fun onClickNegativeButton()
        fun onClickNeutralButton()
    }

}
