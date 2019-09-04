package com.lr.biyou.ui.temporary.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 完善资料  工作信息
 */
public class WorkInfoFragment extends Fragment {
    @BindView(R.id.work_company_name_tv)
    TextView mWorkCompanyNameTv;
    @BindView(R.id.work_kind_value_tv)
    TextView mWorkKindValueTv;
    @BindView(R.id.work_phone_value_tv)
    TextView mWorkPhoneValueTv;
    @BindView(R.id.work_address_value_tv)
    TextView mWorkAddressValueTv;
    @BindView(R.id.work_address_detail_tv)
    TextView mWorkAddressDetailValueTv;
    @BindView(R.id.work_name_value_tv)
    TextView mWorkNameValueTv;
    @BindView(R.id.work_xingzhi_value_tv)
    TextView mWorkXingzhiValueTv;
    @BindView(R.id.work_time_value_tv)
    TextView mWorkTimeValueTv;
    @BindView(R.id.work_start_value_tv)
    TextView mWorkStartValueTv;
    @BindView(R.id.yue_money_value_tv)
    TextView mWorkMoneyValueTv;
    Unbinder unbinder;
    private View mRootView;

    private Map<String, Object> mDataMap;

    public WorkInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mRootView = inflater.inflate(R.layout.fragment_work_info, (ViewGroup) getActivity().findViewById(R.id.info_manager_page), false);
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
        return mRootView;
    }

    public void updateValue(Map<String,Object> map){
        mDataMap = map;
        initView();
    }

    public void initView() {
        if (UtilTools.empty(mDataMap.get("cmpname")+"")){
            mWorkCompanyNameTv.setText("");
        }else {
            mWorkCompanyNameTv.setText(mDataMap.get("cmpname")+"");
        }

        Map<String,Object> mHangyeMap = null;
        String workKind = mDataMap.get("cmptrades") + "";

        if (!UtilTools.empty(workKind)){
            String ss = SelectDataUtil.getJson(getActivity(),"hangye.json");
            List<Map<String,Object>> mHangyeList =   JSONUtil.getInstance().jsonToList(ss);
            for (Map mm : mHangyeList){
                String code = mm.get("code")+"";
                if (code.equals(workKind)){
                    mHangyeMap = mm;
                    break;
                }
            }
        }
        if (mHangyeMap != null ){
            mWorkKindValueTv.setText(mHangyeMap.get("name") + "");
        }else {
            mWorkKindValueTv.setText(getResources().getString(R.string.tv_defalut_value));
        }


      /*  if (UtilTools.empty(workKind)) {
            mWorkKindValueTv.setText(getResources().getString(R.string.tv_defalut_value));
        } else {
            mWorkKindValueTv.setText(workKind);
        }*/
        String cmptel = mDataMap.get("cmptel") + "";
        if (UtilTools.empty(cmptel)) {
            mWorkPhoneValueTv.setText("");
        } else {
            mWorkPhoneValueTv.setText(cmptel);
        }

        String workAddress = mDataMap.get("cmpprname") + ""+mDataMap.get("cmpciname");

        String cmpaddr = mDataMap.get("cmpaddr") + "";
        if (UtilTools.empty(cmpaddr)) {
            mWorkAddressValueTv.setText(getResources().getString(R.string.tv_defalut_value));
        } else {
            mWorkAddressValueTv.setText(workAddress);
            mWorkAddressDetailValueTv.setText(cmpaddr);

        }
        String position = mDataMap.get("posiname") + "";//职业
        if (UtilTools.empty(position)) {
            mWorkNameValueTv.setText(getResources().getString(R.string.tv_defalut_value));
        } else {
            mWorkNameValueTv.setText(position);
        }
        String jobnature = mDataMap.get("jobnature") + "";//工作性质
        if (UtilTools.empty(jobnature)) {
            mWorkXingzhiValueTv.setText(getResources().getString(R.string.tv_defalut_value));
        } else {
            //Map<String,Object> map = SelectDataUtil.getMap(jobnature,SelectDataUtil.getJobType());
            Map<String,Object> map = SelectDataUtil.getMap(jobnature,SelectDataUtil.getNameCodeByType("job"));
            mWorkXingzhiValueTv.setText(map.get("name")+"");
        }

        String income = mDataMap.get("income") + "";//月收入
        if (UtilTools.empty(income)) {
            mWorkMoneyValueTv.setText("");
        } else {
            mWorkMoneyValueTv.setText(income);
        }


        String jobstartdate = mDataMap.get("jobstartdate") + "";
        if (UtilTools.empty(jobstartdate)) {
            mWorkTimeValueTv.setText(getResources().getString(R.string.tv_defalut_value));
        } else {
            String ss = UtilTools.getStringFromSting2(jobstartdate,"yyyyMMdd","yyyy年MM月dd日");
            mWorkTimeValueTv.setText(ss);
        }
        String tradesstartdate = mDataMap.get("tradesstartdate") + "";
        if (UtilTools.empty(tradesstartdate)) {
            mWorkStartValueTv.setText(getResources().getString(R.string.tv_defalut_value));
        } else {
            String ss = UtilTools.getStringFromSting2(tradesstartdate,"yyyyMMdd","yyyy年MM月dd日");
            mWorkStartValueTv.setText(ss);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
