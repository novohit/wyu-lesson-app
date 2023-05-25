package com.wyu;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.wyu.config.ContextHolder;
import com.wyu.config.MyState;
import com.wyu.data.Subject;
import com.wyu.model.Score;
import com.wyu.model.ScoreVO;
import com.wyu.util.MyApplication;

public class ScoreListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    private List<Score> scoreList;

    private String selectedTerm;

    public String getSelectedTerm() {
        return selectedTerm;
    }

    public void setSelectedTerm(String selectedTerm) {
        this.selectedTerm = selectedTerm;
        ScoreVO scoreVO = ContextHolder.scoreData.get(selectedTerm);
        if (scoreVO != null) {
            Log.i(MyState.TAG, "切换为第" + selectedTerm + "学期");
            scoreList = scoreVO.getScoreList();
        }
    }

    static class ViewHolderContent extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView courseName;
        TextView score;
        TextView credit;
        TextView studyType;
        TextView category;
        TextView username;

        public ViewHolderContent(View view) {
            super(view);
            cardView = (CardView) view;
            courseName = (TextView) view.findViewById(R.id.scores_kecheng);
            score = (TextView) view.findViewById(R.id.score_value);
            credit = (TextView) view.findViewById(R.id.scores_credit);
            studyType = (TextView) view.findViewById(R.id.score_study_type);
            category = (TextView) view.findViewById(R.id.score_category);
            username = (TextView) view.findViewById(R.id.score_username);
        }
    }

    public ScoreListAdapter(String selectedTerm) {
        if (ContextHolder.scoreData.get(selectedTerm) == null) {
            scoreList = new ArrayList<>();
            ScoreVO scoreVO = new ScoreVO(new ArrayList<>(), 0);
            ContextHolder.scoreData.put(selectedTerm, scoreVO);
        } else {
            setSelectedTerm(selectedTerm);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scores_cell,
                parent, false);
        return new ViewHolderContent(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderContent) {
            ViewHolderContent holderContent = (ViewHolderContent) holder;
            Score score = scoreList.get(position);
            holderContent.score.setText(score.getScore());
            holderContent.courseName.setText(score.getCourseName());
            holderContent.credit.setText(score.getCredit());
            holderContent.studyType.setText(score.getStudyType());
            holderContent.category.setText(score.getCategory());
            holderContent.username.setText(score.getUsername());
            if ((!score.getScore().equals("")) && ("不及格".equals(score.getScore()) || (isInteger(score.getScore()) == true && Integer.valueOf(score.getScore()) < 60))) {
                holderContent.score.setTextColor(MyApplication.getContext().getResources().getColor(R.color.red));
                holderContent.courseName.setTextColor(MyApplication.getContext().getResources().getColor(R.color.red));
                holderContent.credit.setTextColor(MyApplication.getContext().getResources().getColor(R.color.red));
                holderContent.studyType.setTextColor(MyApplication.getContext().getResources().getColor(R.color.red));
                holderContent.category.setTextColor(MyApplication.getContext().getResources().getColor(R.color.red));
                holderContent.username.setTextColor(MyApplication.getContext().getResources().getColor(R.color.red));
                holderContent.cardView.setCardBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.darkgrey));
            } else {
                holderContent.score.setTextColor(MyApplication.getContext().getResources().getColor(R.color.black));
                holderContent.courseName.setTextColor(MyApplication.getContext().getResources().getColor(R.color.black));
                holderContent.credit.setTextColor(MyApplication.getContext().getResources().getColor(R.color.black));
                holderContent.studyType.setTextColor(MyApplication.getContext().getResources().getColor(R.color.black));
                holderContent.category.setTextColor(MyApplication.getContext().getResources().getColor(R.color.black));
                holderContent.username.setTextColor(MyApplication.getContext().getResources().getColor(R.color.black));
                holderContent.cardView.setCardBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.white));
            }
        }
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }
}