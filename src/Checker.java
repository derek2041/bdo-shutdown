import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TimerTask;

public class Checker extends TimerTask {
    public void run() {
        String line;
        ArrayList<String> taskList = new ArrayList<String>();
        Process p;
        try {
            //p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
            p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe /nh /fi " + "\"IMAGENAME eq BlackDesert64.exe\" /fo csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            line = reader.readLine();
            String[] info = line.split(",");
            if (info[0].equals("INFO: No tasks are running which match the specified criteria.")) {
                //Process does not exist
                System.out.println("BDO process does not exist.");
                Process shutdown = Runtime.getRuntime().exec("shutdown -s -t 2");
                System.exit(0);
            } else {
                int bdo_process_id = Integer.parseInt(info[1].substring(1, info[1].length() - 1));
                p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "netstat.exe -aon");
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String[] info2;
                String before = "";
                boolean BDO_IS_LISTED_IN_NETSTAT = false;
                while ((before = reader2.readLine()) != null) {
                    String after = before.trim().replaceAll(" +", " ");
                    info2 = after.split(" ");
                    if (info2[0].equals("TCP")) {
                        int currPID = Integer.parseInt(info2[4]);
                        if (currPID == bdo_process_id) {
                            BDO_IS_LISTED_IN_NETSTAT = true;
                            System.out.println("BDO Connection(s) (PID: " + bdo_process_id + ") is " + info2[3]);
                        }
                    }
                }
                if (!(BDO_IS_LISTED_IN_NETSTAT)) {
                    //Process exists, but no network interaction is occurring
                    System.out.println("BDO Connection is disconnected.");
                    //System.out.println("shutdown -s -t 60");
                    Process shutdown = Runtime.getRuntime().exec("shutdown -s -t 2");
                    System.exit(0);
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
