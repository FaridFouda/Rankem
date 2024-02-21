//Matrix and Eigenvector Library.
import java.text.DecimalFormat;
import java.io.*;

import org.apache.commons.math3.linear.*;

public class Rankem {


   public static void main (String [] args) throws FileNotFoundException, IOException{

    //Test Matrix from 2023 Fantasy Hockey League  
    //For some reason only works with 0 = win and 1 = loss.
    double [][] testMatrix = { {1,0,1,1,1,1,1,1,0,1,1,1}, {1,1,1,1,0,1,1,1,2,1,1,0} ,{0,0,1,0,0,0,1,2,0,1,0,0},{0,0,1,1,0,0,0,0,0,0,0,0},{0,1,1,1,1,2,1,1,0,0,1,0},{0,0,1,1,0,1,1,1,1,0,0,0},{0,0,0,2,0,0,1,1,1,0,1,1},{0,0,0,1,0,0,0,1,0,0,0,0},{1,0,1,1,1,0,0,1,1,0,1,1},{0,0,0,1,1,1,1,1,1,1,2,1},{1,0,1,1,0,1,0,1,0,0,1,0},{1,1,1,1,1,1,0,1,1,0,1,1}};
    RealMatrix test = new Array2DRowRealMatrix(testMatrix);
    GetRanking(test);

}

    //Method to return width
    public static int getWidth(RealMatrix x){
        return x.getRowDimension();
    
    }

    //Method to return height
    public static int getHeight ( RealMatrix x){
        return x.getColumnDimension();
    }


    //Method to transform a matrix into a stochastic one.
    //Adds the sum of individual columns and divides each number in that column by that sum.
    public static RealMatrix Stochafy(RealMatrix x){
        int n = getHeight(x);
        double[] sumArray = new double[n];

        // Calculate the sum of each row
        for (int i = 0; i < n; i++) {
            Double sum = 0.0;
            for (int k = 0; k < n; k++) {
                sum += x.getEntry(k, i) ;
            }
            sumArray[i] = sum;
    }
    
        //Divide by the sum of each row
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                x.setEntry(k, i, x.getEntry(k, i)/sumArray[i]);
        }
       
}
return x ; 
}
//Method to negate eigenvector if need be
public static RealVector Negate( RealMatrix x){

    RealVector y = x.getColumnVector(0);
    for(int i =0 ; i<getHeight(x); i++){
        y.setEntry(i, (y.getEntry(i))*-1);
    }
    return y ; 
}
    
    
    public static void GetRanking ( RealMatrix x) throws FileNotFoundException, IOException{


    //Transform matrix into a stochastic one
    Stochafy(x);
    //Create EigenDecomposition
    EigenDecomposition test1 = new EigenDecomposition(x);
    
    //Get the eigenvector associated with 1
    RealMatrix test2 = test1.getV();
    boolean bool = true; 
    RealVector column = test2.getColumnVector(0);
    
        RealVector endVector = test1.getEigenvector(0);
        RealVector Ranking = test1.getEigenvector(0);
        
    //Check if the vector needs negating (If all the entries are negative).
    for(int i =0 ; i<getHeight(x);i++){
        if(column.getEntry(i)<0){
            bool = true ;
        }else{
            bool = false;
            break;
        }

    }
    //Negate if need be.
    if (bool){
         endVector = Negate(test1.getV());
         Ranking = Negate(test1.getV());
    }

    

    double sorted []  = new double [getHeight(x)];
    //Sort the entries in the eigenvector from biggest to smallest.
    double epsilon = 1e-12;
    for(int k =0 ; k<getHeight(x); k++){
        double comparable =endVector.getEntry(0) ; 
        int index =0;

        int counter =0 ; 

    for(int i=0 ; i<Math.pow(getHeight(x),2);i++){

        if(counter == getHeight(x)-1){
            endVector.setEntry(index, -1000000000);
            sorted[k]= comparable;
            break;
        }
        if(comparable < endVector.getEntry(counter+1) && Math.abs(comparable - endVector.getEntry(counter + 1)) > epsilon){
            comparable = endVector.getEntry(counter+1);
            index = counter + 1 ;
            counter = 0 ;   
        
        }else{
            counter ++ ;    
        }   
    }
}   
    FileOutputStream fout = new FileOutputStream("/Users/faridfouda/Documents/GitHub/Rankem/docs/testfile.html",true);
    //Clears the html file of the previous ranking
    new FileOutputStream("/Users/faridfouda/Documents/GitHub/Rankem/docs/testfile.html").close();
    String content = "<html><style>body { background-color: white; color: black; font-family: Arial; text-align: center; }</style><body>";
    fout.write(content.getBytes());
    
    DecimalFormat df = new DecimalFormat("0.0000");
    String [] TeamNames = {"GustavoFring", "Haïti ", "Alicia's Amazing Team","I love ","Fitzroy Owner","BelAzurThalassa","Layers","Puck4life","Cairo Sand Dunes","Jacob's Quality Team","eroche0202's Quality Team","Galleta"};
    //Find the corresponding ranking using the sorted array.
    System.out.println("The ranking is:");


    String st2;
    String P = "<P>";
    String End = "</body></html>";
    for(int i =0 ; i<getHeight(x);i++){
        double find= sorted[i];
        for(int k =0 ; k<getHeight(x); k++){
            if(Ranking.getEntry(k)==find){
                System.out.println(i+1+". "+TeamNames[k] + " ("+df.format(find) + ")");
                 st2 = (i+1+". "+TeamNames[k] + " ("+df.format(find) + ")");
                 byte [] array2 =st2.getBytes();
                 fout.write(System.getProperty("line.separator").getBytes());
                 fout.write(P.getBytes());
                 fout.write(array2);
                 fout.write(P.getBytes());

            }
        }
    }
    fout.write(End.getBytes());
    fout.close();
    }
    
}