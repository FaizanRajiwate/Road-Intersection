import java.util.ArrayList;
import java.util.List;

public interface Subject {
    List<Observer> observers = new ArrayList<>();

    public void attach(Observer observer);

    public void detach(Observer observer);

    public void notifyObservers();
}
