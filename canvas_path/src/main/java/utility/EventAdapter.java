package utility;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jierui.canvas_path.R;

import java.text.SimpleDateFormat;
import java.util.List;

import model.EventRecord;

/**
 * Created by jierui on 2017/1/1.
 */

public class EventAdapter extends BaseAdapter {
    private SimpleDateFormat format = new SimpleDateFormat("MM dd HH mm");
    private List<EventRecord> list;
    private LayoutInflater layoutInflater = null;
    private Context context;
    public void changeItem(int i, EventRecord eventRecord){
        list.set(i, eventRecord);
        this.notifyDataSetChanged();
    }
    public EventAdapter(){}
    public EventAdapter(Context context, List<EventRecord> list){
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list == null? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.event_list_item_single, null);
            viewHolder.start_time = (TextView) convertView.findViewById(R.id.start_time);
            viewHolder.end_time = (TextView) convertView.findViewById(R.id.end_time);
            viewHolder.event_title = (TextView) convertView.findViewById(R.id.event_title);
            viewHolder.event_instant = (SandyClock) convertView.findViewById(R.id.event_instant);
            viewHolder.event_important = (ImageView) convertView.findViewById(R.id.event_important);
            viewHolder.event_progress = (ImageView) convertView.findViewById(R.id.event_progress);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        EventRecord eventRecord = list.get(i);
        String[] strart_str = format.format(eventRecord.getStartDateTime()).split(" ");
        String[] end_str = format.format(eventRecord.getEndDateTime()).split(" ");

        for (int j = 0; j < strart_str.length; j++){
            strart_str[j] = String.valueOf(Integer.parseInt(strart_str[j]));
            end_str[j] = String.valueOf(Integer.parseInt(end_str[j]));
        }
        viewHolder.start_time.setText(strart_str[0] + "/" + strart_str[1] + "  " + strart_str[2] + ":" + strart_str[3]);
        viewHolder.end_time.setText(end_str[0] + "/" + end_str[1] + "  " + end_str[2] + ":" + end_str[3]);

        viewHolder.event_title.setText(eventRecord.getTitle());
//        viewHolder.event_important.setImageDrawable(new ColorDrawable(Color.RED));
//        viewHolder.event_instant.setImageDrawable(new ColorDrawable(Color.RED));
//        viewHolder.event_progress.setImageDrawable(new ColorDrawable(Color.RED));

        Drawable drawable = context.getResources().getDrawable(R.drawable.star_indictor);
        viewHolder.event_important.setImageDrawable(drawable);
        viewHolder.event_important.setImageLevel(eventRecord.getLevel());

        convertView.measure(0,0);
        int eventProgressHeight = viewHolder.event_progress.getMeasuredHeight();
        int eventProgressWidth = viewHolder.event_progress.getMeasuredHeight();
//        int eventInstantHeight = viewHolder.event_instant.getMaxHeight();
//        int eventInstantWidth = viewHolder.event_instant.getMaxWidth();
        viewHolder.event_progress.setImageDrawable(new ProgressView(eventRecord.getProgress(), eventProgressWidth, eventProgressHeight));
        viewHolder.event_instant.setPercent(eventRecord.getInstant());
        return convertView;
    }

    private final class ViewHolder{
        public TextView start_time, end_time, event_title;
        public ImageView event_important, event_progress;
        public SandyClock event_instant;
    }

}
