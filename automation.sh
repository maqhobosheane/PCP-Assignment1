
for i in {3..15..2}
	do 
	
	
		java -cp bin MedianFilterSerial Images/Image6.jpg Ouput.jpg $i>> median_sa1.csv
		java -cp bin MedianFilterParallel Images/Image6.jpg Output.jpg $i>> median_pa1.csv
	
done
	