# TimeShare Job Scheduling Simulator

## Overview

This project simulates a basic job scheduling system using a queue-based approach. It is structured with three main classes:

- `Queue`: Manages job flow using a circular linked list.
- `Job`: Represents a job control block with relevant attributes.
- `TimeShare`: The driver program responsible for reading input, processing jobs, and generating a report.

---

## Class Descriptions

### `Queue` Class

The `Queue` class implements a queue using a circular linked list. It is used to manage job progression through different stages:

- **Structure**:  
  - `lastNode` points to the last item in the queue.
  - `lastNode.next` points to the front of the queue.
- **Methods**:
  - `enqueue(item)`: Adds an item to the rear of the queue.
    - If the queue is empty, the item becomes both the front and rear.
    - Otherwise, it is added after the last node and `lastNode` is updated.
  - `dequeue()`: Removes and returns the item at the front.
    - Throws a `QueueException` if the queue is empty.
    - Sets `lastNode` to `null` if there's only one item.
    - Otherwise, removes the front node and updates links.
  - `front()`: Returns the item at the front.
    - Throws `QueueException` if empty.
  - `isEmpty()`: Returns `true` if the queue is empty.

The `TimeShare` program uses three instances:
- `inputQueue`
- `jobQueue`
- `finishQueue`

---

### `Job` Class

Represents a job control block. Each job contains:

- `jobName`
- `arrivalTime`
- `startTime`
- `runTime`
- `waitTime`
- `turnTime`

Jobs are created from input file lines and hold all necessary information for scheduling analysis.

---

### `TimeShare` Class (Driver)

The `TimeShare` class orchestrates the scheduling process. It includes:

#### Main Components:

- `main()`: Entry point. Uses a `Scanner` to take user input and coordinates:
  - `readInputFile()`: Reads job info and populates `inputQueue`.
  - `process()`: Simulates scheduling and transitions between queues.
  - `printSummaryReport()`: Outputs job details and final statistics.

#### Processing Logic:

1. **Input Handling**:
   - Reads from file and populates `inputQueue`.

2. **Scheduling Loop** (within `process()`):
   - Moves jobs from `inputQueue` to `jobQueue` when arrival time matches current clock.
   - Moves completed jobs from `jobQueue` to `finishQueue`.
   - Starts jobs when CPU is free and jobs are ready.
   - Increments `idleTime` when CPU is idle but more jobs are pending.
   - Increments `clock` with each loop cycle.

3. **Summary Report**:
   - Iterates over `finishQueue` to compute and display:
     - Job details (arrival, start, run, wait, turnaround)
     - Averages and usage stats:
       - `averageWaitTime`
       - `cpuUsage`
       - `idleTime`
       - `cpuUsagePercentage`

---

## Sample Input (jobs3.dat)

```txt
job1 01 04
job2 03 02 
job3 06 01
job4 08 05
job5 10 02
```
## Sample Output 
```
Job Control Analysis: Summary Report

job id  arrival  start  run  wait  turnaround
        time     time   time time  time
------  -------  -----  ---- ----  ----------
job1    1        1      1    0     1
job2    3        3      4    0     4
job3    6        7      1    1     2
job4    8        8      5    0     5
job5    10       13     2    3     5

Average Wait Time => 0.80  
CPU Usage => 13.00  
CPU Idle => 1.00  
CPU Usage (%) => 92.86%
```
### Explanation of Metrics 

- Average Wait Time is calculated by adding the individual wait times of all jobs. (0 + 0 + 1 + 0 + 3) / 5 = 4 / 5 = 0.8
- CPU Usage is calculated by the adding the run times of all jobs. 1 + 4 + 1 + 5 + 2 = 13
- Idle time is 1 unit when CPU waits while jobs are pending.
- CPU Usage (%) is calculated by dividing the cpuUsage by cpuUsage + idleTime, all multiplied by 100. (13 / (13 + 2)) * 100 = 92.86%

## How to Compile and Run
```
javac TimeShare.java
java TimeShare jobs3.dat
```

 ## License
 No license has been provided for this project
