
package com.foursquare.pilgrim.react;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.foursquare.pilgrim.CurrentLocation;
import com.foursquare.pilgrim.PilgrimNotificationTester;
import com.foursquare.pilgrim.PilgrimSdk;
import com.foursquare.pilgrim.Result;

public class RNPilgrimSdkModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNPilgrimSdkModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNPilgrimSdk";
  }

  @ReactMethod
  public void isSupportedDevice(Promise promise) {
    promise.resolve(true);
  }

  @ReactMethod
  public void canEnable(Promise promise) {
    promise.resolve(true);
  }

  @ReactMethod
  public void getInstallId(Promise promise) {
    promise.resolve(PilgrimSdk.getInstallId());
  }

  @ReactMethod
  public void start(Promise promise) {
    PilgrimSdk.start(reactContext);
  }

  @ReactMethod
  public void stop(Promise promise) {
    PilgrimSdk.stop(reactContext);
  }

  @ReactMethod
  public void getCurrentLocation(final Promise promise) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        Result<CurrentLocation, Exception> currentLocationResult = PilgrimSdk.get().getCurrentLocation();
        if (currentLocationResult.isOk()) {
          CurrentLocation currentLocation = currentLocationResult.getResult();
          promise.resolve(null);
        } else {
          promise.reject("E_GET_CURRENT_LOCATION", currentLocationResult.getErr());
        }
      }
    }).start();
  }

  @ReactMethod
  public void fireTestVisit(double latitude, double longitude) {
    new PilgrimNotificationTester().sendTestVisitArrivalAtLocation(reactContext, latitude, longitude, false);
  }
}