import java.io.*;
import java.math.*;
import java.util.*;

import yahoofinance.*;

public class myStock{


	//declare the hashmap for O(1) get and put operations
	HashMap<String, stockInfo> stockHashDB;

	//for O(K) iteration when returning top K stocks from the tree
	TreeSet<stockInfo> stockTreeSetDB;


	private static class stockInfo{
		protected String name;
		protected BigDecimal price;
		public stockInfo(String nameIn, BigDecimal priceIn) {
			name = nameIn;
			price = priceIn;
		}
		public String toString() {
			StringBuilder stockInfoString = new StringBuilder("");
			stockInfoString.append(name + " " + price.toString());
			return stockInfoString.toString();
		}


	}

	//implement the comparator interface so the TreeSet knows which order to sort the stocks of type StockInfo being passed in
	//created another class for:
	// (1) ability to add a new constructor with symbol since i am sorting by stockInfo's price, never passing symbol inside
	// (2) not to interfere with the original code

	public static class stockInfoWithSymbol extends stockInfo implements Comparator <stockInfo>{

		protected String stockSymbol;

		//explicit value constructor to add in the string with the treeSet
		public stockInfoWithSymbol(String symbol, String nameIn, BigDecimal priceIn){
			//intitialize name, price, symbol
			super(nameIn, priceIn);
			stockSymbol = symbol;
		}

		//actual output of the object is changed to add the symbol
		public String toString() {
			StringBuilder stockInfoString = new StringBuilder("");
			stockInfoString.append(stockSymbol + " " + name + " " + price.toString());

			return stockInfoString.toString();
		}

		//compare function being told to compare the objects based on their price
		//we need to do this otherwise the tree does not know how to add an arbitrary stockInfo type of object
		@Override
		public int compare(stockInfo o1, stockInfo o2) {
			return(o1.price.compareTo(o2.price));
		}
	}

	
	public myStock () {
		// Creating the data structures used for the database here,
		//		 and override the data structure's compare method if needed
		//       such that the stocks would be sorted by price in the data structure


		//Initialize the hash map
		stockHashDB = new HashMap<String, stockInfo>();

		//Initialize the tree set with stockInfo and stockInfoWithSymbol being passed inside for the comparator, toString methods
		stockTreeSetDB = new TreeSet<stockInfo>(new stockInfoWithSymbol(null,null,null));


	}

	public void insertOrUpdate(String symbol, stockInfo stock) {

		//check if the hashmap already contains the symbol, if it is not the first time running, then we will replace
		//technically this will never run since our hashmap is being created each time the program runs
		//if we saved our database to an external source, then we can compare the values and replace accordingly
		if(stockHashDB.containsKey(symbol)){
			stockHashDB.replace(symbol, stock);
			System.out.println("running");
		}
		else{
			//O(1) time complexity but also depends on how our key is hashed
			stockHashDB.put(symbol,stock);

			//O(log(n) add time complexity
			stockTreeSetDB.add(new stockInfoWithSymbol(symbol,stock.name,stock.price));
		}





	}
	
	public stockInfo get(String symbol) {
		// Time complexity is O(1) constant time


        //getting values from a hash map is O(1)
		return stockHashDB.get(symbol);

	}


	public List<stockInfo> top(int k) {
		// retrieval should be done in O(k)
		// 	using Iterator to retrieve items in the sorted order.

        //declare and initialize the List of type ArrayList that will return the values inside stockInfo
		List<stockInfo> topStocks = new ArrayList<stockInfo>();

		//declaring and initializing an iterator to iterate the values in the treeSet in descending order
		Iterator<stockInfo> treeSetIterator = stockTreeSetDB.descendingIterator();

		for(int i = 0; i<k; i++){
				topStocks.add(treeSetIterator.next());
		}

		return topStocks;
	}
	

    public static void main(String[] args) throws IOException {   	
    	
    	// test the database creation based on the input file
    	myStock techStock = new myStock();
    	BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("./US-Tech-Symbols.txt"));
			String line = reader.readLine();
			System.out.println("Line read: "+ line);
			while (line != null) {
				String[] var = line.split(":");

				System.out.println(Arrays.toString(var));
				
				// YahooFinance API is used
				// make sure the lib files are included in the project build path
				Stock stock = YahooFinance.get(var[0]);
				System.out.println(stock);
				
				// test the insertOrUpdate operation 
				// here we are initializing the database
				if(stock.getQuote().getPrice() != null) {
					techStock.insertOrUpdate(var[0], new stockInfo(var[1], stock.getQuote().getPrice()));
					//techStock.insertOrUpdate(var[0], new stockInfoWithSymbol(var[0],var[1], stock.getQuote().getPrice()));

				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int i = 1;
		System.out.println("===========Top 10 stocks===========");


        for(stockInfo entry : techStock.top(10)) {
            System.out.println("[" + i + "]" + entry);
            i++;
        }



		// test the get operation
		System.out.println("===========Stock info retrieval===========");
    	System.out.println("VMW" + " " + techStock.get("VMW"));
    	System.out.println("CHL" + " " + techStock.get("CHL"));
		//System.out.println("GOOG" + " " + techStock.get("GOOG"));



	}
}