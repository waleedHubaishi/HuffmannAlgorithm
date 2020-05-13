import java.io.*;
import java.util.*;

/**
 * Created by Hubaishi on 01.06.17.
 */
public class HuffmanTree {
    static Node n=null;
    static String table="";
    static String fileContent="";

    //this function would return the probability of each character
    public static float GetProb(String fileContint,char a){
        float count=0;
        for (int i = 0; i < fileContint.length(); i++) {
            if(fileContint.charAt(i)==a)
                count++;
        }
        return count/fileContint.length();
    }

    //this function would construct the actual tree of nodes so we can assign each node to character with correct probability
    public  void CountChar() throws IOException {

        String temp="";
        String ch="";
        int counter=0,index1=0,index2=0;
        float prob=0;
        //the actual tree
        ArrayList<Node> nodes=new ArrayList<Node>();

        fileContent = readFile("text");

        //assuring not to repeat the characters, and add only the one not written before
        while(true){
            if(counter>=fileContent.length())
                break;

            ch+=fileContent.charAt(counter);
            if(temp.contains(ch)){
                ch="";
                //go for next position
                counter++;
                continue;
            }

            else
            {
                temp+=ch;
                prob=GetProb(fileContent, ch.charAt(0));

                //create the node with the character and its probability
                Node node=new Node(ch, prob, null, null);

                //add node to the tree
                nodes.add(node);

                //preparing for the next character
                ch="";
                counter++;
            }
        }


        //keep combining till 2 are only left
        while(nodes.size()>2){
            index1=getLeastProb(nodes);
            String data=nodes.get(index1).getData();
            float prob2=nodes.get(index1).getProberty();
            Node tempNode=new Node(data, prob, nodes.get(index1).getright(), nodes.get(index1).getLeft());

            nodes.remove(index1);
            index2=getLeastProb(nodes);
            String data2=nodes.get(index2).getData();
            float prob3=nodes.get(index2).getProberty();

            Node tempNode2=new Node(data2, prob3, nodes.get(index2).getright(), nodes.get(index2).getLeft());
            nodes.remove(index2);

            n=new Node((data+data2), (prob2+prob3), tempNode, tempNode2);

            nodes.add(n);

        }
        n=new Node((nodes.get(0).getData()+nodes.get(1).getData()), nodes.get(0).getProberty()+nodes.get(1).getProberty(), nodes.get(0),nodes.get(1));


    }


    //create the nodes with the right code assigned to the left and the right
    public static void inOrder(Node curr,String code){
        if(curr!=null)
        {
            curr.setCode(code);
            inOrder(curr.getright(),code+1);
            inOrder(curr.getLeft(),code+0);
        }
    }

    public static void inOrder(){
        inOrder(n,"");
    }


    //this function would write the table of the probabilities
    public static void printLeafCode(Node curr) {

        Node curr2=null;
        Node  curr3=null;
        //as long as i'm not yet finished with all nodes
        if(curr!=null){
            curr2=curr.getright();
            curr3=curr.getLeft();

            //if no under left or right nodes, write to table
            if(curr2==null && curr3==null){

                table+="-"+((int)curr.getData().charAt(0))+":"+curr.getCode();

            }

            printLeafCode(curr2);
            printLeafCode(curr3);
        }
    }

    public static void printLeafCode() {
        inOrder();
        printLeafCode(n);
    }

    //return the index of the minimum probability
    public static int getLeastProb(ArrayList<Node> nodes){
        int counter=0;
        float temp=1;
        int index=0;

        while(counter<nodes.size()){

            if(temp>=nodes.get(counter).getProberty()){
                temp=nodes.get(counter).getProberty();
                index=counter;
            }
            counter++;
        }

        return index;
    }


