package fr.project.warningObservers;

public interface WarningObserver {

    void onWarningDetected(String warning, String token);
    void showWarning();
    int numberOfWarningsDetected();

}
