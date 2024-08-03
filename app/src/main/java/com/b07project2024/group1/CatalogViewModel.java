package com.b07project2024.group1;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * CatalogViewModel provides data to fill the CatalogFragment normally and through a CatalogItem search
 * Depends on the CatalogRepository abstraction following dependency inversion and
 * Liskov Substitution
 * Single Responsibility ViewModel for Catalogs
 */
@HiltViewModel
public class CatalogViewModel extends ViewModel{
    private final CatalogRepository repository;
    private final MutableLiveData<List<CatalogItem>> liveList;
    private final MutableLiveData<CatalogItem> filter;
    private String lastKey;

    @Inject
    public CatalogViewModel(CatalogRepository repository) {
        this.repository = repository;
        liveList = new MutableLiveData<>();
        filter = new MutableLiveData<>();
    }

    /**
     * Returns LiveData List to be observed by the attached Fragment/View
     * @return LiveData list
     */
    public LiveData<List<CatalogItem>> getInitialCatalogPage() {
        repository.getCatalogPage(liveList);
        return liveList;
    }

    /**
     * Retrieves the next catalog page based off the id of the last item loaded
     */
    public void getNextCatalogPage() {
        if (filter.getValue() != null)
            return;
        if (liveList.getValue() != null && !liveList.getValue().isEmpty())
            setLastKey(String.valueOf(liveList.getValue().get(liveList.getValue().size() - 1).getLot()));
        repository.getNextCatalogPage(String.valueOf(Integer.parseInt(getLastKey()) + 1), liveList);
    }

    // Search is implemented here as opposed to a different ViewModel to have a single source
    // of information for CatalogFragment
    /**
     * Modifies liveData according to search to be observed by the attached Fragment/View
     * Supports search through an CatalogItem parameter
     * @param filter the incomplete CatalogItem filter
     */
    public void getCatalogPageBySearch(CatalogItem filter) {
        setFilter(filter);
        repository.getCatalogPageByItem(filter, liveList);
    }

    public void clearSearch (){
        setFilter(null);
        setLastKey(null);
        getInitialCatalogPage();
    }

    public void setFilter(CatalogItem filter) {
        this.filter.setValue(filter);
    }
    public CatalogItem getFilter() {
        return filter.getValue();
    }
    public LiveData<CatalogItem> getFilterLive() { return filter; }

    public void setLastKey(String lastKey) {
        this.lastKey = lastKey;
    }
    public String getLastKey() {
        if (lastKey == null){
            return "-1";
        }
        return lastKey;
    }

    public void setLiveList(List<CatalogItem> items){
        this.liveList.setValue(items);
    }
    public MutableLiveData<List<CatalogItem>> getLiveList(){
        return liveList;
    }
}