package com.xplo.code.ui.components;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.xplo.code.R;

/**
 * Copyright 2020 (C) bongo
 * <p>
 * Created  : 4/12/20
 * Updated  :
 * Author   : Nasif Ahmed
 * Desc     :
 * Comment  :
 */
public class BDialog extends DialogFragment {

    private static final String TAG = "BDialog";

    //private Activity activity;
    private FragmentManager fragmentManager;
    private int layoutId;
    private int thumbId;
    private int thumbTint;
    private String title;
    private String message;
    private String posButtonText;
    private String negButtonText;
    private String neuButtonText;
    private boolean isCancelable;
    private DialogListener listener;

    private LinearLayout layoutRoot;
    private ImageView ivDialogThumb;
    private TextView tvDialogTitle;
    private TextView tvDialogMessage;
    private MaterialButton btDialogPositiveButton;
    private MaterialButton btDialogNegativeButton;
    private MaterialButton btDialogNeutralButton;


    public BDialog(Builder builder) {
        //this.activity = builder.activity;
        this.fragmentManager = builder.fragmentManager;
        this.layoutId = builder.layoutId;
        this.thumbId = builder.thumbId;
        this.thumbTint = builder.thumbTint;
        this.title = builder.title;
        this.message = builder.message;
        this.posButtonText = builder.posButtonText;
        this.negButtonText = builder.negButtonText;
        this.neuButtonText = builder.neuButtonText;
        this.isCancelable = builder.isCancelable;
        this.listener = builder.listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(layoutId, container, false);
        rootView.setMinimumHeight(dpToPixel(150));
        rootView.setMinimumWidth(dpToPixel(200));
        return rootView;

    }

    private int dpToPixel(int dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        layoutRoot = view.findViewById(R.id.layoutRoot);
        ivDialogThumb = view.findViewById(R.id.ivDialogThumb);
        tvDialogTitle = view.findViewById(R.id.tvDialogTitle);
        tvDialogMessage = view.findViewById(R.id.tvDialogMessage);
        btDialogPositiveButton = view.findViewById(R.id.btDialogPositiveButton);
        btDialogNegativeButton = view.findViewById(R.id.btDialogNegativeButton);
        btDialogNeutralButton = view.findViewById(R.id.btDialogNeutralButton);


        setView();


    }

    private void setView() {

        setCancelable(isCancelable);

        if (thumbId == -1 && ivDialogThumb != null) ivDialogThumb.setVisibility(View.GONE);
        if (title == null) tvDialogTitle.setVisibility(View.GONE);
        if (message == null) tvDialogMessage.setVisibility(View.GONE);
        if (posButtonText == null) btDialogPositiveButton.setVisibility(View.GONE);
        if (negButtonText == null) btDialogNegativeButton.setVisibility(View.GONE);
        if (neuButtonText == null) btDialogNeutralButton.setVisibility(View.GONE);


        if (thumbId != -1 && ivDialogThumb != null) ivDialogThumb.setImageResource(thumbId);
        if (thumbTint != -1 && ivDialogThumb != null) {
            ImageViewCompat.setImageTintList(ivDialogThumb, ColorStateList.valueOf(thumbTint));
        }
        if (title != null) tvDialogTitle.setText(title);
        if (message != null) tvDialogMessage.setText(message);
        if (posButtonText != null) btDialogPositiveButton.setText(posButtonText);
        if (negButtonText != null) btDialogNegativeButton.setText(negButtonText);
        if (neuButtonText != null) btDialogNeutralButton.setText(neuButtonText);


        btDialogPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickPositiveButton();
                dismiss();
            }
        });

        btDialogNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickNegativeButton();
                dismiss();
            }
        });

        btDialogNeutralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickNeutralButton();
                dismiss();
            }
        });
    }

    public void show() {
        if (!fragmentManager.isDestroyed()) {
            super.show(fragmentManager, TAG);
        }
    }

    public static class Builder {

        //private Activity activity;
        private FragmentManager fragmentManager;
        private int layoutId = R.layout.custom_dialog_pr_nl;
        private int thumbId = -1;
        private int thumbTint = -1;
        private String title = null;
        private String message = null;
        private String posButtonText = null;
        private String negButtonText = null;
        private String neuButtonText = null;
        private boolean isCancelable = true;
        private DialogListener listener;

//        public Builder(Activity activity) {
//            this.activity = activity;
//        }

        public Builder(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
        }


        public Builder setLayoutId(int layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        public Builder setThumbId(int thumbId) {
            this.thumbId = thumbId;
            return this;
        }

        /**
         * To tint thumb icon
         *
         * @param thumbTint must be a "color int". If you have a color resource like R.color.blue, you need to load the color int first:
         */
        public Builder setThumbTint(int thumbTint) {
            this.thumbTint = thumbTint;
            return this;
        }

        public Builder setTitle(String txt) {
            this.title = txt;
            return this;
        }

        public Builder setMessage(String txt) {
            this.message = txt;
            return this;
        }

        public Builder setPosButtonText(String txt) {
            this.posButtonText = txt;
            return this;
        }

        public Builder setNegButtonText(String txt) {
            this.negButtonText = txt;
            return this;
        }

        public Builder setNeuButtonText(String txt) {
            this.neuButtonText = txt;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            isCancelable = cancelable;
            return this;
        }

        public Builder setListener(DialogListener listener) {
            this.listener = listener;
            return this;
        }

        public BDialog build() {
            return new BDialog(this);
        }
    }

    public interface DialogListener {

        void onClickPositiveButton();

        void onClickNegativeButton();

        void onClickNeutralButton();


    }


}
