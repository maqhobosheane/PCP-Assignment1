for file in Images/*
do 
	
	java -cp bin MedianFilterSerial $file Ouput.jpg 11>> medianS4.csv
	java -cp bin MedianFilterParallel $file Output.jpg 11>> medianP4.csv
	
done