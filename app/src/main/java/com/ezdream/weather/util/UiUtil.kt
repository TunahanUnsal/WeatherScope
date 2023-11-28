package com.ezdream.weather.util

import android.app.AlertDialog
import android.content.Context
import android.graphics.Movie
import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.io.InputStream

object UiUtil {

    fun customAlertDialog(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)

        with(builder)
        {
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    fun showSnackBar(parentView: View, message: String) {
        val snack = Snackbar.make(parentView, message, Snackbar.LENGTH_SHORT)
        snack.show()
    }

    fun getDuration(context: Context, id: Int): Int {
        val temp: InputStream = context.resources.openRawResource(id)
        val movie = Movie.decodeStream(temp)  //deprecation i know but i can not find new one in AnimationDrawable
        return movie.duration()
    }
}
