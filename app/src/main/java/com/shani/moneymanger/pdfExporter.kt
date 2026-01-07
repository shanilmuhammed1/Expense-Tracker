package com.shani.moneymanger

import android.content.ContentValues
import android.content.Context
import android.widget.Toast
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

object pdfExporter {
    fun exportTransactions(
        context: Context,
        transactions: List<Transaction>,
        dateRange: DateRange
    ) {
        val titlePaint = Paint().apply {
            textSize = 24f
            isFakeBoldText = true
            color = android.graphics.Color.BLACK
        }
        val headerPaint = Paint().apply {
            textSize = 16f
            isFakeBoldText = true
            color = android.graphics.Color.BLACK
        }
        val normalPaint = Paint().apply {
            textSize = 12f
            color = android.graphics.Color.BLACK
        }
        val linePaint = Paint().apply {
            strokeWidth = 2f
            color = android.graphics.Color.BLACK
        }
        val pdfDocument = PdfDocument()
        val pageWidth = 595
        val pageHeight = 842
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        var yPosition = 40f

        canvas.drawText("Expense statement", 40f, yPosition, titlePaint)
        yPosition += 30f

        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val periodText = "Period: ${dateRange.startDate.format(dateFormatter)} - ${dateRange.endDate.format(dateFormatter)}"
        canvas.drawText(periodText, 40f, yPosition, normalPaint)
        yPosition += 30f
        canvas.drawLine(40f, yPosition, pageWidth - 40f, yPosition, linePaint)
        yPosition += 20f

        canvas.drawText("SUMMARY", 40f, yPosition, headerPaint)
        yPosition += 25f

        val totalIncome = transactions
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }

        val totalExpense = transactions
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }

        val netBalance = totalIncome - totalExpense

        canvas.drawText("Total Income:     ₹$totalIncome", 40f, yPosition, normalPaint)
        yPosition += 18f

        canvas.drawText("Total Expenses:   ₹$totalExpense", 40f, yPosition, normalPaint)
        yPosition += 18f

        canvas.drawText("Net Balance:      ₹$netBalance", 40f, yPosition, normalPaint)
        yPosition += 30f

        canvas.drawLine(40f, yPosition, pageWidth - 40f, yPosition, linePaint)
        yPosition += 20f

        canvas.drawText("TRANSACTIONS", 40f, yPosition, headerPaint)
        yPosition += 25f

        val dateX = 40f
        val categoryX = 130f
        val descriptionX = 250f
        val amountX = 450f

        canvas.drawText("Date", dateX, yPosition, normalPaint)
        canvas.drawText("Category", categoryX, yPosition, normalPaint)
        canvas.drawText("Description", descriptionX, yPosition, normalPaint)
        canvas.drawText("Amount", amountX, yPosition, normalPaint)
        yPosition += 5f

        canvas.drawLine(40f, yPosition, pageWidth - 40f, yPosition, linePaint)
        yPosition += 15f

        val transactionDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")
        for (transaction in transactions) {
            val dateText = transaction.date.format(transactionDateFormatter)
            val amountText = if (transaction.type == TransactionType.INCOME) {
                "+₹${transaction.amount}"
            } else {
                "-₹${transaction.amount}"
            }
            canvas.drawText(dateText, dateX, yPosition, normalPaint)
            canvas.drawText(transaction.category.name, categoryX, yPosition, normalPaint)
            canvas.drawText(transaction.description, descriptionX, yPosition, normalPaint)
            canvas.drawText(amountText, amountX, yPosition, normalPaint)

            yPosition += 18f

            if (yPosition > pageHeight - 50) {
                // Stop drawing (we'll handle multiple pages later if needed)
                canvas.drawText("... and more", dateX, yPosition, normalPaint)
                break
            }
        }
        pdfDocument.finishPage(page)
        val timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val fileName = "expense_statement_$timestamp.pdf"

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ way: Use MediaStore
                val contentValues = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                    put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val uri = context.contentResolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    contentValues
                )

                uri?.let {
                    context.contentResolver.openOutputStream(it)?.use { outputStream ->
                        pdfDocument.writeTo(outputStream)
                    }
                    Toast.makeText(context, "PDF saved to Downloads: $fileName", Toast.LENGTH_LONG).show()
                } ?: run {
                    Toast.makeText(context, "Failed to create file", Toast.LENGTH_SHORT).show()
                }

            } else {
                // Android 9 and below: Direct file access
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file = File(downloadsDir, fileName)

                FileOutputStream(file).use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }

                Toast.makeText(context, "PDF saved to Downloads: $fileName", Toast.LENGTH_LONG).show()
            }

        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }

    }
}
