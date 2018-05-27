package org.tec.datosII.OdysseyClient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Pagina de canciones
 */
public class TablePage {
    /**
     * Numero de pagina
     */
    public int pageNumber;
    /**
     * Numero de canciones totales
     */
    public int totalSongs;
    /**
     * Numero de paginas totales
     */
    public int pages;
    /**
     * Tamano de paginas
     */
    public int pageSize;
    /**
     * Canciones almacenadas en esta pagina
     */
    public ObservableList<Metadata> songs = FXCollections.observableArrayList();
}
