
package com.losdebuggers.oftapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import java.io.File
import java.io.FileOutputStream

fun renderizarPdf(
    context: Context,
    nombreArchivo: String,
    anchoMaximo: Int = 1000
): List<Bitmap> {

    if (nombreArchivo.isBlank()) return emptyList()

    return try {
        val carpetaCache = File(context.cacheDir, "pdf_preview")

        if (!carpetaCache.exists()) {
            carpetaCache.mkdirs()
        }

        val archivoTemporal = File(carpetaCache, nombreArchivo)

        context.assets.open(nombreArchivo).use { input ->
            FileOutputStream(archivoTemporal).use { output ->
                input.copyTo(output)
            }
        }

        ParcelFileDescriptor.open(
            archivoTemporal,
            ParcelFileDescriptor.MODE_READ_ONLY
        ).use { pfd ->

            PdfRenderer(pfd).use { renderer ->

                val paginas = mutableListOf<Bitmap>()

                for (indice in 0 until renderer.pageCount) {

                    renderer.openPage(indice).use { page ->

                        val escala = anchoMaximo.toFloat() / page.width

                        val ancho = (page.width * escala).toInt()
                        val alto = (page.height * escala).toInt()

                        val bitmap = Bitmap.createBitmap(
                            ancho,
                            alto,
                            Bitmap.Config.ARGB_8888
                        )

                        val matriz = Matrix().apply {
                            setScale(escala, escala)
                        }

                        page.render(
                            bitmap,
                            null,
                            matriz,
                            PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                        )

                        paginas.add(bitmap)
                    }
                }

                paginas
            }
        }

    } catch (e: Exception) {
        emptyList()
    }
}
