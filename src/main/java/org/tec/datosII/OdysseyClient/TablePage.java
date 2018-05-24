package org.tec.datosII.OdysseyClient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TablePage {
    public int pageNumber;
    public int totalSongs;
    public int pages;
    public int pageSize;
    public ObservableList<Metadata> songs = FXCollections.observableArrayList();
}
