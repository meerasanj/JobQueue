import java.util.Scanner;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class TimeShare {
	private static Queue inputQueue = new Queue(); // instance of Queue class to store incoming jobs
	private static Queue jobQueue = new Queue(); // instance of Queue class to store jobs being processed
	private static Queue finishQueue = new Queue(); // instance of Queue class to store finished jobs 		
	private static int clock = 1; // simulated time 
	private static int idleTime = 0;
	private static int count = 0;
	private static double totalWaitTime = 0;
	private static double totalTurnaroundTime = 0;
	private static double totalRunTime = 0;
	
	public static void main(String[] args) { // main method, entry point of program 
		try {
			Scanner scanner = new Scanner(new File(args[0])); // create Scanner object; command line input
			readInputFile(inputQueue, scanner);
			process(inputQueue, jobQueue, finishQueue);
			printSummaryReport(finishQueue);
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + e.getMessage());
		} 
	}

	private static void readInputFile(Queue inputQueue, Scanner scanner) { // method to read input file and fill inputQueue
		while (scanner.hasNext()) { // reads input file line by line  
			String jobId = scanner.next();
			int arrivalTime = scanner.nextInt();
			int runTime = scanner.nextInt();
			
			Job job = new Job(jobId, arrivalTime, runTime); // creates Job object
			inputQueue.enqueue(job); // adds to inputQueue
		}	
		scanner.close();	
	}

	public static void process(Queue inputQueue, Queue jobQueue, Queue finishQueue) { // method to update counters and simulate processing of jobs 
		Job currentJob = new Job(); // Job object 
		while (!inputQueue.isEmpty() || !jobQueue.isEmpty()) { //main processing loop, continues until inputQueue or processQueue is empty 
			if (!inputQueue.isEmpty()) { // checks if jobs are in inputQueue
				currentJob = (Job)inputQueue.front(); // sets currentJob to first record in inputQueue 
				if (currentJob.arrivalTime == clock) { 
					jobQueue.enqueue(inputQueue.dequeue()); // removes from inputQueue and stores in processQueue 
				}
			}

			if (!jobQueue.isEmpty()) { // checks if if jobs are in jobQueue 
				currentJob = (Job) jobQueue.front(); // first record in jobQueue
				if((currentJob.startTime != -1) && (currentJob.runTime == (clock - currentJob.startTime))) {
					currentJob.turnTime = clock - currentJob.arrivalTime;
					finishQueue.enqueue(jobQueue.dequeue()); // removes from jobQueue and stores in finishQueue
				}
			}

			if (!jobQueue.isEmpty()) { // checks if jobs are in jobQueue 
				currentJob = (Job) jobQueue.front(); 
				if ((currentJob.arrivalTime <= clock) && (currentJob.startTime == -1)) { // checks if it is not started and if arrival time is less than or equal to clock time
					currentJob.startTime = clock; 
					currentJob.waitTime = clock - currentJob.arrivalTime;
				}
			}

			if (jobQueue.isEmpty() && !inputQueue.isEmpty()) {
				idleTime++;
			}

			clock++;
		}
	} 
	
	private static void printSummaryReport(Queue finishQueue) { // prints job control analysis report 
		System.out.println("Job Control Analysis: Summary Report");
		System.out.println();
		System.out.println("job id  arrival  start  run  wait  turnaround");
		System.out.println("        time     time   time time  time");
		System.out.println("------  -------  -----  ---- ----  ----------");
		
		Job finishedJob = new Job();
		while (!finishQueue.isEmpty()) { // continues until finishQueue is empty 
			finishedJob = (Job) finishQueue.dequeue(); // removes from finishQueue
			System.out.println(
				finishedJob.jobName + "\t" + 
				finishedJob.arrivalTime + "\t" +  
				finishedJob.startTime + "\t" + 
				finishedJob.runTime + "\t" + 
				finishedJob.waitTime + "\t" + 
				finishedJob.turnTime);
			// updates totalWaitTime, totalTurnaroundTime, totalRunTime, and count (# of jobs)
			totalWaitTime += finishedJob.waitTime;
			totalTurnaroundTime += (clock - finishedJob.arrivalTime);
			totalRunTime += finishedJob.runTime;
			count++;
		}
		
		System.out.println();
		// for formatting, i used %.2f. source: https://stackoverflow.com/questions/2538787/how-to-print-a-float-with-2-decimal-places-in-java
		double averageWaitTime = (double) (totalWaitTime / count);
		System.out.printf("Average Wait Time => %.2f\n", averageWaitTime);
			
		double cpuUsage = (double) totalRunTime;
		System.out.printf("CPU Usage =>  %.2f\n", cpuUsage);
		
		double idleTimeDecimal = (double) idleTime;
		System.out.printf("CPU Idle => %.2f\n", idleTimeDecimal);
		
		double cpuUsagePercentage = (double) (((cpuUsage) / (cpuUsage + idleTimeDecimal)) * 100);
		System.out.println("CPU Usage (%) => " + String.format("%.2f", cpuUsagePercentage) + "%");
	}	
}
