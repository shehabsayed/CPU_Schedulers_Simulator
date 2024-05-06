import java.util.*;

// Generic Pair class to hold two values
class Pair<F, S> {
    public final F first;
    public final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }
}

// Comparator for sorting processes based on priority
class PriorityWorkComparator implements Comparator<Process> {
    @Override
    public int compare(Process p1, Process p2) {
        int result = Integer.compare(p1.getProcessPriority(), p2.getProcessPriority());
        if (result == 0) {
            result = Integer.compare(p1.getWork(), p2.getWork());
        }
        return result;
    }
}

// Comparator for sorting processes based on burst time
class BurstWorkComparator implements Comparator<Process> {
    @Override
    public int compare(Process p1, Process p2) {
        int result = Integer.compare(p1.getProcessBurstTime(), p2.getProcessBurstTime());
        if (result == 0) {
            result = Integer.compare(p1.getWork(), p2.getWork());
        }
        return result;
    }
}

// Class to manage different scheduling algorithms
class SchedulerManager {
    Scanner scanner = new Scanner(System.in);
    Random rand = new Random();
    Process[] processes;
    int contextTime;
    float quantum;
    int numberOfProcesses;

    // Method to start different schedulers based on user input
    public void startSchedulers(Process[] processes,int contextTime,float quantum,int numberOfProcesses) {
        this.processes = processes;
        this.contextTime = contextTime;
        this.quantum = quantum;
        this.numberOfProcesses = numberOfProcesses;
        boolean flag = true;
        while (flag) {
            System.out.println("Choose Schedule Type: ");
            System.out.println("1- SJFScheduler");
            System.out.println("2- SRTFScheduler");
            System.out.println("3- PriorityScheduler");
            System.out.println("4- AGScheduler");
            System.out.println("5- Exit");
            int option = scanner.nextInt();
            Process [] testcase = new Process[numberOfProcesses];
            switch (option) {
                case 1:
                    for(int i=0;i<numberOfProcesses;i++){
                        testcase[i] = new Process(
                                processes[i].getProcessName(),
                                processes[i].getProcessColor(),
                                processes[i].getProcessArrivalTime(),
                                processes[i].getProcessBurstTime(),
                                processes[i].getProcessPriority()
                        );
                    }
                    SJFScheduler(testcase, contextTime,numberOfProcesses);
                    break;
                case 2:
                    for(int i=0;i<numberOfProcesses;i++){
                        testcase[i] = new Process(
                                processes[i].getProcessName(),
                                processes[i].getProcessColor(),
                                processes[i].getProcessArrivalTime(),
                                processes[i].getProcessBurstTime(),
                                processes[i].getProcessPriority()
                        );
                    }
                    SRTFScheduler(testcase, contextTime,numberOfProcesses);
                    break;
                case 3:
                    for(int i=0;i<numberOfProcesses;i++){
                        testcase[i] = new Process(
                                processes[i].getProcessName(),
                                processes[i].getProcessColor(),
                                processes[i].getProcessArrivalTime(),
                                processes[i].getProcessBurstTime(),
                                processes[i].getProcessPriority()
                        );
                    }
                    PriorityScheduler(testcase, contextTime,numberOfProcesses);
                    break;
                case 4:
                    for(int i=0;i<numberOfProcesses;i++){
                        testcase[i] = new Process(
                                processes[i].getProcessName(),
                                processes[i].getProcessColor(),
                                processes[i].getProcessArrivalTime(),
                                processes[i].getProcessBurstTime(),
                                processes[i].getProcessPriority()
                        );
                    }
                    AGScheduler(testcase, contextTime, quantum,numberOfProcesses);
                    break;
                case 5:
                    flag = false;
                    break;
                default:
                    System.out.println("Error,Enter a valid option!");
                    break;
            }
        }
    }

