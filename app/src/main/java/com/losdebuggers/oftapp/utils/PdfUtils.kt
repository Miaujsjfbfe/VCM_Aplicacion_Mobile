package com.losdebuggers.oftapp.utils

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import android.content.ContentValues
import android.os.Build
import android.os.Environment

fun compartirPdf(
    context: Context,
    nombreArchivo: String,
    tituloDocumento: String
) {
    if (nombreArchivo.isBlank()) {
        Toast.makeText(context, "No hay PDF asociado para compartir", Toast.LENGTH_SHORT).show()
        return
    }

    try {
        val carpetaCompartir = File(context.cacheDir, "shared_pdfs")
        if (!carpetaCompartir.exists()) carpetaCompartir.mkdirs()

        val archivoDestino = File(carpetaCompartir, nombreArchivo)
        context.assets.open(nombreArchivo).use { input ->
            FileOutputStream(archivoDestino).use { output ->
                input.copyTo(output)
            }
        }

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            archivoDestino
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, tituloDocumento)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(
            Intent.createChooser(intent, "Compartir informe PDF")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    } catch (_: Exception) {
        Toast.makeText(
            context,
            "No se pudo compartir '$nombreArchivo'",
            Toast.LENGTH_LONG
        ).show()
    }
}

fun descargarPdfDesdeAssets(
    context: Context,
    nombreArchivo: String,
    mensajeExito: String = "PDF guardado en Descargas"
) {
    if (nombreArchivo.isBlank()) {
        Toast.makeText(context, "No hay archivo PDF asociado", Toast.LENGTH_SHORT).show()
        return
    }

    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, nombreArchivo)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val uri = context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                contentValues
            )
            if (uri != null) {
                context.assets.open(nombreArchivo).use { input ->
                    context.contentResolver.openOutputStream(uri)?.use { output ->
                        input.copyTo(output)
                    }
                }
                Toast.makeText(context, mensajeExito, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "No se pudo crear el archivo en Descargas", Toast.LENGTH_SHORT).show()
            }
        } else {
            val carpetaDescargas = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!carpetaDescargas.exists()) carpetaDescargas.mkdirs()
            val archivoDestino = File(carpetaDescargas, nombreArchivo)
            context.assets.open(nombreArchivo).use { input ->
                FileOutputStream(archivoDestino).use { output ->
                    input.copyTo(output)
                }
            }
            Toast.makeText(context, mensajeExito, Toast.LENGTH_LONG).show()
        }
    } catch (_: Exception) {
        Toast.makeText(
            context,
            "No se encontró '$nombreArchivo' en assets",
            Toast.LENGTH_LONG
        ).show()
    }
}

