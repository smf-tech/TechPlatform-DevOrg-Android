package com.octopus.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.octopus.R;
import com.octopus.adapter.DownloadListAdapter;

import java.io.File;
import java.util.ArrayList;

public class ContentDownloadedActivity extends AppCompatActivity implements DownloadListAdapter.fileFilterListener{

    RecyclerView recyclerView;
    Context context;
    private SearchView searchView;
    private String path= Environment.getExternalStorageDirectory().getAbsolutePath() + "/MV";
    private ArrayList<String>filenames;
    private DownloadListAdapter downloadListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_downloaded);
        context=ContentDownloadedActivity.this;
        initViews();
        getFilesFromDirectory(path);
        setUpRecycleView();


    }

    private void getFilesFromDirectory(String path) {
        File directory=new File(path);
        if(directory.exists()){
            if(directory.isDirectory()){

                filenames=new ArrayList<>();
                File[] files=directory.listFiles();
                for(int i=0;i<files.length;i++){
                    filenames.add(files[i].getName());
                }
            }
        }

    }

    private void setUpRecycleView() {

        if(filenames!=null&&filenames.size()>0){
            downloadListAdapter=new DownloadListAdapter(context,filenames,path,this);
            recyclerView.setAdapter(downloadListAdapter);
        }

    }

    private void initViews() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Content Management");

        recyclerView=findViewById(R.id.list_download);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_download_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                downloadListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                downloadListAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.action_search){
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSelected(String name) {
        Toast.makeText(this, "Selected: " + filenames, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}
