package sara.converter.model;

public class FAQItem {

    private String mQuestion;
    private String mAnswer;
    private boolean mIsExpanded;

    /**
     * FAQ Item constructor
     *
     * @param question - question text
     * @param answer   - answer text
     */
    public FAQItem(String question, String answer) {
        this.mQuestion = question;
        this.mAnswer = answer;
        mIsExpanded = false;
    }

    public boolean isExpanded() {
        return mIsExpanded;
    }

    public void setExpanded(boolean expanded) {
        mIsExpanded = expanded;
    }

    public String getQuestion() {
        return mQuestion;
    }

    /** @noinspection unused*/
    public void setQuestion(String question) {
        this.mQuestion = question;
    }

    public String getAnswer() {
        return mAnswer;
    }

    /** @noinspection unused*/
    public void setAnswer(String answer) {
        this.mAnswer = answer;
    }
}