    //this function would return the content of the file in a String
    public String readFile(String fileName) throws IOException {
        File file = new File(fileName+".txt");

        String line="";
        String content;

        StringBuffer stringBuffer = new StringBuffer("");
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            int a = 0;
            while ((a = reader.read()) != -1) {
                char c = (char) a;
                stringBuffer.append(c);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        content = stringBuffer.toString();
        return content;

    }

    //this function would write the code of each character and save it to the file
    public void commpress(String fileName) throws IOException {

        CountChar();
        printLeafCode();
        String toCompressString = readFile("text");
        System.out.println(toCompressString);
        String resultOfCompress = "";

        try {
            PrintWriter writeTable=new PrintWriter(new BufferedWriter(new FileWriter("dec_tab.txt")));
            FileOutputStream fos = new FileOutputStream("output.dat");

            String newTable = table.substring(1);
            writeTable.print(newTable);

            //make the asii and code in a row
            String[] tableArray = newTable.split("-");

            //go throught each letter
            for(int i=0;i<toCompressString.length();i++)
            {
                for(int z=0;z<tableArray.length;z++)
                {

                    //seperate the ascii from the code using the :
                    String[] str_array = tableArray[z].split(":");

                    if(String.valueOf((int)toCompressString.charAt(i)).equals(str_array[0]))
                    {
                        // if you have found the corresponding ascii write the coresponding code
                        resultOfCompress += str_array[1];
                    }
                }
            }


            resultOfCompress += "1";

            //make it divisble by 8, by adding 1 then 0's
            if(resultOfCompress.length()%8 != 0)
            {

                while(resultOfCompress.length()%8 != 0)
                {
                    resultOfCompress += "0";
                }
            }

            //make each 8 characters together
            List<String> byteStringList = new ArrayList<String>();
            int index = 0;
            while (index < resultOfCompress.length()) {
                byteStringList.add(resultOfCompress.substring(index, Math.min(index + 8,resultOfCompress.length())));
                index += 8;
            }


            //create the byte array and save each from in the above list in it
            byte[] byteArray = new byte[byteStringList.size()];
            for(int i=0;i<byteStringList.size();i++)
            {
                byteArray [i] = (byte) Integer.parseInt(byteStringList.get(i), 2);
            }

            fos.write(byteArray);
            fos.close();
            writeTable.close();

        } catch (Exception e) {
            System.out.println("no file found");
        }



    }


    //this function would decompress the compressed text
    public void decommpress() throws IOException {
        String comData="";
        String originalFile="";

        String myTable = readFile("dec_tab");
        File file = new File("output.dat");
        byte[] bFile = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bFile);
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //return the byte into its binary string representation
        String [] binaryRepresentationOfByte = new String[bFile.length];
        for(int i=0;i<bFile.length;i++)
        {
            binaryRepresentationOfByte[i] = String.format("%8s",
                    Integer.toBinaryString((bFile[i] + 256) % 256)).replace(' ', '0');
            comData += binaryRepresentationOfByte[i];
        }

        //get the code and ascii array and make it as an array, each row is an ascii and its Huffman code
        String[] tableArray = myTable.split("-");

        //this counter is a way to get out, the idea is to check for each block of code as maximum the count of the avalible Huffmann code
        //when not found, then there is no ascii representation for the written code, in other words, we are done decoding
        int counter = 0;

        //plus to the above comment, we will check if the data to be compressed is all decompressed, as we are deleting each block of code
        //which has already be decompressed from the compressed data
        while(counter < tableArray.length && comData!="") {
            for (int i = 0; i < tableArray.length; i++) {
                String[] str_array = tableArray[i].split(":");
                //we are taking as many binaries as each Huffman code to compare with
                String upToNCharacters = comData.substring(0, Math.min(comData.length(), str_array[1].length()));

                //if we have less than 7 letters remaining, we will start to check if we have a 1 followed by 0's
                if(comData.length() <= 8 && comData.length() !=0 && comData.charAt(0) == '1' )
                {

                    boolean isRepeatedZero = true;
                    for(int t=1;t<comData.length();t++)
                    {
                        //are we sitting on 1 followed by 0's
                        if(comData.charAt(t) == '1')
                        {
                            isRepeatedZero = false ;
                            break;
                        }
                    }

                    if(isRepeatedZero == true)
                    {
                        comData = "";
                        continue;
                    }
                }

                //comparing the selected part of the compressed data with each Huffman code in our table, if yes then decompress code
                if (upToNCharacters.equals(str_array[1])) {
                    originalFile += String.valueOf(Character.toChars(Integer.valueOf(str_array[0])));
                    counter = 0;
                    String newString = comData.substring(str_array[1].length());
                    comData = newString;

                    //one try is done, remeber max number of tries each time as the number of avalible ascii's from the table
                } else if(!(upToNCharacters.equals(str_array[1]))) {
                    counter++;
                }

            }
        }

        System.out.println("original file  = "+originalFile);
        try {
            PrintWriter out=new PrintWriter(new BufferedWriter(new FileWriter("decompress.txt")));
            out.println(originalFile);
            out.close();
        } catch (Exception e) {
            System.out.println("error");

        }
    }
}

