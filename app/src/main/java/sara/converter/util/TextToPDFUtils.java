package sara.converter.util;

import static sara.converter.util.Constants.MASTER_PWD_STRING;
import static sara.converter.util.Constants.STORAGE_LOCATION;
import static sara.converter.util.Constants.appName;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import org.jetbrains.annotations.Contract;

import java.io.FileOutputStream;
import java.io.IOException;

import sara.converter.R;
import sara.converter.database.DatabaseHelper;
import sara.converter.model.TextToPDFOptions;

public class TextToPDFUtils {

    private final Activity mContext;
    private final SharedPreferences mSharedPreferences;
    private final TextFileReader mTextFileReader;
    private final DocFileReader mDocFileReader;
    private final DocxFileReader mDocxFileReader;

    public TextToPDFUtils(Activity context) {
        mContext = context;
        mTextFileReader = new TextFileReader(mContext);
        mDocFileReader = new DocFileReader(mContext);
        mDocxFileReader = new DocxFileReader(mContext);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    /**
     * Create a PDF from a Text File
     *
     * @param mTextToPDFOptions TextToPDFOptions Object
     * @param fileExtension     file extension represented as string
     */
    public void createPdfFromTextFile(@NonNull TextToPDFOptions mTextToPDFOptions, String fileExtension)
            throws DocumentException, IOException {

        String masterpwd = mSharedPreferences.getString(MASTER_PWD_STRING, appName);

        Rectangle pageSize = new Rectangle(PageSize.getRectangle(mTextToPDFOptions.getPageSize()));
        pageSize.setBackgroundColor(getBaseColor(mTextToPDFOptions.getPageColor()));
        Document document = new Document(pageSize);

        String finalOutput = mSharedPreferences.getString(STORAGE_LOCATION,
                StringUtils.getInstance().getDefaultStorageLocation()) +
                mTextToPDFOptions.getOutFileName() + ".pdf";
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(finalOutput));
        writer.setPdfVersion(PdfWriter.VERSION_1_7);
        if (mTextToPDFOptions.isPasswordProtected()) {
            writer.setEncryption(mTextToPDFOptions.getPassword().getBytes(),
                    masterpwd.getBytes(),
                    PdfWriter.ALLOW_PRINTING | PdfWriter.ALLOW_COPY,
                    PdfWriter.ENCRYPTION_AES_128);
        }

        document.open();

        Font myfont = new Font(mTextToPDFOptions.getFontFamily());
        myfont.setStyle(Font.NORMAL);
        myfont.setSize(mTextToPDFOptions.getFontSize());
        myfont.setColor(getBaseColor(mTextToPDFOptions.getFontColor()));

        document.add(new Paragraph("\n"));
        addContentToDocument(mTextToPDFOptions, fileExtension, document, myfont);
        document.close();

        new DatabaseHelper(mContext).insertRecord(finalOutput, mContext.getString(R.string.created));
    }

    private void addContentToDocument(TextToPDFOptions mTextToPDFOptions, String fileExtension,
                                      Document document, Font myfont) throws DocumentException {
        if (fileExtension == null)
            throw new DocumentException();

        switch (fileExtension) {
            case Constants.docExtension ->
                    mDocFileReader.read(mTextToPDFOptions.getInFileUri(), document, myfont);
            case Constants.docxExtension ->
                    mDocxFileReader.read(mTextToPDFOptions.getInFileUri(), document, myfont);
            default -> mTextFileReader.read(mTextToPDFOptions.getInFileUri(), document, myfont);
        }
    }

    /**
     * Read the BaseColor of passed color
     *
     * @param color value of color in int
     */
    @NonNull
    @Contract("_ -> new")
    private BaseColor getBaseColor(int color) {
        return new BaseColor(
                Color.red(color),
                Color.green(color),
                Color.blue(color)
        );
    }
}
