package sara.converter.interfaces;

public interface MergeFilesListener {
    void resetValues(boolean isPDFMerged, String path);

    void mergeStarted();
}
