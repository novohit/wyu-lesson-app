package com.wyu.activity;

import android.content.DialogInterface;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wyu.R;
import com.wyu.adapter.ScoreListAdapter;
import com.wyu.util.CommonUtil;
import com.wyu.constant.Constant;
import com.wyu.util.ToastUtil;

public class ScoreListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ScoreListAdapter scoreListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        toolbar.setTitle("成绩查看");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }
        initScoresList();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_custom_bar, menu);
        MenuItem item = menu.findItem(R.id.ic_cur_week);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.ic_mark:
                String selectedTerm = scoreListAdapter.getSelectedTerm();
                int lastIndex = 0;
                for (int i = 0; i < Constant.SELECTED_TERM_LIST.length; i++) {
                    if (Constant.SELECTED_TERM_LIST[i].equals(selectedTerm)) {
                        lastIndex = i;
                    }
                }

                new AlertDialog.Builder(ScoreListActivity.this).setTitle("选择要查看的学期").setSingleChoiceItems(Constant.SELECTED_TERM_LIST, lastIndex, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        scoreListAdapter.setSelectedTerm(Constant.SELECTED_TERM_LIST[which]);
                        scoreListAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", null).show();
                break;
            case R.id.ic_settings:
                ToastUtil.show("TODO");
                break;
            default:
        }
        return true;
    }

    //toolbar相关
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    void initScoresList() {
        scoreListAdapter = new ScoreListAdapter(CommonUtil.getCurrentTerm());
        recyclerView = (RecyclerView) findViewById(R.id.scores_list_recycler_view);
        recyclerView.setAdapter(scoreListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ScoreListActivity.this));
    }

}
