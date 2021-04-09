package org.me.gcu.earthquakecw.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.me.gcu.earthquakecw.Data.EarthQuake;
import org.me.gcu.earthquakecw.R;
import org.me.gcu.earthquakecw.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * THis is adapter which is used to create the recyclerviewer list
 * TODO: Replace the implementation with code for your data type.
 */
public class MyEQuakeListRecyclerViewAdapter extends RecyclerView.Adapter<MyEQuakeListRecyclerViewAdapter.ViewHolder> {

    private final List<EarthQuake> earthQuakes;
    private OnNoteListener mainOnNoteListener;

    public MyEQuakeListRecyclerViewAdapter(List<EarthQuake> earthQuakes, OnNoteListener onNoteListener) {
        this.earthQuakes = earthQuakes;
        this.mainOnNoteListener = onNoteListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);

        return new ViewHolder(view, mainOnNoteListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.earthQuake = earthQuakes.get(position);
        holder.location.setText(earthQuakes.get(position).getLocation());
        holder.strength.setText(String.format("Magnitude of %s", earthQuakes.get(position).getMagnitude()));
        holder.strengthImage.setColorFilter(colorCalc(earthQuakes.get(position).getMagnitude()));
    }

    @Override
    public int getItemCount() {
        return earthQuakes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        TextView location, strength;
        ImageView strengthImage;
        public EarthQuake earthQuake;
        OnNoteListener onNoteListener;


        public ViewHolder(View view, OnNoteListener onNoteListener) {
            super(view);
            mView = view;
            location = (TextView) view.findViewById(R.id.location);
            strength = (TextView) view.findViewById(R.id.strength);
            strengthImage = (ImageView) view.findViewById(R.id.strengthIcon);
            this.onNoteListener = onNoteListener;
            view.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + location.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }
    
    public interface OnNoteListener{
        void onNoteClick(int position);
    }

    /**
     * Method to get a color between green and red based on the strength of the earthquake
     * for testing, max magnitude is set to 3 as all the earthquakes are max 2.3magnitude, so color
     * coding would be bearly seen
     * @param strength - of the earthquake
     * @return - color code to color the earthquake identifier
     */
    public int colorCalc(float strength)
    {
        int color = (Integer) new ArgbEvaluator().evaluate(strength/3f, 0x00ff00, 0xff0000);
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        return Color.parseColor(hexColor);
    }
}