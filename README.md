# benchmarking-algorithms

A Java Benchmarking program to compare the average running times of five Sorting Algorithms:<space><space><space><space>*<space> 
  Bubble Sort 
  Heap Sort 
  Bucket Sort 
  Insertion Sort 
  Quick Sort
  
Both worst and best times are displayed to gain a greater view of each sorting algorithm. 
Repository also contains a Project Report, detailing and examining the selected algorithms and concludes results.

## Process followed to examine the average times for the sorting algorithms:
1. Generate random arrays of the input sizes in increments of ‘000 [1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000,10000].
2. Carry out warmup runs x 10
3. Run 10 times, divide results by 10 to calculate the average time
4. Convert results to milliseconds (ms)
5. Output the results to the console
