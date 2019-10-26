package cn.wildfire.chat.kit.location.ui.activity;

import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.basic.MbsConstans;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.Location;
import com.tencent.lbssearch.object.param.Geo2AddressParam;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.CameraPosition;
import com.tencent.mapsdk.raster.model.Circle;
import com.tencent.mapsdk.raster.model.CircleOptions;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

import butterknife.BindView;
import cn.wildfire.chat.kit.location.ui.base.BaseActivity;
import cn.wildfire.chat.kit.location.ui.presenter.MyLocationAtPresenter;
import cn.wildfire.chat.kit.location.ui.view.IMyLocationAtView;
import cn.wildfire.chat.kit.third.utils.UIUtils;

/**
 * @创建者 CSDN_LQR
 * @描述
 */
public class TestActivity extends BaseActivity<IMyLocationAtView, MyLocationAtPresenter> implements IMyLocationAtView, TencentLocationListener, SensorEventListener {

    int maxHeight = UIUtils.dip2Px(300);
    int minHeight = UIUtils.dip2Px(150);

    //位置传感器
    private SensorManager mSensorManager;
    private Sensor mOritationSensor;
    private TencentLocationManager mLocationManager;
    private TencentLocationRequest mLocationRequest;
    private TencentMap mTencentMap;
    private Marker myLocation;
    private Circle accuracy;
    private TencentSearch mTencentSearch;

    @BindView(R.id.confirmButton)
    Button mBtnToolbarSend;
    @BindView(R.id.rlMap)
    RelativeLayout mRlMap;
    @BindView(R.id.map)
    MapView mMap;
    @BindView(R.id.ibShowLocation)
    ImageButton mIbShowLocation;
    @BindView(R.id.rvPOI)
    RecyclerView mRvPOI;
    @BindView(R.id.pb)
    ProgressBar mPb;

    @Override
    public void initView() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mBtnToolbarSend.setVisibility(View.VISIBLE);
        setRlMapHeight(maxHeight);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mOritationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mLocationManager = TencentLocationManager.getInstance(this);
        mLocationRequest = TencentLocationRequest.create();
        mTencentMap = mMap.getMap();
        mTencentSearch = new TencentSearch(this);
    }

    @Override
    public void initData() {
        requestLocationUpdate();
    }

    @Override
    public void initListener() {
        mBtnToolbarSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.buildDrawingCache();
                Bitmap orignal = mMap.getDrawingCache();
                int width = Math.min(orignal.getWidth(), orignal.getHeight());
                width = width > 240 ? 240 : width;
                Bitmap thumbnail = Bitmap.createBitmap(mMap.getDrawingCache(), (orignal.getWidth() - width) / 2, (orignal.getHeight() - width) / 2, width, width);
                mMap.destroyDrawingCache();
                mPresenter.sendLocation(thumbnail);
            }
        });
        mRvPOI.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && Math.abs(dy) > 10 && ((GridLayoutManager) mRvPOI.getLayoutManager()).findFirstCompletelyVisibleItemPosition() <= 1 && mRlMap.getHeight() == maxHeight) {
                    setRlMapHeight(minHeight);
                    UIUtils.postTaskDelay(() -> mRvPOI.scrollToPosition(0), 0);
                } else if (dy < 0 && Math.abs(dy) > 10 && ((GridLayoutManager) mRvPOI.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 1 && mRlMap.getHeight() == minHeight) {
                    setRlMapHeight(maxHeight);
                    UIUtils.postTaskDelay(() -> mRvPOI.scrollToPosition(0), 0);
                }
            }
        });
        mIbShowLocation.setOnClickListener(v -> requestLocationUpdate());
//        mSensorManager.registerListener(MyLocationActivity.this, mOritationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mTencentMap.setOnMapCameraChangeListener(new TencentMap.OnMapCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (myLocation != null)
                    myLocation.setPosition(mTencentMap.getMapCenter());
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                if (accuracy != null) {
                    accuracy.setCenter(mTencentMap.getMapCenter());
                }
                search(mTencentMap.getMapCenter());
            }
        });
    }

    private void requestLocationUpdate() {
        //开启定位
        int error = mLocationManager.requestLocationUpdates(mLocationRequest, TestActivity.this);
        switch (error) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }
    }

    private void setRlMapHeight(int height) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlMap.getLayoutParams();
        params.height = height;
        mRlMap.setLayoutParams(params);
    }

    @Override
    protected MyLocationAtPresenter createPresenter() {
        return new MyLocationAtPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.location_activity_my_location;
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        if (isFinishing()) {
            return;
        }
        if (i == tencentLocation.ERROR_OK) {
            LatLng latLng = new LatLng(tencentLocation.getLatitude(), tencentLocation.getLongitude());
            if (myLocation == null) {
                myLocation = mTencentMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.arm)).anchor(0.5f, 0.8f));
            }
            if (accuracy == null) {
                accuracy = mTencentMap.addCircle(new CircleOptions().center(latLng).radius(tencentLocation.getAccuracy()).fillColor(0x440000ff).strokeWidth(0f));
            }
            myLocation.setPosition(latLng);
            accuracy.setCenter(latLng);
            accuracy.setRadius(tencentLocation.getAccuracy());
            mTencentMap.animateTo(latLng);
            mTencentMap.setZoom(16);
            search(latLng);
            //取消定位
            mLocationManager.removeUpdates(TestActivity.this);
        } else {

        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {
        if (isFinishing()) {
            return;
        }
        String desc = "";
        switch (i) {
            case STATUS_DENIED:
                desc = "权限被禁止";
                break;
            case STATUS_DISABLED:
                desc = "模块关闭";
                break;
            case STATUS_ENABLED:
                desc = "模块开启";
                break;
            case STATUS_GPS_AVAILABLE:
                desc = "GPS可用，代表GPS开关打开，且搜星定位成功";
                break;
            case STATUS_GPS_UNAVAILABLE:
                desc = "GPS不可用，可能 gps 权限被禁止或无法成功搜星";
                break;
            case STATUS_LOCATION_SWITCH_OFF:
                desc = "位置信息开关关闭，在android M系统中，此时禁止进行wifi扫描";
                break;
            case STATUS_UNKNOWN:
                break;
            default:
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        if (myLocation != null) {
//            myLocation.setRotation(event.values[0]);
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void search(LatLng latLng) {
        mPb.setVisibility(View.VISIBLE);
        mRvPOI.setVisibility(View.GONE);
        Location location = new Location().lat((float) latLng.getLatitude()).lng((float) latLng.getLongitude());
        //还可以传入其他坐标系的坐标，不过需要用coord_type()指明所用类型
        //这里设置返回周边poi列表，可以在一定程度上满足用户获取指定坐标周边poi的需求
        Geo2AddressParam geo2AddressParam = new Geo2AddressParam().
                location(location).get_poi(true);
        mTencentSearch.geo2address(geo2AddressParam, new HttpResponseListener() {

            @Override
            public void onSuccess(int arg0, BaseObject arg1) {
                if (isFinishing()) {
                    return;
                }
                mPb.setVisibility(View.GONE);
                mRvPOI.setVisibility(View.VISIBLE);
                if (arg1 == null) {
                    return;
                }
                mPresenter.loadData((Geo2AddressResultObject) arg1);
            }

            @Override
            public void onFailure(int arg0, String arg1, Throwable arg2) {
                if (isFinishing()) {
                    return;
                }
                mPb.setVisibility(View.GONE);
                mRvPOI.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public RecyclerView getRvPOI() {
        return mRvPOI;
    }
}
