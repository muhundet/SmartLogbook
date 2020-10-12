package com.example.smartlogbook.PDF;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;


@TargetApi(Build.VERSION_CODES.KITKAT)
public class PDFUtil {

    private static final String TAG = PDFUtil.class.getName();
    public static final double PDF_PAGE_WIDTH = 8.3 * 72;
    public static final double PDF_PAGE_HEIGHT = 11.7 * 72;
//    public static final double PDF_PAGE_WIDTH_INCH = 8.3;
//    public static final double PDF_PAGE_HEIGHT_INCH = 11.7;

    private static PDFUtil sInstance;

    private PDFUtil() {

    }

    public static PDFUtil getInstance() {
        if (sInstance == null) {
            sInstance = new PDFUtil();
        }
        return sInstance;
    }

    public final void generatePDF(final List<View> contentViews, final String filePath, final PDFUtilListener listener) {
        // Check Api Version.
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            // Kitkat
            new GeneratePDFAsync(contentViews, filePath, listener).execute();
        } else {
            // Before Kitkat
            Log.e(TAG, "Generate PDF is not available for your android version.");
            listener.pdfGenerationFailure(new APINotSupportedException("Generate PDF is not available for your android version."));
        }

    }

    public interface PDFUtilListener {
        void pdfGenerationSuccess(File savedPDFFile);

        void pdfGenerationFailure(final Exception exception);
    }

    private class GeneratePDFAsync extends AsyncTask<Void, Void, File> {

        private List<View> mContentViews;

        private String mFilePath;

        private PDFUtilListener mListener = null;

        private Exception mException;

        public GeneratePDFAsync(final List<View> contentViews, final String filePath, final PDFUtilListener listener) {
            this.mContentViews = contentViews;
            this.mFilePath = filePath;
            this.mListener = listener;
        }

        @Override
        protected File doInBackground(Void... params) {
            try {
                // Create PDF Document.
                PdfDocument pdfDocument = new PdfDocument();
                // Write content to PDFDocument.
                writePDFDocument(pdfDocument);
                // Save document to file.
                return savePDFDocumentToStorage(pdfDocument);
            } catch (Exception exception) {
                Log.e(TAG, exception.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(File savedPDFFile) {
            super.onPostExecute(savedPDFFile);
            if (savedPDFFile != null) {
                //Send Success callback.
                mListener.pdfGenerationSuccess(savedPDFFile);
            } else {
                //Send Error callback.
                mListener.pdfGenerationFailure(mException);
            }
        }

        private void writePDFDocument(final PdfDocument pdfDocument) {

            for (int i = 0; i < mContentViews.size(); i++) {

                //Get Content View.
                View contentView = mContentViews.get(i);

                // crate a page description
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.
                        Builder((int) PDF_PAGE_WIDTH, (int) PDF_PAGE_HEIGHT, i + 1).create();

                // start a page
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                // draw view on the page
                Canvas pageCanvas = page.getCanvas();
                pageCanvas.scale(1f, 1f);
                int pageWidth = pageCanvas.getWidth();
                int pageHeight = pageCanvas.getHeight();
                int measureWidth = View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY);
                int measuredHeight = View.MeasureSpec.makeMeasureSpec(pageHeight, View.MeasureSpec.EXACTLY);
                contentView.measure(measureWidth, measuredHeight);
                contentView.layout(0, 0, pageWidth, pageHeight);
                contentView.draw(pageCanvas);

                // finish the page
                pdfDocument.finishPage(page);

            }
        }

        private File savePDFDocumentToStorage(final PdfDocument pdfDocument) throws IOException {
            FileOutputStream fos = null;
            // Create file.
            File pdfFile;
            if (mFilePath == null || mFilePath.isEmpty()) {
                pdfFile = File.createTempFile(Long.toString(new Date().getTime()), "pdf");
            } else {
                pdfFile = new File(Environment.getExternalStorageDirectory()+File.separator + mFilePath);
            }

            //Create parent directories
            File parentFile = pdfFile.getParentFile();
            if (!parentFile.exists() && !parentFile.mkdirs()) {
                throw new IllegalStateException("Couldn't create directory: " + parentFile);
            }
            boolean fileExists = pdfFile.exists();
            // If File already Exists. delete it.
            if (fileExists) {
                fileExists = !pdfFile.delete();
            }
            try {
                if (!fileExists) {
                    // Create New File.
                    fileExists = pdfFile.createNewFile();
                }

                if (fileExists) {
                    // Write PDFDocument to the file.
                    fos = new FileOutputStream(pdfFile);
                    pdfDocument.writeTo(fos);

                    //Close output stream
                    fos.close();

                    // close the document
                    pdfDocument.close();
                }
                return pdfFile;
            } catch (IOException exception) {
                exception.printStackTrace();
                if (fos != null) {
                    fos.close();
                }
                throw exception;
            }
        }
    }


    private static class APINotSupportedException extends Exception {
        // mErrorMessage.
        private String mErrorMessage;

        public APINotSupportedException(final String errorMessage) {
            this.mErrorMessage = errorMessage;
        }

        @Override
        public String toString() {
            return "APINotSupportedException{" + "mErrorMessage='" + mErrorMessage + '\'' + '}';
        }
    }
}
