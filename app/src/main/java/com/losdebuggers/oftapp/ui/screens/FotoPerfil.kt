package com.losdebuggers.oftapp.ui.screens

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

internal const val PERFIL_PREFS = "perfil_prefs"

/** Clave estable por usuario (solo letras y dígitos del RUT). */
internal fun claveFotoUsuario(rut: String): String = rut.filter { it.isLetterOrDigit() }

/**
 * Devuelve la foto de perfil actual del usuario (o null si no tiene).
 * Se actualiza sola cuando se cambia la foto desde la pantalla de Perfil.
 */
@Composable
fun rememberFotoPerfil(rut: String): ImageBitmap? {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences(PERFIL_PREFS, Context.MODE_PRIVATE) }
    val clave = remember(rut) { claveFotoUsuario(rut) }

    var path by remember(clave) { mutableStateOf(prefs.getString("foto_$clave", null)) }

    DisposableEffect(prefs, clave) {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { p, key ->
            if (key == "foto_$clave") path = p.getString("foto_$clave", null)
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        onDispose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    val bitmap by produceState<ImageBitmap?>(initialValue = null, path) {
        value = path?.let { p ->
            withContext(Dispatchers.IO) { cargarBitmap(p) }?.asImageBitmap()
        }
    }
    return bitmap
}

/** Copia la imagen elegida a almacenamiento interno (el URI del selector es temporal). */
internal fun guardarFoto(context: Context, uri: Uri, clave: String): String? {
    return try {
        val destino = File(context.filesDir, "foto_${clave}_${System.currentTimeMillis()}.jpg")
        val entrada = context.contentResolver.openInputStream(uri) ?: return null
        entrada.use { input -> destino.outputStream().use { input.copyTo(it) } }
        destino.absolutePath
    } catch (e: Exception) {
        null
    }
}

/** Decodifica la foto reducida y corrige la rotación EXIF. */
internal fun cargarBitmap(path: String, maxLado: Int = 600): Bitmap? {
    return try {
        val limites = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(path, limites)

        var muestra = 1
        while (limites.outWidth / muestra > maxLado * 2 || limites.outHeight / muestra > maxLado * 2) {
            muestra *= 2
        }
        val bmp = BitmapFactory.decodeFile(
            path,
            BitmapFactory.Options().apply { inSampleSize = muestra }
        ) ?: return null

        val rotacion = when (
            ExifInterface(path).getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
        ) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }
        if (rotacion == 0f) bmp
        else Bitmap.createBitmap(
            bmp, 0, 0, bmp.width, bmp.height,
            Matrix().apply { postRotate(rotacion) }, true
        )
    } catch (e: Exception) {
        null
    }
}