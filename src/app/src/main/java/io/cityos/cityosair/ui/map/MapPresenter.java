package io.cityos.cityosair.ui.map;

import android.content.res.Resources;

import java.util.List;

import javax.inject.Inject;

import io.cityos.cityosair.data.datasource.datafetch.MapDataFetch;
import io.cityos.cityosair.data.model.Device;
import io.cityos.cityosair.data.model.map.MapData;
import io.cityos.cityosair.data.model.map.MapPlace;
import io.cityos.cityosair.ui.base.presenter.PagerPresenter;
import io.cityos.cityosair.util.cache.CacheUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MapPresenter extends PagerPresenter<MapView> {

  private final MapDataSingleton mapDataSingleton;
  private MapDataFetch mapDataFetch;

  public enum MAP_FILTER {
    OUTDOOR,
    INDOOR,
    MINE
  }

  @Inject
  MapPresenter(MapDataFetch mapDataFetch, MapDataSingleton mapDataSingleton) {
    this.mapDataFetch = mapDataFetch;
    this.mapDataSingleton = mapDataSingleton;
  }

  void getMapPlaces(Resources resources) {

    getCompositeDisposable().add(Observable.just(MapPlace.getPlaces(resources, 1))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableObserver<List<MapPlace>>() {
          @Override public void onNext(List<MapPlace> places) {
            getView().placesLoaded(places);
          }

          @Override public void onError(Throwable e) { }
          @Override public void onComplete() { }
        }));
  }

  void getCachedMapDevices(MAP_FILTER mapFilter) {

    if (mapDataSingleton.deviceList != null) {

      getCompositeDisposable().add(
          Observable.fromIterable(mapDataSingleton.deviceList)
              .filter(newMapDevice -> {
                switch (mapFilter) {
                  case MINE:
                    return newMapDevice.isMine();
                  case INDOOR:
                    return newMapDevice.isIndoor();
                  case OUTDOOR:
                    return !newMapDevice.isIndoor();
                  default:
                    return true;
                }
              })
              .toList()
              .toObservable()
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeWith(new DisposableObserver<List<Device>>() {

                @Override public void onNext(List<Device> devices) {
                  getView().cachedDevicesLoaded(devices);
                }

                @Override public void onError(Throwable e) { }
                @Override public void onComplete() { }
              }));
    }
  }

  void getMapDevices(MAP_FILTER mapFilter) {
    String filter = mapFilter.name().toLowerCase();
    Observable<List<Device>> devicesObservable = mapDataFetch.fetch(filter)
        .map(MapData::getDevices)
        .flatMap(Observable::fromIterable)
        .toList()
        .toObservable()
        .subscribeOn(Schedulers.io());

    getCompositeDisposable().add(
        devicesObservable.subscribeWith(new DisposableObserver<List<Device>>() {

          @Override public void onNext(List<Device> devices) {
            CacheUtil.getSharedCache().updatedPredefined(devices);
            getView().devicesLoaded(devices);
            getView().loadPlaces();
          }

          @Override public void onError(Throwable e) { }
          @Override public void onComplete() { }
        }));
  }

}
