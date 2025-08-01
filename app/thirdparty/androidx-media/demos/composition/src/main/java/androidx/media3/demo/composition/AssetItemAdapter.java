/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.demo.composition;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/** A {@link RecyclerView.Adapter} that displays assets in a sequence in a {@link RecyclerView}. */
public final class AssetItemAdapter extends RecyclerView.Adapter<AssetItemAdapter.ViewHolder> {
  private static final String TAG = "AssetItemAdapter";

  private final List<String> data;

  /**
   * Creates a new instance
   *
   * @param data A list of items to populate RecyclerView with.
   */
  public AssetItemAdapter(List<String> data) {
    this.data = data;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.preset_item, parent, false);
    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.getTextView().setText(data.get(position));
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  /** A {@link RecyclerView.ViewHolder} used to build {@link AssetItemAdapter}. */
  public static final class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView textView;

    private ViewHolder(View view) {
      super(view);
      textView = view.findViewById(R.id.preset_name_text);
    }

    private TextView getTextView() {
      return textView;
    }
  }
}
