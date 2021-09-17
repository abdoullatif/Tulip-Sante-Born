package com.example.tulipsante.pulse.oximeter.device.bluetooth;



import com.example.tulipsante.pulse.oximeter.device.bluetooth.bean.BleDevice;
import com.example.tulipsante.pulse.oximeter.device.bluetooth.bean.EBleState;
import com.example.tulipsante.pulse.oximeter.device.bluetooth.bean.ESwitchState;

import java.util.List;

public interface IBleClient {
    /**
     * 设置BLE的扫描、连接参数
     *
     * @param config ble配置
     * @return 实例
     */
    IBleClient setBleConfig(BleConfig config);

    /**
     * 监听蓝牙的开关事件
     *
     * @param switchListener 开关回调
     * @return 实例
     */
    IBleClient setBleSwitchListener(OnSwitchListener switchListener);

    /**
     * 监听蓝牙扫描事件
     *
     * @param scanListener
     * @return
     */
    IBleClient setBleScanListener(OnScanListener scanListener);

    /**
     * 监听BLE的操作事件
     *
     * @param eventListener 事件回调
     * @return 实例
     */
    IBleClient setBleEventListener(OnEventListener eventListener);

    /**
     * BLE接收回调
     *
     * @param recvCallback 接收回调
     * @return 实例
     */
    IBleClient setBleRecvCallback(OnRecvCallback recvCallback);

    /**
     * 判断手机是否支持BLE
     *
     * @return true/false
     */
    boolean isSupportBle();

    /**
     * 判断手机是否打开蓝牙
     *
     * @return true/false
     */
    boolean isOpenBle();

    /**
     * 引导用户打开蓝牙
     */
    void openBle();

    /**
     * 扫描设备（扫描时长，广播名通过setBleConfig设置）
     *
     * @return true/false
     */
    boolean scanDevice();

    /**
     * 主动停止扫描
     */
    void stopScanDevice();

    /**
     * 主动去获取当前的连接状态
     *
     * @return 连接状态
     */
    EBleState getConnectState();

    /**
     * 连接设备
     *
     * @param mac mac地址
     * @return true/false
     */
    boolean connectDevice(String mac);

    /**
     * 断开连接
     */
    void disconnectDevice();

    /**
     * 释放资源。在断开连接不使用蓝牙的时候请释放资源
     */
    void close();

    /**
     * 发送数据
     *
     * @param data 字节数组，最大不超过20个字节
     * @return true/false
     */
    boolean sendData(byte[] data);

    interface OnSwitchListener {
        void onSwitch(ESwitchState event);
    }

    interface OnScanListener {
        void onScan(List<BleDevice> devices);

        void onEnd();
    }

    interface OnEventListener {
        void onEvent(EBleState event);
    }

    interface OnRecvCallback {
        void onRecv(byte[] data, int len);
    }
}


