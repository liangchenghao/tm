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
//
//    public void setSelectedPositions(SparseBooleanArray selectedPositions) {
//        this.selectedPositions = selectedPositions;
//    }

    public void removeItemChecked(int position){
        selectedPositions.delete(position);
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
        Integer[] positions = new Integer[selectedPositions.size()];
        for (int i = 0;i <selectedPositions.size();i++){
            positions[i] = selectedPositions.keyAt(i);
        }
        if (positions.length > 0){
            quickSort(positions , 0 , positions.length - 1);
        }
        return positions;
    }

    //快速排序
    private void quickSort(Integer[] data,int start, int end){
        int key = data[start];
        int i = start;
        int j = end;
        while (i < j){
            while (data[j] > key && j > i){
                j--;
            }
            data[i] = data[j];

            while (data[i] < key && i < j){
                i++;
            }
            data[j] = data[i];
        }
        data[i] = key;
        //递归
        if (i - 1 > start){
            quickSort(data,start,i - 1);
        }
        if (i + 1 < end){
            quickSort(data,i + 1,end);
        }
    }
}
