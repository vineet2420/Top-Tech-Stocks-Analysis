# Top-Tech-Stocks-Analysis
Using the Yahoo Finance API to fetch the given list of tech stocks, store individual stock objects, compare based on price, return sorted stocks efficiently
* Data Structures
  * HashMap -  for constant time stock retrieval
  * TreeSet - Overriding Comparator to store stocks in sorted order based on their stock price
 
* Insert
  * O(1) time complexity for HashMap put since no duplicate stock names will occur
  * O(log(n) due to iteration of log(n) levels in the self balancing binary search tree
  
  
* Retrieve
  * O(1) for HashMap since no collisions in hashing function will occur
  * O(K) for the TreeSet where K is an integer of the top K tech stocks to return.
  *Ex. top(10) = Cable One [1773.01], Alphabet [1518.66], Shopify [1040.02] ...
 

