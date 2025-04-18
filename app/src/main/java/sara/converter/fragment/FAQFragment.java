package sara.converter.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sara.converter.R;
import sara.converter.adapter.FAQAdapter;
import sara.converter.interfaces.OnItemClickListener;
import sara.converter.model.FAQItem;


public class FAQFragment extends Fragment implements OnItemClickListener {

    @BindView((R.id.recycler_view_faq))
    RecyclerView mFAQRecyclerView;
    private FAQAdapter mFaqAdapter;
    private List<FAQItem> mFaqs;
    private List<FAQItem> mFaqsCopy;
    private Context mContext;
    /** @noinspection FieldCanBeLocal*/
    private SearchView mSearchView;

    public FAQFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_faq, container, false);

        mSearchView = view.findViewById(R.id.searchView);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterFaq(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newQuery) {
                filterFaq(newQuery);
                return true;
            }
        });

        ButterKnife.bind(this, view);
        mContext = view.getContext();

        initFAQs();
        initFAQRecyclerView();

        return view;
    }

    /**
     * @param text - This is the search text entered in the search box.
     *             Simply filtering out the questions that contains the given search query.
     */
    public void filterFaq(@NonNull String text) {
        mFaqs.clear();
        if (text.isEmpty())
            mFaqs.addAll(mFaqsCopy);
        else {
            text = text.toLowerCase();

            for (FAQItem faq : mFaqsCopy) {
                if (faq.getQuestion().toLowerCase().contains(text)) {
                    mFaqs.add(faq);
                }
            }
        }
        mFaqAdapter.notifyDataSetChanged();
    }

    /**
     * Initializes the FAQs questions and answers by populating data from the
     * strings.xml file and adding it to the custom FAQItem Model.
     */
    private void initFAQs() {
        mFaqs = new ArrayList<>();
        mFaqsCopy = new ArrayList<>();
        String[] questionAnswers = mContext.getResources().getStringArray(R.array.faq_question_answers);
        FAQItem faqItem;
        for (String questionAnswer : questionAnswers) {
            String[] questionAnswerSplit = questionAnswer.split("#####");
            faqItem = new FAQItem(questionAnswerSplit[0], questionAnswerSplit[1]);
            mFaqs.add(faqItem);
        }
        mFaqsCopy.addAll(mFaqs);
    }


    /**
     * Initializes the RecyclerView using the FAQAdapter to populate the views
     * for FAQs
     */
    private void initFAQRecyclerView() {
        mFaqAdapter = new FAQAdapter(mFaqs, this);
        mFAQRecyclerView.setAdapter(mFaqAdapter);
    }

    /**
     * This method defines the behavior of the FAQItem when clicked.
     * It makes the FAQItem view expanded or contracted.
     *
     * @param position - view position
     */
    @Override
    public void onItemClick(int position) {
        FAQItem faqItem = mFaqs.get(position);
        faqItem.setExpanded(!faqItem.isExpanded());
        mFaqAdapter.notifyItemChanged(position);
    }
}
