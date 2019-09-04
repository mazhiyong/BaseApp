package com.lr.biyou.ui.temporary.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 完善资料   基本信息
 */
public class BaseInfoFragment extends Fragment {

    @BindView(R.id.idcard_value_tv)
    TextView mIdcardValueTv;
    @BindView(R.id.country_value_tv)
    TextView mCountryValueTv;
    @BindView(R.id.huji_value_tv)
    TextView mHujiValueTv;
    @BindView(R.id.islove_value_tv)
    TextView mIsloveValueTv;
    @BindView(R.id.study_value_tv)
    TextView mStudyValueTv;
    @BindView(R.id.house_address_value_tv)
    TextView mHouseAddressValueTv;
    @BindView(R.id.house_detail_value_tv)
    TextView mHouseDetailValueTv;
    @BindView(R.id.house_info_value_tv)
    TextView mHouseInfoValueTv;
    @BindView(R.id.phone_value_tv)
    TextView mPhoneValueTv;
    @BindView(R.id.phone_address_value_tv)
    TextView mPhoneAddressValueTv;
    @BindView(R.id.phone_address_detail_value_tv)
    TextView mPhoneAddressDetailValueTv;
    Unbinder unbinder;
    @BindView(R.id.name_value_tv)
    TextView mNameValueTv;
    @BindView(R.id.minzu_value_tv)
    TextView mMinzuValueTv;
    @BindView(R.id.zujin_line)
    View mZujinLine;
    @BindView(R.id.zujin_value_edit)
    TextView mZujinValueEdit;
    @BindView(R.id.zujin_lay)
    CardView mZujinLay;
    private View mRootView;

    private Map<String, Object> mDataMap = new HashMap<>();

    private int mPage = 1;

    public BaseInfoFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mRootView = inflater.inflate(R.layout.fragment_base_info, (ViewGroup) getActivity().findViewById(R.id.info_manager_page), false);
        unbinder = ButterKnife.bind(this, mRootView);
        Bundle bundle = getArguments();
        mDataMap = (Map<String, Object>) bundle.getSerializable("DATA");
        initView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup p = (ViewGroup) mRootView.getParent();
        if (p != null) {
            p.removeAllViewsInLayout();
        }
        unbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    public void updateValue(Map<String, Object> map) {
        mDataMap = map;
        initView();
    }


