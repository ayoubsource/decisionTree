/* Machine learning supervised algorithm to decide(classify) data based on previous data set.
 * This algorithm is commonly called as decision tree.
 */

import java.io.*;
import java.util.*;

public class DecisionTree
{
    static class StringInfo
    {
    //Meta data about the text file.
    String[][] data;
    int size;
    int col;
    }

    static class Tree
    {
        Hashtable<String, Tree> node;
    }


    //global info
    StringInfo info = new StringInfo();
    String file_name;
    
    DecisionTree(String namee) throws IOException
    {
        file_name = namee;
        info.col = get_columns();
        read();
    }


    public int get_columns() throws IOException
    {   String[] array = new String[1000];

        BufferedReader buffer = new BufferedReader(new FileReader(file_name));
        String temp = buffer.readLine();
        array = temp.split(",");
        buffer.close();

        return array.length;
    }
        
    
    public void read() throws IOException
    { /*Create 2D array to store data.
        Each row corressponds to one data set
        Each column lists the attributes of the data set
        Usually last element in row represents class.
      */

       BufferedReader buffer = new BufferedReader(new FileReader(file_name));

       //temp variable
       String line;
       String[][] data = new String[100000][100];
       String[] temp = new String[1000];

       //size = data set size
       int size = 0;

       int i = 0;
       while((line = buffer.readLine()) != null)
       {
           temp = line.split(",");
           for(i = 0; i < info.col; i++)
           {
               data[size][i] = temp[i];
           }

           size += 1;
       }
       info.size = size;
       info.data = data;
       buffer.close();
 
    }

    public Hashtable<String, Integer> getFrequencyClasses(String[][] dataset, int col, int size)
    {
        Hashtable<String, Integer> frequency = new Hashtable<String, Integer>();
        int row = 0;

        for(row = 0; row < size; row++)
        {
            if(frequency.containsKey(dataset[row][col]))
            {
                frequency.put(dataset[row][col], frequency.get(dataset[row][col]) + 1);
            }

            else
             {
                 frequency.put(dataset[row][col], 1);

             }
            
        }
        return frequency;
    }


    public Hashtable<String, Hashtable<String, Integer>> getFrequencyAttribute(String[][] dataset, int attribute, int size, int col)
    {
        /*we calculate entropy by getting the probablity of each
         * class with the related attribute.
         * The returned Hashtable is a nested Hashtable.
         */

        Hashtable<String, Hashtable<String, Integer>> frequency = new Hashtable<String, Hashtable<String, Integer>>();
        //store the frequency of each attribute in the hashtable wrt to classes.
        //refer http://people.revoledu.com/kardi/tutorial/DecisionTree/how-decision-tree-algorithm-work.htm

        for(int row = 0; row < size; row++)
        {   
            //check whether the attribute is in the parent dictionary
            //and update the nested dictionary accordingly

            if(frequency.containsKey(dataset[row][attribute]))
                {
                    //check whether the nested hashtable contains the class.
                    //if yes, update it by adding 1. If no, update by creating the class.
                if(frequency.get(dataset[row][attribute]).containsKey(dataset[row][col]))
                 {
                   frequency.get(dataset[row][attribute]).put(dataset[row][col], frequency.get(dataset[row][attribute]).get(dataset[row][col]) +1);
                 }

                else
                 {
                    frequency.get(dataset[row][attribute]).put(dataset[row][col], 1);
                 }


                }

                else
                {
                     Hashtable<String, Integer> temp = new Hashtable<String, Integer>();
                     temp.put(dataset[row][col], 1);
                     frequency.put(dataset[row][attribute], temp);
                }
            }
       }

       return frequency;
    } 


    public double getTotalFrequency(Hashtable <String, Hashtable<String, Integer>> frequency)
    {
        //We calculate size again in case we reach a base condition
        //of no longer using a particular attribute.
        //Keep adding frequency of each class

        double total = 0;
        Enumeration parent_keys = frequency.keys();
        Enumeration nested_keys;
        Hashtable<String, Integer> nested_hashtable = new Hashtable<String, Integer>();

        while(parent_keys.hasMoreElements())
        {
            nested_hashtable = frequency.get(parent_keys.nextElement());
            nested_keys = nested_hashtable.keys();
            
            while(nested_keys.hasMoreElements())
            {
                total += nested_hashtable.get(nested_keys.nextElement());
            }

        }
    return total;    
    }


    public double getEntropy(Hashtable<String, Integer>frequency, double total)
    {
        //frequency is nested hashtable from getFrequency method
        //storing frequency of each attribute type's classes.
        //refer http://people.revoledu.com/kardi/tutorial/DecisionTree/how-to-measure-impurity.htm#Entropy

        double answer = 0;

        Enumeration keys = frequency.keys();

        double prob;

        while(keys.hasMoreElements())
        {
            //Caclulate entropy of each attribute

            prob = frequency.get(keys.nextElement()) / total;
            answer -= prob * Math.log(prob);
        }

        return answer;
    }


    public double getGain(Hashtable<String, Integer> class_frequency, Hashtable<String, Hashtable<String, Integer>> attribute_frequency)
    {
        double total_classes = getTotalFrequency(attribute_frequency);
        double total_entropy = getEntropy(class_frequency, total_classes);
        double answer = 1;

        Enumeration attribute_keys = attribute_frequency.keys();

        while(attribute_keys.hasMoreElements())
        {
            answer *= 

    }


    private Tree _createTree(String[][] dataset, Tree tree, int col_size, int row_size)
    {
        int i = 0;
        double gain;
        double max = 0;
        int max_index = -1;

        if(col_size == 1)
        {
            return Tree;
        }

        Hashtable<String, Hashtable<String, Integer>> temp = new Hashtable<String, Hashtable<String, Integer>>();

        for(i = 0; i < col_size; i++)
        {    

             Hashtable<String, Hashtable<String, Integer>> frequency_attribute = getFrequencyAttribute(dataset, i, row_size, col_size);
             Hashtable<String, Integer> class_frequency = getFrequencyClasses(dataset, col_size, row_size);
             gain = getGain(classes_frequency, attribute_frequency);
             if(gain > max)
              {
                  temp = frequency_attribute;
                  max = gain;
                  max_index = i;
               }
            
        }


        Enumeration keys = temp.keys();

        //create new datasets for use
        while(keys.hasMoreElements())
        {
            int r = 0;
            sub_attribute = keys.getNext();
            length = temp.get(sub_attribute).length;

            String[][] temp = new String[length][col_size-1];
           
            for(j = 0; j < row_size ; j++)
            {

                if(dataset[j][max_index] == sub_attribute_concerned)
                {
                    temp[r] = dataset[j];
                    r+= 1;
                }

            }

            tree = _createTree(temp, tree.CHANGETHISTHING, col_size-1, length);
        }
    }


   public void createTree()
   {
       Tree tree = new Tree();
       tree = _createTree(info.data, tree, info.col, info.size);
   }


    public static void main(String []args) throws IOException
    {  //For testing 
        DecisionTree tree = new DecisionTree("nursery.data");
        System.out.println(tree.info.col);
        System.out.println(tree.info.size);
        System.out.println(tree.info.data[5][5]);
        Hashtable<String, Hashtable<String, Integer>> x = tree.getFrequencyAttribute(6);
        System.out.println(x.get("nonprob").get("priority"));
    
    }
}