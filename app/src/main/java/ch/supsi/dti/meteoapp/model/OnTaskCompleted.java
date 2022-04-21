package ch.supsi.dti.meteoapp.model;

public interface OnTaskCompleted {
    void OnTextGet(String weather);
    void OnTempGet(String temperature);
    void onImageGet(byte[] img);
}
