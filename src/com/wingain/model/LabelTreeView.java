package com.wingain.model;

import java.util.Collection;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class LabelTreeView<T>
{
    private TableView<T> resultTable;
    private final ObservableList<T> searchResult = FXCollections.observableArrayList();
    public LabelTreeView(TableView<T> r){
        resultTable = r;
    }
    
    public void init()
    {
        ObservableList<TableColumn<T,?>> tableColumns = resultTable.getColumns();
        tableColumns.get(0).setCellValueFactory(new PropertyValueFactory<>(LabelItem.LabelItemIndex));
        tableColumns.get(1).setCellValueFactory(new PropertyValueFactory<>(LabelItem.LabelItemOrderNum));
        tableColumns.get(2).setCellValueFactory(new PropertyValueFactory<>(LabelItem.LabelItemProductCode));
        tableColumns.get(3).setCellValueFactory(new PropertyValueFactory<>(LabelItem.LabelItemLabelContent));
        tableColumns.get(4).setCellValueFactory(new PropertyValueFactory<>(LabelItem.LabelItemTime));
        resultTable.setItems(searchResult);
        
        resultTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        resultTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<T>()
        {

            @Override
            public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue)
            {
                // TODO Auto-generated method stub
                
            }
        });
        
    }
    
    public void addItem(T v)
    {
        searchResult.add(v);
    }
    public boolean removeItem(T v)
    {
        return searchResult.remove(v);
    }
    
    public T removeItem(int i)
    {
        return searchResult.remove(i);
    }
    
    public void clear()
    {
        searchResult.clear();
    }
    
    public void addAll(Collection<? extends T> c)
    {
        searchResult.addAll(c);
    }
}
