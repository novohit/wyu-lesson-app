package com.wyu;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import com.wyu.config.ContextHolder;

public class ScoresListActivity extends AppCompatActivity {
    private RecyclerView scoresListRV;
    private ScoresListAdapter scoresListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores_list);
        Toolbar toolbar = (Toolbar)findViewById(R.id.custom_toolbar);
        toolbar.setTitle("成绩查看");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }
        initScoresList();
    }
    //toolbar相关
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    void initScoresList(){
        scoresListAdapter = new ScoresListAdapter(ContextHolder.myScoresListsList, ContextHolder.allScores);
        scoresListRV = (RecyclerView)findViewById(R.id.scores_list_recycler_view);
        scoresListRV.setAdapter(scoresListAdapter);
        scoresListRV.setLayoutManager(new LinearLayoutManager(ScoresListActivity.this));
    }

}
