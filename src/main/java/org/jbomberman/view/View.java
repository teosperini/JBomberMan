package org.jbomberman.view;

import javafx.scene.layout.*;
public abstract class View {
    public abstract AnchorPane getRoot();
    protected abstract void initializeView();

    protected abstract void createPanes();
}