    // Implementation of Shortest Job First (SJF) scheduling algorithm
    public void SJFScheduler(Process[] processes,int contextTime,int numberOfProcesses) {
        System.out.println("*****SJFScheduler*****");
        Map <Process,Boolean> added =  new HashMap<>();
        Map <Process,Integer> endTime = new HashMap<>();
        Queue<Process> order = new LinkedList<>();
        List<Process> arrived = new ArrayList<>();
        Process [] tempProcess = new Process[numberOfProcesses];
        Process  temp = new Process("nothing","white",Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE);
        arrived.add(temp);
        int currentTime = 0;
        int workingTime = 0;
        int timer = 0;
        for(int i=0;i<numberOfProcesses;i++) {
            added.put(processes[i],false);
            workingTime += processes[i].getProcessBurstTime();
            tempProcess[i] = new Process(
                    processes[i].getProcessName(),
                    processes[i].getProcessColor(),
                    processes[i].getProcessArrivalTime(),
                    processes[i].getProcessBurstTime(),
                    processes[i].getProcessPriority()
            );
        }
        while(timer < workingTime){
            for(int i=0;i<numberOfProcesses;i++){
                if(!added.get(processes[i])){
                    if(currentTime >= processes[i].getProcessArrivalTime()){
                        arrived.add(processes[i]);
                        added.put(processes[i],true);
                    }
                }
            }
            arrived.sort(null);
            if(arrived.get(0).getProcessName().equals("nothing")){
                currentTime++;
                arrived.getFirst().setBurstTime(arrived.getFirst().getProcessBurstTime()-1);
            }
            else{
                currentTime += arrived.getFirst().getProcessBurstTime();
                timer += arrived.getFirst().getProcessBurstTime();
                arrived.getFirst().setBurstTime(0);
            }
            order.add(arrived.getFirst());
            if(arrived.getFirst().getProcessBurstTime() == 0){
                endTime.put(arrived.getFirst(), currentTime);
                arrived.remove(0);
            }
        }
        String [] orderTemp = new String[order.size()];
        int sz = order.size();
        for(int i=0;i<sz;i++){
            orderTemp[i] = order.peek().getProcessName();
            order.remove();
        }
        System.out.print("start -> " + orderTemp[0] + " -> ");
        for(int i=1;i<orderTemp.length;i++){
            if(!orderTemp[i].equals(orderTemp[i-1])){
                System.out.print(orderTemp[i] + " -> ");
            }
        }
        System.out.println("finished");
        float avrgWaitingTime = 0F;
        float avrgTurnAroundTime = 0F;
        System.out.println("Process Name \t" + "Waiting Time \t" + "Turn Around Time");
        for(int i=0;i<numberOfProcesses;i++){
            int ans = endTime.get(processes[i]) - tempProcess[i].getProcessArrivalTime();
            int ans2 = ans;
            for(int j=0;j<numberOfProcesses;j++){
                if(processes[i].getProcessName().equals(tempProcess[j].getProcessName())){
                    ans -= tempProcess[j].getProcessBurstTime();
                    break;
                }
            }
            for(int j=1;j<orderTemp.length;j++){
                if(processes[i].getProcessName().equals(orderTemp[j])){
                    if(!orderTemp[j-1].equals("nothing") && !orderTemp[j-1].equals(orderTemp[j])){
                        ans += contextTime;
                        ans2 += contextTime;
                    }
                }
            }
            avrgTurnAroundTime += ans2;
            avrgWaitingTime += ans;
            System.out.println(processes[i].getProcessName() + "\t\t\t\t" + ans + "\t\t\t\t" + ans2);
        }
        avrgWaitingTime /= numberOfProcesses;
        avrgTurnAroundTime /= numberOfProcesses;
        System.out.println("-)Average Waiting Time: " + avrgWaitingTime);
        System.out.println("-)Average Turn Around Time: " + avrgTurnAroundTime);
    }

