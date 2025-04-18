package sara.converter.util;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.InputStream;

public class DocxFileReader extends FileReader {

    public DocxFileReader(Context context) {
        super(context);
    }

    @Override
    protected void createDocumentFromStream(
            Uri uri, @NonNull Document document, Font myfont, InputStream inputStream) throws Exception {

        XWPFDocument doc = new XWPFDocument(inputStream);
        XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
        String fileData = extractor.getText();

        Paragraph documentParagraph = new Paragraph(fileData + "\n", myfont);
        documentParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(documentParagraph);
    }
}