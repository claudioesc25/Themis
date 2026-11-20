package com.Duoc.Themis.ui

import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.Duoc.Themis.model.Documento
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object PdfGenerador {

    private const val ANCHO = 595
    private const val ALTO = 842
    private const val MARGEN = 48
    private const val LIMITE_INFERIOR = ALTO - MARGEN - 24f

    fun guardarDocumento(contexto: Context, doc: Documento): Boolean {
        val tituloDoc = doc.titulo.ifBlank { "Documento #${doc.id}" }
        val referenciaDoc = buildString {
            append("Tipo: ${doc.tipo_documento}")
            if (doc.fecha_publicacion.isNotBlank()) {
                append("  |  Publicación: ${doc.fecha_publicacion}")
            }
            if (doc.fecha_actualizacion.isNotBlank()) {
                append("  |  Actualización: ${doc.fecha_actualizacion}")
            }
        }
        val contenidoDoc = doc.contenido

        val pdf = PdfDocument()
        var numeroPagina = 1
        var uriInsertada: Uri? = null

        fun iniciarPagina(): PdfDocument.Page {
            val pagina = pdf.startPage(
                PdfDocument.PageInfo.Builder(ANCHO, ALTO, numeroPagina).create()
            )
            numeroPagina++
            return pagina
        }

        fun dibujarCabecera(canvas: Canvas, esPrimeraPagina: Boolean) {
            if (esPrimeraPagina) {
                canvas.drawRect(
                    0f, 0f, ANCHO.toFloat(), 90f,
                    Paint().apply { color = 0xFF0A1F44.toInt() }
                )
                val pCabecera = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = 0xFFD4AF37.toInt()
                    textSize = 26f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                canvas.drawText("THEMIS", MARGEN.toFloat(), 56f, pCabecera)
            } else {
                val pEncabezadoSecundario = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = 0xFF666666.toInt()
                    textSize = 10f
                }
                canvas.drawText(
                    "THEMIS — ${tituloDoc.take(65)}",
                    MARGEN.toFloat(),
                    32f,
                    pEncabezadoSecundario
                )
            }
        }

        var paginaActual = iniciarPagina()
        var y = 130f
        dibujarCabecera(paginaActual.canvas, esPrimeraPagina = true)

        fun bloque(texto: String, paint: TextPaint, espacio: Float) {
            if (texto.isBlank()) return

            val anchoUtil = ANCHO - 2 * MARGEN
            val layout = StaticLayout.Builder
                .obtain(texto, 0, texto.length, paint, anchoUtil)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(4f, 1f)
                .build()

            if (y + layout.height > LIMITE_INFERIOR) {
                pdf.finishPage(paginaActual)
                paginaActual = iniciarPagina()
                y = 60f
                dibujarCabecera(paginaActual.canvas, esPrimeraPagina = false)
            }

            val canvas = paginaActual.canvas
            canvas.save()
            canvas.translate(MARGEN.toFloat(), y)
            layout.draw(canvas)
            canvas.restore()
            y += layout.height + espacio
        }

        val pTitulo = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFF0A1F44.toInt()
            textSize = 20f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val pReferencia = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFF8A6D00.toInt()
            textSize = 13f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        }
        val pSubtitulo = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFF0A1F44.toInt()
            textSize = 15f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val pTexto = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFF222222.toInt()
            textSize = 14f
        }
        val pPie = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFF666666.toInt()
            textSize = 11f
        }

        return try {
            bloque(tituloDoc, pTitulo, 10f)
            bloque(referenciaDoc, pReferencia, 24f)
            bloque("Contenido", pSubtitulo, 8f)
            bloque(contenidoDoc, pTexto, 30f)
            bloque(
                "Documento informativo generado por la app Themis. Para el texto oficial y vigente " +
                        "consulta la Biblioteca del Congreso Nacional de Chile (bcn.cl/leychile).",
                pPie,
                0f
            )

            pdf.finishPage(paginaActual)

            val nombreBase = tituloDoc
                .replace(Regex("[^A-Za-z0-9]+"), "_")
                .trim('_')
                .take(50)
                .ifBlank { "Documento" }
            val nombreArchivo = "Themis_$nombreBase.pdf"

            val salida: OutputStream? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val valores = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, nombreArchivo)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val uri = contexto.contentResolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    valores
                )
                uriInsertada = uri
                uri?.let { contexto.contentResolver.openOutputStream(it) }
            } else {
                val carpeta = contexto.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                carpeta?.mkdirs()
                FileOutputStream(File(carpeta, nombreArchivo))
            }

            if (salida == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && uriInsertada != null) {
                    contexto.contentResolver.delete(uriInsertada, null, null)
                }
                return false
            }

            salida.use { pdf.writeTo(it) }
            true
        } catch (e: Exception) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && uriInsertada != null) {
                try {
                    contexto.contentResolver.delete(uriInsertada, null, null)
                } catch (_: Exception) {
                }
            }
            false
        } finally {
            pdf.close()
        }
    }
}