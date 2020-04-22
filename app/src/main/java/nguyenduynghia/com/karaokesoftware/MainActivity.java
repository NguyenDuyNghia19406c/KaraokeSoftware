package nguyenduynghia.com.karaokesoftware;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import nguyenduynghia.com.karaokesoftware.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    String DATABASE_NAME="Arirang.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database=null;
    public static String SongTable="ArirangSongList";
    //ActivityMainBinding binding;
    SongAdapter songAdapter;
    SongAdapter likeSongAdapter;

    ListView lvSongs;
    ListView lvLikeSongs;
    TabHost tabHost;

    public static int selectedTab=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);
        processCopy();
        setupTabHost();
        addControls();
        loadAllSong();
        addEEvents();
    }

    private void addEEvents() {
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals("tab1")) {
                    loadAllSong();
                    selectedTab=0;
                }
                else{
                    loadLikeSong();
                    selectedTab=1;
                }
            }
        });
    }

    private void loadLikeSong() {
        Cursor cursor=database.query(SongTable,null,"YEUTHICH=?",new String[]{"1"},null,null,null);

        likeSongAdapter.clear();
        while (cursor.moveToNext()){
            String ma=cursor.getString(0);
            String ten=cursor.getString(1);
            String casi=cursor.getString(3);
            int love=cursor.getInt(5);
            Song song=new Song(ma,ten,casi,love);
            likeSongAdapter.add(song);
        }
        cursor.close();
    }


    private void setupTabHost()
    {
        tabHost= findViewById(R.id.tabhost);
        //tabHost=getTabHost();
        tabHost.setup();

        TabHost.TabSpec tab1=tabHost.newTabSpec("tab1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("Song List");
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2=tabHost.newTabSpec("tab2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("Love Song List");
        tabHost.addTab(tab2);

    }

    private void loadAllSong() {
        database=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor=database.query(SongTable,null,null,null,null,null,null);

        songAdapter.clear();
        while (cursor.moveToNext()){
            String ma=cursor.getString(0);
            String ten=cursor.getString(1);
            String casi=cursor.getString(3);
            int love=cursor.getInt(5);
            Song song=new Song(ma,ten,casi,love);
            songAdapter.add(song);
        }
        cursor.close();
    }

    private void addControls() {
        lvSongs=findViewById(R.id.lvSongs);
        songAdapter=new SongAdapter(MainActivity.this,R.layout.item);
        lvSongs.setAdapter(songAdapter);

        lvLikeSongs=findViewById(R.id.lvLikeSongs);
        likeSongAdapter=new SongAdapter(MainActivity.this,R.layout.item);
        lvLikeSongs.setAdapter(likeSongAdapter);
    }


    private void processCopy() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists())
        {
            try
            {
                CopyDataBaseFromAsset();
                Toast.makeText(this,
                        "Copying sucess from Assets folder",
                        Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }

    public void CopyDataBaseFromAsset()
    {
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
            String outFileName = getDatabasePath();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem mnuSearch=menu.findItem(R.id.mnuSearch);
        SearchView searchView= (SearchView) mnuSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                xuLyTimKiem(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);


    }

    private void xuLyTimKiem(String s) {
        Cursor cursor=database.query(SongTable,
                null,
                "MABH like ? or TENBH like ? or TACGIA like ?",
                new String[]{"%"+s+"%","%"+s+"%","%"+s+"%"},
                null,null,null);

        songAdapter.clear();
        while (cursor.moveToNext()){
            String ma=cursor.getString(0);
            String ten=cursor.getString(1);
            String casi=cursor.getString(3);
            int love=cursor.getInt(5);
            Song song=new Song(ma,ten,casi,love);
            songAdapter.add(song);
        }
        cursor.close();
    }
}
