package sara.converter.model;

public class PDFOptions {

    private String mOutFileName;
    private boolean mPasswordProtected;
    private String mPassword;
    private String mPageSize;
    private int mBorderWidth;
    private boolean mWatermarkAdded;
    private Watermark mWatermark;
    private int mPageColor;

    PDFOptions() {

    }

    PDFOptions(String mFileName, String mPageSize, boolean mPasswordProtected, String mPassword,
               int mBorderWidth, boolean mWatermarkAdded, Watermark mWatermark, int pageColor) {
        this.mOutFileName = mFileName;
        this.mPageSize = mPageSize;
        this.mPasswordProtected = mPasswordProtected;
        this.mPassword = mPassword;
        this.mBorderWidth = mBorderWidth;
        this.mWatermarkAdded = mWatermarkAdded;
        this.mWatermark = mWatermark;
        this.mPageColor = pageColor;
    }

    public String getOutFileName() {
        return mOutFileName;
    }

    public void setOutFileName(String mOutFileName) {
        this.mOutFileName = mOutFileName;
    }

    public String getPageSize() {
        return mPageSize;
    }

    public void setPageSize(String mPageSize) {
        this.mPageSize = mPageSize;
    }

    public boolean isPasswordProtected() {
        return mPasswordProtected;
    }

    public void setPasswordProtected(boolean mPasswordProtected) {
        this.mPasswordProtected = mPasswordProtected;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int mBorderWidth) {
        this.mBorderWidth = mBorderWidth;
    }

    public boolean isWatermarkAdded() {
        return mWatermarkAdded;
    }

    public void setWatermarkAdded(boolean mWatermarkAdded) {
        this.mWatermarkAdded = mWatermarkAdded;
    }

    public Watermark getWatermark() {
        return this.mWatermark;
    }

    public void setWatermark(Watermark mWatermark) {
        this.mWatermark = mWatermark;
    }

    public int getPageColor() {
        return mPageColor;
    }

    public void setPageColor(int pageColor) {
        this.mPageColor = pageColor;
    }
}