    public void initView() {
        String name = mDataMap.get("finame") + "";
        if (UtilTools.empty(name)) {
            mNameValueTv.setText(getResources().getString(R.string.tv_defalut_value));
        } else {
            mNameValueTv.setText(name);
        }
        String idCard = mDataMap.get("farnzjno") + "";
        if (UtilTools.empty(idCard)) {
            mIdcardValueTv.setText(getResources().getString(R.string.tv_defalut_value));
        } else {
            mIdcardValueTv.setText(UtilTools.getIDCardXing(idCard));
        }
        String nationality = mDataMap.get("nationality") + "";
        if (UtilTools.empty(nationality)) {
            mCountryValueTv.setText(getResources().getString(R.string.tv_defalut_value));
        } else {
            //Map<String, Object> codeMap = SelectDataUtil.getMap(nationality, SelectDataUtil.getCountry());
            Map<String, Object> codeMap = SelectDataUtil.getMap(nationality, SelectDataUtil.getNameCodeByType("nation"));

            mCountryValueTv.setText(codeMap.get("name") + "");
        }
        String huji = mDataMap.get("register") + "";
        if (UtilTools.empty(huji)) {
            mHujiValueTv.setText(getResources().getString(R.string.tv_defalut_value));
        } else {
            mHujiValueTv.setText(huji);
        }

        String minzu = mDataMap.get("nationname") + "";
        if (UtilTools.empty(minzu)) {
            mMinzuValueTv.setText(getResources().getString(R.string.tv_defalut_value));
        } else {
            mMinzuValueTv.setText(minzu);
        }

        String marry = mDataMap.get("marry") + "";
        if (UtilTools.empty(marry)) {
            mIsloveValueTv.setText(getResources().getString(R.string.tv_defalut_value));
        } else {
            //Map<String, Object> codeMap = SelectDataUtil.getMap(marry, SelectDataUtil.getMarry());
            Map<String, Object> codeMap = SelectDataUtil.getMap(marry, SelectDataUtil.getNameCodeByType("marital"));
            mIsloveValueTv.setText(codeMap.get("name") + "");
        }
        String education = mDataMap.get("education") + "";
        if (UtilTools.empty(education)) {
            mStudyValueTv.setText(getResources().getString(R.string.tv_defalut_value));
        } else {
            //Map<String, Object> codeMap = SelectDataUtil.getMap(education, SelectDataUtil.getEducation());
            Map<String, Object> codeMap = SelectDataUtil.getMap(education, SelectDataUtil.getNameCodeByType("education"));

            mStudyValueTv.setText(codeMap.get("name") + "");
        }
        String houprname = mDataMap.get("houprname") + "" + mDataMap.get("houciname");
        if (UtilTools.empty(houprname)) {
            mHouseAddressValueTv.setText(getResources().getString(R.string.tv_defalut_value));
        } else {
            mHouseAddressValueTv.setText(houprname);
        }
        String houdetail = mDataMap.get("houaddr") + "";
        if (UtilTools.empty(houdetail)) {
            mHouseDetailValueTv.setText(getResources().getString(R.string.tv_defalut_value));
            mHouseDetailValueTv.setVisibility(View.GONE);
        } else {
            mHouseDetailValueTv.setVisibility(View.VISIBLE);
            mHouseDetailValueTv.setText(houdetail);
        }
        String zhufangZk = mDataMap.get("houproperty") + "";
        if (UtilTools.empty(zhufangZk)) {
            mHouseInfoValueTv.setText(getResources().getString(R.string.tv_defalut_value));
        } else {
            //Map<String, Object> codeMap = SelectDataUtil.getMap(zhufangZk, SelectDataUtil.getHouse());
            Map<String, Object> codeMap = SelectDataUtil.getMap(zhufangZk, SelectDataUtil.getNameCodeByType("house"));
            mHouseInfoValueTv.setText(codeMap.get("name") + "");
            if (zhufangZk.equals("6")) {
                mZujinLay.setVisibility(View.GONE);
                mZujinLine.setVisibility(View.GONE);
                mHouseInfoValueTv.setText(codeMap.get("name") + "\n"+mDataMap.get("houmemo"));
            } else if (zhufangZk.equals("5")) {
                mZujinLay.setVisibility(View.VISIBLE);
                mZujinLine.setVisibility(View.VISIBLE);
                mZujinValueEdit.setText(mDataMap.get("houmonthrent")+"");
            } else {
                mZujinLay.setVisibility(View.GONE);
                mZujinLine.setVisibility(View.GONE);
            }
        }
        String phone = mDataMap.get("phone") + "";
        if (UtilTools.empty(phone)) {
            mPhoneValueTv.setText(getResources().getString(R.string.tv_defalut_value));
        } else {
            mPhoneValueTv.setText(phone);
        }
        String cmnaddr = mDataMap.get("cmnprname") + "" + mDataMap.get("cmnciname");
        if (UtilTools.empty(cmnaddr)) {
            mPhoneAddressValueTv.setText(getResources().getString(R.string.tv_defalut_value));
        } else {
            mPhoneAddressValueTv.setText(cmnaddr);
        }
        String cmnaddrDetail = mDataMap.get("cmnaddr") + "";
        if (UtilTools.empty(cmnaddrDetail)) {
            mPhoneAddressDetailValueTv.setText(getResources().getString(R.string.tv_defalut_value));
            mPhoneAddressDetailValueTv.setVisibility(View.GONE);
        } else {
            mPhoneAddressDetailValueTv.setVisibility(View.VISIBLE);
            mPhoneAddressDetailValueTv.setText(cmnaddrDetail);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
