package cn.wildfire.chat.kit.channel.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lr.biyou.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CategoryViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.categoryTextView)
    TextView categoryTextView;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(String category) {
        categoryTextView.setText(category);
    }
}
