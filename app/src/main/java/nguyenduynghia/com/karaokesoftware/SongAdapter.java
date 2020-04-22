package nguyenduynghia.com.karaokesoftware;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SongAdapter extends ArrayAdapter<Song> {
    Activity context;
    int resource;
    public SongAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context=context;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=this.context.getLayoutInflater().inflate(this.resource,null);

        TextView txtMa=view.findViewById(R.id.txtMa);
        TextView txtTen=view.findViewById(R.id.txtTen);
        TextView txtCasi=view.findViewById(R.id.txtCasi);
        final ImageView imgLike=view.findViewById(R.id.imgLike);
        final ImageView imgDisLike=view.findViewById(R.id.imgDisLike);

        final Song song=getItem(position);
        txtMa.setText(song.getMa());
        txtTen.setText(song.getTen());
        txtCasi.setText(song.getCasi());

        if(song.getLove()==1){
            imgDisLike.setVisibility(View.VISIBLE);
            imgLike.setVisibility(View.INVISIBLE);
        }else{
            imgDisLike.setVisibility(View.INVISIBLE);
            imgLike.setVisibility(View.VISIBLE);
        }

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgLike.setVisibility(View.INVISIBLE);
                imgDisLike.setVisibility(View.VISIBLE);
                xuLyLike(song);
            }
        });

        imgDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgDisLike.setVisibility(View.INVISIBLE);
                imgLike.setVisibility(View.VISIBLE);
                xuLyDisLike(song);
                if(MainActivity.selectedTab==1){
                    remove(song);
                }
            }
        });

        return view;
    }

    private void xuLyDisLike(Song song) {
        ContentValues values=new ContentValues();
        values.put("YEUTHICH",0);
        int kq=MainActivity.database.update(
                MainActivity.SongTable,
                values,
                "MABH=?",
                new String[]{song.getMa()});
        if(kq>0){
            Toast.makeText(context,"Bạn đã gỡ bài hát "+song.getTen()+" khỏi danh sách yêu thích thành công",Toast.LENGTH_SHORT).show();
        }
    }

    private void xuLyLike(Song song) {
        ContentValues values=new ContentValues();
        values.put("YEUTHICH",1);
        int kq=MainActivity.database.update(
                MainActivity.SongTable,
                values,
                "MABH=?",
                new String[]{song.getMa()});
        if(kq>0){
            Toast.makeText(context,"Bạn đã thêm bài hát "+song.getTen()+" vào danh sách yêu thích thành công",Toast.LENGTH_SHORT).show();
        }
    }
}
