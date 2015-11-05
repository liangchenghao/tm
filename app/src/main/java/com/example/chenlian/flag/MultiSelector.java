package com.example.chenlian.flag;

import android.util.SparseBooleanArray;

/**
 * Created by chenlian on 11/4/2015.
 */
public class MultiSelector {
    private SparseBooleanArray selectedPositions = new SparseBooleanArray();
    private boolean isSelected = false;

    public SparseBooleanArray getSelectedPositions() {
        return selectedPositions;
    }

    public void setSelectedPositions(SparseBooleanArray selectedPositions) {
        this.selectedPositions = selectedPositions;
    }

    public void removeItemChecked(int key){
        selectedPositions.delete(key);
    }

    public boolean isSelectable() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setItemChecked(int position, boolean isChecked) {
        selectedPositions.put(position, isChecked);
    }

    public boolean isItemChecked(int position) {
        return selectedPositions.get(position);
    }

    public Integer[] hasSelected() {
        Integer[] keys = new Integer[selectedPositions.size()];
        for (int i = 0;i <selectedPositions.size();i++){
            keys[i] = selectedPositions.keyAt(i);
        }
        return keys;
    }
}