    // Implementation of Shortest Remaining Time First (SRTF) scheduling algorithm
    public void SRTFScheduler(Process[] processes,int contextTime,int numberOfProcesses) {
        System.out.println("*****SRTFScheduler*****");
        Map <Process,Boolean> added =  new HashMap<>();
        Map <Process,Integer> endTime = new HashMap<>();
        Queue<Process> order = new LinkedList<>();
        List<Process> arrived = new ArrayList<>();
        Process [] tempProcess = new Process[numberOfProcesses];
        Process  temp = new Process("nothing","white",Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE);
        temp.setWork(Integer.MAX_VALUE);
        arrived.add(temp);
        int currentTime = 0;
        int workingTime = 0;
        int timer = 0;
        for(int i=0;i<numberOfProcesses;i++) {
            added.put(processes[i],false);
            workingTime += processes[i].getProcessBurstTime();
            tempProcess[i] = new Process(
                    processes[i].getProcessName(),
                    processes[i].getProcessColor(),
                    processes[i].getProcessArrivalTime(),
                    processes[i].getProcessBurstTime(),
                    processes[i].getProcessPriority()
            );
        }
        while(timer < workingTime){
            for(int i=0;i<numberOfProcesses;i++){
                if(!added.get(processes[i])){
                    if(currentTime == processes[i].getProcessArrivalTime()){
                        processes[i].setWork(0);
                        arrived.add(processes[i]);
                        added.put(processes[i],true);
                    }
                }
            }
            Collections.sort(arrived, new BurstWorkComparator());
            currentTime++;
            arrived.getFirst().setBurstTime(arrived.getFirst().getProcessBurstTime()-1);
            arrived.getFirst().setWork(arrived.getFirst().getWork() + 1);
            if(!arrived.getFirst().getProcessName().equals("nothing")) {
                timer++;
            }
            order.add(arrived.getFirst());
            if(arrived.getFirst().getProcessBurstTime() == 0){
                endTime.put(arrived.getFirst(), currentTime);
                arrived.remove(0);
            }
        }
        String [] orderTemp = new String[order.size()];
        int sz = order.size();
        for(int i=0;i<sz;i++){
            orderTemp[i] = order.peek().getProcessName();
            order.remove();
        }
        System.out.print("start -> " + orderTemp[0] + " -> ");
        for(int i=1;i<orderTemp.length;i++){
            if(!orderTemp[i].equals(orderTemp[i-1])){
                System.out.print(orderTemp[i] + " -> ");
            }
        }

        System.out.println("finished");
        float avrgWaitingTime = 0F;
        float avrgTurnAroundTime = 0F;
        System.out.println("Process Name \t" + "Waiting Time \t" + "Turn Around Time");
        for(int i=0;i<numberOfProcesses;i++){
            int ans = endTime.get(processes[i]) - tempProcess[i].getProcessArrivalTime();
            int ans2 = ans;
            for(int j=0;j<numberOfProcesses;j++){
                if(processes[i].getProcessName().equals(tempProcess[j].getProcessName())){
                    ans -= tempProcess[j].getProcessBurstTime();
                    break;
                }
            }
            for(int j=1;j<orderTemp.length;j++){
                if(processes[i].getProcessName().equals(orderTemp[j])){
                    if(!orderTemp[j-1].equals("nothing") && !orderTemp[j-1].equals(orderTemp[j])){
                        ans += contextTime;
                        ans2 += contextTime;
                    }
                }
            }
            avrgTurnAroundTime += ans2;
            avrgWaitingTime += ans;
            System.out.println(processes[i].getProcessName() + "\t\t\t\t" + ans + "\t\t\t\t" + ans2 + "\t\t\t\t" + endTime.get(processes[i]));
        }
        avrgWaitingTime /= numberOfProcesses;
        avrgTurnAroundTime /= numberOfProcesses;
        System.out.println("-)Average Waiting Time: " + avrgWaitingTime);
        System.out.println("-)Average Turn Around Time: " + avrgTurnAroundTime);
    }

