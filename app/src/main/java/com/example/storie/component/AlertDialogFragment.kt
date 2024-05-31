package com.example.storie.component

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AlertDialogFragment : DialogFragment() {

    interface ButtonClickListener {
        fun onClick(dialog: DialogInterface, which: Int)
    }

    private var onPositiveButtonClickListener: ButtonClickListener? = null
    private var onNegativeButtonClickListener: ButtonClickListener? = null
    private var onNeutralButtonClickListener: ButtonClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = arguments?.getString("title")
        val message = arguments?.getString("message")
        val positiveButtonTitle = arguments?.getString("positiveButtonTitle")
        val negativeButtonTitle = arguments?.getString("negativeButtonTitle")
        val neutralButtonTitle = arguments?.getString("neutralButtonTitle")

        val builder = MaterialAlertDialogBuilder(
            requireContext(),
        )
            .setTitle(title)
            .setMessage(message)

        if (positiveButtonTitle != null) {
            builder.setNegativeButton(positiveButtonTitle) { dialog, which ->
                onPositiveButtonClickListener?.onClick(
                    dialog,
                    which
                )
            }
        }
        if (negativeButtonTitle != null) {
            builder.setNegativeButton(negativeButtonTitle) { dialog, which ->
                onNegativeButtonClickListener?.onClick(
                    dialog,
                    which
                )
            }
        }
        if (neutralButtonTitle != null) {
            builder.setNegativeButton(neutralButtonTitle) { dialog, which ->
                onNeutralButtonClickListener?.onClick(
                    dialog,
                    which
                )
            }
        }

        return builder.create()
    }

    fun setOnPositiveButtonClickListener(listener: ButtonClickListener) {
        onPositiveButtonClickListener = listener
    }

    fun setOnNegativeButtonClickListener(listener: ButtonClickListener) {
        onNegativeButtonClickListener = listener
    }

    fun setOnNeutralButtonClickListener(listener: ButtonClickListener) {
        onNeutralButtonClickListener = listener
    }

}