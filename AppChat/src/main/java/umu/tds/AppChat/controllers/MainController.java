package umu.tds.AppChat.controllers;

public class MainController {
    private UIController uiController;

    public MainController() {
        this.uiController = new UIController(this);
    }

    public void startApp() {
        uiController.showLogin();
    }

    public void onLoginSuccess() {
        uiController.showMainPanel();
    }
}
