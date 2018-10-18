import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;

public class Driver {
    public static void main(String[] args) {
        Timer myTimer = new Timer();
        myTimer.schedule(new Checker(), 0, 3000);
    }
}