    // Implementation of Priority Scheduling algorithm
    public void PriorityScheduler(Process[] processes,int contextTime,int numberOfProcesses) {
        System.out.println("*****PriorityScheduler*****");
        Map <Process,Boolean> added =  new HashMap<>();
        Map <Process,Integer> endTime = new HashMap<>();
        Queue<Process> order = new LinkedList<>();
        List<Process> arrived = new ArrayList<>();
        Process [] tempProcess = new Process[numberOfProcesses];
        Process  temp = new Process("nothing","white",Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE);
        arrived.add(temp);
        int currentTime = 0;
        int workingTime = 0;
        int timer = 0;
        for(int i=0;i<numberOfProcesses;i++) {
            added.put(processes[i],false);
            workingTime += processes[i].getProcessBurstTime();
            tempProcess[i] = new Process(
                    processes[i].getProcessName(),
                    processes[i].getProcessColor(),
                    processes[i].getProcessArrivalTime(),
                    processes[i].getProcessBurstTime(),
                    processes[i].getProcessPriority()
            );
        }
        while(timer < workingTime){
            for(int i=0;i<numberOfProcesses;i++){
                if(!added.get(processes[i])){
                    if(currentTime == processes[i].getProcessArrivalTime()){
                        arrived.add(processes[i]);
                        added.put(processes[i],true);
                    }
                }
            }
            Collections.sort(arrived, new PriorityWorkComparator());
            currentTime++;
            arrived.getFirst().setBurstTime(arrived.getFirst().getProcessBurstTime()-1);
            arrived.getFirst().setWork(arrived.getFirst().getWork() + 1);
            if(!arrived.getFirst().getProcessName().equals("nothing")) {
                timer++;
            }
            order.add(arrived.getFirst());
            if(arrived.getFirst().getProcessBurstTime() == 0){
                endTime.put(arrived.getFirst(), currentTime);
                arrived.remove(0);
            }
        }
        String [] orderTemp = new String[order.size()];
        int sz = order.size();
        for(int i=0;i<sz;i++){
            orderTemp[i] = order.peek().getProcessName();
            order.remove();
        }
        System.out.print("start -> " + orderTemp[0] + " -> ");
        for(int i=1;i<orderTemp.length;i++){
            if(!orderTemp[i].equals(orderTemp[i-1])){
                System.out.print(orderTemp[i] + " -> ");
            }
        }
        System.out.println("finished");
        float avrgWaitingTime = 0F;
        float avrgTurnAroundTime = 0F;
        System.out.println("Process Name \t" + "Waiting Time \t" + "Turn Around Time");
        for(int i=0;i<numberOfProcesses;i++){
            int ans = endTime.get(processes[i]) - tempProcess[i].getProcessArrivalTime();
            int ans2 = ans;
            for(int j=0;j<numberOfProcesses;j++){
                if(processes[i].getProcessName().equals(tempProcess[j].getProcessName())){
                    ans -= tempProcess[j].getProcessBurstTime();
                    break;
                }
            }
            for(int j=1;j<orderTemp.length;j++){
                if(processes[i].getProcessName().equals(orderTemp[j])){
                    if(!orderTemp[j-1].equals("nothing") && !orderTemp[j-1].equals(orderTemp[j])){
                        ans += contextTime;
                        ans2 += contextTime;
                    }
                }
            }
            avrgTurnAroundTime += ans2;
            avrgWaitingTime += ans;
            System.out.println(processes[i].getProcessName() + "\t\t\t\t" + ans + "\t\t\t\t" + ans2);
        }
        avrgWaitingTime /= numberOfProcesses;
        avrgTurnAroundTime /= numberOfProcesses;
        System.out.println("-)Average Waiting Time: " + avrgWaitingTime);
        System.out.println("-)Average Turn Around Time: " + avrgTurnAroundTime);
    }

