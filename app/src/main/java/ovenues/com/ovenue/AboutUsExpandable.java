package ovenues.com.ovenue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ovenues.com.ovenue.expandableRecyclerview.aboutus.ExpandableRecyclerAdapter;
import ovenues.com.ovenue.expandableRecyclerview.aboutus.SubTitle;
import ovenues.com.ovenue.expandableRecyclerview.aboutus.Title;
import ovenues.com.ovenue.utils.Const;

public class AboutUsExpandable extends AppCompatActivity {
    
    String imageUrl[] = Const.image;
    String names[] = Const.name;
    String subNames[] = Const.subName;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_web_view);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark,getTheme()));
        ButterKnife.bind(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About Us");

        List<Title> list = getList();
        ExpandableRecyclerAdapter adapter = new ExpandableRecyclerAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

    }

    private List<Title> getList() {
        List<Title> list = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            List<SubTitle> subTitles = new ArrayList<>();
            for (int j = 0; j< subNames.length; j++){
                SubTitle subTitle = new SubTitle(subNames[j]);
                subTitles.add(subTitle);
            }
            Title model = new Title(names[i],subTitles, imageUrl[i]);
            list.add(model);
        }
        return list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}