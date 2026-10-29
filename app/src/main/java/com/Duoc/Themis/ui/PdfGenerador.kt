package com.Duoc.Themis.ui

import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.Duoc.Themis.model.DocumentoLegal
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object PdfGenerador {

    private const val ANCHO = 595
    private const val ALTO = 842
    private const val MARGEN = 48

    fun guardar(contexto: Context, documento: DocumentoLegal): Boolean {
        val pdf = PdfDocument()
        return try {
            val pagina = pdf.startPage(PdfDocument.PageInfo.Builder(ANCHO, ALTO, 1).create())
            val canvas = pagina.canvas

            // Cabecera azul con el nombre de la app
            canvas.drawRect(0f, 0f, ANCHO.toFloat(), 90f, Paint().apply { color = 0xFF0A1F44.toInt() })
            val pCabecera = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                color = 0xFFD4AF37.toInt()
                textSize = 26f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            canvas.drawText("THEMIS", MARGEN.toFloat(), 56f, pCabecera)

            var y = 130f
            fun bloque(texto: String, paint: TextPaint, espacio: Float) {
                val layout = StaticLayout.Builder
                    .obtain(texto, 0, texto.length, paint, ANCHO - 2 * MARGEN)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(4f, 1f)
                    .build()
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

            bloque(documento.titulo, pTitulo, 10f)
            bloque(documento.referencia, pReferencia, 24f)
            bloque("Resumen", pSubtitulo, 8f)
            bloque(documento.resumen, pTexto, 30f)
            bloque(
                "Resumen informativo generado por la app Themis. Para el texto oficial y vigente " +
                        "consulta la Biblioteca del Congreso Nacional de Chile (bcn.cl/leychile).",
                pPie, 0f
            )

            pdf.finishPage(pagina)

            val nombreArchivo = "Themis_" +
                    documento.titulo.replace(Regex("[^A-Za-z0-9]+"), "_").trim('_').take(50) + ".pdf"

            val salida: OutputStream? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val valores = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, nombreArchivo)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val uri = contexto.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, valores)
                uri?.let { contexto.contentResolver.openOutputStream(it) }
            } else {
                val carpeta = contexto.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                carpeta?.mkdirs()
                FileOutputStream(File(carpeta, nombreArchivo))
            }

            salida?.use { pdf.writeTo(it) } != null
        } catch (e: Exception) {
            false
        } finally {
            pdf.close()
        }
    }
}