    // Implementation of custom scheduling algorithm (AGScheduler)
    public void AGScheduler(Process[] processes,int contextTime,float quantum,int numberOfProcesses) {
        System.out.println("*****AGScheduler*****");
        Map <Process,Boolean> added =  new HashMap<>();
        Map <Process,Integer> endTime = new HashMap<>();
        Map <Process,Float> quantumMap = new HashMap<>();
        Map <Process,Float> halfQuantum = new HashMap<>();
        Map <Process,Float> tempQuantum = new HashMap<>();
        Queue<Process> order = new LinkedList<>();
        List<Process> arrived = new ArrayList<>();
        Queue<Pair<Process,Integer>> wait = new LinkedList<>();
        Process [] tempProcess = new Process[numberOfProcesses];
        Process  temp = new Process("nothing","white",Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE);
        quantumMap.put(temp,Float.MAX_VALUE);
        tempQuantum.put(temp,Float.MAX_VALUE);
        halfQuantum.put(temp,Float.MAX_VALUE);
        arrived.add(temp);
        int currentTime = 0;
        int workingTime = 0;
        int timer = 0;
        for(int i=0;i<numberOfProcesses;i++) {
            endTime.put(processes[i],-1);
            added.put(processes[i],false);
            quantumMap.put(processes[i],quantum);
            tempQuantum.put(processes[i],quantum);
            float val = quantumMap.get(processes[i]) / 2.0F;
            halfQuantum.put(processes[i],val);
            workingTime += processes[i].getProcessBurstTime();
            tempProcess[i] = new Process(
                    processes[i].getProcessName(),
                    processes[i].getProcessColor(),
                    processes[i].getProcessArrivalTime(),
                    processes[i].getProcessBurstTime(),
                    processes[i].getProcessPriority()
            );
        }
        int agFactor;
        Map <Process,Integer> agMap = new HashMap<>();
        for(int i=0;i<numberOfProcesses;i++){
            int randomNumber = rand.nextInt(21);
            if(randomNumber < 10){
                agFactor = randomNumber + processes[i].getProcessArrivalTime() + processes[i].getProcessBurstTime();
            } else if (randomNumber > 10) {
                agFactor = 10 + processes[i].getProcessArrivalTime() + processes[i].getProcessBurstTime();
            }else{
                agFactor = processes[i].getProcessPriority() + processes[i].getProcessArrivalTime() + processes[i].getProcessBurstTime();
            }
            agMap.put(processes[i],agFactor);
        }
        agMap.put(temp,Integer.MAX_VALUE);
        System.out.print("(");
        for(int i=0;i<numberOfProcesses;i++){
            if(i < numberOfProcesses-1){
                System.out.print(tempQuantum.get(processes[i]) + ", ");
            }else{
                System.out.print(tempQuantum.get(processes[i]));
            }
        }
        System.out.println(")");
        while(timer < workingTime){
            for(int i=0;i<numberOfProcesses;i++){
                if(!added.get(processes[i])){
                    if(currentTime == processes[i].getProcessArrivalTime()){
                        if(arrived.size() == 2){
                            wait.add(new Pair<>(processes[i],agMap.get(processes[i])));
                        }else {
                            arrived.add(0,processes[i]);
                        }
                        added.put(processes[i],true);
                    }
                }
            }
            if(!arrived.isEmpty() && quantumMap.get(arrived.getFirst()) <= halfQuantum.get(arrived.getFirst())){
                if (!wait.isEmpty()) {
                    if ( agMap.get(arrived.getFirst()) > agMap.get(wait.peek().first)) {
                        quantumMap.put(arrived.getFirst(), quantumMap.get(arrived.getFirst()) + tempQuantum.get(arrived.getFirst()));
                        tempQuantum.put(arrived.getFirst(), quantumMap.get(arrived.getFirst()));
                        float val = tempQuantum.get(arrived.getFirst()) / 2.0F;
                        halfQuantum.put(arrived.getFirst(), val);
                        wait.add(new Pair<>(arrived.getFirst(),agMap.get(arrived.getFirst())));
                        arrived.remove(0);
                        arrived.add(0,wait.peek().first);
                        wait.remove();
                        List<Pair<Process,Integer>> sortingList = new ArrayList<>();
                        int sz = wait.size();
                        for(int i=0;i<sz;i++){
                            sortingList.add(wait.peek());
                            wait.remove();
                        }
                        Collections.sort(sortingList, Comparator.comparing(pair -> pair.second));
                        sz = sortingList.size();
                        for(int i=0;i<sz;i++){
                            wait.add(sortingList.getFirst());
                            sortingList.remove(0);

                        }
                        System.out.print("(");
                        for(int i=0;i<numberOfProcesses;i++){
                            if(i < numberOfProcesses-1){
                                System.out.print(tempQuantum.get(processes[i]) + ", ");
                            }else{
                                System.out.print(tempQuantum.get(processes[i]));
                            }
                        }
                        System.out.println(")");
                    }
                }
            }
            currentTime++;
            if(!arrived.isEmpty() && !arrived.getFirst().getProcessName().equals("nothing")){
                timer++;
            }
            arrived.getFirst().setBurstTime(arrived.getFirst().getProcessBurstTime() - 1);
            quantumMap.put(arrived.getFirst(),quantumMap.get(arrived.getFirst()) - 1);
            order.add(arrived.getFirst());
            if(arrived.getFirst().getProcessBurstTime() == 0){
                endTime.put(arrived.getFirst(), currentTime);
                tempQuantum.put(arrived.getFirst(),0.0F);
                quantumMap.put(arrived.getFirst(),0.0F);
                halfQuantum.put(arrived.getFirst(),0.0F);
                arrived.remove(0);
                if(!wait.isEmpty()) {
                    arrived.add(0, wait.peek().first);
                    wait.remove();
                }
                List<Pair<Process,Integer>> sortingList = new ArrayList<>();
                int sz = wait.size();
                for(int i=0;i<sz;i++){
                    sortingList.add(wait.peek());
                    wait.remove();
                }
                Collections.sort(sortingList, Comparator.comparing(pair -> pair.second));
                sz = sortingList.size();
                for(int i=0;i<sz;i++){
                    wait.add(sortingList.getFirst());
                    sortingList.remove(0);
                }
                System.out.print("(");
                for(int i=0;i<numberOfProcesses;i++){
                    if(i < numberOfProcesses-1){
                        System.out.print(tempQuantum.get(processes[i]) + ", ");
                    }else{
                        System.out.print(tempQuantum.get(processes[i]));
                    }
                }
                System.out.println(")");
            }
            if(!arrived.isEmpty() && quantumMap.get(arrived.getFirst()) == 0){
                float mean = 0;
                for(int i=0;i<numberOfProcesses;i++){
                    mean += tempQuantum.get(processes[i]);
                }
                mean /= (float) numberOfProcesses;
                mean = (float) Math.ceil(mean * 0.1F);
                quantumMap.put(arrived.getFirst(),tempQuantum.get(arrived.getFirst()) + mean);
                tempQuantum.put(arrived.getFirst(),quantumMap.get(arrived.getFirst()));
                float val = tempQuantum.get(arrived.getFirst()) / 2.0F;
                halfQuantum.put(arrived.getFirst(), val);
                wait.add(new Pair<>(arrived.getFirst(),agMap.get(arrived.getFirst())));
                arrived.remove(0);
                arrived.add(0,wait.peek().first);
                wait.remove();
                List<Pair<Process,Integer>> sortingList = new ArrayList<>();
                int sz = wait.size();
                for(int i=0;i<sz;i++){
                    sortingList.add(wait.peek());
                    wait.remove();
                }
                Collections.sort(sortingList, Comparator.comparing(pair -> pair.second));
                sz = sortingList.size();
                for(int i=0;i<sz;i++){
                    wait.add(sortingList.getFirst());
                    sortingList.remove(0);
                }
                System.out.print("(");
                for(int i=0;i<numberOfProcesses;i++){
                    if(i < numberOfProcesses-1){
                        System.out.print(tempQuantum.get(processes[i]) + ", ");
                    }else{
                        System.out.print(tempQuantum.get(processes[i]));
                    }
                }
                System.out.println(")");
            }
        }
        String [] orderTemp = new String[order.size()];
        int sz = order.size();
        for(int i=0;i<sz;i++){
            orderTemp[i] = order.peek().getProcessName();
            order.remove();
        }
        System.out.print("start -> " + orderTemp[0] + " -> ");
        for(int i=1;i<orderTemp.length;i++){
            if(!orderTemp[i].equals(orderTemp[i-1])){
                System.out.print(orderTemp[i] + " -> ");
            }
        }
        System.out.println("finished");
        float avrgWaitingTime = 0F;
        float avrgTurnAroundTime = 0F;
        System.out.println("Process Name \t" + "Waiting Time \t" + "Turn Around Time");
        for(int i=0;i<numberOfProcesses;i++){
            int ans = endTime.get(processes[i]) - tempProcess[i].getProcessArrivalTime();
            int ans2 = ans;
            for(int j=0;j<numberOfProcesses;j++){
                if(processes[i].getProcessName().equals(tempProcess[j].getProcessName())){
                    ans -= tempProcess[j].getProcessBurstTime();
                    break;
                }
            }
            for(int j=1;j<orderTemp.length;j++){
                if(processes[i].getProcessName().equals(orderTemp[j])){
                    if(!orderTemp[j-1].equals("nothing") && !orderTemp[j-1].equals(orderTemp[j])){
                        ans += contextTime;
                        ans2 += contextTime;
                    }
                }
            }
            avrgTurnAroundTime += ans2;
            avrgWaitingTime += ans;
            System.out.println(processes[i].getProcessName() + "\t\t\t\t" + ans + "\t\t\t\t" + ans2);
        }
        avrgWaitingTime /= numberOfProcesses;
        avrgTurnAroundTime /= numberOfProcesses;
        System.out.println("-)Average Waiting Time: " + avrgWaitingTime);
        System.out.println("-)Average Turn Around Time: " + avrgTurnAroundTime);
    }
}
class Process implements Comparable <Process>{
    String processName;
    String processColor;
    int arrivalTime;
    int burstTime;
    int priority;
    int work;
    public Process(String processName,String processColor,int arrivalTime,int burstTime,int priority){
        this.processName = processName;
        this.processColor = processColor;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.work = 0;
    }
    String getProcessName(){
        return this.processName;
    }
    String getProcessColor(){
        return this.processColor;
    }
    int getProcessArrivalTime(){
        return this.arrivalTime;
    }
    int getProcessBurstTime(){
        return this.burstTime;
    }
    int getProcessPriority(){
        return this.priority;
    }
    void setBurstTime(int val){
        this.burstTime = val;
    }
    public int getWork() {
        return work;
    }
    public void setWork(int age) {
        this.work = age;
    }
    @Override
    public int compareTo(Process other) {
        return Integer.compare(this.burstTime, other.burstTime);
    }

}

public class Main {

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of processes: ");
        int numberOfProcesses = scanner.nextInt();
        Process[] processes = new Process[numberOfProcesses];
        System.out.println("Enter Round Robin Quantum Time: ");
        float quantum = scanner.nextInt();
        System.out.println("Enter Context Switching Time: ");
        int contextSwitching = scanner.nextInt();
        for(int i=0;i<numberOfProcesses;i++){
            System.out.println("Enter Name, Color, Arrival Time, Burst Time and Priority for Process [" + (i + 1) + "] respectively:");
            String processName = scanner.next();
            String processColor = scanner.next();
            int arrivalTime = scanner.nextInt();
            int burstTime = scanner.nextInt();
            int priority = scanner.nextInt();
            Process temp = new Process(processName,processColor,arrivalTime,burstTime,priority);
            processes[i] = temp;
        }
        SchedulerManager manager = new SchedulerManager();
        manager.startSchedulers(processes,contextSwitching,quantum,numberOfProcesses);
    }
}