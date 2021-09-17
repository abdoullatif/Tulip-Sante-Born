package com.example.tulipsante.pulse.oximeter.view;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.tulipsante.pulse.oximeter.BaseAct;
import com.example.tulipsante.pulse.oximeter.device.bluetooth.BleClient;
import com.example.tulipsante.pulse.oximeter.device.bluetooth.IBleClient;
import com.example.tulipsante.pulse.oximeter.device.bluetooth.LocationPermission;
import com.example.tulipsante.pulse.oximeter.device.bluetooth.bean.BleDevice;
import com.example.tulipsante.pulse.oximeter.device.bluetooth.bean.EBleState;
import com.example.tulipsante.R;
import com.example.tulipsante.pulse.oximeter.util.view.progress.ProgressUtil;

import java.util.ArrayList;
import java.util.List;

public class ScanActivity extends BaseAct implements AdapterView.OnItemClickListener {
    private ListView mScanList;

    private LeDeviceListAdapter mLeDeviceListAdapter;
    private boolean mScanning = false;
    private IBleClient mBleClient;
    private EBleState mConnectState;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        mScanList = findViewById(R.id.list_scan);
        mScanList.setOnItemClickListener(this);
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        mScanList.setAdapter(mLeDeviceListAdapter);

        mBleClient = BleClient.getSingleton(this)
                .setBleSwitchListener(event -> {

                })
                .setBleScanListener(new IBleClient.OnScanListener() {
                    @Override
                    public void onScan(List<BleDevice> devices) {
                        mLeDeviceListAdapter.setDevices(devices);
                        mLeDeviceListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onEnd() {
                        mScanning = false;
                        invalidateOptionsMenu();
                    }
                })
                .setBleEventListener(event -> {
                    mConnectState = mBleClient.getConnectState();
                    if (event == EBleState.connecting) {
                        invalidateOptionsMenu();
                    } else if (event == EBleState.connected) {
                        ProgressUtil.dismissProgressHUD();
                        invalidateOptionsMenu();
                        mHandler.postDelayed(() -> finish(), 1000);
                    } else if (event == EBleState.disconnected) {
                        ProgressUtil.dismissProgressHUD();
                        invalidateOptionsMenu();
                    }
                });
        mConnectState = mBleClient.getConnectState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mScanning) {
            mScanning = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scan, menu);
        if (mScanning) {
            mScanList.setVisibility(View.VISIBLE);
            mLeDeviceListAdapter.clear();
            mLeDeviceListAdapter.notifyDataSetChanged();

            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_connecting).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(R.layout.actionbar_progress);
        } else {
            if (mConnectState == EBleState.connecting) {
                mScanList.setVisibility(View.VISIBLE);
                menu.findItem(R.id.menu_stop).setVisible(false);
                menu.findItem(R.id.menu_scan).setVisible(false);
                menu.findItem(R.id.menu_connecting).setVisible(true);
                menu.findItem(R.id.menu_disconnect).setVisible(false);
                menu.findItem(R.id.menu_refresh).setActionView(R.layout.actionbar_progress);
            } else if (mConnectState == EBleState.connected) {
                mScanList.setVisibility(View.GONE);
                menu.findItem(R.id.menu_stop).setVisible(false);
                menu.findItem(R.id.menu_scan).setVisible(false);
                menu.findItem(R.id.menu_connecting).setVisible(false);
                menu.findItem(R.id.menu_disconnect).setVisible(true);
                menu.findItem(R.id.menu_refresh).setActionView(null);
            } else {
                mScanList.setVisibility(View.VISIBLE);
                menu.findItem(R.id.menu_stop).setVisible(false);
                menu.findItem(R.id.menu_scan).setVisible(true);
                menu.findItem(R.id.menu_connecting).setVisible(false);
                menu.findItem(R.id.menu_disconnect).setVisible(false);
                menu.findItem(R.id.menu_refresh).setActionView(null);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                startScan();
                break;
            case R.id.menu_stop:
                stopScan();
                break;
            case R.id.menu_connecting:
            case R.id.menu_disconnect:
                disconnect();
                break;
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final BleDevice device = mLeDeviceListAdapter.getDevice(position);
        if (device == null) return;

        mBleClient.connectDevice(device.getMac());
        ProgressUtil.showProgressHUD(this, "", false, null);
        if (mScanning) {
            mBleClient.stopScanDevice();
            mScanning = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (LocationPermission.isRequestLocationPermissionGranted(requestCode, permissions, grantResults)) {
            startScan();
        }
    }

    private void startScan() {
        if (!mBleClient.isSupportBle()) {
            Toast.makeText(this, getString(R.string.toast_not_support_ble), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!LocationPermission.isOpenLocation(this)) {
            Toast.makeText(this, getString(R.string.toast_open_location), Toast.LENGTH_SHORT).show();
            LocationPermission.openLocation(this);
            return;
        }

        if (!mBleClient.isOpenBle()) {
            Toast.makeText(this, getString(R.string.toast_open_ble), Toast.LENGTH_SHORT).show();
            mBleClient.openBle();
            return;
        }

        if (LocationPermission.checkLocationPermission(this)) {
            mScanning = mBleClient.scanDevice();
            if (mScanning) {
                invalidateOptionsMenu();
            }
        } else {
            LocationPermission.requestLocationPermission(this);
        }
    }

    private void stopScan() {
        mBleClient.stopScanDevice();
    }

    private void disconnect() {
        mBleClient.disconnectDevice();
    }

    private class LeDeviceListAdapter extends BaseAdapter {
        private List<BleDevice> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<>();
            mInflator = getLayoutInflater();
        }

        public void setDevices(List<BleDevice> devices) {
            mLeDevices.clear();
            mLeDevices.addAll(devices);
        }

        public BleDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = mInflator.inflate(R.layout.list_item_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = view.findViewById(R.id.device_address);
                viewHolder.deviceName = view.findViewById(R.id.device_name);
                viewHolder.deviceRssi = view.findViewById(R.id.device_rssi);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BleDevice device = mLeDevices.get(i);
            String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText("未知设备");
            viewHolder.deviceAddress.setText(device.getMac());
            viewHolder.deviceRssi.setText("" + device.getRssi());

            return view;
        }
    }

    private class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
        TextView deviceRssi;
    }
}
