package com.wyu;

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
import com.wyu.data.ScoresList;
import com.wyu.data.Subject;
import com.wyu.R;
import com.wyu.util.MyApplication;

public class ScoresListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    private List<String> myScoresListsList;
    private Map<String,ScoresList> allScores;
    private List<Subject> allAllSubjects;
    private int sumNum;

    static class  ViewHolderBiao extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolderBiao(View view) {
            super(view);
            textView = (TextView)view.findViewById(R.id.tv_biao);
        }
    }

    static class ViewHolderContent extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView kecheng;
        TextView chengji;
        TextView xuefen;
        TextView kechengxz;
        TextView leibie;
        TextView biaozhi;
        public ViewHolderContent(View view) {
            super(view);
            cardView = (CardView) view;
            kecheng = (TextView) view.findViewById(R.id.scores_kecheng);
            chengji = (TextView) view.findViewById(R.id.scores_chengji);
            xuefen = (TextView) view.findViewById(R.id.scores_xuefen);
            kechengxz = (TextView) view.findViewById(R.id.scores_kechengxz);
            leibie = (TextView) view.findViewById(R.id.scores_leibie);
            biaozhi = (TextView) view.findViewById(R.id.scores_biaozhi);
        }
    }
    public ScoresListAdapter(List<String> myScoresListsList,Map<String,ScoresList> allScores) {
         this.myScoresListsList = myScoresListsList;
         this.allScores = allScores;
         if(myScoresListsList!=null&&allScores!=null)
            getSum();
         else
             sumNum = 0;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if(viewType==123456){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_biao,
                    parent, false);
            return new ViewHolderBiao(view);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scores_cell,
                    parent, false);
            return new ViewHolderContent(view);
        }


    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof  ViewHolderContent){
            ViewHolderContent hholder = (ViewHolderContent)holder;
            Subject sub = allAllSubjects.get(position);
            hholder.chengji.setText(sub.chengji);
            hholder.kecheng.setText(sub.mingcheng);
            hholder.xuefen.setText(sub.xuefen);
            hholder.kechengxz.setText(sub.kechengxz);
            hholder.leibie.setText(sub.leibie);
            hholder.biaozhi.setText(sub.biaozhi);
            if((!sub.chengji.equals(""))&&("不及格".equals(sub.chengji)||(isInteger(sub.chengji)==true&&Integer.valueOf(sub.chengji)<60))){
                hholder.chengji.setTextColor(MyApplication.getContext().getResources().getColor(R.color.red));
                hholder.kecheng.setTextColor(MyApplication.getContext().getResources().getColor(R.color.red));
                hholder.xuefen.setTextColor(MyApplication.getContext().getResources().getColor(R.color.red));
                hholder.kechengxz.setTextColor(MyApplication.getContext().getResources().getColor(R.color.red));
                hholder.leibie.setTextColor(MyApplication.getContext().getResources().getColor(R.color.red));
                hholder.biaozhi.setTextColor(MyApplication.getContext().getResources().getColor(R.color.red));
                hholder.cardView.setCardBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.darkgrey));
            }else {
                hholder.chengji.setTextColor(MyApplication.getContext().getResources().getColor(R.color.black));
                hholder.kecheng.setTextColor(MyApplication.getContext().getResources().getColor(R.color.black));
                hholder.xuefen.setTextColor(MyApplication.getContext().getResources().getColor(R.color.black));
                hholder.kechengxz.setTextColor(MyApplication.getContext().getResources().getColor(R.color.black));
                hholder.leibie.setTextColor(MyApplication.getContext().getResources().getColor(R.color.black));
                hholder.biaozhi.setTextColor(MyApplication.getContext().getResources().getColor(R.color.black));
                hholder.cardView.setCardBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.white));
            }
            }else if(holder instanceof  ViewHolderBiao){
                ViewHolderBiao hholder = (ViewHolderBiao)holder;
                hholder.textView.setText(allAllSubjects.get(position).mingcheng);
        }
    }

    @Override
    public int getItemViewType(int position) {
        String str = allAllSubjects.get(position).buchongxq;
        if((!str.equals(""))&&(isInteger(str)==true&&Integer.valueOf(str)==123456)){
            return 123456;
        }
        return 1;
    }

    void getSum(){
        sumNum = 0;
        allAllSubjects = new ArrayList<>();
        for(int i = myScoresListsList.size()-1; i >= 0; --i){
            String str = myScoresListsList.get(i);
            List<Subject> subjects = allScores.get(str).getSubjects();
            Subject biao = new Subject();
            biao.setMingcheng(str);
            biao.setBuchongxq("123456");
            allAllSubjects.add(biao);
            for(Subject sub:subjects){
                allAllSubjects.add(sub);
            }
        }
        sumNum=allAllSubjects.size();
    }
    @Override
    public int getItemCount() {
        return sumNum;
    }


}