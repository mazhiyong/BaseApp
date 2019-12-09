package com.lr.biyou.ui.moudle1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lr.biyou.R;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.List;
import java.util.Map;

public class MoreTypeAdapter extends RecyclerView.Adapter {
    private static final int TITLE_TYPE = 1;
    private static final int CONTENT_TYPE = 2;
    private List<Map<String,Object>> mapList;

    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public MoreTypeAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setRankList(List<Map<String,Object>> list) {
        this.mapList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {
        View view;
        if (itemType == TITLE_TYPE) {
            view = inflater.inflate(R.layout.item_title, viewGroup, false);
            return new TitleViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_rank, viewGroup, false);
            return new RiseRankViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        viewHolder.itemView.setTag(position);
        int itemViewType = getItemViewType(position);
        Map<String,Object> content = mapList.get(position);
        if (itemViewType == TITLE_TYPE) {
            String title = content.get("title")+"";
            TitleViewHolder titleViewHolder = (TitleViewHolder) viewHolder;
            titleViewHolder.tvTitle.setText(title);
        } else {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClickListener(v, position);
                    }
                }
            });



            RiseRankViewHolder riseRankViewHolder = (RiseRankViewHolder) viewHolder;
            Map<String,Object> listUpBean = (Map<String, Object>) content;
//            riseRankViewHolder.tvCoinName.setText(Html.fromHtml(UiTools.getString(R.string.defaultName)
//                    .replace("BTC", listUpBean.getName()).replace("USDT", listUpBean.getArea())));
            riseRankViewHolder.tvCoinName.setText(listUpBean.get("name")+"");
            riseRankViewHolder.tvArea.setText("/" + listUpBean.get("area")+"");

            riseRankViewHolder.tvCurrentPrice.setText( UtilTools.formatNumber(listUpBean.get("price")+"","#.########"));
//            riseRankViewHolder.tvCurrentPriceCny.setText(UiTools.getString(R.string.defaultCny4)
//                    .replace("%s", UiTools.formatNumber(listUpBean.getCnyNumber(), "#.##")));
//            riseRankViewHolder.tvCurrentPriceCny.setText(listUpBean.get("cnyNumber")+"CNY");

                //0 红跌绿涨   1红涨绿跌
            String colorType =  SPUtils.get(context, MbsConstans.SharedInfoConstans.COLOR_TYPE,"0").toString();

            if ((listUpBean.get("increase")+"").contains("-")) { //跌
                if (colorType.equals("0")){
                    riseRankViewHolder.tvRiseFallRatio.setBackgroundResource(R.drawable.shape_rise_bg);
                }else {
                    riseRankViewHolder.tvRiseFallRatio.setBackgroundResource(R.drawable.shape_fall_bg);
                }
                riseRankViewHolder.tvRiseFallRatio.setText(listUpBean.get("increase")+ "");
            } else {
                if (colorType.equals("0")){
                    riseRankViewHolder.tvRiseFallRatio.setBackgroundResource(R.drawable.shape_fall_bg);
                }else {
                    riseRankViewHolder.tvRiseFallRatio.setBackgroundResource(R.drawable.shape_rise_bg);
                }
                riseRankViewHolder.tvRiseFallRatio.setText(listUpBean.get("increase")+"");
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
//
//        if (mapList.get(position) instanceof String) {
//            return TITLE_TYPE;
//        } else {
//            return CONTENT_TYPE;
//        }

        return CONTENT_TYPE;
    }

    @Override
    public int getItemCount() {
        if (mapList != null && mapList.size() > 0) {
            /*if (mapList.size() > 10) {
                return 10;
            }*/
            return mapList.size();
        }
        return 0;
    }

    /*设置点击事件*/
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(View view, int position);
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }

    static class RiseRankViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCoinName, tvArea, tvCurrentPrice, tvRiseFallRatio, tvCurrentPriceCny;

        public RiseRankViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCoinName = itemView.findViewById(R.id.tvCoinName);
            tvArea = itemView.findViewById(R.id.tvArea);
            tvCurrentPrice = itemView.findViewById(R.id.tvCurrentPrice);
            tvRiseFallRatio = itemView.findViewById(R.id.tvRiseFallRatio);
            tvCurrentPriceCny = itemView.findViewById(R.id.tvCurrentPriceCny);

        }
    }
}
