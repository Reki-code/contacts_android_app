package me.rekii.contacts.ui;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import me.rekii.contacts.R;

/**
 * A class that adds item click support to a {@link RecyclerView}.
 *
 * @author Hugo Visser
 * @see <a href="http://www.littlerobots.nl/blog/Handle-Android-RecyclerView-Clicks/">
 * Getting your clicks on RecyclerView</a>
 */
public class ItemClickSupport {
    public static final String TAG = "me.rekii.contacts.ui.ItemClickSupport";
    private final RecyclerView recyclerView;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                final int position = recyclerView.getChildAdapterPosition(v);
                final long id = recyclerView.getChildItemId(v);
                itemClickListener.onItemClick(recyclerView, v, position, id);
            }
        }
    };

    private View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (itemLongClickListener != null) {
                final int position = recyclerView.getChildAdapterPosition(v);
                final long id = recyclerView.getChildItemId(v);
                return itemLongClickListener.onItemLongClick(recyclerView, v, position, id);
            }
            return false;
        }
    };

    private RecyclerView.OnChildAttachStateChangeListener attachListener
            = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(@NonNull View view) {
            if (itemClickListener != null) {
                view.setOnClickListener(clickListener);
            }

            if (itemLongClickListener != null) {
                view.setOnLongClickListener(longClickListener);
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(@NonNull View view) {
            if (itemClickListener != null) {
                view.setOnClickListener(null);
            }

            if (itemLongClickListener != null) {
                view.setOnLongClickListener(null);
            }
        }
    };

    private ItemClickSupport(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public static ItemClickSupport addTo(@NonNull RecyclerView view) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
        if (support == null) {
            support = new ItemClickSupport(view);
            support.attach(view);
        } else {
            Log.w(TAG, "RecyclerView already has ItemClickSupport.");
        }
        return support;
    }

    public static ItemClickSupport removeFrom(@NonNull RecyclerView view) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
        if (support != null) {
            support.detach(view);
        } else {
            Log.w(TAG, "RecyclerView does not have ItemClickSupport.");
        }
        return support;
    }

    private void attach(RecyclerView view) {
        view.setTag(R.id.item_click_support, this);
        view.addOnChildAttachStateChangeListener(attachListener);
    }

    private void detach(RecyclerView view) {
        view.removeOnChildAttachStateChangeListener(attachListener);
        view.setTag(R.id.item_click_support, null);
        itemClickListener = null;
        itemLongClickListener = null;
    }

    /**
     * @return The callback to be invoked when an item in the {@link RecyclerView}
     * has been clicked, or {@code null} if no callback has been set.
     */
    public OnItemClickListener getOnItemClickListener() {
        return itemClickListener;
    }

    /**
     * Register a callback to be invoked when an item in the
     * {@link RecyclerView} has been clicked.
     *
     * @param listener callback that will be invoked
     */
    public ItemClickSupport setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
        return this;
    }

    /**
     * @return The callback to be invoked when an item in the {@link RecyclerView}
     * has been clicked and held, or {@code null} if no callback has been set.
     */
    public OnItemLongClickListener getOnItemLongClickListener() {
        return itemLongClickListener;
    }

    /**
     * Register a callback to be invoked when an item in the
     * {@link RecyclerView} has been clicked and held.
     *
     * @param listener callback that will be invoked
     */
    public ItemClickSupport setOnItemLongClickListener(OnItemLongClickListener listener) {
        itemLongClickListener = listener;
        return this;
    }

    /**
     * Interface definition for a callback to be invoked when an item in the
     * {@link RecyclerView} has been clicked.
     */
    public interface OnItemClickListener {
        /**
         * Callback method to be invoked when an item in the {@code RecyclerView}
         * has been clicked.
         *
         * @param parent   {@code RecyclerView} where the click happened
         * @param view     view within the {@code RecyclerView} that was clicked
         * @param position position of the view in the adapter
         * @param id       row ID of the item that was clicked
         */
        void onItemClick(RecyclerView parent, View view, int position, long id);
    }

    /**
     * Interface definition for a callback to be invoked when an item in the
     * {@link RecyclerView} has been clicked and held.
     */
    public interface OnItemLongClickListener {
        /**
         * Callback method to be invoked when an item in the {@code RecyclerView}
         * has been clicked and held.
         *
         * @param parent   {@code RecyclerView} where the click happened
         * @param view     view within the {@code RecyclerView} that was clicked
         * @param position position of the view in the adapter
         * @param id       row ID of the item that was clicked
         * @return {@code true} if the callback consumed the long click; {@code false} otherwise.
         */
        boolean onItemLongClick(RecyclerView parent, View view, int position, long id);
    }
}